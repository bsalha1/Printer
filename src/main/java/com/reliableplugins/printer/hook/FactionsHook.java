/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook;

import com.massivecraft.factions.*;
import com.massivecraft.factions.struct.Relation;
import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.task.BukkitTask;
import com.reliableplugins.printer.type.PrinterPlayer;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class FactionsHook
{
    public static boolean areNeutralOrEnemies(Player player1, Player player2)
    {
        FPlayer fPlayer1 = getFPlayer(player1);
        FPlayer fPlayer2 = getFPlayer(player2);
        return fPlayer1.getRelationTo(fPlayer2).equals(Relation.ENEMY) || fPlayer1.getRelationTo(fPlayer2).equals(Relation.NEUTRAL);
    }

    public static boolean isEnemyOrNeutralNearby(Player player)
    {
        List<Player> nearbyPlayers = BukkitUtil.getNearbyPlayers(player);
        for(Player nearbyPlayer : nearbyPlayers)
        {
            if(areNeutralOrEnemies(player, nearbyPlayer))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean inOwnTerritory(Player player)
    {
        FPlayer fPlayer = getFPlayer(player);
        FLocation fLocation = new FLocation(player.getWorld().getName(), player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
        Faction faction = Board.getInstance().getFactionAt(fLocation);

        return faction.equals(fPlayer.getFaction()) && !faction.isWilderness();
    }

    public static boolean inWilderness(Player player)
    {
        FLocation fLocation = new FLocation(player.getWorld().getName(), player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
        Faction faction = Board.getInstance().getFactionAt(fLocation);

        return faction.isWilderness();
    }

    public static boolean canBuild(Player player)
    {
        FLocation fLocation = new FLocation(player.getWorld().getName(), player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
        Faction faction = Board.getInstance().getFactionAt(fLocation);
        if(faction.isWilderness())
        {
            return true;
        }
        FPlayer fPlayer = getFPlayer(player);
        return fPlayer.getFaction().equals(faction);
    }

    public static FPlayer getFPlayer(Player player)
    {
        return FPlayers.getInstance().getByPlayer(player);
    }

    public static class FactionScanner extends BukkitTask
    {
        public FactionScanner(long delay, long period)
        {
            super(delay, period);
        }

        @Override
        public void run()
        {
            for(Player player : Bukkit.getOnlinePlayers())
            {
                if(Printer.INSTANCE.printerPlayers.containsKey(player))
                {
                    PrinterPlayer printerPlayer = Printer.INSTANCE.printerPlayers.get(player);
                    if(printerPlayer.isPrinting())
                    {
                        // If player is not allowed to build in wilderness and they're in wilderness, cancel printer
                        // Or if player is not allowed to build at their current location, cancel printer
                        if((!Printer.INSTANCE.getMainConfig().allowInWilderness() && FactionsHook.inWilderness(player)) || !FactionsHook.canBuild(player))
                        {
                            printerPlayer.printerOff();
                            player.sendMessage(Message.ERROR_NOT_IN_TERRITORY.getMessage());
                        }
                        else if(FactionsHook.isEnemyOrNeutralNearby(player))
                        {
                            printerPlayer.printerOff();
                            player.sendMessage(Message.ERROR_ENEMY_NEARBY.getMessage());
                        }
                    }


                }
            }
        }
    }
}
