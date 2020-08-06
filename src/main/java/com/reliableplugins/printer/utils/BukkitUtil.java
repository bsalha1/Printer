/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class BukkitUtil
{
    private static final HashMap<Material, Material> itemToBlockMap = new HashMap<>();
    private static final ArrayList<Material> noBlockPlaceItems = new ArrayList<>();

    static
    {
        noBlockPlaceItems.add(Material.WATER_BUCKET);
        noBlockPlaceItems.add(Material.LAVA_BUCKET);
        noBlockPlaceItems.add(Material.POTION);

        itemToBlockMap.put(Material.POTION,              null);
        itemToBlockMap.put(Material.WATER_BUCKET,        Material.WATER);
        itemToBlockMap.put(Material.LAVA_BUCKET,         Material.LAVA);
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

    public static boolean isNoBlockPlaceItem(Material material)
    {
        return noBlockPlaceItems.contains(material);
    }

    public static boolean isItem(Material material)
    {
        return itemToBlockMap.containsKey(material);
    }

    public static boolean isBlock(Material material)
    {
        return itemToBlockMap.containsValue(material);
    }

    public static Material getBlock(Material item)
    {
        return itemToBlockMap.get(item);
    }

    public static Material getItem(Material block)
    {
        for (Map.Entry<Material, Material> entry : itemToBlockMap.entrySet())
        {
            if (entry.getValue() != null && entry.getValue().equals(block))
            {
                return entry.getKey();
            }
        }
        return null;
    }

    public static List<Player> getNearbyPlayers(Player player)
    {
        ArrayList<Player> nearbyPlayers = new ArrayList<>();

        for(Entity entity : player.getNearbyEntities(MinecraftUtil.getPlayerLoadDistance(player.getWorld()), 256, MinecraftUtil.getPlayerLoadDistance(player.getWorld())))
        {
            if(entity instanceof Player)
            {
                nearbyPlayers.add((Player)entity);
            }
        }
        return nearbyPlayers;
    }

    public static String color(String text)
    {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<Entity> getEntities(Location location, double radius)
    {
        List<Entity> entities = new ArrayList<>();
        World world = location.getWorld();

        int xMin = (int)Math.floor((location.getX() - radius) / 16.0D);
        int xMax = (int)Math.floor((location.getX() + radius) / 16.0D);
        int zMin = (int)Math.floor((location.getZ() - radius) / 16.0D);
        int zMax = (int)Math.floor((location.getZ() + radius) / 16.0D);

        for (int x = xMin; x <= xMax; x++)
            for (int z = zMin; z <= zMax; z++)
            {
                if (world.isChunkLoaded(x, z))
                {
                    entities.addAll(Arrays.asList(world.getChunkAt(x, z).getEntities())); // Add all entities from this chunk to the list
                }
            }

        entities.removeIf(entity -> entity.getLocation().distanceSquared(location) > radius * radius);
        return entities;
    }
}
