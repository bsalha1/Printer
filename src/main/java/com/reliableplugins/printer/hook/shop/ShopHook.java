/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook.shop;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public abstract class ShopHook implements IShopHook
{
    private final HashMap<ItemStack, Double> priceCache;

    protected ShopHook()
    {
        this.priceCache = new HashMap<>();
    }

    public final double getCachedPrice(ItemStack item)
    {
        Double price = this.priceCache.get(item);
        if(price == null)
        {
            price = this.getPrice(item);
            this.priceCache.put(item, price);
        }
        return price;
    }

    public final void clearCache()
    {
        this.priceCache.clear();
    }
}
