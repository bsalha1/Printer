/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook.shop.shopgui;

import com.reliableplugins.printer.hook.shop.ShopHook;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.exception.player.PlayerDataNotLoadedException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ShopGuiPlusHook_1_0_1 extends ShopHook
{
    private final HashMap<ItemStack, Double> priceCache;

    public ShopGuiPlusHook_1_0_1()
    {
        this.priceCache = new HashMap<>();
    }

    @Override
    public double getPrice(Player player, ItemStack item)
    {
        Double price = this.priceCache.get(item);
        if(price == null)
        {
            try
            {
                price = ShopGuiPlusApi.getItemStackPriceBuy(player, item);
            }
            catch (PlayerDataNotLoadedException e)
            {
                e.printStackTrace();
                price = null;
            }

            this.priceCache.put(item, price);
        }
        return price;
    }
}
