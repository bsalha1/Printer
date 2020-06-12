package com.reliableplugins.printer.utils;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class BukkitUtil
{
    private static HashMap<Material, Material> itemMaterials = new HashMap<>();

    static
    {
        itemMaterials.put(Material.WATER_BUCKET,        null);
        itemMaterials.put(Material.LAVA_BUCKET,         null);
        itemMaterials.put(Material.REDSTONE,            Material.REDSTONE_WIRE);
        itemMaterials.put(Material.DIODE,               Material.DIODE_BLOCK_OFF);
        itemMaterials.put(Material.REDSTONE_COMPARATOR, Material.REDSTONE_COMPARATOR_OFF);
        itemMaterials.put(Material.STRING,              Material.TRIPWIRE);
        itemMaterials.put(Material.SEEDS,               Material.CROPS);
        itemMaterials.put(Material.MELON_SEEDS,         Material.MELON_STEM);
        itemMaterials.put(Material.PUMPKIN_SEEDS,       Material.PUMPKIN_STEM);
        itemMaterials.put(Material.NETHER_STALK,        Material.NETHER_WARTS);
    }

    public static boolean isItemMaterial(Material material)
    {
        return itemMaterials.containsKey(material);
    }

    public static boolean isItemMaterialBlock(Material material)
    {
        return itemMaterials.containsValue(material);
    }

    public static Material getItemMaterialBlock(Material itemMaterial)
    {
        return itemMaterials.get(itemMaterial);
    }

    public static Material getItemMaterial(Material itemMaterialBlock)
    {
        for (Map.Entry<Material, Material> entry : itemMaterials.entrySet())
        {
            if (entry.getValue() != null && entry.getValue().equals(itemMaterialBlock))
            {
                return entry.getKey();
            }
        }
        return null;
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
