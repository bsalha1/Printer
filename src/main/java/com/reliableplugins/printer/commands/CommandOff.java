package com.reliableplugins.printer.commands;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.annotation.CommandBuilder;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.type.PrinterPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandBuilder(label = "off", description = "Turns off printer", permission = "printer.off", playerRequired = true)
public class CommandOff extends Command
{
    @Override
    public void execute(CommandSender executor, String[] args)
    {
        Player player = (Player) executor;

        PrinterPlayer printerPlayer = Printer.INSTANCE.printerPlayers.get(player);
        if(printerPlayer == null || !printerPlayer.isPrinting())
        {
            player.sendMessage(Message.ERROR_PRINTER_NOT_ON.getMessage());
            return;
        }

        printerPlayer.printerOff();
        player.sendMessage(Message.PRINTER_OFF.getMessage());
    }
}
