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
