/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.nms;

import io.netty.channel.Channel;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Version_1_8_R3 implements INMSHandler
{
    public void sendToolTipText(Player player, String message)
    {
        CraftPlayer p = (CraftPlayer) player;
        IChatBaseComponent chatComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(chatComponent,(byte)2);
        p.getHandle().playerConnection.sendPacket(packet);
    }

    public Channel getSocketChannel(Player player)
    {
        CraftPlayer p = (CraftPlayer) player;
        return p.getHandle().playerConnection.a().channel;
    }

    public Player processPacket(Player player, Object packet)
    {
        if(packet instanceof PacketPlayInUseEntity)
        {
            PacketPlayInUseEntity pack = (PacketPlayInUseEntity) packet;
            if(pack.a().equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK))
            {
                Entity damaged = pack.a(((CraftWorld) player.getWorld()).getHandle());
                if(damaged instanceof EntityPlayer)
                {
                    return ((EntityPlayer) damaged).getBukkitEntity();
                }
            }
        }
        return null;
    }

    @Override
    public boolean isArmor(ItemStack itemStack)
    {
        return CraftItemStack.asNMSCopy(itemStack).getItem() instanceof ItemArmor;
    }
}
