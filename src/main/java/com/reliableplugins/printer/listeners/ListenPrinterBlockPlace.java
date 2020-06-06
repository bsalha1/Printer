package com.reliableplugins.printer.listeners;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.type.PrinterPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class ListenPrinterBlockPlace implements Listener
{
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if(Printer.INSTANCE.printerPlayers.containsKey(event.getPlayer()))
        {
            PrinterPlayer player = Printer.INSTANCE.printerPlayers.get(event.getPlayer());
            if(player.isPrinting())
            {
                // TODO: prohibited blocks or allowed blocks
                if(!Printer.INSTANCE.withdrawMoney(player.getPlayer(), 69))
                {
                    event.setCancelled(true);
                }
            }
        }
    }
}
