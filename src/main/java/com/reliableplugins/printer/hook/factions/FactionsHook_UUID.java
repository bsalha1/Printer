/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook.factions;

import com.massivecraft.factions.*;
import com.massivecraft.factions.struct.Relation;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.entity.Player;

import java.util.List;

public class FactionsHook_UUID implements FactionsHook
{
    public boolean areNeutralOrEnemies(Player player1, Player player2)
    {
        FPlayer fPlayer1 = getFPlayer(player1);
        FPlayer fPlayer2 = getFPlayer(player2);
        return fPlayer1.getRelationTo(fPlayer2).equals(Relation.ENEMY) || fPlayer1.getRelationTo(fPlayer2).equals(Relation.NEUTRAL);
    }

    public boolean isEnemyOrNeutralNearby(Player player)
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

    public boolean inOwnTerritory(Player player)
    {
        FPlayer fPlayer = getFPlayer(player);
        FLocation fLocation = new FLocation(player.getWorld().getName(), player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
        Faction faction = Board.getInstance().getFactionAt(fLocation);

        return faction.equals(fPlayer.getFaction()) && !faction.isWilderness();
    }

    public boolean inWilderness(Player player)
    {
        FLocation fLocation = new FLocation(player.getWorld().getName(), player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
        Faction faction = Board.getInstance().getFactionAt(fLocation);

        return faction.isWilderness();
    }

    public boolean canBuild(Player player)
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
}
