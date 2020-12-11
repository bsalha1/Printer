/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.nms;

import io.netty.channel.Channel;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Version_1_14_R1 implements INMSHandler
{
    public void sendToolTipText(Player player, String message)
    {
        CraftPlayer p = (CraftPlayer) player;
        IChatBaseComponent chatComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(chatComponent, ChatMessageType.GAME_INFO);
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
            if(pack.b().equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK))
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
