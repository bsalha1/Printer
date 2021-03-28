/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.commands;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.PrinterPlayer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandOn extends Command
{
    public CommandOn()
    {
        super("on", "printer.on", "Turns on printer", true, new String[0]);
    }

    @Override
    public void execute(CommandSender executor, String[] args)
    {
        Player player = (Player) executor;
        PrinterPlayer printerPlayer = PrinterPlayer.fromPlayer(player);
        if(printerPlayer == null)
        {
            printerPlayer = PrinterPlayer.addPlayer(player);
        }
        else if(printerPlayer.isPrinting())
        {
            Message.ERROR_PRINTER_ALREADY_ON.sendColoredMessage(player);
            return;
        }

        if(Printer.INSTANCE.isTerritoryRestricted(player))
        {
            return;
        }

        if(Printer.INSTANCE.getMainConfig().requireEmptyInventory() && (!BukkitUtil.isArmorInventoryEmpty(player) || !BukkitUtil.isInventoryEmpty(player)))
        {
            Message.ERROR_NON_EMPTY_INVENTORY.sendColoredMessage(player);
            return;
        }

        printerPlayer.printerOn();
        Message.PRINTER_ON.sendColoredMessage(player);
    }

    @Override
    public String getDescription()
    {
        return Message.HELP_PRINTER_ON.getColoredMessageWithoutHeader();
    }
}
