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

@CommandBuilder(label = "version", alias = "v", permission = "printer.version", playerRequired = false, description = "Get current version of Printer")
public class CommandVersion extends Command
{
    @Override
    public void execute(CommandSender executor, String[] args)
    {
        executor.sendMessage(Message.VERSION_MESSAGE.getMessage().replace("{NUM}", Printer.INSTANCE.getVersion()));
    }
}
