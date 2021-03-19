/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.listeners;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.PrinterPlayer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.type.ColoredMaterial;
import com.reliableplugins.printer.utils.BukkitUtil;
import com.sun.org.apache.xml.internal.security.keys.content.keyvalues.ECKeyValue;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ListenPrinterBlockPlace implements Listener
{
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        PrinterPlayer player = PrinterPlayer.fromPlayer(event.getPlayer());
        if(player == null || !player.isPrinting())
        {
            return;
        }

        // Check if Unplaceable
        if(Printer.INSTANCE.getMainConfig().isUnplaceable(event.getBlockPlaced().getType()))
        {
            event.setCancelled(true);
            Message.ERROR_BLOCK_PLACE_NOT_ALLOWED.sendColoredMessage(event.getPlayer());
            return;
        }

        if(Printer.INSTANCE.isTerritoryRestricted(player.getPlayer(), event.getBlock().getLocation()))
        {
            event.setCancelled(true);
            Message.ERROR_NOT_IN_TERRITORY.sendColoredMessage(event.getPlayer());
            return;
        }

        // Get Price
        // - Prioritize colored price, then uncolored price, then shopgui price
        //   . We want to sell the blue wool for price of blue wool not for the price of uncolored wool
        //   . We want our prices.yml to overwrite ShopGUIPlus
        Double price = null;
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
        else if(Printer.INSTANCE.hasShopHook())
        {
            ItemStack toPlaceCopy = toPlace.clone();
            toPlaceCopy.setAmount(1);
            price = Printer.INSTANCE.getShopHook().getCachedPrice(toPlaceCopy);
            price = price < 0 ? null : price; // ShopGui returns -1 on invalid price... that would be bad if we put -1
        }

        // Clear inventory from inventory block before placement
        if(event.getBlock().getState() instanceof InventoryHolder)
        {
            ((InventoryHolder) event.getBlock().getState()).getInventory().clear();
        }

        if(price == null)
        {
            event.setCancelled(true);
            Message.ERROR_BLOCK_PLACE_NOT_ALLOWED.sendColoredMessage(event.getPlayer());
            return;
        }
        else if(!player.hasEnoughMoney(price))
        {
            Printer.INSTANCE.getNmsHandler().sendToolTipText(player.getPlayer(), Message.ERROR_NO_MONEY.getColoredMessage());
            event.setCancelled(true);
            return;
        }

        player.incrementCost(price);
        if(Printer.INSTANCE.getMainConfig().onlyBreakPlaced())
        {
            player.addPlacedBlock(event.getBlock());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        // Allow itemblocks like DIODE, COMPARATOR, etc...
        if(event.getItem() == null || event.getItem().getType().isBlock())
        {
            return;
        }

        PrinterPlayer player = PrinterPlayer.fromPlayer(event.getPlayer());
        if (player == null || !player.isPrinting())
        {
            return;
        }

        // Check if Unplaceable
        if(Printer.INSTANCE.getMainConfig().isUnplaceable(event.getItem().getType()))
        {
            event.setCancelled(true);
            Message.ERROR_BLOCK_PLACE_NOT_ALLOWED.sendColoredMessage(player.getPlayer());
            return;
        }

        if(Printer.INSTANCE.isTerritoryRestricted(player.getPlayer(),
                event.getClickedBlock() != null ? event.getClickedBlock().getLocation() : player.getPlayer().getLocation()))
        {
            event.setCancelled(true);
            Message.ERROR_NOT_IN_TERRITORY.sendColoredMessage(event.getPlayer());
            return;
        }

        // Get Price
        Double price = null;
        ItemStack toPlace = event.getItem();
        if(Printer.INSTANCE.getPricesConfig().getItemPrices().containsKey(toPlace.getType()))
        {
            price = Printer.INSTANCE.getPricesConfig().getItemPrices().get(toPlace.getType());
        }
        else if(Printer.INSTANCE.hasShopHook())
        {
            ItemStack toPlaceCopy = toPlace.clone();
            toPlaceCopy.setAmount(1);
            price = Printer.INSTANCE.getShopHook().getCachedPrice(toPlaceCopy);
            price = price < 0 ? null : price; // ShopGui returns -1 on invalid price... that would be bad if we put -1
        }

        // Charge Player
        if(price == null)
        {
            event.setCancelled(true);
            Message.ERROR_BLOCK_PLACE_NOT_ALLOWED.sendColoredMessage(player.getPlayer());
        }
        else if(BukkitUtil.isNoBlockPlaceItem(toPlace.getType()))
        {
            if(!player.hasEnoughMoney(price))
            {
                Printer.INSTANCE.getNmsHandler().sendToolTipText(player.getPlayer(), Message.ERROR_NO_MONEY.getColoredMessage());
                event.setCancelled(true);
            }
            else
            {
                player.incrementCost(price);
            }
        }
        else if(BukkitUtil.isItemBlock(toPlace.getType()))
        {

        }
        else // Bow shoot, snowball, egg, etc..
        {
            event.setCancelled(true);
            Message.ERROR_ITEM_PLACE_NOT_ALLOWED.sendColoredMessage(player.getPlayer());
        }
    }
}
