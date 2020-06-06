package com.reliableplugins.printer.listeners;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.hook.FactionsHook;
import com.reliableplugins.printer.task.BukkitTask;
import com.reliableplugins.printer.utils.ReflectUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import org.bukkit.Bukkit;

import java.util.UUID;

@ChannelHandler.Sharable
public class SocketChannelListener extends ASocketChannelListener
{
    @Override
    public void write(ChannelHandlerContext context, Object packet, ChannelPromise promise) throws Exception
    {
        if(packet instanceof PacketPlayOutNamedEntitySpawn)
        {
            PacketPlayOutNamedEntitySpawn pack = (PacketPlayOutNamedEntitySpawn) packet;
            UUID uid = ReflectUtil.getPrivateField("b", pack);
            if(FactionsHook.areNeutralOrEnemies(player, Bukkit.getPlayer(uid)))
            {
                if(Printer.INSTANCE.printerPlayers.containsKey(player))
                {
                    new BukkitTask(0)
                    {
                        @Override
                        public void run()
                        {
                            Printer.INSTANCE.printerPlayers.get(player).printerOff();
                            player.sendMessage(Message.ERROR_ENEMY_NEARBY_EXPLOIT.getMessage());
                        }
                    };
                }
            }
        }

        super.write(context, packet, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception
    {
        super.channelRead(channelHandlerContext, packet);
    }
}
