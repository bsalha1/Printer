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
        int loadDistance = 48;
        if(Printer.INSTANCE.isSpigot())
        {
            loadDistance = SpigotUtil.getPlayerLoadDistance(world);
        }

        // If the config's check radius is less than or equal to the physical load distance, it's valid
        return Math.min(Printer.INSTANCE.getMainConfig().getCheckRadius(), loadDistance);
    }
}
