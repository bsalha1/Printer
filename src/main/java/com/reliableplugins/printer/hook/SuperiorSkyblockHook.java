/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.task.BukkitTask;
import com.reliableplugins.printer.type.PrinterPlayer;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

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
        SuperiorPlayer superiorPlayer = getSuperiorPlayer(player);
        return island.getIslandMembers(true).contains(superiorPlayer);
    }

    public static boolean isOnOwnIsland(Player player)
    {
        Island locationIsland = SuperiorSkyblockAPI.getIslandAt(player.getLocation());
        if(locationIsland == null)
        {
            return false;
        }

        return isMemberOfIsland(player, locationIsland);
    }

    public static SuperiorPlayer getSuperiorPlayer(Player player)
    {
        return SuperiorSkyblockAPI.getPlayer(player.getUniqueId());
    }

    public static Island getIsland(Player player)
    {
        return SuperiorSkyblockAPI.getPlayer(player.getUniqueId()).getIsland();
    }

    public static boolean isNonIslandMemberNearby(Player player)
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

    public static class SuperiorSkyBlockScanner extends BukkitTask
    {
        public SuperiorSkyBlockScanner(long delay, long period)
        {
            super(delay, period);
        }

        @Override
        public void run()
        {
            for(Player player : Bukkit.getOnlinePlayers())
            {
                if(Printer.INSTANCE.printerPlayers.containsKey(player))
                {
                    PrinterPlayer printerPlayer = Printer.INSTANCE.printerPlayers.get(player);
                    if(printerPlayer.isPrinting())
                    {
                        // If printing is allowed on a non island and the player cannot build
                        // Or if that player is not on their own island, turn printer off
                        if((Printer.INSTANCE.getMainConfig().allowInNonIsland() && !SuperiorSkyblockHook.canPlayerBuild(player, player.getLocation()))
                                || !SuperiorSkyblockHook.isOnOwnIsland(player))
                        {
                            printerPlayer.printerOff();
                            player.sendMessage(Message.ERROR_NOT_IN_ISLAND.getMessage());
                        }
                        else if(Printer.INSTANCE.getMainConfig().allowNearNonIslandMembers() && SuperiorSkyblockHook.isNonIslandMemberNearby(player))
                        {
                            printerPlayer.printerOff();
                            player.sendMessage(Message.ERROR_NON_ISLAND_MEMBER_NEARBY.getMessage());
                        }
                    }
                }
            }
        }
    }
}
