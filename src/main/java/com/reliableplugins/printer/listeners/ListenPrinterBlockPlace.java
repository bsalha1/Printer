package com.reliableplugins.printer.listeners;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.type.ColoredMaterial;
import com.reliableplugins.printer.type.PrinterPlayer;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

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
                Double price = null;

                // Prioritize colored price, then configured price, then shopgui price
                ItemStack toPlace = event.getItemInHand();
                ColoredMaterial coloredMaterial = ColoredMaterial.fromItemstack(toPlace);
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
                    ItemStack toPlaceCopy = toPlace.clone();
                    toPlaceCopy.setAmount(1);
                    price = Printer.INSTANCE.getShopGuiPlusHook().getPrice(toPlaceCopy);
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
                    Printer.INSTANCE.getNmsHandler().sendToolTipText(player.getPlayer(), Message.ERROR_NO_MONEY.getMessage());
                    event.setCancelled(true);
                    return;
                }
                player.incrementCost(price);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        // Allow itemblocks like DIODE, COMPARATOR, etc...
        if(event.getItem() != null && !event.getItem().getType().isBlock()
                && Printer.INSTANCE.printerPlayers.containsKey(event.getPlayer()))
        {
            PrinterPlayer player = Printer.INSTANCE.printerPlayers.get(event.getPlayer());

            if (player.isPrinting())
            {
//                Printer.LOGGER.logDebug("onPlayerInteract: " + event.getItem().getType());
                Double price = null;

                ItemStack toPlace = event.getItem();
                if(Printer.INSTANCE.getPricesConfig().getItemPrices().containsKey(toPlace.getType()))
                {
                    price = Printer.INSTANCE.getPricesConfig().getItemPrices().get(toPlace.getType());
                }
                else if(Printer.INSTANCE.isShopGuiPlus())
                {
                    ItemStack toPlaceCopy = toPlace.clone();
                    toPlaceCopy.setAmount(1);
                    price = Printer.INSTANCE.getShopGuiPlusHook().getPrice(toPlaceCopy);
                    price = price < 0 ? null : price; // ShopGui returns -1 on invalid price... that would be bad if we put -1
                }

                if(price == null)
                {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(Message.ERROR_BLOCK_NOT_ALLOWED.getMessage());
                }
                else if(BukkitUtil.isNoBlockPlaceItem(toPlace.getType()))
                {
                    if(!Printer.INSTANCE.withdrawMoney(player.getPlayer(), price))
                    {
                        Printer.INSTANCE.getNmsHandler().sendToolTipText(player.getPlayer(), Message.ERROR_NO_MONEY.getMessage());
                        event.setCancelled(true);
                    }
                    else
                    {
                        player.incrementCost(price);
                    }
                }
            }
        }
    }
}
