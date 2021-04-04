/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer;

import com.reliableplugins.printer.commands.*;
import com.reliableplugins.printer.config.*;
import com.reliableplugins.printer.exception.VaultException;
import com.reliableplugins.printer.hook.citizens.CitizensHook;
import com.reliableplugins.printer.hook.citizens.CitizensHook_v2_0_16;
import com.reliableplugins.printer.hook.economy.EconomyHook;
import com.reliableplugins.printer.hook.economy.VaultHook;
import com.reliableplugins.printer.hook.packets.ProtocolLibHook;
import com.reliableplugins.printer.hook.shop.DynamicShopHook;
import com.reliableplugins.printer.hook.shop.ShopHook;
import com.reliableplugins.printer.hook.shop.ZShopHook;
import com.reliableplugins.printer.hook.shop.shopgui.ShopGuiPlusHook_1_3_to_1_5;
import com.reliableplugins.printer.hook.territory.TerritoryHook;
import com.reliableplugins.printer.hook.territory.TerritoryScanner;
import com.reliableplugins.printer.hook.territory.factions.FactionsHook;
import com.reliableplugins.printer.hook.territory.factions.FactionsHook_MassiveCraft;
import com.reliableplugins.printer.hook.territory.factions.FactionsHook_UUID;
import com.reliableplugins.printer.hook.territory.factions.FactionsHook_X;
import com.reliableplugins.printer.hook.territory.lands.LandsHook;
import com.reliableplugins.printer.hook.territory.residence.ResidenceHook;
import com.reliableplugins.printer.hook.territory.skyblock.ASkyBlockHook;
import com.reliableplugins.printer.hook.territory.skyblock.BentoBoxHook;
import com.reliableplugins.printer.hook.territory.skyblock.IridiumSkyblockHook;
import com.reliableplugins.printer.hook.territory.skyblock.SuperiorSkyblockHook;
import com.reliableplugins.printer.listeners.*;
import com.reliableplugins.printer.nms.*;
import com.reliableplugins.printer.task.BukkitTask;
import com.reliableplugins.printer.task.InventoryScanner;
import com.reliableplugins.printer.type.ColoredMaterial;
import com.reliableplugins.printer.utils.BukkitUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class Printer extends JavaPlugin
{
    public static Printer INSTANCE;
    private String version;

    private CommandHandler commandHandler;
    private PacketListenerManager packetListenerManager;

    // Hooks
    private CitizensHook citizensHook;
    private BukkitTask territoryScanner;
    private BukkitTask inventoryScanner;
    private ProtocolLibHook protocolLibHook;
    private ShopHook shopHook;
    private EconomyHook economyHook;
    private INMSHandler nmsHandler;
    private boolean hasShopHook;
    private boolean hasCitizensHook;
    private boolean hasFactionsHook;
    private boolean hasProtocolLibHook;
    private boolean hasSkyblockHook;
    private boolean hasResidenceHook;
    private boolean hasLandsHook;
    private boolean hasSpigot;

    private ArrayList<TerritoryHook> territoryHooks;

    // Configs
    private FileManager fileManager;
    private MainConfig mainConfig;
    private MessageConfig messageConfig;
    private PricesConfig pricesConfig;

    // Database
    HashMap<Player, PrinterPlayer> printerPlayers = new HashMap<>();

    @Override
    public void onEnable()
    {
        Printer.INSTANCE = this;
        this.version = getDescription().getVersion();

        this.hasSpigot = true;
        try
        {
            Bukkit.class.getMethod("spigot");
        }
        catch (NoSuchMethodException e)
        {
            this.hasSpigot = false;
        }

        try
        {
            this.fileManager = setupConfigs();
            this.nmsHandler = setupNMS();
            this.territoryHooks = new ArrayList<>();
            setupEconomyHook();
            setupCitizensHook();
            setupFactionsHook();
            setupSkyblockHook();
            setupResidenceHook();
            setupLandsHook();
            setupShopHook();
            setupCommands();
            setupTasks();
            setupListeners();
            setupPacketListeners();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        getLogger().log(Level.INFO, this.getDescription().getName() + " v" + this.version + " has been loaded");
    }

    @Override
    public void onDisable()
    {
        getLogger().log(Level.INFO, "Deactivating all printing users");
        for(PrinterPlayer player : this.printerPlayers.values())
        {
            if(player.isPrinting())
            {
                player.printerOff();
            }
        }

        if(this.packetListenerManager != null)
        {
            for(Player player : Bukkit.getOnlinePlayers())
            {
                this.packetListenerManager.removePlayer(player);
            }
        }

        // Shut off scanners
        if(this.territoryScanner != null)
        {
            this.territoryScanner.cancel();
        }
        if(this.inventoryScanner != null)
        {
            this.inventoryScanner.cancel();
        }
        getLogger().log(Level.INFO, this.getDescription().getName() + " v" + this.getDescription().getVersion() + " has been unloaded");
    }

    private void downloadResources()
    {
        try
        {
            InputStream initialStream = getResource("example-prices.yml");
            byte[] buffer = new byte[initialStream.available()];
            initialStream.read(buffer);

            File targetFile = new File(getDataFolder(), "example-prices.yml");
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private FileManager setupConfigs()
    {
        FileManager fileManager = new FileManager();
        fileManager.addFile(this.mainConfig = new MainConfig());
        fileManager.addFile(this.messageConfig = new MessageConfig());
        fileManager.addFile(this.pricesConfig = new PricesConfig());

        downloadResources();

        return fileManager;
    }

    private INMSHandler setupNMS()
    {
        String nmsVersion = getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        switch(nmsVersion)
        {
            case "v1_8_R2":
                return new Version_1_8_R2();
            case "v1_8_R3":
                return new Version_1_8_R3();
            case "v_1_9_R1":
                return new Version_1_9_R1();
            case "v_1_9_R2":
                return new Version_1_9_R2();
            case "v_1_10_R1":
                return new Version_1_10_R1();
            case "v1_11_R1":
                return new Version_1_11_R1();
            case "v1_12_R1":
                return new Version_1_12_R1();
            case "v1_13_R1":
                return new Version_1_13_R1();
            case "v1_13_R2":
                return new Version_1_13_R2();
            case "v1_14_R1":
                return new Version_1_14_R1();
            case "v1_15_R1":
                return new Version_1_15_R1();
            case "v1_16_R1":
                return new Version_1_16_R1();
            case "v1_16_R2":
                return new Version_1_16_R2();
            case "v1_16_R3":
            default:
                return new Version_1_16_R3();
        }
    }

    public void setupSkyblockHook()
    {
        TerritoryHook skyBlockHook = null;

        if(this.mainConfig.useSuperiorSkyBlock())
        {
            if(getServer().getPluginManager().isPluginEnabled("SuperiorSkyblock2"))
            {
                skyBlockHook = new SuperiorSkyblockHook();
                getLogger().log(Level.INFO, "Successfully hooked into SuperiorSkyblock2");
            }
            else
            {
                getLogger().log(Level.WARNING, "SuperiorSkyblock2 jar not found!");
            }
        }
        else if(this.mainConfig.useBentoBox())
        {
            if(getServer().getPluginManager().isPluginEnabled("BentoBox"))
            {
                skyBlockHook = new BentoBoxHook();
                getLogger().log(Level.INFO, "Successfully hooked into BentoBox");
            }
            else
            {
                getLogger().log(Level.WARNING, "BentoBox jar not found!");
            }
        }
        else if(this.mainConfig.useIridiumSkyblock())
        {
            if(getServer().getPluginManager().isPluginEnabled("IridiumSkyblock"))
            {
                skyBlockHook = new IridiumSkyblockHook();
                getLogger().log(Level.INFO, "Successfully hooked into IridiumSkyblock");
            }
            else
            {
                getLogger().log(Level.WARNING, "IridiumSkyblock jar not found!");
            }
        }
        else if(this.mainConfig.useASkyBlock())
        {
            if(getServer().getPluginManager().isPluginEnabled("ASkyBlock"))
            {
                skyBlockHook = new ASkyBlockHook();
                getLogger().log(Level.INFO, "Successfully hooked into ASkyBlock");
            }
            else
            {
                getLogger().log(Level.WARNING, "ASkyBlock jar not found!");
            }
        }

        if(skyBlockHook != null)
        {
            this.hasSkyblockHook = true;
            this.territoryHooks.add(skyBlockHook);
        }
    }

    public void setupResidenceHook()
    {
        if(!this.mainConfig.useResidence())
        {
            return;
        }

        if(!getServer().getPluginManager().isPluginEnabled("Residence"))
        {
            getLogger().log(Level.WARNING, "Residence jar not found!");
            return;
        }

        this.hasResidenceHook = true;
        this.territoryHooks.add(new ResidenceHook());
        getLogger().log(Level.INFO, "Successfully hooked into Residence");
    }

    public void setupLandsHook()
    {
        if(!this.mainConfig.useLands())
        {
            return;
        }

        if(!getServer().getPluginManager().isPluginEnabled("Lands"))
        {
            getLogger().log(Level.WARNING, "Lands jar not found!");
            return;
        }

        this.hasLandsHook = true;
        this.territoryHooks.add(new LandsHook());
        getLogger().log(Level.INFO, "Successfully hooked into Lands");

    }

    public void setupShopHook()
    {
        // ShopGUIPlus
        if (this.mainConfig.useShopGuiPlus())
        {
            if (getServer().getPluginManager().isPluginEnabled("ShopGUIPlus"))
            {
                this.shopHook = new ShopGuiPlusHook_1_3_to_1_5();
                this.hasShopHook = true;
                getLogger().log(Level.INFO, "Successfully hooked into ShopGUIPlus");
                return;
            }
            else
            {
                getLogger().log(Level.WARNING, "ShopGUIPlus jar not found!");
            }
        }

        // ZShop
        if (this.mainConfig.useZShop())
        {
            if (getServer().getPluginManager().isPluginEnabled("zShop"))
            {
                this.shopHook = new ZShopHook();
                this.hasShopHook = true;
                getLogger().log(Level.INFO, "Successfully hooked into zShop");
                return;
            }
            else
            {
                getLogger().log(Level.WARNING, "zShop jar not found!");
            }
        }

        // DynamicShop
        if(this.mainConfig.useDynamicShop())
        {
            if(getServer().getPluginManager().isPluginEnabled("DynamicShop"))
            {
                this.shopHook = new DynamicShopHook();
                this.hasShopHook = true;
                getLogger().log(Level.INFO, "Successfully hooked into DynamicShop");
            }
            else
            {
                getLogger().log(Level.WARNING, "DynamicShop jar not found!");
            }
        }
    }

    public void setupPacketListeners()
    {
        if(getServer().getPluginManager().isPluginEnabled("ProtocolLib"))
        {
            this.protocolLibHook = new ProtocolLibHook();
            this.protocolLibHook.registerListeners();
            this.hasProtocolLibHook = true;
            getLogger().log(Level.INFO, "Successfully hooked packet listeners into ProtocolLib");
        }
        else
        {
            this.packetListenerManager = new PacketListenerManager();
            this.packetListenerManager.addAllOnline();
            Bukkit.getPluginManager().registerEvents(this.packetListenerManager, this);
            getLogger().log(Level.INFO, "NMS packet listeners initialized");
        }
    }

    public void setupFactionsHook()
    {
        FactionsHook factionsHook;

        if(!this.mainConfig.useFactions())
        {
            return;
        }

        if (getServer().getPluginManager().isPluginEnabled("FactionsX"))
        {
            factionsHook = new FactionsHook_X();
            getLogger().log(Level.INFO, "Successfully hooked into FactionsX");
        }
        else if(getServer().getPluginManager().isPluginEnabled("Factions"))
        {
            if(getServer().getPluginManager().getPlugin("Factions").getDescription().getDepend().contains("MassiveCore"))
            {
                factionsHook = new FactionsHook_MassiveCraft();
                getLogger().log(Level.INFO, "Successfully hooked into MassiveCraft Factions");
            }
            else
            {
                factionsHook = new FactionsHook_UUID();
                getLogger().log(Level.INFO, "Successfully hooked into FactionsUUID");
            }
        }
        else
        {
            getLogger().log(Level.WARNING, "Factions jar not found!");
            return;
        }

        this.hasFactionsHook = true;
        this.territoryHooks.add(factionsHook);
    }

    public void setupCitizensHook()
    {
        if (!getServer().getPluginManager().isPluginEnabled("Citizens"))
        {
            return;
        }

        this.citizensHook = new CitizensHook_v2_0_16();
        this.hasCitizensHook = true;
        getLogger().log(Level.INFO, "Successfully hooked into Citizens");
    }

    private void setupEconomyHook() throws VaultException
    {
        if(Bukkit.getPluginManager().getPlugin("Vault") == null)
        {
            throw new VaultException("not enabled");
        }

        RegisteredServiceProvider<Economy> economyRegistration = getServer().getServicesManager().getRegistration(Economy.class);
        if(economyRegistration == null)
        {
            throw new VaultException("no economy service");
        }

        Economy economy = economyRegistration.getProvider();
        if(economy == null)
        {
            throw new VaultException("no economy service provider");
        }

        this.economyHook = new VaultHook(economy);
    }

    private void setupListeners()
    {
        Bukkit.getPluginManager().registerEvents(new ListenPluginLoad(), this);
        Bukkit.getPluginManager().registerEvents(new ListenPrinterBlockPlace(), this);
        Bukkit.getPluginManager().registerEvents(new ListenPrinterExploit(), this);
        Bukkit.getPluginManager().registerEvents(new ListenPlayerQuit(), this);
    }

    private void setupTasks()
    {
        this.territoryScanner = new TerritoryScanner(0, 5L);
        this.inventoryScanner = new InventoryScanner(0L, 1L);
    }

    private void setupCommands()
    {
        this.commandHandler = new CommandHandler("printer");
        this.commandHandler.addCommand(new CommandOn());
        this.commandHandler.addCommand(new CommandOff());
        this.commandHandler.addCommand(new CommandToggle());
        this.commandHandler.addCommand(new CommandReload());
        this.commandHandler.addCommand(new CommandVersion());
    }

    public Double getPrice(ItemStack itemStack)
    {
        Material material = itemStack.getType();
        if(BukkitUtil.isItemOfBlock(material))
        {
            material = BukkitUtil.getBlockOfItem(material);
        }
        ColoredMaterial coloredMaterial = ColoredMaterial.fromItemstack(itemStack);

        // Get Price
        // - Prioritize colored price, then uncolored price, then shopgui price
        //   . We want to sell the blue wool for price of blue wool not for the price of uncolored wool
        //   . We want our prices.yml to overwrite ShopGUIPlus
        Double price = null;
        if(coloredMaterial != null && Printer.INSTANCE.getPricesConfig().getColoredPrices().containsKey(coloredMaterial))
        {
            price = Printer.INSTANCE.getPricesConfig().getColoredPrices().get(coloredMaterial);
        }
        else if(Printer.INSTANCE.getPricesConfig().getBlockPrices().containsKey(material))
        {
            price = Printer.INSTANCE.getPricesConfig().getBlockPrices().get(material);
        }
        else if(Printer.INSTANCE.hasShopHook())
        {
            ItemStack item = itemStack.clone();
            item.setAmount(1);
            price = Printer.INSTANCE.getShopHook().getCachedPrice(item);
            price = price < 0 ? null : price; // ShopGui returns -1 on invalid price... that would be bad if we put -1
        }
        return price;
    }


    public Double getItemBlockPrice(ItemStack itemStack)
    {
        Double price = null;

        if(Printer.INSTANCE.getPricesConfig().getItemPrices().containsKey(itemStack.getType()))
        {
            price = Printer.INSTANCE.getPricesConfig().getItemPrices().get(itemStack.getType());
        }
        else if(Printer.INSTANCE.hasShopHook())
        {
            ItemStack toPlaceCopy = itemStack.clone();
            toPlaceCopy.setAmount(1);
            price = Printer.INSTANCE.getShopHook().getCachedPrice(toPlaceCopy);
            price = price < 0 ? null : price; // ShopGui returns -1 on invalid price... that would be bad if we put -1
        }
        return price;
    }

    public Double getItemBlockPrice(Material material)
    {
        ItemStack itemStack = new ItemStack(material, 1);
        return getItemBlockPrice(itemStack);
    }

    public Double getPrice(Material material, byte data)
    {
        Double price = null;
        ColoredMaterial coloredMaterial = ColoredMaterial.fromMaterial(material, data);

        // Get Price
        // - Prioritize colored price, then uncolored price, then shopgui price
        //   . We want to sell the blue wool for price of blue wool not for the price of uncolored wool
        //   . We want our prices.yml to overwrite ShopGUIPlus
        if(coloredMaterial != null && Printer.INSTANCE.getPricesConfig().getColoredPrices().containsKey(coloredMaterial))
        {
            price = Printer.INSTANCE.getPricesConfig().getColoredPrices().get(coloredMaterial);
        }
        else if(Printer.INSTANCE.getPricesConfig().getBlockPrices().containsKey(material))
        {
            price = Printer.INSTANCE.getPricesConfig().getBlockPrices().get(material);
        }
        else if(Printer.INSTANCE.hasShopHook())
        {
            ItemStack item = new ItemStack(material, 1);
            price = Printer.INSTANCE.getShopHook().getCachedPrice(item);
            price = price < 0 ? null : price; // ShopGui returns -1 on invalid price... that would be bad if we put -1
        }
        return price;
    }

    public String getVersion()
    {
        return this.version;
    }

    public void reloadConfigs()
    {
        this.fileManager = setupConfigs();
    }

    public ShopHook getShopHook()
    {
        return this.shopHook;
    }

    public MainConfig getMainConfig()
    {
        return this.mainConfig;
    }

    public PricesConfig getPricesConfig()
    {
        return this.pricesConfig;
    }

    public MessageConfig getMessageConfig()
    {
        return this.messageConfig;
    }

    public INMSHandler getNmsHandler()
    {
        return this.nmsHandler;
    }

    public boolean hasShopHook()
    {
        return this.hasShopHook;
    }

    public boolean hasFactionsHook()
    {
        return this.hasFactionsHook;
    }

    public boolean hasCitizensHook()
    {
        return this.hasCitizensHook;
    }

    public boolean hasResidenceHook()
    {
        return this.hasResidenceHook;
    }

    public boolean hasLandsHook()
    {
        return hasLandsHook;
    }

    public boolean hasSkyblockHook()
    {
        return this.hasSkyblockHook;
    }

    public boolean isSpigot()
    {
        return this.hasSpigot;
    }

    public CitizensHook getCitizensHook()
    {
        return this.citizensHook;
    }

    public EconomyHook getEconomyHook()
    {
        return this.economyHook;
    }

    public boolean isTerritoryRestricted(Player player, Location location)
    {
        for(TerritoryHook hook : this.territoryHooks)
        {
            if(!hook.canBuild(player, location, this.mainConfig.allowInWilderness()))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isTerritoryRestricted(Player player)
    {
        for(TerritoryHook hook : this.territoryHooks)
        {
            // If player isn't in their own territory and they aren't in wilderness (if they're allowed)
            if(!hook.isInOwnTerritory(player) &&
                    (hook.isInATerritory(player) || !Printer.INSTANCE.getMainConfig().allowInWilderness()))
            {
                Message.ERROR_NOT_IN_TERRITORY.sendColoredMessage(player);
                return true;
            }
            else if(!Printer.INSTANCE.getMainConfig().allowNearNonMembers() &&
                    (hook instanceof FactionsHook ? ((FactionsHook) hook).isNonTerritoryMemberNearby(player, this.mainConfig.allowNearAllies()) : hook.isNonTerritoryMemberNearby(player)))
            {
                Message.ERROR_NON_TERRITORY_MEMBER_NEARBY.sendColoredMessage(player);
                return true;
            }
        }
        return false;
    }
}
