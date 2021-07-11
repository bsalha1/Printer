package com.reliableplugins.printer.hook.territory.skyblock;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Island;
import com.iridium.iridiumskyblock.User;
import com.reliableplugins.printer.hook.territory.ClaimHook;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class IridiumSkyblockHook implements ClaimHook
{
    private static User getUser(Player player)
    {
        return IridiumSkyblock.getIslandManager().users.get(player.getUniqueId().toString());
    }

    @Override
    public boolean isClaimFriend(Player owner, Player accessor)
    {
        User user = getUser(owner);
        User nearbyUser = getUser(accessor);
        if(nearbyUser == null || user == null || user.getIsland() == null)
        {
            return false;
        }

        return user.getIsland().equals(nearbyUser.getIsland());
    }

    @Override
    public boolean isInAClaim(Player player)
    {
        return IridiumSkyblock.getIslandManager().getIslandViaLocation(player.getLocation()) != null;
    }

    @Override
    public boolean isInOwnClaim(Player player)
    {
        Island currentIsland = IridiumSkyblock.getIslandManager().getIslandViaLocation(player.getLocation());
        User user = getUser(player);
        if(user == null || currentIsland == null)
        {
            return false;
        }

        return currentIsland.equals(user.getIsland());
    }

    @Override
    public boolean canBuild(Player player, Location location, boolean allowWilderness)
    {
        Island currentIsland = IridiumSkyblock.getIslandManager().getIslandViaLocation(location);
        User user = getUser(player);

        if(currentIsland == null)
        {
            return allowWilderness;
        }

        if(user == null)
        {
            return false;
        }

        return currentIsland.equals(user.getIsland());
    }
}
