package com.reliableplugins.printer.commands;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.annotation.CommandBuilder;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.hook.FactionsHook;
import com.reliableplugins.printer.type.PrinterPlayer;
import org.bukkit.command.CommandSender;

@CommandBuilder(label = "reload", permission = "printer.reload", description = "Reloads the printer configs")
public class CommandReload extends Command
{
    @Override
    public void execute(CommandSender executor, String[] args)
    {
        boolean allowBefore = Printer.INSTANCE.getMainConfig().allowInWilderness();

        Printer.INSTANCE.reloadConfigs();
        executor.sendMessage(Message.RELOAD.getMessage());

        // If printer was allowed before in wilderness, but now is not
        // deactivate all printers who are in wilderness
        if(Printer.INSTANCE.isFactions() && allowBefore && !Printer.INSTANCE.getMainConfig().allowInWilderness())
        {
            for(PrinterPlayer player : Printer.INSTANCE.printerPlayers.values())
            {
                if(player.isPrinting() && FactionsHook.inWilderness(player.getPlayer()))
                {
                    player.printerOff();
                    player.getPlayer().sendMessage(Message.ERROR_NOT_IN_TERRITORY.getMessage());
                }
            }
        }
    }
}
