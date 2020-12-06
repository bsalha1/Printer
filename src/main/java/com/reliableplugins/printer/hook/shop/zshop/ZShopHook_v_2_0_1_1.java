package com.reliableplugins.printer.hook.shop.zshop;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.hook.shop.ShopHook;
import fr.maxlego08.shop.api.ShopManager;
import org.bukkit.inventory.ItemStack;

public class ZShopHook_v_2_0_1_1 implements ShopHook
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