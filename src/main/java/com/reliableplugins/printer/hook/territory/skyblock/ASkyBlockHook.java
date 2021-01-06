package com.reliableplugins.printer.hook.territory.skyblock;

import com.reliableplugins.printer.hook.territory.TerritoryHook;
import com.reliableplugins.printer.utils.BukkitUtil;
import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ASkyBlockHook implements TerritoryHook
{
    private Location getIslandLocation(Player player)
    {
        return ASkyBlock.getPlugin().getPlayers().getIslandLocation(player.getUniqueId());
    }

    @Override
    public boolean isNonTerritoryMemberNearby(Player player)
    {
        Location playerIsland = getIslandLocation(player);
        for(Player nearbyPlayer : BukkitUtil.getNearbyPlayers(player))
        {
            Location nearbyPlayerIsland = getIslandLocation(nearbyPlayer);
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
}
