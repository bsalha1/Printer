/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook.factions;

import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.entity.Player;

import java.util.List;

public class FactionsHook_UUID_v0_5_18 implements FactionsHook
{
    public boolean areNeutralOrEnemies(Player player1, Player player2)
    {
        com.massivecraft.factions.Faction faction1 = getFPlayer(player1).getFaction();
        com.massivecraft.factions.Faction faction2 = getFPlayer(player2).getFaction();
        return faction1.getRelationTo(faction2).equals(com.massivecraft.factions.perms.Relation.ENEMY) || faction1.getRelationTo(faction2).equals(com.massivecraft.factions.perms.Relation.NEUTRAL);
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
        com.massivecraft.factions.FPlayer fPlayer = getFPlayer(player);
        com.massivecraft.factions.FLocation fLocation = new com.massivecraft.factions.FLocation(player.getWorld().getName(), player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
        com.massivecraft.factions.Faction faction = com.massivecraft.factions.Board.getInstance().getFactionAt(fLocation);

        return faction.equals(fPlayer.getFaction()) && !faction.isWilderness();
    }

    public boolean inWilderness(Player player)
    {
        com.massivecraft.factions.FLocation fLocation = new com.massivecraft.factions.FLocation(player.getWorld().getName(), player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
        com.massivecraft.factions.Faction faction = com.massivecraft.factions.Board.getInstance().getFactionAt(fLocation);

        return faction.isWilderness();
    }

    public boolean canBuild(Player player)
    {
        com.massivecraft.factions.FLocation fLocation = new com.massivecraft.factions.FLocation(player.getWorld().getName(), player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
        com.massivecraft.factions.Faction faction = com.massivecraft.factions.Board.getInstance().getFactionAt(fLocation);
        if(faction.isWilderness())
        {
            return true;
        }
        com.massivecraft.factions.FPlayer fPlayer = getFPlayer(player);
        return fPlayer.getFaction().equals(faction);
    }

    public static com.massivecraft.factions.FPlayer getFPlayer(Player player)
    {
        return com.massivecraft.factions.FPlayers.getInstance().getByPlayer(player);
    }
}
