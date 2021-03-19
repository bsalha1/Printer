package com.reliableplugins.printer.hook.territory.skyblock;

import com.reliableplugins.printer.hook.territory.TerritoryHook;
import com.reliableplugins.printer.utils.BukkitUtil;
import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ASkyBlockHook implements TerritoryHook
{
    private Location getIslandLocation(Player player)
    {
        return ASkyBlock.getPlugin().getPlayers().getIslandLocation(player.getUniqueId());
    }

    private Island getIsland(Player player)
    {
        return ASkyBlockAPI.getInstance().getIslandOwnedBy(player.getUniqueId());
    }

    @Override
    public boolean isNonTerritoryMemberNearby(Player player)
    {
        Island playerIsland = getIsland(player);
        for(Player nearbyPlayer : BukkitUtil.getNearbyPlayers(player))
        {
            Island nearbyPlayerIsland = getIsland(nearbyPlayer);
            if(playerIsland == null || !playerIsland.equals(nearbyPlayerIsland))
            {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isInATerritory(Player player)
    {
        return ASkyBlockAPI.getInstance().getIslandAt(player.getLocation()) != null;
    }

    @Override
    public boolean isInOwnTerritory(Player player)
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
