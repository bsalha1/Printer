/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.type;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashSet;
import java.util.concurrent.Executors;

public class PrinterPlayer
{
    private GameMode initialGamemode;
    private ItemStack[] initialInventory;
    private ItemStack[] initialArmor;
    private HashSet<Block> placedBlocks;

    private final Player player;
    private volatile boolean printing = false;
    private double totalCost;
    private int totalBlocks;
    private long printerOffTimestamp;

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
        this.placedBlocks = new HashSet<>();
    }

    public void printerOn()
    {
        if(!printing)
        {
            // Initialize scoreboard
            if(Printer.INSTANCE.getMainConfig().isScoreboardEnabled())
            {
                initializeScoreboard();
            }

            totalBlocks = 0;
            totalCost = 0;

            // Cache initial values
            initialGamemode = player.getGameMode();
            initialInventory = player.getInventory().getContents();
            initialArmor = player.getInventory().getArmorContents();
            placedBlocks = new HashSet<>();

            if(player.getOpenInventory() != null)
            {
                player.getOpenInventory().close();
            }

            player.getInventory().clear();
            player.getInventory().setArmorContents(new ItemStack[player.getInventory().getArmorContents().length]);
            player.setGameMode(GameMode.CREATIVE);
            printing = true;
            Executors.newSingleThreadExecutor().submit(this::showCost);
        }
    }

    public void printerOff()
    {
        if(printing)
        {
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
            printing = false;
            printerOffTimestamp = System.currentTimeMillis();
        }
    }

    public void showCost()
    {
        while(printing)
        {
            Printer.INSTANCE.getNmsHandler().sendToolTipText(player, Message.WITHDRAW_MONEY.getMessage().replace("{NUM}", Double.toString(totalCost)));
            try
            {
                Thread.sleep(Printer.INSTANCE.getMainConfig().getCostNotificationTime() * 1000);
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
                Printer.INSTANCE.getMainConfig().getCostFormat().replace("{NUM}", Double.toString(0.0d))
        );
        cost.setScore(15);

        objective.getScore("").setScore(14); // Margin
        objective.getScore(Printer.INSTANCE.getMainConfig().getBlocksScoreTitle()).setScore(13);
        blocks = objective.getScore(
                Printer.INSTANCE.getMainConfig().getBlocksFormat().replace("{NUM}", Integer.toString(0))
        );
        blocks.setScore(12);

        objective.getScore(StringUtil.getSpaces(Printer.INSTANCE.getMainConfig().getScoreboardMargin())).setScore(11); // Margin
        objective.getScore(Printer.INSTANCE.getMainConfig().getBalanceScoreTitle()).setScore(10);
        balance = objective.getScore(
                Printer.INSTANCE.getMainConfig().getBalanceFormat().replace("{NUM}", Double.toString(Printer.INSTANCE.getBalance(player)))
        );
        balance.setScore(9);

        player.setScoreboard(board);
    }

    public void incrementCost(double cost)
    {
        this.totalCost += cost;
        this.totalBlocks++;

        // Update cost on scoreboard
        if(Printer.INSTANCE.getMainConfig().isScoreboardEnabled())
        {
            objective.getScoreboard().resetScores(this.cost.getEntry());
            this.cost = objective.getScore(
                    Printer.INSTANCE.getMainConfig().getCostFormat().replace("{NUM}", Double.toString(this.totalCost))
            );
            this.cost.setScore(15);

            objective.getScoreboard().resetScores(this.blocks.getEntry());
            this.blocks = objective.getScore(
                    Printer.INSTANCE.getMainConfig().getBlocksFormat().replace("{NUM}", Integer.toString(this.totalBlocks))
            );
            this.blocks.setScore(12);

            objective.getScoreboard().resetScores(this.balance.getEntry());
            this.balance = objective.getScore(
                    Printer.INSTANCE.getMainConfig().getBalanceFormat().replace("{NUM}", Double.toString(Printer.INSTANCE.getBalance(player)))
            );
            this.balance.setScore(9);
        }
    }

    public boolean isPlacedBlock(Block block)
    {
        return placedBlocks.contains(block);
    }

    public void removePlacedBlock(Block block)
    {
        placedBlocks.remove(block);
    }

    public void addPlacedBlock(Block block)
    {
        placedBlocks.add(block);
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
}
