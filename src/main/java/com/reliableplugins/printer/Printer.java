package com.reliableplugins.printer;

import com.reliableplugins.printer.commands.CommandHandler;
import com.reliableplugins.printer.commands.CommandOff;
import com.reliableplugins.printer.commands.CommandOn;
import com.reliableplugins.printer.config.*;
import com.reliableplugins.printer.exception.VaultException;
import com.reliableplugins.printer.listeners.SocketChannelListener;
import com.reliableplugins.printer.listeners.ListenPrinterBlockPlace;
import com.reliableplugins.printer.listeners.ListenPrinterExploit;
import com.reliableplugins.printer.nms.*;
import com.reliableplugins.printer.type.PrinterPlayer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;

public class Printer extends JavaPlugin implements Listener
{
    public static Printer INSTANCE;

    private INMSHandler nmsHandler;
    private CommandHandler commandHandler;
    private Economy economy;
    private SocketChannelManager socketChannelManager;

    private FileManager fileManager;
    private MainConfig mainConfig;
    private MessageConfig messageConfig;
    private PricesConfig pricesConfig;

    // Database
    public HashMap<Player, PrinterPlayer> printerPlayers = new HashMap<>();

    public static final HashSet<Material> prohibitedItems = new HashSet<>();

    static
    {
//        prohibitedItems.add(Material.MINECART.is);
//        prohibitedItems.add(Material.COMMAND_MINECART);
//        prohibitedItems.add(Material.EXPLOSIVE_MINECART);
//        prohibitedItems.add(Material.HOPPER_MINECART);
//        prohibitedItems.add(Material.POWERED_MINECART);
//        prohibitedItems.add(Material.STORAGE_MINECART);
//        prohibitedItems.add(Material.JUKEBOX);
//        prohibitedItems.add(Material.ITEM_FRAME);
//        prohibitedItems.add(Material.PAINTING);
//        prohibitedItems.add(Material.EXP_BOTTLE);
//        prohibitedItems.add(Material.GLASS_BOTTLE);
    }

    @Override
    public void onEnable()
    {
        Printer.INSTANCE = this;

        try
        {
            fileManager = setupConfigs();
            nmsHandler = setupNMSHandler();
            economy = setupEconomy();
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

    public FileManager setupConfigs()
    {
        FileManager fileManager = new FileManager();
        fileManager.addFile(mainConfig = new MainConfig());
        fileManager.addFile(messageConfig = new MessageConfig());
        fileManager.addFile(pricesConfig = new PricesConfig());
        return fileManager;
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
        Bukkit.getPluginManager().registerEvents(socketChannelManager = new SocketChannelManager(), this);
        Bukkit.getPluginManager().registerEvents(new ListenPrinterBlockPlace(), this);
        Bukkit.getPluginManager().registerEvents(new ListenPrinterExploit(), this);

        socketChannelManager.loadChannelListener(new SocketChannelListener());
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

    private CommandHandler setupCommands()
    {
        CommandHandler commandHandler = new CommandHandler("printer");
        commandHandler.addCommand(new CommandOn());
        commandHandler.addCommand(new CommandOff());
        return commandHandler;
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
}
