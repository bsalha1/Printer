package com.reliableplugins.printer.listeners;

import com.bgsoftware.superiorskyblock.api.events.IslandEnterEvent;
import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.hook.SuperiorSkyblockHook;
import com.reliableplugins.printer.type.PrinterPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ListenSkyblockEvent implements Listener
{
    @EventHandler
    public void onIslandEnter(IslandEnterEvent event)
    {
        if(Printer.INSTANCE.printerPlayers.containsKey(event.getPlayer().asPlayer()))
        {
            PrinterPlayer player = Printer.INSTANCE.printerPlayers.get(event.getPlayer().asPlayer());
            if(player.isPrinting() && SuperiorSkyblockHook.isMemberOfIsland(event.getPlayer().asPlayer(), event.getIsland()))
            {
                player.printerOff();
                player.getPlayer().sendMessage(Message.ERROR_NOT_IN_ISLAND.getMessage());
            }
        }
    }
}
