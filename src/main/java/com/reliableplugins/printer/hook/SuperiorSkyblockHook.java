package com.reliableplugins.printer.hook;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SuperiorSkyblockHook
{
    public static boolean canPlayerBuild(Player player, Location location)
    {
        Island locationIsland = SuperiorSkyblockAPI.getIslandAt(location);
        if(locationIsland == null)
        {
            return true;
        }

        return isMemberOfIsland(player, locationIsland);
    }

    public static boolean isMemberOfIsland(Player player, Island island)
    {
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player.getUniqueId());
        return island.getIslandMembers(true).contains(superiorPlayer);
    }
}
