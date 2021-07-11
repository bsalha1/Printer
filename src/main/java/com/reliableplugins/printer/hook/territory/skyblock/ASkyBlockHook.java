package com.reliableplugins.printer.hook.territory.skyblock;

import com.reliableplugins.printer.hook.territory.ClaimHook;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ASkyBlockHook implements ClaimHook
{
    private Island getIsland(Player player)
    {
        return ASkyBlockAPI.getInstance().getIslandOwnedBy(player.getUniqueId());
    }

    @Override
    public boolean isClaimFriend(Player owner, Player accessor)
    {
        Island playerIsland = getIsland(owner);
        Island nearbyPlayerIsland = getIsland(accessor);
        if(playerIsland == null)
        {
            return false;
        }

        return playerIsland.equals(nearbyPlayerIsland);
    }

    @Override
    public boolean isInAClaim(Player player)
    {
        return ASkyBlockAPI.getInstance().getIslandAt(player.getLocation()) != null;
    }

    @Override
    public boolean isInOwnClaim(Player player)
    {
        return ASkyBlockAPI.getInstance().playerIsOnIsland(player);
    }

    @Override
    public boolean canBuild(Player player, Location location, boolean allowWilderness)
    {
        Island playerIsland = getIsland(player);
        Island currentIsland = ASkyBlockAPI.getInstance().getIslandAt(player.getLocation());

        if(currentIsland == null)
        {
            return allowWilderness;
        }

        if(playerIsland == null)
        {
            return false;
        }

        return currentIsland.equals(playerIsland);
    }
}
