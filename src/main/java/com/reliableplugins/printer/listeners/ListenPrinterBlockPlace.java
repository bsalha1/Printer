/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.listeners;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.PrinterPlayer;
import com.reliableplugins.printer.config.Message;
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
        PrinterPlayer player = PrinterPlayer.fromPlayer(event.getPlayer());
        if(player == null || !player.isPrinting())
        {
            return;
        }

        // Clear inventory from container block before placement
        if(event.getBlock().getState() instanceof InventoryHolder)
        {
            ((InventoryHolder) event.getBlock().getState()).getInventory().clear();
        }

        // Check if unplaceable
        if(Printer.INSTANCE.getMainConfig().isUnplaceable(event.getBlockPlaced().getType()))
        {
            event.setCancelled(true);
            Message.ERROR_BLOCK_PLACE_NOT_ALLOWED.sendColoredMessage(event.getPlayer());
            return;
        }

        // Check if player can build here
        if(Printer.INSTANCE.getClaimHookManager().isTerritoryRestricted(player.getPlayer(), event.getBlock().getLocation()))
        {
            event.setCancelled(true);
            Message.ERROR_NOT_IN_TERRITORY.sendColoredMessage(event.getPlayer());
            return;
        }

        // Get price of block to place
        Double price = Printer.INSTANCE.getBlockPrice(event.getPlayer(), event.getItemInHand());
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

        // Check if unplaceable
        if(Printer.INSTANCE.getMainConfig().isUnplaceable(event.getItem().getType()))
        {
            event.setCancelled(true);
            Message.ERROR_BLOCK_PLACE_NOT_ALLOWED.sendColoredMessage(player.getPlayer());
            return;
        }

        // Check if player can build here
        if(Printer.INSTANCE.getClaimHookManager().isTerritoryRestricted(player.getPlayer(),
                event.getClickedBlock() != null ? event.getClickedBlock().getLocation() : player.getPlayer().getLocation()))
        {
            event.setCancelled(true);
            Message.ERROR_NOT_IN_TERRITORY.sendColoredMessage(event.getPlayer());
            return;
        }

        // Get price
        ItemStack toPlace = event.getItem();
        Double price = Printer.INSTANCE.getItemBlockPrice(event.getPlayer(), toPlace);

        // Charge player
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
        else if (!BukkitUtil.isItemOfBlock(toPlace.getType())) // Bow shoot, snowball, egg, etc..
        {
            event.setCancelled(true);
            Message.ERROR_ITEM_PLACE_NOT_ALLOWED.sendColoredMessage(player.getPlayer());
        }

        // This then gets passed to BlockPlaceEvent above
    }
}
