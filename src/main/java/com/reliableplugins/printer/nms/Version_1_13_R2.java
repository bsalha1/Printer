/*
 * Project: AntiSkid
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.nms;

import io.netty.channel.Channel;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Version_1_13_R2 implements INMSHandler
{
    @Override
    public Channel getSocketChannel(Player player)
    {
        return ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
    }

}

