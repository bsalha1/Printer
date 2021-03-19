/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook.territory;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.PrinterPlayer;
import com.reliableplugins.printer.task.BukkitTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TerritoryScanner extends BukkitTask
{
    public TerritoryScanner(long delay, long period)
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

            if(Printer.INSTANCE.isTerritoryRestricted(player))
            {
                printerPlayer.printerOff();
            }
        }
    }
}
