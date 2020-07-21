package com.reliableplugins.printer.nms;

import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface INMSHandler
{
    void sendToolTipText(Player player, String message);

    boolean isArmor(ItemStack itemStack);
}
