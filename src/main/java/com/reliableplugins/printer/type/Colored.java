package com.reliableplugins.printer.type;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class Colored
{
    private final Material material;
    private final DyeColor color;

    public Colored(Material material, DyeColor color)
    {
        this.material = material;
        this.color = color;
    }

    public static Colored fromItemstack(ItemStack itemStack)
    {
        if(isColorable(itemStack))
        {
            String dataString = itemStack.getData().toString();
            int i = dataString.indexOf('(');
            int j = dataString.indexOf(')');
            if(i == -1 || j == -1)
            {
                return null;
            }
            String data = dataString.substring(i + 1, j);
            DyeColor color = DyeColor.getByWoolData((byte) Short.parseShort(data));

            return new Colored(itemStack.getType(), color);
        }
        else
        {
            return null;
        }
    }

    public static boolean isColorable(ItemStack itemStack)
    {
        switch(itemStack.getType())
        {
            case WOOL:
            case STAINED_GLASS:
            case STAINED_CLAY:
            case STAINED_GLASS_PANE:
                return true;

            default:
                return false;
        }
    }

    public DyeColor getColor()
    {
        return color;
    }

    public Material getMaterial()
    {
        return material;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Colored colored = (Colored) o;
        return material == colored.material &&
                color == colored.color;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(material, color);
    }
}
