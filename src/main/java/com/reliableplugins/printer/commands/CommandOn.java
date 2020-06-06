package com.reliableplugins.printer.commands;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.annotation.CommandBuilder;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.type.PrinterPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandBuilder(label = "on", description = "Turns on printer", permission = "printer.on", playerRequired = true)
public class CommandOn extends Command
{
    @Override
    public void execute(CommandSender executor, String[] args)
    {
        Player player = (Player) executor;
        PrinterPlayer printerPlayer = Printer.INSTANCE.printerPlayers.get(player);
        if(printerPlayer == null)
        {
            Printer.INSTANCE.printerPlayers.put(player, printerPlayer = new PrinterPlayer(player));
        }
        else if(printerPlayer.isPrinting())
        {
            player.sendMessage(Message.ERROR_PRINTER_ALREADY_ON.getMessage());
            return;
        }

        printerPlayer.printerOn();
        player.sendMessage(Message.PRINTER_ON.getMessage());
    }
}
