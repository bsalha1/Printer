package com.reliableplugins.printer.config;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.type.ColoredMaterial;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.logging.Level;

public class PricesConfig extends Config
{
    private HashMap<Material, Double> blockPrices = new HashMap<>();
    private HashMap<Material, Double> itemPrices = new HashMap<>();
    private HashMap<ColoredMaterial, Double> coloredPrices = new HashMap<>();

    public PricesConfig()
    {
        super("prices.yml");
    }

    @Override
    public void load()
    {
        blockPrices = new HashMap<>();
        getConfig().options().header("For a complete list of material names go to: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html\n"
                + "For colored blocks or typed blocks such as oak, birch, spruce and jungle logs, add the name of \n"
                + "the material followed by a dash '-' and then the id");

        getDouble("NETHERRACK", 1.0);
        getDouble("DIRT", 1.0);
        getDouble("STEP-0", 0.5);
        getDouble("SIGN", 1.0);
        getDouble("COBBLESTONE", 1.0);

        getDouble("STONE", 1.0);
        getDouble("SAND-0", 1.0);
        getDouble("SAND-1", 1.0);
        getDouble("DISPENSER", 5.0);
        getDouble("WATER_BUCKET", 5.0);
        getDouble("LAVA_BUCKET", 5.0);
        getDouble("OBSIDIAN", 20.0);
        getDouble("DIODE", 5.0);
        getDouble("REDSTONE_COMPARATOR", 5.0);
        getDouble("REDSTONE_WIRE", 1.0);
        getDouble("REDSTONE_BLOCK", 9.0);
        getDouble("REDSTONE_TORCH", 1.0);
        getDouble("REDSTONE_LAMP_OFF", 5.0);
        getDouble("STONE_BUTTON", 1.0);
        getDouble("WOOD_BUTTON", 1.0);
        getDouble("PISTON_BASE", 5.0);
        getDouble("PISTON_STICKY_BASE", 5.0);
        getDouble("LADDER", 5.0);
        getDouble("TRAP_DOOR", 5.0);
        getDouble("IRON_TRAPDOOR", 5.0);
        getDouble("GLASS", 2.0);
        getDouble("LEVER", 2.0);

        getDouble("SOUL_SAND", 2.0);
        getDouble("NETHER_STALK", 2.0);

        getDouble("WOOL-0", 5.0);
        getDouble("WOOL-13", 5.0);
        getDouble("STAINED_GLASS-12", 5.0);

        // Load Regular Blocks
        for(String blockName : config.getKeys(true))
        {
            double price = config.getDouble(blockName, -1);
            if(price < 0)
            {
                Printer.INSTANCE.getLogger().log(Level.WARNING, blockName + " has an invalid price: " + price);
            }
            else
            {
                if(blockName.contains("-"))
                {
                    coloredPrices.put(getColored(blockName), price);
                }
                else
                {
                    Material material = Material.valueOf(blockName);
                    if(BukkitUtil.isItemMaterial(material))
                    {
                        itemPrices.put(material, price);
                    }
                    else
                    {
                        blockPrices.put(material, price);
                    }
                }
            }
        }
        save();
    }

    private ColoredMaterial getColored(String blockName)
    {
        String materialName = blockName.substring(0, blockName.indexOf('-'));
        byte data = (byte) Short.parseShort(blockName.substring(blockName.indexOf('-') + 1));
        Material material = Material.valueOf(materialName);

        return ColoredMaterial.fromMaterialAndData(material, data);
    }

    public HashMap<ColoredMaterial, Double> getColoredPrices()
    {
        return coloredPrices;
    }

    public HashMap<Material, Double> getBlockPrices()
    {
        return blockPrices;
    }

    public HashMap<Material, Double> getItemPrices()
    {
        return itemPrices;
    }
}
