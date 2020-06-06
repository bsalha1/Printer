package com.reliableplugins.printer.type;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.hook.FactionsHook;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.*;

public class PrinterPlayer
{
    private GameMode initialGamemode;
    private ItemStack[] initialInventory;
    private ItemStack[] initialArmor;
    private final Player player;
    private boolean printing = false;
    private double totalCost;

    private Scoreboard board;
    private Objective objective;
    private Score score;

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
            if(Printer.INSTANCE.getMainConfig().isScoreboard())
            {
                board = Bukkit.getScoreboardManager().getNewScoreboard();
                objective = board.registerNewObjective("test", "dummy");
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                objective.setDisplayName(BukkitUtil.color("&d&lPrinter"));
                score = objective.getScore(ChatColor.WHITE + "Cost:");
                score.setScore(0);
                player.setScoreboard(board);
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
        }
    }

    public void printerOff()
    {
        if(printing)
        {
            if(Printer.INSTANCE.getMainConfig().isScoreboard())
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

    public boolean areEnemiesOrNeutralsNearby()
    {
        if(Printer.INSTANCE.isFactions())
        {
            for(Entity entity : player.getNearbyEntities(48, 256, 48))
            {
                if(entity instanceof Player && FactionsHook.areNeutralOrEnemies((Player) entity, player))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public void incrementCost(double cost)
    {
        this.totalCost += cost;
        if(Printer.INSTANCE.getMainConfig().isScoreboard())
        {
            this.score.setScore((int)Math.ceil(this.totalCost));
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
