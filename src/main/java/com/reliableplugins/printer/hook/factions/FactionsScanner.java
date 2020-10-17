/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook.factions;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.task.BukkitTask;
import com.reliableplugins.printer.type.PrinterPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FactionsScanner extends BukkitTask
{
    public FactionsScanner(long delay, long period)
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
                    // If player is not allowed to print in wilderness and they're in wilderness, cancel printer
                    // Or if player is not allowed to build at their current location, cancel printer
                    if((!Printer.INSTANCE.getMainConfig().allowInWilderness() && Printer.INSTANCE.getFactionsHook().inWilderness(player))
                            || !Printer.INSTANCE.getFactionsHook().canBuild(player))
                    {
                        printerPlayer.printerOff();
                        player.sendMessage(Message.ERROR_NOT_IN_TERRITORY.getMessage());
                    }
                    else if(Printer.INSTANCE.getFactionsHook().isEnemyOrNeutralNearby(player))
                    {
                        printerPlayer.printerOff();
                        player.sendMessage(Message.ERROR_ENEMY_NEARBY.getMessage());
                    }
                }
            }
        }
    }
}
