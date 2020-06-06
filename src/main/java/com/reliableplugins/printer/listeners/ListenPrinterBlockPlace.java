package com.reliableplugins.printer.listeners;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.type.ColoredMaterial;
import com.reliableplugins.printer.type.PrinterPlayer;
import org.bukkit.block.Container;
import org.bukkit.block.ContainerBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.InventoryHolder;

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
                Double price;

                // Prioritize the price of its colored material:
                // We want to sell SLAB:3 for price of SLAB:3, not SLAB
                ColoredMaterial coloredMaterial = ColoredMaterial.fromItemstack(event.getItemInHand());
                if(coloredMaterial != null)
                {
                    price = Printer.INSTANCE.getPricesConfig().getColoredPrices().get(coloredMaterial);
                    if(price == null)
                    {
                        price = Printer.INSTANCE.getPricesConfig().getBlockPrices().get(event.getBlockPlaced().getType());
                    }
                }
                else
                {
                    price = Printer.INSTANCE.getPricesConfig().getBlockPrices().get(event.getBlockPlaced().getType());
                }

                // Don't allow placing of blocks with inventories
                if(event.getBlock().getState() instanceof InventoryHolder)
                {
                    ((InventoryHolder) event.getBlock().getState()).getInventory().clear();
                }

                if(price == null)
                {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(Message.ERROR_BLOCK_NOT_ALLOWED.getMessage());
                    return;
                }
                else if(!Printer.INSTANCE.withdrawMoney(player.getPlayer(), price))
                {
                    event.setCancelled(true);
                    return;
                }
                player.incrementCost(price);
            }
        }
    }
}
