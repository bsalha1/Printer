/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.api;

import com.reliableplugins.printer.PrinterPlayer;
import org.bukkit.event.HandlerList;

public final class PrinterOffEvent extends PrinterEvent
{
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    public PrinterOffEvent(PrinterPlayer printerPlayer)
    {
        super(printerPlayer);
    }

    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    @Override
    public boolean isCancelled()
    {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b)
    {
        this.cancelled = b;
    }
}
