/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer;

import com.reliableplugins.printer.commands.*;
import com.reliableplugins.printer.config.FileManager;
import com.reliableplugins.printer.config.MainConfig;
import com.reliableplugins.printer.config.MessageConfig;
import com.reliableplugins.printer.config.PricesConfig;
import com.reliableplugins.printer.exception.VaultException;
import com.reliableplugins.printer.hook.citizens.CitizensHook;
import com.reliableplugins.printer.hook.citizens.CitizensHook_v2_0_16;
import com.reliableplugins.printer.hook.economy.EconomyHook;
import com.reliableplugins.printer.hook.economy.VaultHook;
import com.reliableplugins.printer.hook.shop.shopgui.ShopGuiPlusHook_v1_3_0;
import com.reliableplugins.printer.hook.shop.shopgui.ShopGuiPlusHook_v1_4_0;
import com.reliableplugins.printer.hook.shop.shopgui.ShopGuiPlusHook_v1_5_0;
import com.reliableplugins.printer.hook.shop.ZShopHook;
import com.reliableplugins.printer.hook.territory.TerritoryHook;
import com.reliableplugins.printer.hook.territory.factions.FactionsHook;
import com.reliableplugins.printer.hook.territory.residence.ResidenceHook;
import com.reliableplugins.printer.hook.territory.residence.ResidenceScanner;
import com.reliableplugins.printer.hook.shop.*;
import com.reliableplugins.printer.hook.territory.factions.FactionsHook_MassiveCraft;
import com.reliableplugins.printer.hook.territory.factions.FactionsHook_UUID_v0_2_1;
import com.reliableplugins.printer.hook.territory.factions.FactionsScanner;
import com.reliableplugins.printer.hook.territory.skyblock.BentoBoxHook;
import com.reliableplugins.printer.hook.territory.skyblock.IridiumSkyblockHook;
import com.reliableplugins.printer.hook.territory.skyblock.SuperiorSkyblockHook;
import com.reliableplugins.printer.hook.territory.skyblock.SkyblockScanner;
import com.reliableplugins.printer.listeners.*;
import com.reliableplugins.printer.nms.*;
import com.reliableplugins.printer.task.BukkitTask;
import com.reliableplugins.printer.task.InventoryScanner;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;

public class Printer extends JavaPlugin
{
    public static Printer INSTANCE;
    private String version;

    private CommandHandler commandHandler;
    private PacketListenerManager packetListenerManager;

    // Hooks
    private TerritoryHook skyBlockHook;
    private TerritoryHook residenceHook;
    private CitizensHook citizensHook;
    private FactionsHook factionsHook;
    private ShopHook shopHook;
    private EconomyHook economyHook;
    private BukkitTask factionScanner;
    private BukkitTask skyblockScanner;
    private BukkitTask residenceScanner;
    private INMSHandler nmsHandler;
    private boolean hasShopHook;
    private boolean hasCitizensHook;
    private boolean hasFactionsHook;
    private boolean hasSkyblockHook;
    private boolean hasResidenceHook;
    private boolean hasSpigot;

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
        version = getDescription().getVersion();

        hasSpigot = true;
        try
        {
            Bukkit.class.getMethod("spigot");
        }
        catch (NoSuchMethodException e)
        {
            hasSpigot = false;
        }

        try
        {
            fileManager = setupConfigs();
            nmsHandler = setupNMS();
            setupEconomyHook();
            setupCitizensHook();
            setupFactionsHook();
            setupSkyblockHook();
            setupResidenceHook();
            setupShopHook();
            setupCommands();
            setupTasks();
            setupListeners();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        getLogger().log(Level.INFO, this.getDescription().getName() + " v" + version + " has been loaded");
    }

    @Override
    public void onDisable()
    {
        getLogger().log(Level.INFO, "Deactivating all printing users");
        for(PrinterPlayer player : printerPlayers.values())
        {
            if(player.isPrinting())
            {
                player.printerOff();
            }
        }

        for(Player player : Bukkit.getOnlinePlayers())
        {
            packetListenerManager.removePlayer(player);
        }

        // Shut off scanners
        if(factionScanner != null)
        {
            factionScanner.cancel();
        }
        if(skyblockScanner != null)
        {
            skyblockScanner.cancel();
        }
        if(residenceScanner != null)
        {
            residenceScanner.cancel();
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
        fileManager.addFile(mainConfig = new MainConfig());
        fileManager.addFile(messageConfig = new MessageConfig());
        fileManager.addFile(pricesConfig = new PricesConfig());

        downloadResources();

        return fileManager;
    }

    public <T> T getProvider(Class<T> clazz)
    {
        RegisteredServiceProvider<T> provider = getServer().getServicesManager().getRegistration(clazz);
        if (provider == null)
            return null;
        return provider.getProvider() != null ? provider.getProvider() : null;
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
        if(mainConfig.useSuperiorSkyBlock())
        {
            if(getServer().getPluginManager().isPluginEnabled("SuperiorSkyblock2"))
            {
                skyBlockHook = new SuperiorSkyblockHook();
                skyblockScanner = new SkyblockScanner(0L, 5L);
                hasSkyblockHook = true;
                getLogger().log(Level.INFO, "Successfully hooked into SuperiorSkyblock2");
                return;
            }
            else
            {
                getLogger().log(Level.WARNING, "SuperiorSkyblock2 jar not found!");
            }
        }

        if(mainConfig.useBentoBox())
        {
            if(getServer().getPluginManager().isPluginEnabled("BentoBox"))
            {
                skyBlockHook = new BentoBoxHook();
                skyblockScanner = new SkyblockScanner(0L, 5L);
                hasSkyblockHook = true;
                getLogger().log(Level.INFO, "Successfully hooked into BentoBox");
            }
            else
            {
                getLogger().log(Level.WARNING, "BentoBox jar not found!");
            }
        }

        if(mainConfig.useIridiumSkyblock())
        {
            if(getServer().getPluginManager().isPluginEnabled("IridiumSkyblock"))
            {
                skyBlockHook = new IridiumSkyblockHook();
                skyblockScanner = new SkyblockScanner(0L, 5L);
                hasSkyblockHook = true;
                getLogger().log(Level.INFO, "Successfully hooked into IridiumSkyblock");
            }
            else
            {
                getLogger().log(Level.WARNING, "IridiumSkyblock jar not found!");
            }
        }

    }

    public void setupResidenceHook()
    {
        if(!mainConfig.useResidence())
        {
            return;
        }

        if(!getServer().getPluginManager().isPluginEnabled("Residence"))
        {
            getLogger().log(Level.WARNING, "Residence jar not found!");
            return;
        }

        residenceHook = new ResidenceHook();
        residenceScanner = new ResidenceScanner(0L, 5L);
        hasResidenceHook = true;
        getLogger().log(Level.INFO, "Successfully hooked into Residence");
    }

    public void setupShopHook()
    {
        // ShopGUIPlus
        if (mainConfig.useShopGuiPlus())
        {
            if (getServer().getPluginManager().isPluginEnabled("ShopGUIPlus"))
            {
                Plugin shopGui = getServer().getPluginManager().getPlugin("ShopGUIPlus");
                String[] versions = shopGui.getDescription().getVersion().split("\\.");
                if (versions.length > 2)
                {

                    int major = Integer.parseInt(versions[0]);
                    int minor = Integer.parseInt(versions[1]);
                    int build = Integer.parseInt(versions[2]);
                    if (minor >= 33 && minor <= 34)
                    {
                        shopHook = new ShopGuiPlusHook_v1_3_0();
                    }
                    else if (minor == 35)
                    {
                        shopHook = new ShopGuiPlusHook_v1_4_0();
                    }
                    else
                    {
                        shopHook = new ShopGuiPlusHook_v1_5_0();
                    }

                    hasShopHook = true;
                    getLogger().log(Level.INFO, "Successfully hooked into ShopGUIPlus");
                    return;
                }
                getLogger().log(Level.WARNING, "Failed to parse ShopGUIPlus version!");
            }
            else
            {
                getLogger().log(Level.WARNING, "ShopGUIPlus jar not found!");
            }
        }

        // ZShop
        if (mainConfig.useZShop())
        {
            if (getServer().getPluginManager().isPluginEnabled("zShop"))
            {
                shopHook = new ZShopHook();
                hasShopHook = true;
                getLogger().log(Level.INFO, "Successfully hooked into zShop");
            }
            else
            {
                getLogger().log(Level.WARNING, "zShop jar not found!");
            }
        }

        // DynamicShop
        if(mainConfig.useDynamicShop())
        {
            if(getServer().getPluginManager().isPluginEnabled("DynamicShop"))
            {
                shopHook = new DynamicShopHook();
                hasShopHook = true;
                getLogger().log(Level.INFO, "Successfully hooked into DynamicShop");
            }
            else
            {
                getLogger().log(Level.WARNING, "DynamicShop jar not found!");
            }
        }
    }

    public void setupFactionsHook()
    {
        if(!mainConfig.useFactions())
        {
            return;
        }

        if(!getServer().getPluginManager().isPluginEnabled("Factions"))
        {
            getLogger().log(Level.WARNING, "Factions jar not found!");
            return;
        }

        if(getServer().getPluginManager().getPlugin("Factions").getDescription().getDepend().contains("MassiveCore"))
        {
            factionsHook = new FactionsHook_MassiveCraft();
        }
        else
        {
            String version = getServer().getPluginManager().getPlugin("Factions").getDescription().getVersion();
            String build = version.split("-")[1].replaceAll("U", "");
            String[] buildData = build.split("[.]");
            int buildVersion = Integer.parseInt(buildData[1]);

//            if (buildVersion >= 5)
//            {
//                factionsHook = new FactionsHook_UUID_v0_5_18();
//            }
//            else
//            {
                factionsHook = new FactionsHook_UUID_v0_2_1();
//            }
        }

        factionScanner = new FactionsScanner(0L, 5L);
        hasFactionsHook = true;
        getLogger().log(Level.INFO, "Successfully hooked into Factions");
    }

    public void setupCitizensHook()
    {
        if(Printer.INSTANCE.mainConfig.useCitizens())
        {
            if (getServer().getPluginManager().isPluginEnabled("Citizens"))
            {
                citizensHook = new CitizensHook_v2_0_16();
                hasCitizensHook = true;
                getLogger().log(Level.INFO, "Successfully hooked into Citizens");
            }
            else
            {
                getLogger().log(Level.WARNING, "Citizens jar not found!");
            }
        }
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

        economyHook = new VaultHook(economy);
    }

    private void setupListeners()
    {
        packetListenerManager = new PacketListenerManager();
        Bukkit.getPluginManager().registerEvents(packetListenerManager, this);
        packetListenerManager.addAllOnline();

        Bukkit.getPluginManager().registerEvents(new ListenPluginLoad(), this);
        Bukkit.getPluginManager().registerEvents(new ListenPrinterBlockPlace(), this);
        Bukkit.getPluginManager().registerEvents(new ListenPrinterExploit(), this);
        Bukkit.getPluginManager().registerEvents(new ListenPlayerQuit(), this);
    }

    private void setupTasks()
    {
        new InventoryScanner(0L, 1L);
    }

    private void setupCommands()
    {
        commandHandler = new CommandHandler("printer");
        commandHandler.addCommand(new CommandOn());
        commandHandler.addCommand(new CommandOff());
        commandHandler.addCommand(new CommandReload());
        commandHandler.addCommand(new CommandVersion());
    }

    public String getVersion()
    {
        return version;
    }

    public void reloadConfigs()
    {
        fileManager = setupConfigs();
    }

    public ShopHook getShopGuiPlusHook()
    {
        return shopHook;
    }

    public MainConfig getMainConfig()
    {
        return mainConfig;
    }

    public PricesConfig getPricesConfig()
    {
        return pricesConfig;
    }

    public MessageConfig getMessageConfig()
    {
        return messageConfig;
    }

    public INMSHandler getNmsHandler()
    {
        return nmsHandler;
    }

    public PacketListenerManager getPacketListenerManager()
    {
        return packetListenerManager;
    }

    public boolean hasShopHook()
    {
        return hasShopHook;
    }

    public boolean hasFactionsHook()
    {
        return hasFactionsHook;
    }

    public boolean hasCitizensHook()
    {
        return hasCitizensHook;
    }

    public boolean hasResidenceHook()
    {
        return hasResidenceHook;
    }

    public boolean hasSkyblockHook()
    {
        return hasSkyblockHook;
    }

    public boolean isSpigot()
    {
        return hasSpigot;
    }

    public FactionsHook getFactionsHook()
    {
        return factionsHook;
    }

    public CitizensHook getCitizensHook()
    {
        return citizensHook;
    }

    public TerritoryHook getSkyblockHook()
    {
        return skyBlockHook;
    }

    public EconomyHook getEconomyHook()
    {
        return economyHook;
    }

    public TerritoryHook getResidenceHook()
    {
        return residenceHook;
    }
}
