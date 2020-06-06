/*
 * Project: AntiSkid
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.nms;

import com.reliableplugins.printer.type.packet.PacketServerNamedEntitySpawn;
import com.reliableplugins.printer.utils.ReflectUtil;
import io.netty.channel.Channel;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Version_1_8_R3 implements INMSHandler
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

