package com.reliableplugins.printer.hook.territory.lands;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.PrinterPlayer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.task.BukkitTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LandsScanner extends BukkitTask
{
    public LandsScanner(long delay, long period)
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

            // If people near who aren't in same land
            if(!Printer.INSTANCE.getMainConfig().allowNearNonLandMembers() && Printer.INSTANCE.getLandsHook().isNonTerritoryMemberNearby(player))
            {
                printerPlayer.printerOff();
                Message.ERROR_NON_LAND_MEMBER_NEARBY.sendColoredMessage(player);
            }
            // If player isn't in their own land and they aren't in wilderness
            else if(!Printer.INSTANCE.getLandsHook().isInOwnTerritory(player) &&
                    (!Printer.INSTANCE.getMainConfig().allowInNonLand() && !Printer.INSTANCE.getLandsHook().isInATerritory(player)))
            {
                printerPlayer.printerOff();
                Message.ERROR_NOT_IN_LAND.sendColoredMessage(player);
            }
        }
    }
}
