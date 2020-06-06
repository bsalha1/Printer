package com.reliableplugins.printer.commands;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.annotation.CommandBuilder;
import com.reliableplugins.printer.utils.BukkitUtil;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandBuilder(label = "help", alias = {"h"}, permission = "antiskid.help")
public class CommandHelp extends Command
{
    private CommandHandler baseCommand;
    private static final String color = "&d";

    public CommandHelp(CommandHandler baseCommand)
    {
        this.baseCommand = baseCommand;
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        Command[] commands = baseCommand.getCommands().toArray(new Command[baseCommand.getCommands().size()]);
        Player player = Bukkit.getPlayer(sender.getName());

        int page = 0;

        if (args.length != 0)
        {
            if (args[0].matches("^[0-9]"))
            {
                page = (Integer.parseInt(args[0]) - 1);
            }
        }

        int maxPage = (int) Math.floor(commands.length / 5) + 1;
        if(page > maxPage)
        {
            page = maxPage;
        }

        String header = "&7&m----------&7[ " + color + Printer.INSTANCE.getDescription().getName() + " &f%s&7/&f%s &7]&m----------";
        String line = color + "/" + baseCommand.getLabel() + " %s&7 %s";
        String footer = "&7&oHover for description";

        sender.sendMessage(BukkitUtil.color(String.format(header, (page + 1), maxPage)));
        for (int i = (page * 5); i < (page * 5) + 5; i++)
        {
            if (i > commands.length - 1) continue;

            Command command = commands[i];

            String entry = String.format(line, command.getLabel(), command.getPermission());
            TextComponent message = new TextComponent(BukkitUtil.color(entry));
            message.setHoverEvent(
                    new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(command.getDescription()).create()));

            if (player != null)
            {
                player.spigot().sendMessage(message);
            }
            else
            {
                sender.sendMessage(message.getText());
            }

        }
        sender.sendMessage(BukkitUtil.color(footer));
    }
}
