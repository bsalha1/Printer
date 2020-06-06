package com.reliableplugins.printer.listeners;

import com.reliableplugins.printer.Printer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ListenPlayerQuit implements Listener
{
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        if(Printer.INSTANCE.printerPlayers.containsKey(event.getPlayer()))
        {
            Printer.INSTANCE.printerPlayers.get(event.getPlayer()).printerOff();
        }
    }
}
