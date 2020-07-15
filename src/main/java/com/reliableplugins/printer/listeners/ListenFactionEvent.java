package com.reliableplugins.printer.listeners;

import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.event.*;
import com.massivecraft.factions.struct.Relation;
import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.hook.FactionsHook;
import com.reliableplugins.printer.type.PrinterPlayer;
import com.reliableplugins.printer.utils.MinecraftUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ListenFactionEvent implements Listener
{
    @EventHandler
    public void onFPlayerLeave(FPlayerLeaveEvent event)
    {
        disableNearbyPrinters(event.getfPlayer().getPlayer());
    }

    @EventHandler
    public void onLandClaim(LandClaimEvent event)
    {
        // On land claim, if this isn't the person's faction, kick them out of printer
        Chunk chunk = event.getLocation().getChunk();
        for(Entity entity : chunk.getEntities())
        {
            if(entity instanceof Player)
            {
                Player player = (Player) entity;
                if(!FactionsHook.getFPlayer(player).getFaction().equals(event.getFaction()) && Printer.INSTANCE.printerPlayers.containsKey(player))
                {
                    PrinterPlayer printer = Printer.INSTANCE.printerPlayers.get(player);
                    if(printer.isPrinting())
                    {
                        printer.printerOff();
                        player.sendMessage(Message.ERROR_NOT_IN_TERRITORY.getMessage());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onLandUnclaim(LandUnclaimEvent event)
    {
        if(!Printer.INSTANCE.getMainConfig().allowInWilderness())
        {
            disablePrinters(event.getLocation().getChunk());
        }
    }

    @EventHandler
    public void onLandUnclaimAll(LandUnclaimAllEvent event)
    {
        if(!Printer.INSTANCE.getMainConfig().allowInWilderness())
        {
            for(FLocation fLocation : event.getFaction().getAllClaims())
            {
                disablePrinters(fLocation.getChunk());
            }
        }
    }

    @EventHandler
    public void onFPlayerEnteredFaction(FPlayerEnteredFactionEvent event)
    {
        // If player cannot build in this territory, don't allow them
        if(!FactionsHook.canBuild(event.getfPlayer().getPlayer(), event.getFactionTo()))
        {
            PrinterPlayer player = Printer.INSTANCE.printerPlayers.get(event.getfPlayer().getPlayer());
            if(player != null && player.isPrinting())
            {
                player.printerOff();
                player.getPlayer().sendMessage(Message.ERROR_NOT_IN_TERRITORY.getMessage());
            }
        }
        else if(!Printer.INSTANCE.getMainConfig().allowInWilderness() && !event.getFactionTo().equals(event.getfPlayer().getFaction()))
        {
            PrinterPlayer player = Printer.INSTANCE.printerPlayers.get(event.getfPlayer().getPlayer());
            if(player != null && player.isPrinting())
            {
                player.printerOff();
                player.getPlayer().sendMessage(Message.ERROR_NOT_IN_TERRITORY.getMessage());
            }
        }
    }

    @EventHandler
    public void onFactionRelation(FactionRelationEvent event)
    {
        if(event.getRelation().equals(Relation.ENEMY) || event.getRelation().equals(Relation.NEUTRAL))
        {
            for(FPlayer player : event.getFaction().getFPlayers())
            {
                disableNearbyPrinters(player.getPlayer());
            }
            for(FPlayer player : event.getTargetFaction().getFPlayers())
            {
                disableNearbyPrinters(player.getPlayer());
            }
        }
    }

    public static void disableNearbyPrinters(Player player)
    {
        if(player == null)
        {
            return;
        }
        for(Entity entity : player.getNearbyEntities(MinecraftUtil.getPlayerLoadDistance(player.getWorld()), 256, MinecraftUtil.getPlayerLoadDistance(player.getWorld())))
        {
            if(entity instanceof Player)
            {
                Player nearbyPlayer = (Player) entity;

                // If they are printing, turn them off
                PrinterPlayer nearbyPrinter = Printer.INSTANCE.printerPlayers.get(nearbyPlayer);
                if(nearbyPrinter != null && nearbyPrinter.isPrinting())
                {
                    nearbyPrinter.printerOff();
                    nearbyPlayer.sendMessage(Message.ERROR_ENEMY_NEARBY.getMessage());
                }

                // If we are printing turn us off
                PrinterPlayer printer = Printer.INSTANCE.printerPlayers.get(player);
                if(printer != null && printer.isPrinting())
                {
                    printer.printerOff();
                    player.sendMessage(Message.ERROR_ENEMY_NEARBY.getMessage());
                }
            }
        }
    }

    public static void disablePrinters(Chunk chunk)
    {
        for(Entity entity : chunk.getEntities())
        {
            if(entity instanceof Player)
            {
                Player player = (Player) entity;
                PrinterPlayer printer = Printer.INSTANCE.printerPlayers.get(player);
                if(printer != null && printer.isPrinting())
                {
                    printer.printerOff();
                    player.sendMessage(Message.ERROR_NOT_IN_TERRITORY.getMessage());
                }
            }
        }
    }
}
