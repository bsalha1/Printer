/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.commands;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import org.bukkit.command.CommandSender;

public class CommandVersion extends Command
{
    public CommandVersion()
    {
        super("version", "printer.version", "Get current version of Printer", false, new String[]{"v"});
    }

    @Override
    public void execute(CommandSender executor, String[] args)
    {
        executor.sendMessage(Message.VERSION_MESSAGE.getColoredMessage().replace("{NUM}", Printer.INSTANCE.getDescription().getVersion()));
    }

    @Override
    public String getDescription()
    {
        return Message.HELP_PRINTER_VERSION.getColoredMessageWithoutHeader();
    }
}
