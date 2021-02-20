/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook.shop;

import org.bukkit.inventory.ItemStack;

public interface IShopHook
{
    double getPrice(ItemStack item);
}
