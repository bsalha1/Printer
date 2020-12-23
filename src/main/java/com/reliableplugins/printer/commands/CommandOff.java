/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.commands;

import com.reliableplugins.printer.PrinterPlayer;
import com.reliableplugins.printer.annotation.CommandBuilder;
import com.reliableplugins.printer.config.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandBuilder(label = "off", description = "Turns off printer", permission = "printer.off", playerRequired = true)
public class CommandOff extends Command
{
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
