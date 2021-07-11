package com.reliableplugins.printer.hook.territory.skyblock;

/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.reliableplugins.printer.hook.territory.ClaimHook;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SuperiorSkyblockHook implements ClaimHook
{
    @Override
    public boolean isClaimFriend(Player owner, Player accessor)
    {
        SuperiorPlayer sPlayer = SuperiorSkyblockAPI.getPlayer(owner);
        SuperiorPlayer nearbySPlayer = SuperiorSkyblockAPI.getPlayer(accessor);
        if(nearbySPlayer == null || sPlayer == null || sPlayer.getIsland() == null)
        {
            return false;
        }

        return sPlayer.getIsland().equals(nearbySPlayer.getIsland());
    }

    @Override
    public boolean isInAClaim(Player player)
    {
        Island currentIsland = SuperiorSkyblockAPI.getIslandAt(player.getLocation());
        return currentIsland != null;
    }

    @Override
    public boolean isInOwnClaim(Player player)
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

