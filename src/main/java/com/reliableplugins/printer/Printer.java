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
import com.reliableplugins.printer.hook.FactionsHook;
import com.reliableplugins.printer.hook.SuperiorSkyblockHook;
import com.reliableplugins.printer.hook.shopguiplus.ShopGuiPlusHook;
import com.reliableplugins.printer.hook.shopguiplus.ShopGuiPlusHook_v1_3_0;
import com.reliableplugins.printer.hook.shopguiplus.ShopGuiPlusHook_v1_4_0;
import com.reliableplugins.printer.hook.shopguiplus.ShopGuiPlusHook_v1_5_0;
import com.reliableplugins.printer.listeners.ListenPlayerQuit;
import com.reliableplugins.printer.listeners.ListenPluginLoad;
import com.reliableplugins.printer.listeners.ListenPrinterBlockPlace;
import com.reliableplugins.printer.listeners.ListenPrinterExploit;
import com.reliableplugins.printer.nms.*;
import com.reliableplugins.printer.task.BukkitTask;
import com.reliableplugins.printer.type.PrinterPlayer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

public class Printer extends JavaPlugin implements Listener
{
    public static Printer INSTANCE;
    private String version;

    private CommandHandler commandHandler;
    private Economy economy;

    private ShopGuiPlusHook shopGuiPlusHook;
    private BukkitTask factionScanner;
    private BukkitTask superiorSkyBlockScanner;
    private INMSHandler nmsHandler;

    private FileManager fileManager;
    private MainConfig mainConfig;
    private MessageConfig messageConfig;
    private PricesConfig pricesConfig;

    private boolean shopGuiPlus;
    private boolean factions;
    private boolean superiorSkyBlock;
    private boolean spigot;

    // Database
    public HashMap<Player, PrinterPlayer> printerPlayers = new HashMap<>();

    @Override
    public void onEnable()
    {
        Printer.INSTANCE = this;
        version = getDescription().getVersion();

        shopGuiPlus = false;
        factions = false;
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
            economy = setupEconomy();
            factions = setupFactionHook();
            superiorSkyBlock = setupSuperiorSkyBlockHook();
            shopGuiPlus = setupShopGuiHook();
            commandHandler = setupCommands();
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
            default:
                return new Version_1_16_R1();
        }
    }

    private boolean setupSuperiorSkyBlockHook()
    {
        if(mainConfig.useSuperiorSkyBlock())
        {
            if(getServer().getPluginManager().isPluginEnabled("SuperiorSkyblock2"))
            {
                superiorSkyBlockScanner = new SuperiorSkyblockHook.SuperiorSkyBlockScanner(0L, 5L);
                getLogger().log(Level.INFO, "Successfully hooked into SuperiorSkyblock2");
                return true;
            }
            else
            {
                getLogger().log(Level.WARNING, "SuperiorSkyblock2 jar not found!");
            }
        }

        return false;
    }

    private boolean setupShopGuiHook()
    {
        if(mainConfig.useShopGuiPlus())
        {
            if(getServer().getPluginManager().isPluginEnabled("ShopGUIPlus"))
            {
                Plugin shopGui = getServer().getPluginManager().getPlugin("ShopGUIPlus");
                String[] versions = shopGui.getDescription().getVersion().split("\\.");
                if(versions.length > 2)
                {
                    int major = Integer.parseInt(versions[0]);
                    int minor = Integer.parseInt(versions[1]);
                    int build = Integer.parseInt(versions[2]);
                    if(minor >= 33 && minor <= 34)
                    {
                        shopGuiPlusHook = new ShopGuiPlusHook_v1_3_0();
                    }
                    else if(minor == 35)
                    {
                        shopGuiPlusHook = new ShopGuiPlusHook_v1_4_0();
                    }
                    else
                    {
                        shopGuiPlusHook = new ShopGuiPlusHook_v1_5_0();
                    }
                }
                getLogger().log(Level.INFO, "Successfully hooked into ShopGUIPlus");
                return true;
            }
            else
            {
                getLogger().log(Level.WARNING, "ShopGUIPlus jar not found!");
            }
        }

        return false;
    }

    public boolean setupFactionHook()
    {
        if(mainConfig.useFactions())
        {
            if(getServer().getPluginManager().isPluginEnabled("Factions"))
            {
                factionScanner = new FactionsHook.FactionScanner(0L, 5L);
                getLogger().log(Level.INFO, "Successfully hooked into Factions");
                return true;
            }
            else
            {
                getLogger().log(Level.WARNING, "Factions jar not found!");
            }
        }
        return false;
    }

    private Economy setupEconomy() throws VaultException
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
        return economy;
    }

    private void setupListeners()
    {
        Bukkit.getPluginManager().registerEvents(new ListenPluginLoad(), this);
        Bukkit.getPluginManager().registerEvents(new ListenPrinterBlockPlace(), this);
        Bukkit.getPluginManager().registerEvents(new ListenPrinterExploit(), this);
        Bukkit.getPluginManager().registerEvents(new ListenPlayerQuit(), this);
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


    public void setFactionScanner(BukkitTask factionScanner)
    {
        this.factionScanner = factionScanner;
    }

    public String getVersion()
    {
        return version;
    }

    public void reloadConfigs()
    {
        fileManager = setupConfigs();
    }

    public ShopGuiPlusHook getShopGuiPlusHook()
    {
        return shopGuiPlusHook;
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

    public boolean isShopGuiPlus()
    {
        return shopGuiPlus;
    }

    public boolean isFactions()
    {
        return factions;
    }

    public boolean isSuperiorSkyBlock()
    {
        return superiorSkyBlock;
    }

    public boolean isSpigot()
    {
        return spigot;
    }
}
