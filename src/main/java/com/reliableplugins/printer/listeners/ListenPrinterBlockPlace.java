package com.reliableplugins.printer.listeners;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.hook.ShopGuiPlusHook;
import com.reliableplugins.printer.type.ColoredMaterial;
import com.reliableplugins.printer.type.PrinterPlayer;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
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
                Printer.LOGGER.logDebug("onBlockPlace: " + event.getBlock().getType());
                Double price = null;

                // Prioritize colored price, then configured price, then shopgui price
                ColoredMaterial coloredMaterial = ColoredMaterial.fromItemstack(event.getItemInHand());
                if(coloredMaterial != null && Printer.INSTANCE.getPricesConfig().getColoredPrices().containsKey(coloredMaterial))
                {
                    price = Printer.INSTANCE.getPricesConfig().getColoredPrices().get(coloredMaterial);
                }
                else if(Printer.INSTANCE.getPricesConfig().getBlockPrices().containsKey(event.getBlockPlaced().getType()))
                {
                    price = Printer.INSTANCE.getPricesConfig().getBlockPrices().get(event.getBlockPlaced().getType());
                }
                else if(Printer.INSTANCE.isShopGuiPlus())
                {
                    price = ShopGuiPlusHook.getPrice(event.getItemInHand());
                    price = price < 0 ? null : price; // ShopGui returns -1 on invalid price... that would be bad if we put -1
                }

                // Clear inventory from block before placement
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
