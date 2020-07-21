package com.reliableplugins.printer.commands;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.annotation.CommandBuilder;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.hook.FactionsHook;
import com.reliableplugins.printer.hook.SuperiorSkyblockHook;
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

        // Factions checks
        if(Printer.INSTANCE.isFactions())
        {
            // In territory
            if(!Printer.INSTANCE.getMainConfig().allowInWilderness() && !FactionsHook.inOwnTerritory(player))
            {
                player.sendMessage(Message.ERROR_NOT_IN_TERRITORY.getMessage());
                return;
            }

            // Enemies/neutral nearby
            else if(FactionsHook.isEnemyOrNeutralNearby(player))
            {
                player.sendMessage(Message.ERROR_ENEMY_NEARBY.getMessage());
                return;
            }
        }
        // SuperiorSkyBlock checks
        else if(Printer.INSTANCE.isSuperiorSkyBlock())
        {
            // In island
            if((Printer.INSTANCE.getMainConfig().allowInNonIsland() && !SuperiorSkyblockHook.canPlayerBuild(player, player.getLocation()))
                    || !SuperiorSkyblockHook.isOnOwnIsland(player))
            {
                player.sendMessage(Message.ERROR_NOT_IN_ISLAND.getMessage());
                return;
            }

            // Non-island members nearby
            else if(Printer.INSTANCE.getMainConfig().allowNearNonIslandMembers() && SuperiorSkyblockHook.isNonIslandMemberNearby(player))
            {
                player.sendMessage(Message.ERROR_NON_ISLAND_MEMBER_NEARBY.getMessage());
                return;
            }

        }

        printerPlayer.printerOn();
        player.sendMessage(Message.PRINTER_ON.getMessage());
    }
}
