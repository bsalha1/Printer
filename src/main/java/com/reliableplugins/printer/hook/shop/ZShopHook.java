package com.reliableplugins.printer.hook.shop;

import com.reliableplugins.printer.Printer;
import fr.maxlego08.shop.api.ShopManager;
import org.bukkit.inventory.ItemStack;

public class ZShopHook implements ShopHook
{
    @Override
    public double getPrice(ItemStack item)
    {
        ShopManager manager = Printer.INSTANCE.getProvider(ShopManager.class);
        final double[] price = {-1};
        manager.getItemButton(item).ifPresent(itemButton -> price[0] = itemButton.getBuyPrice());

        return price[0];
    }
}
