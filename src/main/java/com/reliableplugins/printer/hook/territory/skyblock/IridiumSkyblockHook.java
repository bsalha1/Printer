package com.reliableplugins.printer.hook.territory.skyblock;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Island;
import com.iridium.iridiumskyblock.User;
import com.reliableplugins.printer.hook.territory.TerritoryHook;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class IridiumSkyblockHook implements TerritoryHook
{
    private static User getUser(Player player)
    {
        return IridiumSkyblock.getIslandManager().users.get(player.getUniqueId().toString());
    }

    @Override
    public boolean isNonTerritoryMemberNearby(Player player)
    {
        User user = getUser(player);

        List<Player> nearbyPlayers = BukkitUtil.getNearbyPlayers(player);
        if(nearbyPlayers.size() > 0 && (user == null || user.getIsland() == null))
        {
            return true;
        }

        for(Player nearbyPlayer : nearbyPlayers)
        {
            User nearbyUser = getUser(nearbyPlayer);
            if(nearbyUser == null || nearbyUser.getIsland() == null || !nearbyUser.getIsland().equals(user.getIsland()))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInATerritory(Player player)
    {
        return IridiumSkyblock.getIslandManager().getIslandViaLocation(player.getLocation()) != null;
    }

    @Override
    public boolean isInOwnTerritory(Player player)
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
