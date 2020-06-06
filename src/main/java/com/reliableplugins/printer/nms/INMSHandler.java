/*
 * Project: AntiSkid
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.nms;

import com.reliableplugins.printer.type.packet.PacketServerNamedEntitySpawn;
import io.netty.channel.Channel;
import org.bukkit.entity.Player;

public interface INMSHandler
{
    Channel getSocketChannel(Player player);

    PacketServerNamedEntitySpawn wrapPacketPlayOutNamedEntitySpawn(Object packet);
}