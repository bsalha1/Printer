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
import com.reliableplugins.printer.hook.factions.*;
import com.reliableplugins.printer.hook.residence.ResidenceHook;
import com.reliableplugins.printer.hook.residence.ResidenceHook_v_4_9_2_1;
import com.reliableplugins.printer.hook.residence.ResidenceScanner;
import com.reliableplugins.printer.hook.shop.*;
import com.reliableplugins.printer.hook.superiorskyblock.SuperiorSkyblockHook;
import com.reliableplugins.printer.hook.superiorskyblock.SuperiorSkyblockHook_v1;
import com.reliableplugins.printer.hook.superiorskyblock.SuperiorSkyblockScanner;
import com.reliableplugins.printer.listeners.ListenPlayerQuit;
import com.reliableplugins.printer.listeners.ListenPluginLoad;
import com.reliableplugins.printer.listeners.ListenPrinterBlockPlace;
import com.reliableplugins.printer.listeners.ListenPrinterExploit;
import com.reliableplugins.printer.nms.*;
import com.reliableplugins.printer.task.BukkitTask;
import com.reliableplugins.printer.task.InventoryScanner;
import com.reliableplugins.printer.type.PrinterPlayer;
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
    private Economy economy;

    // Cross/Backwards Compatibility
    private SuperiorSkyblockHook superiorSkyBlockHook;
    private CitizensHook citizensHook;
    private FactionsHook factionsHook;
    private ShopHook shopHook;
    private ResidenceHook residenceHook;
    private BukkitTask factionScanner;
    private BukkitTask superiorSkyBlockScanner;
    private BukkitTask residenceScanner;
    private INMSHandler nmsHandler;

    private boolean shop;
    private boolean citizens;
    private boolean factions;
    private boolean superiorSkyBlock;
    private boolean residence;

    private FileManager fileManager;
    private MainConfig mainConfig;
    private MessageConfig messageConfig;
    private PricesConfig pricesConfig;

    private boolean spigot;

    // Database
    public HashMap<Player, PrinterPlayer> printerPlayers = new HashMap<>();

    @Override
    public void onEnable()
    {
        Printer.INSTANCE = this;
        version = getDescription().getVersion();

        spigot = true;
        try
        {
            Bukkit.class.getMethod("spigot");
        }
        catch (NoSuchMethodException e)
        {
            spigot = false;
        }

        try
        {
            fileManager = setupConfigs();
            nmsHandler = setupNMS();
            setupEconomy();
            setupCitizensHook();
            setupFactionsHook();
            setupSuperiorSkyBlockHook();
            setupResidenceHook();
            setupShopHook();
            commandHandler = setupCommands();
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

        // Shut off scanners
        if(factionScanner != null)
        {
            factionScanner.cancel();
        }
        if(superiorSkyBlockScanner != null)
        {
            superiorSkyBlockScanner.cancel();
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
            default:
                return new Version_1_16_R2();
        }
    }

    public void setupSuperiorSkyBlockHook()
    {
        if(!mainConfig.useSuperiorSkyBlock())
        {
            return;
        }

        if(!getServer().getPluginManager().isPluginEnabled("SuperiorSkyblock2"))
        {
            getLogger().log(Level.WARNING, "SuperiorSkyblock2 jar not found!");
            return;
        }

        superiorSkyBlockHook = new SuperiorSkyblockHook_v1();
        superiorSkyBlockScanner = new SuperiorSkyblockScanner(0L, 5L);
        superiorSkyBlock = true;
        getLogger().log(Level.INFO, "Successfully hooked into SuperiorSkyblock2");
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

        residenceHook = new ResidenceHook_v_4_9_2_1();
        residenceScanner = new ResidenceScanner(0L, 5L);
        residence = true;
        getLogger().log(Level.INFO, "Successfully hooked into Residence");
    }

    public void setupShopHook()
    {
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

                    shop = true;
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

        if (mainConfig.useZShop())
        {
            if (getServer().getPluginManager().isPluginEnabled("zShop"))
            {
                shopHook = new ZShopHook_v_2_0_1_1();
                shop = true;
                getLogger().log(Level.INFO, "Successfully hooked into zShop");
            }
            else
            {
                getLogger().log(Level.WARNING, "zShop jar not found!");
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
        factions = true;
        getLogger().log(Level.INFO, "Successfully hooked into Factions");
    }

    public void setupCitizensHook()
    {
        if (!getServer().getPluginManager().isPluginEnabled("Citizens"))
        {
            return;
        }

        citizensHook = new CitizensHook_v2_0_16();
        citizens = true;
        getLogger().log(Level.INFO, "Successfully hooked into Citizens");
    }

    private void setupEconomy() throws VaultException
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
        economy = economyRegistration.getProvider();
        if(economy == null)
        {
            throw new VaultException("no economy service provider");
        }
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
        new InventoryScanner(0L, 1L);
    }

    private CommandHandler setupCommands()
    {
        CommandHandler commandHandler = new CommandHandler("printer");
        commandHandler.addCommand(new CommandOn());
        commandHandler.addCommand(new CommandOff());
        commandHandler.addCommand(new CommandReload());
        commandHandler.addCommand(new CommandVersion());
        return commandHandler;
    }

    public boolean withdrawMoney(Player player, double amount)
    {
        if(economy.getBalance(player) - amount >= 0)
        {
            economy.withdrawPlayer(player, amount);
            return true;
        }
        else
        {
            return false;
        }
    }

    public double getBalance(Player player)
    {
        return economy.getBalance(player);
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

    public boolean hasShopHook()
    {
        return shop;
    }

    public boolean hasFactionsHook()
    {
        return factions;
    }

    public boolean hasCitizensHook()
    {
        return citizens;
    }

    public boolean hasResidenceHook()
    {
        return residence;
    }

    public boolean hasSuperiorSkyBlockHook()
    {
        return superiorSkyBlock;
    }

    public boolean isSpigot()
    {
        return spigot;
    }

    public FactionsHook getFactionsHook()
    {
        return factionsHook;
    }

    public CitizensHook getCitizensHook()
    {
        return citizensHook;
    }

    public SuperiorSkyblockHook getSuperiorSkyBlockHook()
    {
        return superiorSkyBlockHook;
    }

    public ResidenceHook getResidenceHook()
    {
        return residenceHook;
    }
}
