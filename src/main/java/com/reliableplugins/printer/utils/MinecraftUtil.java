/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.utils;

import com.reliableplugins.printer.Printer;
import org.bukkit.World;

public class MinecraftUtil
{
    public static int getPlayerLoadDistance(World world)
    {
        if(Printer.INSTANCE.isSpigot())
        {
            return SpigotUtil.getPlayerLoadDistance(world);
        }
        return 48;
    }
}
