package com.reliableplugins.printer.nms;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Version_1_8_R3 implements INMSHandler
{
    public void sendToolTipText(Player player, String message)
    {
        CraftPlayer p = (CraftPlayer) player;
        IChatBaseComponent chatComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(chatComponent,(byte)2);
        p.getHandle().playerConnection.sendPacket(packet);
    }
}
