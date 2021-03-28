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

public class CommandToggle extends Command
{
    public CommandToggle()
    {
        super("toggle", "printer.toggle", "Toggles current state of printer", true, new String[]{"t"});
    }

    @Override
    public void execute(CommandSender executor, String[] args)
    {
        Player player = (Player) executor;

        PrinterPlayer printerPlayer = PrinterPlayer.fromPlayer(player);
        if(printerPlayer == null)
        {
            printerPlayer = PrinterPlayer.addPlayer(player);
            printerPlayer.printerOn();
            Message.PRINTER_ON.sendColoredMessage(player);
        }
        else
        {
            if(printerPlayer.isPrinting())
            {
                printerPlayer.printerOff();
                Message.PRINTER_OFF.sendColoredMessage(player);
            }
            else
            {
                printerPlayer.printerOn();
                Message.PRINTER_ON.sendColoredMessage(player);
            }
        }

    }

    @Override
    public String getDescription()
    {
        return Message.HELP_PRINTER_TOGGLE.getColoredMessageWithoutHeader();
    }
}
