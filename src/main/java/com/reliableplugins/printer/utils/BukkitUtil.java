package com.reliableplugins.printer.utils;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class BukkitUtil
{
    private static final HashMap<Material, Material> itemToBlockMap = new HashMap<>();

    static
    {
        itemToBlockMap.put(Material.WATER_BUCKET,        null);
        itemToBlockMap.put(Material.LAVA_BUCKET,         null);
        itemToBlockMap.put(Material.REDSTONE,            Material.REDSTONE_WIRE);
        itemToBlockMap.put(Material.DIODE,               Material.DIODE_BLOCK_OFF);
        itemToBlockMap.put(Material.REDSTONE_COMPARATOR, Material.REDSTONE_COMPARATOR_OFF);
        itemToBlockMap.put(Material.STRING,              Material.TRIPWIRE);

        itemToBlockMap.put(Material.SEEDS,               Material.CROPS);
        itemToBlockMap.put(Material.MELON_SEEDS,         Material.MELON_STEM);
        itemToBlockMap.put(Material.PUMPKIN_SEEDS,       Material.PUMPKIN_STEM);
        itemToBlockMap.put(Material.CARROT_ITEM,         Material.CARROT);
        itemToBlockMap.put(Material.NETHER_STALK,        Material.NETHER_WARTS);
        itemToBlockMap.put(Material.SUGAR_CANE,          Material.SUGAR_CANE_BLOCK);

        itemToBlockMap.put(Material.WOOD_DOOR,           Material.WOODEN_DOOR);
        itemToBlockMap.put(Material.ACACIA_DOOR_ITEM,    Material.ACACIA_DOOR);
        itemToBlockMap.put(Material.BIRCH_DOOR_ITEM,     Material.BIRCH_DOOR);
        itemToBlockMap.put(Material.DARK_OAK_DOOR_ITEM,  Material.DARK_OAK_DOOR);
        itemToBlockMap.put(Material.JUNGLE_DOOR_ITEM,    Material.JUNGLE_DOOR);
        itemToBlockMap.put(Material.SPRUCE_DOOR_ITEM,    Material.SPRUCE_DOOR);
        itemToBlockMap.put(Material.IRON_DOOR,           Material.IRON_DOOR_BLOCK);

        itemToBlockMap.put(Material.BED,                 Material.BED_BLOCK);
        itemToBlockMap.put(Material.BREWING_STAND_ITEM,  Material.BREWING_STAND);
        itemToBlockMap.put(Material.SKULL_ITEM,          Material.SKULL);
        itemToBlockMap.put(Material.FLOWER_POT_ITEM,     Material.FLOWER_POT);
        itemToBlockMap.put(Material.CAULDRON_ITEM,       Material.CAULDRON);
        itemToBlockMap.put(Material.CAKE,                Material.CAKE_BLOCK);
    }

    public static boolean isItemMaterial(Material material)
    {
        return itemToBlockMap.containsKey(material);
    }

    public static boolean isItemMaterialBlock(Material material)
    {
        return itemToBlockMap.containsValue(material);
    }

    public static Material getItemMaterialBlock(Material itemMaterial)
    {
        return itemToBlockMap.get(itemMaterial);
    }

    public static Material getItemMaterial(Material itemMaterialBlock)
    {
        for (Map.Entry<Material, Material> entry : itemToBlockMap.entrySet())
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
