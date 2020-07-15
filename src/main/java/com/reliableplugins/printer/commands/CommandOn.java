package com.reliableplugins.printer.commands;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.annotation.CommandBuilder;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.hook.FactionsHook;
import com.reliableplugins.printer.hook.SuperiorSkyblockHook;
import com.reliableplugins.printer.type.PrinterPlayer;
import com.reliableplugins.printer.utils.MinecraftUtil;
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

        // Factions checks
        if(Printer.INSTANCE.isFactions())
        {
            // If you must be in your own territory and you aren't, no printer
            if(!Printer.INSTANCE.getMainConfig().allowInWilderness() && !FactionsHook.inOwnTerritory(player))
            {
                player.sendMessage(Message.ERROR_NOT_IN_TERRITORY.getMessage());
                return;
            }

            // If enemies or neutrals nearby, no printer
            if(printerPlayer.areEnemiesOrNeutralsNearby(MinecraftUtil.getPlayerLoadDistance(player.getWorld()), 256, MinecraftUtil.getPlayerLoadDistance(player.getWorld())))
            {
                player.sendMessage(Message.ERROR_ENEMY_NEARBY.getMessage());
                return;
            }
        }
        // SuperiorSkyBlock checks
        else if(Printer.INSTANCE.isSuperiorSkyBlock())
        {
            if(!SuperiorSkyblockHook.canPlayerBuild(player, player.getLocation()))
            {
                player.sendMessage(Message.ERROR_NOT_IN_ISLAND.getMessage());
                return;
            }
        }

        printerPlayer.printerOn();
        player.sendMessage(Message.PRINTER_ON.getMessage());
    }
}
