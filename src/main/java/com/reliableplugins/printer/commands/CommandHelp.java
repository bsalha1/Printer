/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.commands;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.annotation.CommandBuilder;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.utils.BukkitUtil;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandBuilder(label = "help", alias = {"h"})
public class CommandHelp extends Command
{
    private final CommandHandler baseCommand;
    private static final String color = "&d";
    private static final String descriptionColor = "&7";

    public CommandHelp(CommandHandler baseCommand)
    {
        this.baseCommand = baseCommand;
    }

    public void execute(CommandSender sender, String[] args)
    {
        int pageNum = 1;
        if (args.length > 0)
        {
            try
            {
                pageNum = Integer.parseInt(args[0]);
            }
            catch (Exception ignored)
            {
            }
        }
        Command[] commands = baseCommand.getSubCommands().toArray(new Command[0]);
        int commandPerPage = 5;

        int maxPage = (int) Math.ceil((double) commands.length / commandPerPage);
        if (pageNum > maxPage)
        {
            pageNum = maxPage;
        }

        String header = Message.HELP_PRINTER_HEADER.getWithoutHeader()
                .replace("{PAGE}", Integer.toString(pageNum))
                .replace("{NUM_PAGES}", Integer.toString(maxPage));
        sender.sendMessage(BukkitUtil.color(header));
        int pageIndex = (pageNum - 1) * commandPerPage;

        for (int i = pageIndex; i < (pageIndex + 5) && i < commands.length; i++)
        {
            Command command = commands[i];
            if (!sender.hasPermission(command.getPermission()) && !sender.isOp())
            {
                continue;
            }
            sender.sendMessage(command.getDescription());
        }
    }

}
