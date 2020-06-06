package com.reliableplugins.printer;

import com.reliableplugins.printer.listeners.ASocketChannelListener;
import com.reliableplugins.printer.listeners.SocketChannelListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;

public class SocketChannelManager implements Listener
{
    public void unloadChannelListener(Player player)
    {
        if(Printer.INSTANCE.getNMSHandler().getSocketChannel(player).pipeline().get(Printer.class.getName()) != null)
        {
            Printer.INSTANCE.getNMSHandler().getSocketChannel(player).pipeline().remove(Printer.class.getName());
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

    public void loadChannelListener(ASocketChannelListener listener, Player player)
    {
        try
        {
            ASocketChannelListener listenerCopy = (ASocketChannelListener) listener.clone();
            listenerCopy.setPlayer(player);
            Printer.INSTANCE.getNMSHandler().getSocketChannel(player).pipeline().addBefore("packet_handler", Printer.class.getName(), listenerCopy);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void loadChannelListener(ASocketChannelListener listener)
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
        loadChannelListener(new SocketChannelListener(), event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event)
    {
        unloadChannelListener(event.getPlayer());
    }
}
