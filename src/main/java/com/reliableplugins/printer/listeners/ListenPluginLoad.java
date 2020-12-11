/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.listeners;

import com.reliableplugins.printer.Printer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

public class ListenPluginLoad implements Listener
{
    // This will catch any plugins that load after Printer and hook into them if they were not already hooked into
    @EventHandler
    public void onPluginLoad(PluginEnableEvent event)
    {
        String name = event.getPlugin().getName();
        if(name.contains("Factions") && !Printer.INSTANCE.hasFactionsHook())
        {
            Printer.INSTANCE.setupFactionsHook();
        }
        else if(!Printer.INSTANCE.hasShopHook() &&
                (name.contains("ShopGUIPlus") || name.contains("zShop") || name.contains("DynamicShop")))
        {
            Printer.INSTANCE.setupShopHook();
        }
        else if(name.contains("Citizens") && !Printer.INSTANCE.hasCitizensHook())
        {
            Printer.INSTANCE.setupCitizensHook();
        }
        else if(name.contains("Residence") && !Printer.INSTANCE.hasResidenceHook())
        {
            Printer.INSTANCE.setupResidenceHook();
        }
        else if(Printer.INSTANCE.hasSkyblockHook() &&
                (name.contains("SuperiorSkyblock") || name.contains("BentoBox") || name.contains("IridiumSkyblock")))
        {
            Printer.INSTANCE.setupSkyblockHook();
        }
    }
}
