/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.commands;

import com.reliableplugins.printer.PrinterPlayer;
import com.reliableplugins.printer.config.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandOff extends Command
{
    public CommandOff()
    {
        super("off", "printer.off", "Turns off printer", true, new String[0]);
    }

    @Override
    public void execute(CommandSender executor, String[] args)
    {
        Player player = (Player) executor;

        PrinterPlayer printerPlayer = PrinterPlayer.fromPlayer(player);
        if(printerPlayer == null || !printerPlayer.isPrinting())
        {
            Message.ERROR_PRINTER_NOT_ON.sendColoredMessage(executor);
            return;
        }

        printerPlayer.printerOff();
        Message.PRINTER_OFF.sendColoredMessage(executor);
    }

    @Override
    public String getDescription()
    {
        return Message.HELP_PRINTER_OFF.getColoredMessageWithoutHeader();
    }
}
