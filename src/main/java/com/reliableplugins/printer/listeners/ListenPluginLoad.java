/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.listeners;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.hook.factions.FactionsHook_MassiveCraft;
import com.reliableplugins.printer.hook.factions.FactionsHook_UUID_v0_2_1;
import com.reliableplugins.printer.hook.factions.FactionsScanner;
import com.reliableplugins.printer.hook.shopguiplus.ShopGuiPlusHook_v1_3_0;
import com.reliableplugins.printer.hook.shopguiplus.ShopGuiPlusHook_v1_4_0;
import com.reliableplugins.printer.hook.shopguiplus.ShopGuiPlusHook_v1_5_0;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

import java.util.logging.Level;

public class ListenPluginLoad implements Listener
{
    @EventHandler
    public void onPluginLoad(PluginEnableEvent event)
    {
        if(event.getPlugin().getName().contains("Factions") && Printer.INSTANCE.getMainConfig().useFactions() && !Printer.INSTANCE.isFactions())
        {
            if(event.getPlugin().getDescription().getDepend().contains("MassiveCore"))
            {
                Printer.INSTANCE.setFactionsHook(new FactionsHook_MassiveCraft());
            }
            else
            {
                Printer.INSTANCE.setFactionsHook(new FactionsHook_UUID_v0_2_1());
            }
            Printer.INSTANCE.setFactionScanner(new FactionsScanner(0L, 5L));
            Printer.INSTANCE.setFactions(true);
            Printer.INSTANCE.getLogger().log(Level.INFO, "Successfully hooked into Factions");
        }
        else if(event.getPlugin().getName().contains("ShopGUIPlus") && Printer.INSTANCE.getMainConfig().useShopGuiPlus() && !Printer.INSTANCE.isShopGuiPlus())
        {
            String[] versions = event.getPlugin().getDescription().getVersion().split("\\.");
            int major = Integer.parseInt(versions[0]);
            int minor = Integer.parseInt(versions[1]);
            int build = Integer.parseInt(versions[2]);
            if(minor >= 33 && minor <= 34)
            {
                Printer.INSTANCE.setShopGuiPlusHook(new ShopGuiPlusHook_v1_3_0());
            }
            else if(minor == 35)
            {
                Printer.INSTANCE.setShopGuiPlusHook(new ShopGuiPlusHook_v1_4_0());
            }
            else
            {
                Printer.INSTANCE.setShopGuiPlusHook(new ShopGuiPlusHook_v1_5_0());
            }
            Printer.INSTANCE.setShopGuiPlus(true);
            Printer.INSTANCE.getLogger().log(Level.INFO, "Successfully hooked into ShopGUIPlus");
        }
    }
}
