package com.reliableplugins.printer.nms;

import net.minecraft.server.v1_13_R1.ChatMessageType;
import net.minecraft.server.v1_13_R1.IChatBaseComponent;
import net.minecraft.server.v1_13_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Version_1_13_R1 implements INMSHandler
{
    public void sendToolTipText(Player player, String message)
    {
        CraftPlayer p = (CraftPlayer) player;
        IChatBaseComponent chatComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(chatComponent, ChatMessageType.GAME_INFO);
        p.getHandle().playerConnection.sendPacket(packet);
    }
}
