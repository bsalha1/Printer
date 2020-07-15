package com.reliableplugins.printer.task;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.hook.FactionsHook;
import com.reliableplugins.printer.type.PrinterPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FactionScanner extends BukkitTask
{
    public FactionScanner(long delay, long period)
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
                    // If player is not allowed to build in wilderness and they're in wilderness, cancel printer
                    // Or if player is not allowed to build at their current location, cancel printer
                    if((!Printer.INSTANCE.getMainConfig().allowInWilderness() && FactionsHook.inWilderness(player)) || !FactionsHook.canBuild(player))
                    {
                        printerPlayer.printerOff();
                        player.sendMessage(Message.ERROR_NOT_IN_TERRITORY.getMessage());
                    }
                    else if(FactionsHook.isEnemyOrNeutralNearby(player))
                    {
                        printerPlayer.printerOff();
                        player.sendMessage(Message.ERROR_ENEMY_NEARBY.getMessage());
                    }
                }


            }
        }
    }
}
