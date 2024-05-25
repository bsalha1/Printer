/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.utils;

import com.reliableplugins.printer.Printer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;


public class BukkitUtil
{
    private static final HashMap<Material, Material> itemToBlockMap = new HashMap<>();
    private static final HashMap<Material, Material> blockToItemMap = new HashMap<>();
    private static final ArrayList<Material> noBlockPlaceItems = new ArrayList<>();

    static
    {
        noBlockPlaceItems.add(Material.WATER_BUCKET);
        noBlockPlaceItems.add(Material.LAVA_BUCKET);
        noBlockPlaceItems.add(Material.POTION);

        /*
         * Create a mapping from item in hand to the block to be placed.
         */
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
        itemToBlockMap.put(Material.SIGN,                Material.SIGN_POST);

        /*
         * Create a mapping from block to item. This is mostly a reverse mapping
         * of itemToBlockMap with the exception of some items which map to different
         * types of block i.e. SIGN item can map to SIGN_POST or WALL_SIGN.
         */
        for(Map.Entry<Material, Material> entry : itemToBlockMap.entrySet())
        {
            blockToItemMap.put(entry.getValue(), entry.getKey());
        }

        blockToItemMap.put(Material.WALL_SIGN,           Material.SIGN);
    }

    public static boolean isNoBlockPlaceItem(Material material)
    {
        return noBlockPlaceItems.contains(material);
    }

    public static boolean isItemOfBlock(Material material)
    {
        return itemToBlockMap.containsKey(material);
    }

    public static boolean isBlockOfItem(Material material)
    {
        return blockToItemMap.containsKey(material);
    }

    public static Material getBlockOfItem(Material item)
    {
        return itemToBlockMap.get(item);
    }

    public static Material getItemOfBlock(Material block)
    {
        return blockToItemMap.get(block);
    }

    public static List<Player> getNearbyPlayers(Player player)
    {
        ArrayList<Player> nearbyPlayers = new ArrayList<>();

        for(Entity entity : player.getNearbyEntities(MinecraftUtil.getPlayerLoadDistance(player.getWorld()), 256, MinecraftUtil.getPlayerLoadDistance(player.getWorld())))
        {
            if(entity instanceof Player)
            {
                // Citizens shouldn't count as nearby players
                if(Printer.INSTANCE.hasCitizensHook() && Printer.INSTANCE.getCitizensHook().isCitizen(entity))
                {
                    continue;
                }
                nearbyPlayers.add((Player)entity);
            }
        }

        return nearbyPlayers;
    }

    public static List<Entity> getLookingAt(Player player)
    {
        ArrayList<Entity> entities = new ArrayList<>();
        for(Entity entity : player.getNearbyEntities(MinecraftUtil.getPlayerLoadDistance(player.getWorld()), 256, MinecraftUtil.getPlayerLoadDistance(player.getWorld())))
        {
            Location eye = player.getEyeLocation();
            Vector toEntity = entity.getLocation().toVector().subtract(eye.toVector());
            double dot = toEntity.normalize().dot(eye.getDirection());
            if(dot > 0.96D)
            {
                entities.add(entity);
            }
        }

        return entities;
    }

    public static boolean isNearby(Player player, EntityType entityType, int radius)
    {
        for(Entity entity : player.getNearbyEntities(radius, 256, radius))
        {
            if(entity.getType().equals(entityType))
            {
                return true;
            }
        }

        return false;
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

    public static boolean isArmorInventoryEmpty(Player player)
    {
        ItemStack[] armors = player.getInventory().getArmorContents();
        for(ItemStack armor : armors)
        {
            if(armor != null && !armor.getType().equals(Material.AIR))
            {
                return false;
            }
        }
        return true;
    }

    public static boolean isInventoryEmpty(Player player)
    {
        ItemStack[] items = player.getInventory().getContents();
        for(ItemStack item : items)
        {
            if(item != null)
            {
                return false;
            }
        }
        return true;
    }
}
