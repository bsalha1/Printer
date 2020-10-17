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
            Printer.INSTANCE.getLogger().log(Level.INFO, "Successfully hooked into Factions");
        }
    }
}
