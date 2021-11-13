/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.nms;

import com.reliableplugins.printer.utils.ReflectUtil;
import io.netty.channel.Channel;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import net.minecraft.network.protocol.game.PacketPlayOutChat;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.EnumHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemArmor;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Version_1_17_R1 implements INMSHandler
{
    public void sendToolTipText(Player player, String message)
    {
        CraftPlayer p = (CraftPlayer) player;
        IChatBaseComponent chatComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(chatComponent, ChatMessageType.c, UUID.randomUUID());
        p.getHandle().b.sendPacket(packet);
    }

    public Channel getSocketChannel(Player player)
    {
        CraftPlayer p = (CraftPlayer) player;
        return p.getHandle().b.a.k;
    }

    public Player processPacket(Player player, Object packet)
    {
        final Player[] attackedPlayer = {null};

        if(packet instanceof PacketPlayInUseEntity)
        {
            PacketPlayInUseEntity pack = (PacketPlayInUseEntity) packet;
            pack.a(new PacketPlayInUseEntity.c()
            {
                // Attack
                @Override
                public void a(EnumHand enumHand)
                {
                    Entity damaged = pack.a(((CraftWorld) player.getWorld()).getHandle());
                    if(damaged instanceof EntityPlayer)
                    {
                        attackedPlayer[0] = ((EntityPlayer) damaged).getBukkitEntity();
                    }
                }

                // Interact At
                @Override
                public void a(EnumHand enumHand, Vec3D vec3D) {}

                // Interact
                @Override
                public void a() {}
            });
        }
        return attackedPlayer[0];
    }

    @Override
    public boolean isArmor(ItemStack itemStack)
    {
        return CraftItemStack.asNMSCopy(itemStack).getItem() instanceof ItemArmor;
    }
}
