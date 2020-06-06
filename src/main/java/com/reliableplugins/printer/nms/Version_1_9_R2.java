package com.reliableplugins.printer.nms;


import com.reliableplugins.printer.type.packet.PacketServerNamedEntitySpawn;
import com.reliableplugins.printer.utils.ReflectUtil;
import io.netty.channel.Channel;
import net.minecraft.server.v1_9_R2.PacketPlayOutNamedEntitySpawn;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Version_1_9_R2 implements INMSHandler
{
    @Override
    public Channel getSocketChannel(Player player)
    {
        return ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
    }

    @Override
    public PacketServerNamedEntitySpawn wrapPacketPlayOutNamedEntitySpawn(Object packet)
    {
        if(packet instanceof PacketPlayOutNamedEntitySpawn)
        {
            PacketPlayOutNamedEntitySpawn pack = (PacketPlayOutNamedEntitySpawn) packet;
            try
            {
                UUID uuid = ReflectUtil.getPrivateField("b", pack);
                return new PacketServerNamedEntitySpawn(uuid);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

}
