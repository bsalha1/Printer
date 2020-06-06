package com.reliableplugins.printer.nms;

import io.netty.channel.Channel;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Version_1_10_R1 implements INMSHandler
{
    @Override
    public Channel getSocketChannel(Player player)
    {
        return ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
    }

}
