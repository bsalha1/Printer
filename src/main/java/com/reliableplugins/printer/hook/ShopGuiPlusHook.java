package com.reliableplugins.printer.hook;

import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.inventory.ItemStack;

public class ShopGuiPlusHook
{
    public static double getPrice(ItemStack item)
    {
        return ShopGuiPlusApi.getItemStackPriceBuy(item);
    }
}
