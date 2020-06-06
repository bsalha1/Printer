package com.reliableplugins.printer.listeners;

import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.event.FPlayerEnteredFactionEvent;
import com.massivecraft.factions.event.FPlayerLeaveEvent;
import com.massivecraft.factions.event.LandUnclaimAllEvent;
import com.massivecraft.factions.event.LandUnclaimEvent;
import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.type.PrinterPlayer;
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
    public void onLandUnclaim(LandUnclaimEvent event)
    {
        if(Printer.INSTANCE.getMainConfig().isOnlyInOwnTerritory())
        {
            disablePrinters(event.getLocation().getChunk());
        }
    }

    @EventHandler
    public void onLandUnclaimAll(LandUnclaimAllEvent event)
    {
        if(Printer.INSTANCE.getMainConfig().isOnlyInOwnTerritory())
        {
            for(FLocation fLocation : event.getFaction().getAllClaims())
            {
                disablePrinters(fLocation.getChunk());
            }
        }
    }

    @EventHandler
    public void FPlayerEnteredFaction(FPlayerEnteredFactionEvent event)
    {
        if(Printer.INSTANCE.getMainConfig().isOnlyInOwnTerritory() && !event.getFactionTo().equals(event.getfPlayer().getFaction()))
        {
            PrinterPlayer player = Printer.INSTANCE.printerPlayers.get(event.getfPlayer().getPlayer());
            if(player.isPrinting())
            {
                player.printerOff();
                player.getPlayer().sendMessage(Message.ERROR_NOT_IN_TERRITORY.getMessage());
            }
        }
    }

    public static void disableNearbyPrinters(Player player)
    {
        for(Entity entity : player.getNearbyEntities(48, 256, 48))
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
