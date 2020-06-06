package com.reliableplugins.printer.utils;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.HashSet;

public class BukkitUtil
{
    public static boolean isMinecart(Material material)
    {
        return material.equals(Material.MINECART) ||
                material.equals(Material.COMMAND_MINECART) ||
                material.equals(Material.EXPLOSIVE_MINECART) ||
                material.equals(Material.HOPPER_MINECART) ||
                material.equals(Material.STORAGE_MINECART) ||
                material.equals(Material.POWERED_MINECART);
    }

    public static boolean isItemMaterial(Material material)
    {
        return material.equals(Material.WATER_BUCKET) || material.equals(Material.LAVA_BUCKET);
    }

    public static void reloadChunk(Chunk chunk)
    {
        chunk.getWorld().refreshChunk(chunk.getX(), chunk.getZ());
    }

    public static String color(String text)
    {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
