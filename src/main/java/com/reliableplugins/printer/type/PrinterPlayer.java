package com.reliableplugins.printer.type;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.concurrent.Executors;

public class PrinterPlayer
{
    private GameMode initialGamemode;
    private ItemStack[] initialInventory;
    private ItemStack[] initialArmor;

    private final Player player;
    private boolean printing = false;
    private double totalCost;

    // Scoreboard values
    private Score costScore;
    private Score blocksPlacedScore;

    public PrinterPlayer(Player player)
    {
        this.player = player;
        this.initialGamemode = player.getGameMode();
        this.initialInventory = player.getInventory().getContents();
        this.initialArmor = player.getInventory().getArmorContents();
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

            totalCost = 0;

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
            printing = false;
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
        Objective objective = board.registerNewObjective("test", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(BukkitUtil.color("&d&lPrinter"));

        costScore = objective.getScore(BukkitUtil.color("&7Cost&f:"));
        blocksPlacedScore = objective.getScore(BukkitUtil.color("&7Blocks Placed&f:"));
        costScore.setScore(0);
        blocksPlacedScore.setScore(0);

        player.setScoreboard(board);
    }

    public void incrementCost(double cost)
    {
        this.totalCost += cost;

        // Update cost on scoreboard
        if(Printer.INSTANCE.getMainConfig().isScoreboardEnabled())
        {
            this.costScore.setScore((int)Math.ceil(this.totalCost));
            this.blocksPlacedScore.setScore(blocksPlacedScore.getScore() + 1);
        }
    }

    public double getTotalCost()
    {
        return totalCost;
    }

    public Player getPlayer()
    {
        return player;
    }

    public boolean isPrinting()
    {
        return printing;
    }
}
