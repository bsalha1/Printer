package com.reliableplugins.printer.hook.territory.skyblock;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.PrinterPlayer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.task.BukkitTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SkyblockScanner extends BukkitTask
{
    public SkyblockScanner(long delay, long period)
    {
        super(delay, period);
    }

    @Override
    public void run()
    {
        for(Player player : Bukkit.getOnlinePlayers())
        {
            PrinterPlayer printerPlayer = PrinterPlayer.fromPlayer(player);
            if(printerPlayer == null || !printerPlayer.isPrinting())
            {
                continue;
            }

            // If printing is allowed on a non island and the player cannot build
            // Or if that player is not on their own island, turn printer off
            if(!Printer.INSTANCE.getSkyblockHook().isInOwnTerritory(player) &&
                    (!Printer.INSTANCE.getMainConfig().allowInNonIsland() && !Printer.INSTANCE.getSkyblockHook().isInATerritory(player)))
            {
                printerPlayer.printerOff();
                Message.ERROR_NOT_IN_ISLAND.sendColoredMessage(player);
            }
            else if(Printer.INSTANCE.getMainConfig().allowNearNonIslandMembers() && Printer.INSTANCE.getSkyblockHook().isNonTerritoryMemberNearby(player))
            {
                printerPlayer.printerOff();
                Message.ERROR_NON_ISLAND_MEMBER_NEARBY.sendColoredMessage(player);
            }
        }
    }
}