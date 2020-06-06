package com.reliableplugins.printer;

import com.reliableplugins.printer.listeners.AChannelListener;
import com.reliableplugins.printer.listeners.ChannelListener;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;

public class ChannelManager implements Listener
{
    public void unloadChannelListener(Player player)
    {
        if(Printer.INSTANCE.getNMS().getSocketChannel(player).pipeline().get(Printer.class.getName()) != null)
        {
            Printer.INSTANCE.getNMS().getSocketChannel(player).pipeline().remove(Printer.class.getName());
        }
    }

    public void unloadChannelListener()
    {
        Collection<? extends Player> onlinePlayers = Printer.INSTANCE.getServer().getOnlinePlayers();
        for(Player player : onlinePlayers)
        {
            unloadChannelListener(player);
        }
    }

    public void loadChannelListener(AChannelListener listener, Player player)
    {
        try
        {
            AChannelListener listenerCopy = (AChannelListener) listener.clone();
            listenerCopy.setPlayer(player);
            Printer.INSTANCE.getNMS().getSocketChannel(player).pipeline().addBefore("packet_handler", Printer.class.getName(), listenerCopy);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void loadChannelListener(AChannelListener listener)
    {
        Collection<? extends Player> onlinePlayers = Printer.INSTANCE.getServer().getOnlinePlayers();
        for(Player player : onlinePlayers)
        {
            loadChannelListener(listener, player);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        loadChannelListener(new ChannelListener(), event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event)
    {
        unloadChannelListener(event.getPlayer());
    }
}
