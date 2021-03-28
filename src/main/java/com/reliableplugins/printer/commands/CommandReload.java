/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.commands;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

public class CommandReload extends Command
{
    public CommandReload()
    {
        super("reload", "printer.reload", "Reloads the printer configs", false, new String[]{"r"});
    }

    @Override
    public void execute(CommandSender executor, String[] args)
    {
        Printer.INSTANCE.reloadConfigs();
        Printer.INSTANCE.getShopHook().clearCache();
        Message.RELOAD.sendColoredMessage(executor);
        Player player = (Player) executor;

        Location location = player.getLocation();
        FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(location.clone().add(0, 25, 0), Material.DRAGON_EGG, (byte) 0);
        fallingBlock.setDropItem(false);
//        fallingBlock.setHurtEntities(false);
        fallingBlock.setTicksLived(1);
    }

    @Override
    public String getDescription()
    {
        return Message.HELP_PRINTER_RELOAD.getColoredMessageWithoutHeader();
    }
}
