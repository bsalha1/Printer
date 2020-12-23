package com.reliableplugins.printer.hook.territory.residence;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.PrinterPlayer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.task.BukkitTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ResidenceScanner extends BukkitTask
{
    public ResidenceScanner(long delay, long period)
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

            // If people near who aren't in same residence
            if(!Printer.INSTANCE.getMainConfig().allowNearNonResidentMembers() && Printer.INSTANCE.getResidenceHook().isNonTerritoryMemberNearby(player))
            {
                printerPlayer.printerOff();
                Message.ERROR_NON_RESIDENT_NEARBY.sendColoredMessage(player);
            }
            // If player isn't in their own residence and they aren't in wilderness
            else if(!Printer.INSTANCE.getResidenceHook().isInOwnTerritory(player) &&
                    (!Printer.INSTANCE.getMainConfig().allowInNonResidence() && !Printer.INSTANCE.getResidenceHook().isInATerritory(player)))
            {
                printerPlayer.printerOff();
                Message.ERROR_NOT_IN_RESIDENCE.sendColoredMessage(player);
            }
        }
    }
}
