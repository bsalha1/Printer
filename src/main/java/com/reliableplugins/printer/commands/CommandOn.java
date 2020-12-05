/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.commands;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.annotation.CommandBuilder;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.PrinterPlayer;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandBuilder(label = "on", description = "Turns on printer", permission = "printer.on", playerRequired = true)
public class CommandOn extends Command
{
    @Override
    public void execute(CommandSender executor, String[] args)
    {
        Player player = (Player) executor;
        PrinterPlayer printerPlayer = PrinterPlayer.fromPlayer(player);
        if(printerPlayer == null)
        {
            PrinterPlayer.addPlayer(player);
            printerPlayer = PrinterPlayer.fromPlayer(player);
        }
        else if(printerPlayer.isPrinting())
        {
            player.sendMessage(Message.ERROR_PRINTER_ALREADY_ON.getMessage());
            return;
        }

        // Factions checks
        if(Printer.INSTANCE.hasFactionsHook())
        {
            // In territory
            if(!Printer.INSTANCE.getMainConfig().allowInWilderness() && !Printer.INSTANCE.getFactionsHook().inOwnTerritory(player))
            {
                player.sendMessage(Message.ERROR_NOT_IN_TERRITORY.getMessage());
                return;
            }

            // Enemies/neutral nearby
            else if(Printer.INSTANCE.getFactionsHook().isEnemyOrNeutralNearby(player))
            {
                player.sendMessage(Message.ERROR_ENEMY_NEARBY.getMessage());
                return;
            }
        }

        // SuperiorSkyBlock checks
        if(Printer.INSTANCE.hasSuperiorSkyBlockHook())
        {
            // In island
            if((Printer.INSTANCE.getMainConfig().allowInNonIsland() && !Printer.INSTANCE.getSuperiorSkyBlockHook().canPlayerBuild(player, player.getLocation()))
                    || !Printer.INSTANCE.getSuperiorSkyBlockHook().isOnOwnIsland(player))
            {
                player.sendMessage(Message.ERROR_NOT_IN_ISLAND.getMessage());
                return;
            }

            // Non-island members nearby
            else if(Printer.INSTANCE.getMainConfig().allowNearNonIslandMembers() && Printer.INSTANCE.getSuperiorSkyBlockHook().isNonIslandMemberNearby(player))
            {
                player.sendMessage(Message.ERROR_NON_ISLAND_MEMBER_NEARBY.getMessage());
                return;
            }

        }

        // Residence checks
        if(Printer.INSTANCE.hasResidenceHook())
        {
            if(!Printer.INSTANCE.getMainConfig().allowNearNonResidentMembers() && Printer.INSTANCE.getResidenceHook().isNonResidenceMemberNearby(player))
            {
                player.sendMessage(Message.ERROR_NON_RESIDENT_NEARBY.getMessage());
                return;
            }
            // If player isn't in their own residence and they aren't in wilderness
            else if(!Printer.INSTANCE.getResidenceHook().isInOwnResidence(player) &&
                    (!Printer.INSTANCE.getMainConfig().allowInNonResidence() && !Printer.INSTANCE.getResidenceHook().isInAResidence(player)))
            {
                player.sendMessage(Message.ERROR_NOT_IN_RESIDENCE.getMessage());
                return;
            }
        }

        if(Printer.INSTANCE.getMainConfig().requireEmptyInventory() && (!BukkitUtil.isArmorInventoryEmpty(player) || !BukkitUtil.isInventoryEmpty(player)))
        {
            player.sendMessage(Message.ERROR_NON_EMPTY_INVENTORY.getMessage());
            return;
        }

        printerPlayer.printerOn();
        player.sendMessage(Message.PRINTER_ON.getMessage());
    }

    @Override
    public String getDescription()
    {
        return Message.HELP_PRINTER_ON.getWithoutHeader();
    }
}
