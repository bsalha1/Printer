package com.reliableplugins.printer.hook.shop;

import me.sat7.dynamicshop.DynaShopAPI;
import org.bukkit.inventory.ItemStack;

public class DynamicShopHook extends ShopHook
{
    @Override
    public double getPrice(ItemStack item)
    {
        for(String shopName : DynaShopAPI.getShops())
        {
            double price = DynaShopAPI.getBuyPrice(shopName, item);
            if(price > 0)
            {
                return price;
            }
        }
        return -1;
    }
}
