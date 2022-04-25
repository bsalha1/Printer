/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.nms;

import io.netty.channel.Channel;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import net.minecraft.network.protocol.game.PacketPlayOutChat;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.EnumHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemArmor;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Version_1_18_R2 implements INMSHandler
{
    private final static UUID ZERO_UUID;

    // Fields and methods only accessible in 1_18_R2
    private static Field channelField;
    private static Method getBukkitEntityMethod;
    private static Method sendPacketMethod;

    public void sendToolTipText(Player player, String message)
    {
        CraftPlayer p = (CraftPlayer) player;
        IChatBaseComponent chatComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");

        PacketPlayOutChat packet = new PacketPlayOutChat(chatComponent, ChatMessageType.c, ZERO_UUID);
        try
        {
            sendPacketMethod.invoke(p.getHandle().b, packet);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public Channel getSocketChannel(Player player)
    {
        CraftPlayer p = (CraftPlayer) player;
        
        try 
        {
            return (Channel) channelField.get(p.getHandle().b.a);
        }
        catch(IllegalAccessException e) 
        {
            e.printStackTrace();
        }

        return null;
    }

    public Player processPacket(Player player, Object packet)
    {
        final Player[] attackedPlayer = {null};

        if(packet instanceof PacketPlayInUseEntity)
        {
            PacketPlayInUseEntity pack = (PacketPlayInUseEntity) packet;
            pack.a(new PacketPlayInUseEntity.c()
            {
                // Interact
                @Override
                public void a(EnumHand enumHand) {}

                // Interact At
                @Override
                public void a(EnumHand enumHand, Vec3D vec3D) {}

                // Attack
                @Override
                public void a() {
                    Entity damaged = pack.a(((CraftWorld) player.getWorld()).getHandle());
                    if(damaged instanceof EntityPlayer)
                    {
                        try
                        {
                            attackedPlayer[0] = (Player) getBukkitEntityMethod.invoke(damaged);
                        }
                        catch(Exception e) 
                        {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        return attackedPlayer[0];
    }

    @Override
    public boolean isArmor(ItemStack itemStack)
    {
        return CraftItemStack.asNMSCopy(itemStack).getItem() instanceof ItemArmor;
    }

    static 
    {
        ZERO_UUID = new UUID(0, 0);

        try 
        {
            channelField = NetworkManager.class.getField("m");
        }
        catch(Exception e)
        {
            channelField = null;
            e.printStackTrace();
        }

        try
        {
            getBukkitEntityMethod = EntityPlayer.class.getMethod("getBukkitEntity");
        }
        catch(Exception e)
        {
            getBukkitEntityMethod = null;
            e.printStackTrace();
        }

        try
        {
            sendPacketMethod = PlayerConnection.class.getMethod("a", Packet.class);
        }
        catch(Exception e)
        {
            sendPacketMethod = null;
            e.printStackTrace();
        }
    }
}
