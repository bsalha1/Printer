/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.api;

import com.reliableplugins.printer.PrinterPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public abstract class PrinterEvent extends Event implements Cancellable
{
    private final PrinterPlayer printerPlayer;

    public PrinterEvent(PrinterPlayer printerPlayer)
    {
        this.printerPlayer = printerPlayer;
    }

    public PrinterPlayer getPrinterPlayer()
    {
        return printerPlayer;
    }
}
