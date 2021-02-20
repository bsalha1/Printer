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
import com.reliableplugins.printer.task.AsyncTaskManager;
import com.reliableplugins.printer.hook.economy.EconomyHook;
import com.reliableplugins.printer.hook.economy.VaultHook;
import com.reliableplugins.printer.hook.packets.ProtocolLibHook;
import com.reliableplugins.printer.hook.shop.ShopHook;
import com.reliableplugins.printer.hook.shop.DynamicShopHook;
import com.reliableplugins.printer.hook.shop.ZShopHook;
import com.reliableplugins.printer.hook.shop.shopgui.ShopGuiPlusHook_1_3_to_1_5;
import com.reliableplugins.printer.hook.territory.TerritoryHook;
import com.reliableplugins.printer.hook.territory.factions.FactionsHook;
import com.reliableplugins.printer.hook.territory.factions.FactionsHook_MassiveCraft;
import com.reliableplugins.printer.hook.territory.factions.FactionsHook_UUID_v0_2_1;
import com.reliableplugins.printer.hook.territory.factions.FactionsScanner;
import com.reliableplugins.printer.hook.territory.residence.ResidenceHook;
import com.reliableplugins.printer.hook.territory.residence.ResidenceScanner;
import com.reliableplugins.printer.hook.territory.skyblock.*;
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
import java.util.concurrent.Executors;
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
    private ProtocolLibHook protocolLibHook;
    private ShopHook shopHook;
    private EconomyHook economyHook;
    private BukkitTask factionScanner;
    private BukkitTask skyblockScanner;
    private BukkitTask residenceScanner;
    private AsyncTaskManager asyncTaskManager;
    private INMSHandler nmsHandler;
    private boolean hasShopHook;
    private boolean hasCitizensHook;
    private boolean hasFactionsHook;
    private boolean hasProtocolLibHook;
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
            setupEconomyHook();
            setupCitizensHook();
            setupFactionsHook();
            setupSkyblockHook();
            setupResidenceHook();
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

        for(Player player : Bukkit.getOnlinePlayers())
        {
            this.packetListenerManager.removePlayer(player);
        }

        // Shut off scanners
        if(this.factionScanner != null)
        {
            this.factionScanner.cancel();
        }
        if(this.skyblockScanner != null)
        {
            this.skyblockScanner.cancel();
        }
        if(this.residenceScanner != null)
        {
            this.residenceScanner.cancel();
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
        if(this.mainConfig.useSuperiorSkyBlock())
        {
            if(getServer().getPluginManager().isPluginEnabled("SuperiorSkyblock2"))
            {
                this.skyBlockHook = new SuperiorSkyblockHook();
                this.skyblockScanner = new SkyblockScanner(0L, 5L);
                this.hasSkyblockHook = true;
                getLogger().log(Level.INFO, "Successfully hooked into SuperiorSkyblock2");
                return;
            }
            else
            {
                getLogger().log(Level.WARNING, "SuperiorSkyblock2 jar not found!");
            }
        }

        if(this.mainConfig.useBentoBox())
        {
            if(getServer().getPluginManager().isPluginEnabled("BentoBox"))
            {
                this.skyBlockHook = new BentoBoxHook();
                this.skyblockScanner = new SkyblockScanner(0L, 5L);
                this.hasSkyblockHook = true;
                getLogger().log(Level.INFO, "Successfully hooked into BentoBox");
            }
            else
            {
                getLogger().log(Level.WARNING, "BentoBox jar not found!");
            }
        }

        if(this.mainConfig.useIridiumSkyblock())
        {
            if(getServer().getPluginManager().isPluginEnabled("IridiumSkyblock"))
            {
                this.skyBlockHook = new IridiumSkyblockHook();
                this.skyblockScanner = new SkyblockScanner(0L, 5L);
                this.hasSkyblockHook = true;
                getLogger().log(Level.INFO, "Successfully hooked into IridiumSkyblock");
            }
            else
            {
                getLogger().log(Level.WARNING, "IridiumSkyblock jar not found!");
            }
        }

        if(this.mainConfig.useASkyBlock())
        {
            if(getServer().getPluginManager().isPluginEnabled("ASkyBlock"))
            {
                this.skyBlockHook = new ASkyBlockHook();
                this.skyblockScanner = new SkyblockScanner(0L, 5L);
                this.hasSkyblockHook = true;
                getLogger().log(Level.INFO, "Successfully hooked into ASkyBlock");
            }
            else
            {
                getLogger().log(Level.WARNING, "ASkyBlock jar not found!");
            }
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

        this.residenceHook = new ResidenceHook();
        this.residenceScanner = new ResidenceScanner(0L, 5L);
        this.hasResidenceHook = true;
        getLogger().log(Level.INFO, "Successfully hooked into Residence");
    }

    public void setupShopHook()
    {
        // ShopGUIPlus
        if (this.mainConfig.useShopGuiPlus())
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
                    this.shopHook = new ShopGuiPlusHook_1_3_to_1_5();
                    this.hasShopHook = true;
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
        if (this.mainConfig.useZShop())
        {
            if (getServer().getPluginManager().isPluginEnabled("zShop"))
            {
                this.shopHook = new ZShopHook();
                this.hasShopHook = true;
                getLogger().log(Level.INFO, "Successfully hooked into zShop");
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
        if(!this.mainConfig.useFactions())
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
            this.factionsHook = new FactionsHook_MassiveCraft();
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
            this.factionsHook = new FactionsHook_UUID_v0_2_1();
//            }
        }

        this.factionScanner = new FactionsScanner(0L, 5L);
        this.hasFactionsHook = true;
        getLogger().log(Level.INFO, "Successfully hooked into Factions");
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
        this.asyncTaskManager = new AsyncTaskManager();
        Executors.newSingleThreadExecutor().submit(this.asyncTaskManager);

        new InventoryScanner(0L, 1L);
    }

    private void setupCommands()
    {
        this.commandHandler = new CommandHandler("printer");
        this.commandHandler.addCommand(new CommandOn());
        this.commandHandler.addCommand(new CommandOff());
        this.commandHandler.addCommand(new CommandReload());
        this.commandHandler.addCommand(new CommandVersion());
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

    public boolean hasSkyblockHook()
    {
        return this.hasSkyblockHook;
    }

    public boolean isSpigot()
    {
        return this.hasSpigot;
    }

    public FactionsHook getFactionsHook()
    {
        return this.factionsHook;
    }

    public CitizensHook getCitizensHook()
    {
        return this.citizensHook;
    }

    public TerritoryHook getSkyblockHook()
    {
        return this.skyBlockHook;
    }

    public EconomyHook getEconomyHook()
    {
        return this.economyHook;
    }

    public TerritoryHook getResidenceHook()
    {
        return this.residenceHook;
    }

    public AsyncTaskManager getAsyncTaskManager()
    {
        return asyncTaskManager;
    }
}
