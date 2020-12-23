/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook.territory.factions;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.PrinterPlayer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.task.BukkitTask;
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
            PrinterPlayer printerPlayer = PrinterPlayer.fromPlayer(player);
            if(printerPlayer == null || !printerPlayer.isPrinting())
            {
                continue;
            }

            // If player isn't in their own faction and they aren't in wilderness (if they're allowed)
            if(!Printer.INSTANCE.getFactionsHook().isInOwnTerritory(player) &&
                    (Printer.INSTANCE.getFactionsHook().isInATerritory(player) || !Printer.INSTANCE.getMainConfig().allowInNonFaction()))
            {
                printerPlayer.printerOff();
                Message.ERROR_NOT_IN_TERRITORY.sendColoredMessage(player);
            }
            // If not allowed to print near non-faction members
            if(!Printer.INSTANCE.getMainConfig().allowNearNonFactionMembers() &&
                    Printer.INSTANCE.getFactionsHook().isNonTerritoryMemberNearby(player, Printer.INSTANCE.getMainConfig().allowNearAllies()))
            {
                printerPlayer.printerOff();
                Message.ERROR_NON_FACTION_MEMBER_NEARBY.sendColoredMessage(player);
            }
        }
    }
}
