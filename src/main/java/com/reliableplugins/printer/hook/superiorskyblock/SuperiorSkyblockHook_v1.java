package com.reliableplugins.printer.hook.superiorskyblock;

/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class SuperiorSkyblockHook_v1 implements SuperiorSkyblockHook
{
    public boolean canPlayerBuild(Player player, Location location)
    {
        Island locationIsland = SuperiorSkyblockAPI.getIslandAt(location);
        if(locationIsland == null)
        {
            return true;
        }

        return isMemberOfIsland(player, locationIsland);
    }

    public boolean isMemberOfIsland(Player player, Island island)
    {
        SuperiorPlayer superiorPlayer = getSuperiorPlayer(player);
        return island.getIslandMembers(true).contains(superiorPlayer);
    }

    public boolean isOnOwnIsland(Player player)
    {
        Island locationIsland = SuperiorSkyblockAPI.getIslandAt(player.getLocation());
        if(locationIsland == null)
        {
            return false;
        }

        return isMemberOfIsland(player, locationIsland);
    }

    public SuperiorPlayer getSuperiorPlayer(Player player)
    {
        return SuperiorSkyblockAPI.getPlayer(player.getUniqueId());
    }

    public Island getIsland(Player player)
    {
        return SuperiorSkyblockAPI.getPlayer(player.getUniqueId()).getIsland();
    }

    public boolean isNonIslandMemberNearby(Player player)
    {
        Island playerIsland = getIsland(player);
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
}

