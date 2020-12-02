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
        if(event.getPlugin().getName().contains("Factions") && !Printer.INSTANCE.hasFactionsHook())
        {
            Printer.INSTANCE.setupFactionsHook();
        }
        else if(event.getPlugin().getName().contains("ShopGUIPlus") && !Printer.INSTANCE.hasShopHook())
        {
            Printer.INSTANCE.setupShopHook();
        }
        else if(event.getPlugin().getName().contains("zShop") && !Printer.INSTANCE.hasShopHook())
        {
            Printer.INSTANCE.setupShopHook();
        }
        else if(event.getPlugin().getName().contains("Citizens") && !Printer.INSTANCE.hasCitizensHook())
        {
            Printer.INSTANCE.setupCitizensHook();
        }
        else if(event.getPlugin().getName().contains("Residence") && !Printer.INSTANCE.hasResidenceHook())
        {
            Printer.INSTANCE.setupResidenceHook();
        }
        else if(event.getPlugin().getName().contains("SuperiorSkyblock") && !Printer.INSTANCE.hasSuperiorSkyBlockHook())
        {
            Printer.INSTANCE.setupSuperiorSkyBlockHook();
        }
    }
}
