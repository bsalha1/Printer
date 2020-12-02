package com.reliableplugins.printer.hook.residence;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.task.BukkitTask;
import com.reliableplugins.printer.type.PrinterPlayer;
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
            if(Printer.INSTANCE.printerPlayers.containsKey(player))
            {
                PrinterPlayer printerPlayer = Printer.INSTANCE.printerPlayers.get(player);
                if(printerPlayer.isPrinting())
                {
                    // If people near who aren't in same residence
                    if(!Printer.INSTANCE.getMainConfig().allowNearNonResidentMembers() && Printer.INSTANCE.getResidenceHook().isNonResidenceMemberNearby(player))
                    {
                        System.out.println("here");
                        printerPlayer.printerOff();
                        player.sendMessage(Message.ERROR_NON_RESIDENT_NEARBY.getMessage());
                    }
                    // If player isn't in their own residence and they aren't in wilderness
                    else if(!Printer.INSTANCE.getResidenceHook().isInOwnResidence(player) &&
                            (!Printer.INSTANCE.getMainConfig().allowInNonResidence() && !Printer.INSTANCE.getResidenceHook().isInAResidence(player)))
                    {
                        System.out.println("there");
                        printerPlayer.printerOff();
                        player.sendMessage(Message.ERROR_NOT_IN_RESIDENCE.getMessage());
                    }
                    System.out.println("bear");
                }
            }
        }
    }
}
