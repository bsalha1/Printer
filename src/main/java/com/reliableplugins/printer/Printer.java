package com.reliableplugins.printer;

import com.reliableplugins.printer.commands.CommandHandler;
import com.reliableplugins.printer.commands.CommandOff;
import com.reliableplugins.printer.commands.CommandOn;
import com.reliableplugins.printer.commands.CommandReload;
import com.reliableplugins.printer.config.*;
import com.reliableplugins.printer.exception.VaultException;
import com.reliableplugins.printer.listeners.*;
import com.reliableplugins.printer.nms.*;
import com.reliableplugins.printer.type.PrinterPlayer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.logging.Level;

public class Printer extends JavaPlugin implements Listener
{
    public static Printer INSTANCE;

    private INMSHandler nmsHandler;
    private CommandHandler commandHandler;
    private Economy economy;
    private SocketChannelManager socketChannelManager = null;

    private FileManager fileManager;
    private MainConfig mainConfig;
    private MessageConfig messageConfig;
    private PricesConfig pricesConfig;

    private boolean factions;

    // Database
    public HashMap<Player, PrinterPlayer> printerPlayers = new HashMap<>();

    @Override
    public void onEnable()
    {
        Printer.INSTANCE = this;

        try
        {
            fileManager = setupConfigs();
            nmsHandler = setupNMSHandler();
            economy = setupEconomy();
            setupFactionHook();
            setupListeners();
            commandHandler = setupCommands();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        getLogger().log(Level.INFO, this.getDescription().getName() + " v" + this.getDescription().getVersion() + " has been loaded");
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

        if(socketChannelManager != null)
        {
            socketChannelManager.unloadChannelListener();
        }
        getLogger().log(Level.INFO, this.getDescription().getName() + " v" + this.getDescription().getVersion() + " has been unloaded");
    }

    private FileManager setupConfigs()
    {
        FileManager fileManager = new FileManager();
        fileManager.addFile(mainConfig = new MainConfig());
        fileManager.addFile(messageConfig = new MessageConfig());
        fileManager.addFile(pricesConfig = new PricesConfig());
        return fileManager;
    }

    private void setupFactionHook()
    {
        factions = this.getServer().getPluginManager().isPluginEnabled("Factions");
        if(factions)
        {
            socketChannelManager = new SocketChannelManager();
            socketChannelManager.loadChannelListener(new SocketChannelListener());

            Bukkit.getPluginManager().registerEvents(socketChannelManager, this);
            Bukkit.getPluginManager().registerEvents(new ListenFactionEvent(), this);
            getLogger().log(Level.INFO, "Successfully hooked into Factions");
        }
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
        Bukkit.getPluginManager().registerEvents(new ListenPrinterBlockPlace(), this);
        Bukkit.getPluginManager().registerEvents(new ListenPrinterExploit(), this);
        Bukkit.getPluginManager().registerEvents(new ListenPlayerQuit(), this);
    }

    private INMSHandler setupNMSHandler()
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
            default:
                return new Version_1_15_R1();
        }
    }

    private CommandHandler setupCommands()
    {
        CommandHandler commandHandler = new CommandHandler("printer");
        commandHandler.addCommand(new CommandOn());
        commandHandler.addCommand(new CommandOff());
        commandHandler.addCommand(new CommandReload());
        return commandHandler;
    }

    public boolean withdrawMoney(Player player, double amount)
    {
        if(economy.getBalance(player) - amount >= 0)
        {
            economy.withdrawPlayer(player, amount);
            player.sendMessage(Message.WITHDRAW_MONEY.getMessage().replace("{NUM}", Double.toString(amount)));
            return true;
        }
        else
        {
            player.sendMessage(Message.ERROR_NO_MONEY.getMessage());
            return false;
        }
    }

    public void reloadConfigs()
    {
        fileManager = setupConfigs();
    }

    public FileManager getFileManager()
    {
        return fileManager;
    }

    public MainConfig getMainConfig()
    {
        return mainConfig;
    }

    public PricesConfig getPricesConfig()
    {
        return pricesConfig;
    }

    public INMSHandler getNMSHandler()
    {
        return nmsHandler;
    }

    public Economy getEconomy()
    {
        return economy;
    }

    public MessageConfig getMessageConfig()
    {
        return messageConfig;
    }

    public boolean isFactions()
    {
        return factions;
    }
}
