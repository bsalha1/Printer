/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.commands;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import org.bukkit.command.CommandSender;

public class CommandReload extends Command
{
    public CommandReload()
    {
        super("reload", "printer.reload", "Reloads the printer configs", false, new String[]{"r"});
    }

    @Override
    public void execute(CommandSender executor, String[] args)
    {
        Printer.INSTANCE.reloadConfigs();
        if(Printer.INSTANCE.hasShopHook())
        {
            Printer.INSTANCE.getShopHook().clearCache();
        }
        Message.RELOAD.sendColoredMessage(executor);
    }

    @Override
    public String getDescription()
    {
        return Message.HELP_PRINTER_RELOAD.getColoredMessageWithoutHeader();
    }
}
