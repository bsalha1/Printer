/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.listeners;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.PrinterPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ListenPlayerQuit implements Listener
{
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        PrinterPlayer player = PrinterPlayer.fromPlayer(event.getPlayer());
        if(player != null)
        {
            player.printerOff();
        }
    }
}
