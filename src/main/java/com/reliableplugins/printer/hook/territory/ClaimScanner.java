/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook.territory;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.PrinterPlayer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.task.BukkitTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ClaimScanner extends BukkitTask
{
    public ClaimScanner(long delay, long period)
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

            ClaimRestriction restriction = Printer.INSTANCE.getClaimHookManager().canUsePrinter(player);
            switch (restriction)
            {
                case NOT_IN_OWN_TERRITORY:
                    printerPlayer.printerOff();
                    Message.ERROR_NOT_IN_TERRITORY.sendColoredMessage(player);
                    break;

                case NON_TERRITORY_MEMBER_NEARBY:
                    if(Printer.INSTANCE.getMainConfig().onlyDisableFly())
                    {
                        if(printerPlayer.getPlayer().getAllowFlight())
                        {
                            printerPlayer.getPlayer().setAllowFlight(false);
                            printerPlayer.getPlayer().setFlying(false);
                            Message.ERROR_FLY_DEACTIVATE.sendColoredMessage(player);
                        }
                    }
                    else
                    {
                        printerPlayer.printerOff();
                        Message.ERROR_NON_TERRITORY_MEMBER_NEARBY.sendColoredMessage(player);
                    }
                    break;

                    // If player still has fly deactivated, and no restrictions apply, then their flight must be reactivated
                case NONE:
                    if(!printerPlayer.getPlayer().getAllowFlight())
                    {
                        printerPlayer.getPlayer().setAllowFlight(true);
                        Message.FLY_REACTIVATE.sendColoredMessage(player);
                    }

            }
        }
    }
}
