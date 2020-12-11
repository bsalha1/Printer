package com.reliableplugins.printer.listeners;

import com.reliableplugins.printer.Printer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PacketListenerManager implements Listener
{
    private final static String NAME = "PrinterInjector";

    public void addAllOnline()
    {
        for(Player player : Bukkit.getOnlinePlayers())
        {
            addPlayer(player);
        }
    }

    public void addPlayer(Player player)
    {
        if(Printer.INSTANCE.getNmsHandler().getSocketChannel(player).pipeline().get(NAME) == null)
        {
            PacketListener packetListener = new PacketListener(player);
            Printer.INSTANCE.getNmsHandler().getSocketChannel(player).pipeline().addBefore("packet_handler", NAME, packetListener);
        }
    }

    public void removePlayer(Player player)
    {
        if(Printer.INSTANCE.getNmsHandler().getSocketChannel(player).pipeline().get(NAME) != null)
        {
            Printer.INSTANCE.getNmsHandler().getSocketChannel(player).pipeline().remove(NAME);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        removePlayer(event.getPlayer());
    }
}
