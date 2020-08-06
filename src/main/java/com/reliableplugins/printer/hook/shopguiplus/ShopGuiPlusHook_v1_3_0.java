/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook.shopguiplus;

import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.inventory.ItemStack;

public class ShopGuiPlusHook_v1_3_0 implements ShopGuiPlusHook
{
    @Override
    public double getPrice(ItemStack item)
    {
        return ShopGuiPlusApi.getItemStackPriceBuy(item);
    }
}
