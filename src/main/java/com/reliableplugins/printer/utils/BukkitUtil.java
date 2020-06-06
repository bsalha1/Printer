package com.reliableplugins.printer.utils;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;

import java.util.HashSet;

public class BukkitUtil
{
    private static HashSet<Material> itemMaterials = new HashSet<>();

    static
    {
        itemMaterials.add(Material.WATER_BUCKET);
        itemMaterials.add(Material.LAVA_BUCKET);
        itemMaterials.add(Material.REDSTONE);
        itemMaterials.add(Material.DIODE);
        itemMaterials.add(Material.REDSTONE_COMPARATOR);
        itemMaterials.add(Material.STRING);
        itemMaterials.add(Material.SEEDS);
        itemMaterials.add(Material.MELON_SEEDS);
        itemMaterials.add(Material.PUMPKIN_SEEDS);
        itemMaterials.add(Material.NETHER_STALK);
    }

    public static boolean isItemMaterial(Material material)
    {
        return itemMaterials.contains(material);
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
