package com.reliableplugins.printer.listeners;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.PrinterPlayer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.task.BukkitTask;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;

public class PacketListener extends ChannelDuplexHandler
{
    private final Player player;

    public PacketListener(Player player)
    {
        this.player = player;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception
    {
        super.write(ctx, packet, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception
    {
        // Catch when player attacks someone who is printing
        Player damagedPlayer = Printer.INSTANCE.getNmsHandler().processPacket(player, packet);
        if(damagedPlayer != null)
        {
            PrinterPlayer printerPlayer = PrinterPlayer.fromPlayer(damagedPlayer);
            if(printerPlayer != null && printerPlayer.isPrinting())
            {
                new BukkitTask(0)
                {
                    @Override
                    public void run()
                    {
                        printerPlayer.printerOff();
                        Message.PRINTER_OFF.sendColoredMessage(printerPlayer.getPlayer());
                    }
                };
            }
        }
        super.channelRead(ctx, packet);
    }

    public Player getPlayer()
    {
        return player;
    }
}
