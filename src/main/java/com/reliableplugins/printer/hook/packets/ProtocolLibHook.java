package com.reliableplugins.printer.hook.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.PrinterPlayer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.task.BukkitTask;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ProtocolLibHook
{
    private final ProtocolManager manager;

    public ProtocolLibHook()
    {
        this.manager = ProtocolLibrary.getProtocolManager();
    }

    public void registerListeners()
    {
        this.manager.addPacketListener(
            new PacketAdapter(Printer.INSTANCE, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
                @Override
                public void onPacketReceiving(PacketEvent event)
                {
                    if (event.getPacketType() != PacketType.Play.Client.USE_ENTITY)
                    {
                        return;
                    }

                    Entity entity;

                    // Fixes spamming an exception from ProtocolLib
                    try 
                    {
                        entity = event.getPacket().getEntityModifier(event.getPlayer().getWorld()).read(0);
                    }
                    catch(Exception e) {
                        return;
                    }

                    if(!(entity instanceof Player))
                    {
                        return;
                    }

                    PrinterPlayer printerPlayer = PrinterPlayer.fromPlayer((Player) entity);
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
            });
    }

}
