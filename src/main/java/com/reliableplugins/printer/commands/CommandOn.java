package com.reliableplugins.printer.commands;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.annotation.CommandBuilder;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.hook.FactionsHook;
import com.reliableplugins.printer.type.PrinterPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
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

        for(Entity entity : player.getNearbyEntities(48, 256, 48))
        {
            if(entity instanceof Player && FactionsHook.areNeutralOrEnemies((Player) entity, player))
            {
                player.sendMessage(Message.ERROR_ENEMY_NEARBY.getMessage());
                return;
            }
        }

        printerPlayer.printerOn();
        player.sendMessage(Message.PRINTER_ON.getMessage());
    }
}
