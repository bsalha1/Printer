/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.type;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;

public enum ColoredMaterial
{
    // Wool
    WHITE_WOOL(Material.WOOL, 0),
    ORANGE_WOOL(Material.WOOL, 1),
    MAGENTA_WOOL(Material.WOOL, 2),
    LIGHT_BLUE_WOOL(Material.WOOL, 3),
    YELLOW_WOOL(Material.WOOL, 4),
    LIME_WOOL(Material.WOOL, 5),
    PINK_WOOL(Material.WOOL, 6),
    GRAY_WOOL(Material.WOOL, 7),
    LIGHT_GRAY_WOOL(Material.WOOL, 8),
    CYAN_WOOL(Material.WOOL, 9),
    PURPLE_WOOL(Material.WOOL, 10),
    BLUE_WOOL(Material.WOOL, 11),
    BROWN_WOOL(Material.WOOL, 12),
    GREEN_WOOL(Material.WOOL, 13),
    RED_WOOL(Material.WOOL, 14),
    BLACK_WOOL(Material.WOOL, 15),

    // Glass
    WHITE_STAINED_STAINED_GLASS(Material.STAINED_GLASS, 0),
    ORANGE_STAINED_GLASS(Material.STAINED_GLASS, 1),
    MAGENTA_STAINED_GLASS(Material.STAINED_GLASS, 2),
    LIGHT_BLUE_STAINED_GLASS(Material.STAINED_GLASS, 3),
    YELLOW_STAINED_GLASS(Material.STAINED_GLASS, 4),
    LIME_STAINED_GLASS(Material.STAINED_GLASS, 5),
    PINK_STAINED_GLASS(Material.STAINED_GLASS, 6),
    GRAY_STAINED_GLASS(Material.STAINED_GLASS, 7),
    LIGHT_GRAY_STAINED_GLASS(Material.STAINED_GLASS, 8),
    CYAN_STAINED_GLASS(Material.STAINED_GLASS, 9),
    PURPLE_STAINED_GLASS(Material.STAINED_GLASS, 10),
    BLUE_STAINED_GLASS(Material.STAINED_GLASS, 11),
    BROWN_STAINED_GLASS(Material.STAINED_GLASS, 12),
    GREEN_STAINED_GLASS(Material.STAINED_GLASS, 13),
    RED_STAINED_GLASS(Material.STAINED_GLASS, 14),
    BLACK_STAINED_GLASS(Material.STAINED_GLASS, 15),

    // Glass Pane
    WHITE_STAINED_GLASS_PANE(Material.STAINED_GLASS_PANE, 0),
    ORANGE_STAINED_GLASS_PANE(Material.STAINED_GLASS_PANE, 1),
    MAGENTA_STAINED_GLASS_PANE(Material.STAINED_GLASS_PANE, 2),
    LIGHT_BLUE_STAINED_GLASS_PANE(Material.STAINED_GLASS_PANE, 3),
    YELLOW_STAINED_GLASS_PANE(Material.STAINED_GLASS_PANE, 4),
    LIME_STAINED_GLASS_PANE(Material.STAINED_GLASS_PANE, 5),
    PINK_STAINED_GLASS_PANE(Material.STAINED_GLASS_PANE, 6),
    GRAY_STAINED_GLASS_PANE(Material.STAINED_GLASS_PANE, 7),
    LIGHT_GRAY_STAINED_GLASS_PANE(Material.STAINED_GLASS_PANE, 8),
    CYAN_STAINED_GLASS_PANE(Material.STAINED_GLASS_PANE, 9),
    PURPLE_STAINED_GLASS_PANE(Material.STAINED_GLASS_PANE, 10),
    BLUE_STAINED_GLASS_PANE(Material.STAINED_GLASS_PANE, 11),
    BROWN_STAINED_GLASS_PANE(Material.STAINED_GLASS_PANE, 12),
    GREEN_STAINED_GLASS_PANE(Material.STAINED_GLASS_PANE, 13),
    RED_STAINED_GLASS_PANE(Material.STAINED_GLASS_PANE, 14),
    BLACK_STAINED_GLASS_PANE(Material.STAINED_GLASS_PANE, 15),

    // Clay
    WHITE_STAINED_STAINED_CLAY(Material.STAINED_CLAY, 0),
    ORANGE_STAINED_CLAY(Material.STAINED_CLAY, 1),
    MAGENTA_STAINED_CLAY(Material.STAINED_CLAY, 2),
    LIGHT_BLUE_STAINED_CLAY(Material.STAINED_CLAY, 3),
    YELLOW_STAINED_CLAY(Material.STAINED_CLAY, 4),
    LIME_STAINED_CLAY(Material.STAINED_CLAY, 5),
    PINK_STAINED_CLAY(Material.STAINED_CLAY, 6),
    GRAY_STAINED_CLAY(Material.STAINED_CLAY, 7),
    LIGHT_GRAY_STAINED_CLAY(Material.STAINED_CLAY, 8),
    CYAN_STAINED_CLAY(Material.STAINED_CLAY, 9),
    PURPLE_STAINED_CLAY(Material.STAINED_CLAY, 10),
    BLUE_STAINED_CLAY(Material.STAINED_CLAY, 11),
    BROWN_STAINED_CLAY(Material.STAINED_CLAY, 12),
    GREEN_STAINED_CLAY(Material.STAINED_CLAY, 13),
    RED_STAINED_CLAY(Material.STAINED_CLAY, 14),
    BLACK_STAINED_CLAY(Material.STAINED_CLAY, 15),

    // Carpet
    WHITE_STAINED_CARPET(Material.CARPET, 0),
    ORANGE_CARPET(Material.CARPET, 1),
    MAGENTA_CARPET(Material.CARPET, 2),
    LIGHT_BLUE_CARPET(Material.CARPET, 3),
    YELLOW_CARPET(Material.CARPET, 4),
    LIME_CARPET(Material.CARPET, 5),
    PINK_CARPET(Material.CARPET, 6),
    GRAY_CARPET(Material.CARPET, 7),
    LIGHT_GRAY_CARPET(Material.CARPET, 8),
    CYAN_CARPET(Material.CARPET, 9),
    PURPLE_CARPET(Material.CARPET, 10),
    BLUE_CARPET(Material.CARPET, 11),
    BROWN_CARPET(Material.CARPET, 12),
    GREEN_CARPET(Material.CARPET, 13),
    RED_CARPET(Material.CARPET, 14),
    BLACK_CARPET(Material.CARPET, 15),

    // Slab
    STONE_SLAB(Material.STEP, 0),
    SANDSTONE_SLAB(Material.STEP, 1),
    COBBLESTONE_SLAB(Material.STEP, 3),
    BRICK_SLAB(Material.STEP, 4),
    STONE_BRICK_SLAB(Material.STEP, 5),
    NETHER_BRICK_SLAB(Material.STEP, 6),
    QUARTZ_SLAB(Material.STEP, 7),
    OAK_SLAB(Material.WOOD_STEP, 0),
    SPRUCE_SLAB(Material.WOOD_STEP, 1),
    BIRCH_SLAB(Material.WOOD_STEP, 2),
    JUNGLE_SLAB(Material.WOOD_STEP, 3),
    ACACIA_SLAB(Material.WOOD_STEP, 4),
    DARK_OAK_SLAB(Material.WOOD_STEP, 5),

    // Sapling
    OAK_SAPLING(Material.SAPLING, 0),
    SPRUCE_SAPLING(Material.SAPLING, 1),
    BIRCH_SAPLING(Material.SAPLING, 2),
    JUNGLE_SAPLING(Material.SAPLING, 3),
    ACACIA_SAPLING(Material.SAPLING, 4),
    DARK_OAK_SAPLING(Material.SAPLING, 5),
    
    // Leaves
    OAK_LEAVES(Material.LEAVES, 0),
    SPRUCE_LEAVES(Material.LEAVES, 1),
    BIRCH_LEAVES(Material.LEAVES, 2),
    JUNGLE_LEAVES(Material.LEAVES, 3),
    ACACIA_LEAVES(Material.LEAVES_2, 0),
    DARK_OAK_LEAVES(Material.LEAVES_2, 1),

    // Plank
    OAK_PLANK(Material.WOOD, 0),
    SPRUCE_PLANK(Material.WOOD, 1),
    BIRCH_PLANK(Material.WOOD, 2),
    JUNGLE_PLANK(Material.WOOD, 3),
    ACACIA_PLANK(Material.WOOD, 4),
    DARK_OAK_PLANK(Material.WOOD, 5),

    // Log
    OAK_LOG(Material.LOG, 0),
    SPRUCE_LOG(Material.LOG, 1),
    BIRCH_LOG(Material.LOG, 2),
    JUNGLE_LOG(Material.LOG, 3),
    ACACIA_LOG(Material.LOG_2, 0),
    DARK_OAK_LOG(Material.LOG_2, 1),

    // Sand
    SAND(Material.SAND, 0),
    RED_SAND(Material.SAND, 1),

    // Sponge
    DRY_SPONGE(Material.SPONGE, 0),
    WET_SPONGE(Material.SPONGE, 1),

    // Sandstone
    SANDSTONE(Material.SANDSTONE, 0),
    CHISELED_SANDSTONE(Material.SANDSTONE, 1),
    SMOOTH_SANDSTONE(Material.SANDSTONE, 2),
    RED_SANDSTONE(Material.RED_SANDSTONE, 0),
    CHISELED_RED_SANDSTONE(Material.RED_SANDSTONE, 1),
    SMOOTH_RED_SANDSTONE(Material.RED_SANDSTONE, 2),

    // Stone
    STONE(Material.STONE, 0),
    GRANITE(Material.STONE, 1),
    POLISHED_GRANITE(Material.STONE, 2),
    DIORITE(Material.STONE, 3),
    POLISHED_DIORITE(Material.STONE, 4),
    ANDESITE(Material.STONE, 5),
    POLISHED_ANDESITE(Material.STONE, 6),

    // Stonebricks
    STONE_BRICKS(Material.SMOOTH_BRICK, 0),
    MOSSY_STONE_BRICKS(Material.SMOOTH_BRICK, 1),
    CRACKED_STONE_BRICKS(Material.SMOOTH_BRICK, 2),
    CHISELED_STONE_BRICKS(Material.SMOOTH_BRICK, 3),

    DIRT(Material.DIRT, 0),
    COARSE_DIRT(Material.DIRT, 1),
    PODZOL(Material.DIRT, 2),

    // Wall
    COBBLESTONE_WALL(Material.COBBLE_WALL, 0),
    MOSSY_COBBLESTONE_WALL(Material.COBBLE_WALL, 1),

    // SKULL
    SKELETON_SKULL(Material.SKULL, 0),
    WITHER_SKULL(Material.SKULL, 1),
    ZOMBIE_SKULL(Material.SKULL, 2),
    HEAD(Material.SKULL, 3),
    CREEPER_SKULL(Material.SKULL, 4);


    private final Material material;
    private final int id;

    ColoredMaterial(Material material, int id)
    {
        this.material = material;
        this.id = id;
    }

    public static ColoredMaterial fromMaterialAndData(Material material, int data)
    {
        for(ColoredMaterial coloredMaterial : ColoredMaterial.values())
        {
            if(coloredMaterial.material.equals(material) && coloredMaterial.id == data)
            {
                return coloredMaterial;
            }
        }
        return null;
    }

    public static ColoredMaterial fromItemstack(ItemStack itemStack)
    {
        Material material = itemStack.getType();
        String dataString = itemStack.getData().toString();
        int i = dataString.indexOf('(');
        int j = dataString.indexOf(')');
        if(i == -1 || j == -1)
        {
            return null;
        }
        String data = dataString.substring(i + 1, j);
        byte dataVal = (byte) Short.parseShort(data);

        for(ColoredMaterial coloredMaterial : ColoredMaterial.values())
        {
            if(material.equals(coloredMaterial.getMaterial())
                    && dataVal == coloredMaterial.id)
            {
                return coloredMaterial;
            }
        }

        return null;
    }

    public Material getMaterial()
    {
        return material;
    }
}
