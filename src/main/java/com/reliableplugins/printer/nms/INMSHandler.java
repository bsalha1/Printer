/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface INMSHandler
{
    void sendToolTipText(Player player, String message);

    boolean isArmor(ItemStack itemStack);
}
