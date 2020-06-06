package com.reliableplugins.printer.listeners;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

@ChannelHandler.Sharable
public class ChannelListener extends AChannelListener
{
    @Override
    public void write(ChannelHandlerContext context, Object packet, ChannelPromise promise) throws Exception
    {
        System.out.println(packet.getClass());
//        ANMSHandler nmsHandler = AntiSkid.INSTANCE.getNMS();
//        Packet wrappedPacket = nmsHandler.getPacket(packet, player);

        super.write(context, packet, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception
    {
        super.channelRead(channelHandlerContext, packet);
    }
}
