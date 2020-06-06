package com.reliableplugins.printer.listeners;

import io.netty.channel.ChannelDuplexHandler;
import org.bukkit.entity.Player;

public abstract class ASocketChannelListener extends ChannelDuplexHandler implements Cloneable
{
    protected Player player;

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    public Player getPlayer()
    {
        return player;
    }

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
