/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.commands;

import com.reliableplugins.printer.annotation.CommandBuilder;
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
        if(args.length > 0)
        {
            try
            {
                pageNum = Integer.parseInt(args[0]);
            }
            catch(Exception ignored)
            {
            }
        }
        Command[] commands = baseCommand.getSubCommands().toArray(new Command[0]);
        int commandPerPage = 5;

        int maxPage = (int)Math.ceil((double)commands.length / commandPerPage);
        if(pageNum > maxPage)
        {
            pageNum = maxPage;
        }
        String header = "&7&m----------&7[ &dPrinter &r&f" + pageNum + "&7/&f" + maxPage + "&7]&m----------";
        sender.sendMessage(BukkitUtil.color(header));
        int pageIndex = (pageNum - 1) * commandPerPage;

        if(sender instanceof Player)
        {
            executeToPlayer(sender, commands, pageIndex);
        }
        else
        {
            executeToNonPlayer(sender, commands, pageIndex);
        }
    }

    public void executeToPlayer(CommandSender sender, Command[] commands, int pageIndex)
    {
        Player player = (Player) sender;

        for (int i = pageIndex; i < (pageIndex + 5) && i < commands.length; i++)
        {
            Command command = commands[i];
            String line;
            if(player.hasPermission(command.getPermission()) || sender.isOp())
            {
                line =  color + "/" + baseCommand.getLabel() + " %s&r %s";
            }
            else
            {
                continue;
            }

            TextComponent message = new TextComponent(BukkitUtil.color(String.format(line, command.getLabel(), BukkitUtil.color(descriptionColor + command.getPermission()))));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(command.getDescription()).create()));
            player.spigot().sendMessage(message);
        }

        String footer = "&7&oHover to view descriptions";
        sender.sendMessage(BukkitUtil.color(footer));
    }

    public void executeToNonPlayer(CommandSender sender, Command[] commands, int pageIndex)
    {
        for (int i = pageIndex; i < (pageIndex + 5) && i < commands.length; i++)
        {
            Command command = commands[i];
            String line;
            if(sender.hasPermission(command.getPermission()) || sender.isOp())
            {
                line = color + "/" + baseCommand.getLabel() + " %s&r %s";
            }
            else
            {
                continue;
            }

            String message = BukkitUtil.color(String.format(line, command.getLabel(), ChatColor.GRAY + command.getPermission()));
            sender.sendMessage(message);
        }
    }
}
