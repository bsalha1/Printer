package com.reliableplugins.printer.hook.territory.skyblock;

/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.reliableplugins.printer.hook.territory.TerritoryHook;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class SuperiorSkyblockHook implements TerritoryHook
{
    @Override
    public boolean isNonTerritoryMemberNearby(Player player)
    {
        SuperiorPlayer sPlayer = SuperiorSkyblockAPI.getPlayer(player);
        List<Player> nearbyPlayers = BukkitUtil.getNearbyPlayers(player);

        if(nearbyPlayers.size() > 0 && (sPlayer == null || sPlayer.getIsland() == null))
        {
            return true;
        }

        for(Player nearbyPlayer : nearbyPlayers)
        {
            SuperiorPlayer nearbySPlayer = SuperiorSkyblockAPI.getPlayer(nearbyPlayer);
            if(nearbySPlayer == null || nearbySPlayer.getIsland() == null || !nearbySPlayer.getIsland().equals(sPlayer.getIsland()))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInATerritory(Player player)
    {
        Island currentIsland = SuperiorSkyblockAPI.getIslandAt(player.getLocation());
        return currentIsland != null;
    }

    @Override
    public boolean isInOwnTerritory(Player player)
    {
        Island currentIsland = SuperiorSkyblockAPI.getIslandAt(player.getLocation());
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);

        // If player isn't in an island or doesn't have an island, they aren't in their own island
        if(superiorPlayer == null || currentIsland == null)
        {
            return false;
        }
        return currentIsland.equals(superiorPlayer.getIsland());
    }

    @Override
    public boolean canBuild(Player player, Location location, boolean allowWilderness)
    {
        Island currentIsland = SuperiorSkyblockAPI.getIslandAt(location);
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);

        if(currentIsland == null)
        {
            return allowWilderness;
        }

        if(superiorPlayer == null)
        {
            return false;
        }

        return currentIsland.equals(superiorPlayer.getIsland());
    }
}

