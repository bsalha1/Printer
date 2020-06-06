package com.reliableplugins.printer.listeners;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.type.Colored;
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
                // Get the price of the item - depends if its colored or not
                Colored coloredItem = Colored.fromItemstack(event.getItemInHand());
                Double price;
                if(coloredItem != null)
                {
                    price = Printer.INSTANCE.getPricesConfig().getColoredPrices().get(coloredItem);
                }
                else
                {
                    price = Printer.INSTANCE.getPricesConfig().getBlockPrices().get(event.getBlockPlaced().getType());
                }

                if(price == null)
                {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(Message.ERROR_BLOCK_NOT_ALLOWED.getMessage());
                }
                else if(!Printer.INSTANCE.withdrawMoney(player.getPlayer(), price))
                {
                    event.setCancelled(true);
                }
            }
        }
    }
}
