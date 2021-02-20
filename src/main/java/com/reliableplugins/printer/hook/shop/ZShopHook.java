package com.reliableplugins.printer.hook.shop;

import com.reliableplugins.printer.Printer;
import fr.maxlego08.shop.api.ShopManager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Level;

public class ZShopHook extends ShopHook
{
    private <T> T getProvider(Class<T> clazz)
    {
        RegisteredServiceProvider<T> provider = Printer.INSTANCE.getServer().getServicesManager().getRegistration(clazz);
        if (provider == null)
            return null;
        return provider.getProvider() != null ? provider.getProvider() : null;
    }

    @Override
    public double getPrice(ItemStack item)
    {
        ShopManager manager = getProvider(ShopManager.class);
        if(manager == null)
        {
            Printer.INSTANCE.getLogger().log(Level.SEVERE, "Failed to get ShopManager provider for ZShop");
            return -1;
        }
        final double[] price = {-1};
        manager.getItemButton(item).ifPresent(itemButton -> price[0] = itemButton.getBuyPrice());

        return price[0];
    }
}
