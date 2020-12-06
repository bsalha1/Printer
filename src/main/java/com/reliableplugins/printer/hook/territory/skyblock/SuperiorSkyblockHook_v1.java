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
import org.bukkit.entity.Player;

import java.util.List;

public class SuperiorSkyblockHook_v1 implements TerritoryHook
{

    public boolean isMemberOfIsland(Player player, Island island)
    {
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        if(superiorPlayer == null)
        {
            return false;
        }
        return superiorPlayer.getIsland().equals(island);
    }

    @Override
    public boolean isNonTerritoryMemberNearby(Player player)
    {
        Island playerIsland = SuperiorSkyblockAPI.getPlayer(player).getIsland();
        List<Player> nearbyPlayers = BukkitUtil.getNearbyPlayers(player);
        for(Player nearbyPlayer : nearbyPlayers)
        {
            if(!isMemberOfIsland(nearbyPlayer, playerIsland))
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
        return superiorPlayer.getIsland().equals(currentIsland);
    }
}

