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
        else if(name.contains("ShopGUIPlus") && !Printer.INSTANCE.hasShopHook())
        {
            Printer.INSTANCE.setupShopHook();
        }
        else if(name.contains("zShop") && !Printer.INSTANCE.hasShopHook())
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
        else if((name.contains("SuperiorSkyblock") || name.contains("BentoBox")) && !Printer.INSTANCE.hasSkyblockHook())
        {
            Printer.INSTANCE.setupSkyblockHook();
        }
    }
}
