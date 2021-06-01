/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer;

import com.reliableplugins.printer.api.PrinterOffEvent;
import com.reliableplugins.printer.api.PrinterOnEvent;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.utils.MathUtil;
import com.reliableplugins.printer.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

public class PrinterPlayer
{
    private GameMode initialGamemode;
    private ItemStack[] initialInventory;
    private ItemStack[] initialArmor;
    private HashMap<Location, Material> placedBlocks;
    private HashSet<ItemStack> internalItems; // items obtained from creative inventory

    private final Player player;
    private volatile boolean printing = false;
    private double totalCost;
    private int totalBlocks;
    private long printerOffTimestamp = 0;
    private long lastPickupTimestamp = 0;

    // Scoreboard values
    private Objective objective;
    private Score cost;
    private Score blocks;
    private Score balance;

    public PrinterPlayer(Player player)
    {
        this.player = player;
        this.initialGamemode = player.getGameMode();
        this.initialInventory = player.getInventory().getContents();
        this.initialArmor = player.getInventory().getArmorContents();
        this.placedBlocks = new HashMap<>();
        this.internalItems = new HashSet<>();
    }

    public static PrinterPlayer fromPlayer(Player player)
    {
        return Printer.INSTANCE.printerPlayers.get(player);
    }

    public static PrinterPlayer addPlayer(Player player)
    {
        if(!Printer.INSTANCE.printerPlayers.containsKey(player))
        {
            PrinterPlayer printerPlayer = new PrinterPlayer(player);
            Printer.INSTANCE.printerPlayers.put(player, printerPlayer);
            return printerPlayer;
        }
        else
        {
            return PrinterPlayer.fromPlayer(player);
        }
    }

    public static Collection<PrinterPlayer> getPlayers()
    {
        return Printer.INSTANCE.printerPlayers.values();
    }

    public void printerOn()
    {
        if(printing)
        {
            return;
        }

        PrinterOnEvent event = new PrinterOnEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if(event.isCancelled())
        {
            return;
        }

        // Initialize scoreboard
        if(Printer.INSTANCE.getMainConfig().isScoreboardEnabled())
        {
            initializeScoreboard();
        }

        totalBlocks = 0;
        totalCost = 0;
        placedBlocks = new HashMap<>();
        internalItems = new HashSet<>();

        // Cache initial values
        initialGamemode = player.getGameMode();
        initialInventory = player.getInventory().getContents();
        initialArmor = player.getInventory().getArmorContents();

        if(player.getOpenInventory() != null)
        {
            player.getOpenInventory().close();
        }

        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[player.getInventory().getArmorContents().length]);
        player.setGameMode(GameMode.CREATIVE);
        printing = true;

        if(Printer.INSTANCE.getMainConfig().isTooltipNotificationEnabled())
        {
            Executors.newSingleThreadExecutor().submit(this::showCost);
        }
    }

    public void printerOff()
    {
        if(!printing)
        {
            return;
        }

        PrinterOffEvent event = new PrinterOffEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if(event.isCancelled())
        {
            return;
        }

        // Withdraw
        Printer.INSTANCE.getEconomyHook().withdraw(this.player, this.totalCost);

        // Reset scoreboard
        if(Printer.INSTANCE.getMainConfig().isScoreboardEnabled())
        {
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }

        // Set gamemode and inventory back to initials
        player.setGameMode(initialGamemode);
        player.getInventory().setContents(initialInventory);
        player.getInventory().setArmorContents(initialArmor);
        placedBlocks.clear();
        internalItems.clear();

        printing = false;
        printerOffTimestamp = System.currentTimeMillis(); // for fall-damage timer
    }

    private void showCost()
    {
        while(printing)
        {
            String money = Double.toString(MathUtil.round(totalCost, 2));
            Printer.INSTANCE.getNmsHandler().sendToolTipText(player, Message.WITHDRAW_MONEY.getColoredMessage()
                    .replace("{NUM}", money));
            try
            {
                Thread.sleep(Printer.INSTANCE.getMainConfig().getTooltipNotificationTime() * 1000);
            }
            catch (InterruptedException e)
            {
                return;
            }
        }
    }

    public void initializeScoreboard()
    {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = board.registerNewObjective("test", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(Printer.INSTANCE.getMainConfig().getScoreboardTitle());

        objective.getScore(Printer.INSTANCE.getMainConfig().getCostScoreTitle()).setScore(16);
        cost = objective.getScore(
                Printer.INSTANCE.getMainConfig().getCostFormat().replace("{NUM}", Double.toString(0.0d)));
        cost.setScore(15);

        objective.getScore("").setScore(14); // Margin
        objective.getScore(Printer.INSTANCE.getMainConfig().getBlocksScoreTitle()).setScore(13);
        blocks = objective.getScore(
                Printer.INSTANCE.getMainConfig().getBlocksFormat().replace("{NUM}", Integer.toString(0)));
        blocks.setScore(12);

        String price = Double.toString(MathUtil.round(Printer.INSTANCE.getEconomyHook().getBalance(player), 2)); // round off
        objective.getScore(StringUtil.getSpaces(Printer.INSTANCE.getMainConfig().getScoreboardMargin())).setScore(11); // Margin
        objective.getScore(Printer.INSTANCE.getMainConfig().getBalanceScoreTitle()).setScore(10);
        balance = objective.getScore(
                Printer.INSTANCE.getMainConfig().getBalanceFormat().replace("{NUM}", price));
        balance.setScore(9);

        player.setScoreboard(board);
    }

    public void incrementCost(double cost)
    {
        this.totalCost += cost;
        this.totalBlocks++;

        // Update cost on scoreboard
        updateScoreboard();
    }

    public void decrementCost(double cost)
    {
        this.totalCost -= cost;
        this.totalBlocks--;

        // Update cost on scoreboard
        updateScoreboard();
    }

    private void updateScoreboard()
    {
        if(Printer.INSTANCE.getMainConfig().isScoreboardEnabled())
        {
            String totalCost = Double.toString(MathUtil.round(this.totalCost, 2));
            this.objective.getScoreboard().resetScores(this.cost.getEntry());
            this.cost = this.objective.getScore(
                    Printer.INSTANCE.getMainConfig().getCostFormat().replace("{NUM}", totalCost));
            this.cost.setScore(15);

            this.objective.getScoreboard().resetScores(this.blocks.getEntry());
            this.blocks = this.objective.getScore(
                    Printer.INSTANCE.getMainConfig().getBlocksFormat().replace("{NUM}", Integer.toString(this.totalBlocks)));
            this.blocks.setScore(12);

            String price = Double.toString(MathUtil.round(getRemainingBalance(), 2)); // round off
            this.objective.getScoreboard().resetScores(this.balance.getEntry());
            this.balance = this.objective.getScore(
                    Printer.INSTANCE.getMainConfig().getBalanceFormat().replace("{NUM}", price));
            this.balance.setScore(9);
        }
    }

    public boolean isPlacedBlock(Location location)
    {
        return placedBlocks.containsKey(location);
    }

    public Material popPlacedMaterial(Location location)
    {
        Material material = placedBlocks.get(location);
        placedBlocks.remove(location);
        return material;
    }

    public void addPlacedBlock(Block block)
    {
        placedBlocks.put(block.getLocation(), block.getType());
    }

    public void addInternalItem(ItemStack itemStack)
    {
        internalItems.add(itemStack);
    }

    public boolean isInternalItem(ItemStack itemStack)
    {
        return internalItems.contains(itemStack);
    }

    public double getRemainingBalance()
    {
        return Printer.INSTANCE.getEconomyHook().getBalance(this.player) - this.totalCost;
    }

    public boolean hasEnoughMoney(double transaction)
    {
        return (getRemainingBalance() - transaction) > 0.0d;
    }

    public Player getPlayer()
    {
        return player;
    }

    public boolean isPrinting()
    {
        return printing;
    }

    public long getPrinterOffTimestamp()
    {
        return printerOffTimestamp;
    }

    public void setLastPickupTimestamp(long lastPickupTimestamp)
    {
        this.lastPickupTimestamp = lastPickupTimestamp;
    }

    public long getLastPickupTimestamp()
    {
        return lastPickupTimestamp;
    }
}
