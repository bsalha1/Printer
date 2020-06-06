package com.reliableplugins.printer.type;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;

public enum ColoredMaterial
{
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
    
    STONE_SLAB(Material.STEP, 0),
    SANDSTONE_SLAB(Material.STEP, 1),
    COBBLESTONE_SLAB(Material.STEP, 3),
    BRICK_SLAB(Material.STEP, 4),
    STONE_BRICK_SLAB(Material.STEP, 5),
    NETHER_BRICK_SLAB(Material.STEP, 6),
    QUARTZ_SLAB(Material.STEP, 7),
    
    OAK_PLANK(Material.WOOD, 0),
    SPRUCE_PLANK(Material.WOOD, 1),
    BIRCH_PLANK(Material.WOOD, 2),
    JUNGLE_PLANK(Material.WOOD, 3),
    ACACIA_PLANK(Material.WOOD, 4),
    DARK_OAK_PLANK(Material.WOOD, 5),

    OAK_WOOD(Material.LOG, 0),
    SPRUCE_WOOD(Material.LOG, 1),
    BIRCH_WOOD(Material.LOG, 2),
    JUNGLE_WOOD(Material.LOG, 3),
    ACACIA_WOOD(Material.LOG_2, 0),
    DARK_OAK_WOOD(Material.LOG_2, 1),
    
    OAK_SLAB(Material.WOOD_STEP, 0),
    SPRUCE_SLAB(Material.WOOD_STEP, 1),
    BIRCH_SLAB(Material.WOOD_STEP, 2),
    JUNGLE_SLAB(Material.WOOD_STEP, 3),
    ACACIA_SLAB(Material.WOOD_STEP, 4),
    DARK_OAK_SLAB(Material.WOOD_STEP, 5),

    SAND(Material.SAND, 0),
    RED_SAND(Material.SAND, 1),

    SANDSTONE(Material.SANDSTONE, 0),
    CHISELED_SANDSTONE(Material.SANDSTONE, 1),
    SMOOTH_SANDSTONE(Material.SANDSTONE, 2),
    RED_SANDSTONE(Material.RED_SANDSTONE, 0),
    CHISELED_RED_SANDSTONE(Material.RED_SANDSTONE, 1),
    SMOOTH_RED_SANDSTONE(Material.RED_SANDSTONE, 2),

    COBBLESTONE_WALL(Material.COBBLE_WALL, 0),
    MOSSY_COBBLESTONE_WALL(Material.COBBLE_WALL, 1);

    private final Material material;
    private final int id;

    private static final HashSet<Material> COLORABLE = new HashSet<>();
    static
    {
        for(ColoredMaterial coloredMaterial : ColoredMaterial.values())
        {
            COLORABLE.add(coloredMaterial.getMaterial());
        }
    }

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

    public static boolean isColorable(ItemStack itemStack)
    {
        return COLORABLE.contains(itemStack.getType());
    }
    public static boolean isColorable(Material material)
    {
        return COLORABLE.contains(material);
    }

    public Material getMaterial()
    {
        return material;
    }
}
