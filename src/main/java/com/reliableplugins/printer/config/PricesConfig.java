package com.reliableplugins.printer.config;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.type.Colored;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.DyeColor;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.logging.Level;

public class PricesConfig extends Config
{
    private HashMap<Material, Double> blockPrices = new HashMap<>();
    private HashMap<Material, Double> itemPrices = new HashMap<>();
    private HashMap<Colored, Double> coloredPrices = new HashMap<>();

    public PricesConfig()
    {
        super("prices.yml");
    }

    @Override
    public void load()
    {
        blockPrices = new HashMap<>();
        getConfig().options().header("For a complete list of material names go to: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html\n"
                + "For colored blocks, navigate to the \"colored\" section and add the name of the material followed by a dash '-' and then the color.\n"
                + "For complete list of color names go to: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/DyeColor.html");

        getDouble("blocks.STONE", 1.0);
        getDouble("blocks.NETHERRACK", 1.0);
        getDouble("blocks.DIRT", 1.0);
        getDouble("blocks.STEP", 0.5);
        getDouble("blocks.SIGN", 1.0);
        getDouble("blocks.COBBLESTONE", 1.0);
        getDouble("blocks.WATER_BUCKET", 5.0);
        getDouble("blocks.LAVA_BUCKET", 5.0);
        getDouble("blocks.OBSIDIAN", 20.0);


        getDouble("colored.WOOL-RED", 5.0);
        getDouble("colored.WOOL-LIME", 5.0);
        getDouble("colored.GLASS-BLACK", 5.0);

        // Load Regular Blocks
        for(String blockName : config.getConfigurationSection("blocks").getKeys(true))
        {
            double price = config.getDouble("blocks." + blockName, -1);
            if(price < 0)
            {
                Printer.INSTANCE.getLogger().log(Level.WARNING, blockName + " has an invalid price: " + price);
            }
            else
            {
                // If material is an itemmaterial, put it in the item price list
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

        // Load Colored Blocks
        for(String coloredName : config.getConfigurationSection("colored").getKeys(true))
        {
            double price = config.getDouble("colored." + coloredName, -1);
            if(price < 0)
            {
                Printer.INSTANCE.getLogger().log(Level.WARNING, coloredName + " has an invalid price: " + price);
            }
            else
            {
                String materialName = coloredName.substring(0, coloredName.indexOf('-'));
                String dyeName = coloredName.substring(coloredName.indexOf('-') + 1);
                Material material = Material.valueOf(materialName);
                DyeColor dyeColor;

                // Backwards Compatibility: In earlier versions LIGHT_GRAY is called SILVER - pick whichever one doesn't throw an exception
                if(dyeName.equals("SILVER") || dyeName.equals("LIGHT_GRAY"))
                {
                    try
                    {
                        dyeColor = DyeColor.valueOf("SILVER");
                    }
                    catch (IllegalArgumentException e)
                    {
                        dyeColor = DyeColor.valueOf("LIGHT_GRAY");
                    }
                }
                else
                {
                    dyeColor = DyeColor.valueOf(dyeName);
                }
                coloredPrices.put(new Colored(material, dyeColor), price);
            }
        }
        save();
    }

    public HashMap<Colored, Double> getColoredPrices()
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
