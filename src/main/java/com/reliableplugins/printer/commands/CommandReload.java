/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.commands;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.annotation.CommandBuilder;
import com.reliableplugins.printer.config.Message;
import org.bukkit.command.CommandSender;

@CommandBuilder(label = "reload", permission = "printer.reload", description = "Reloads the printer configs")
public class CommandReload extends Command
{
    @Override
    public void execute(CommandSender executor, String[] args)
    {
        Printer.INSTANCE.reloadConfigs();
        Message.RELOAD.sendColoredMessage(executor);
    }

    @Override
    public String getDescription()
    {
        return Message.HELP_PRINTER_RELOAD.getColoredMessageWithoutHeader();
    }
}
