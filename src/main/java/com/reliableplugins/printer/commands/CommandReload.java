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
        executor.sendMessage(Message.RELOAD.getMessage());
    }
}
