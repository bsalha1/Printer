/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.task;

import com.reliableplugins.printer.Printer;
import org.bukkit.Bukkit;

public abstract class BukkitTask implements Runnable
{
    private final int id;
    private final boolean delayed;
    private final boolean repeating;

    // Set delayed task
    public BukkitTask(long delay)
    {
        this.id = Bukkit.getScheduler().scheduleSyncDelayedTask(Printer.INSTANCE, this, delay);
        delayed = delay > 0;
        repeating = false;
    }

    // Set delayed repeating task
    public BukkitTask(long delay, long period)
    {
        this.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Printer.INSTANCE, this, delay, period);
        delayed = delay > 0;
        repeating = true;
    }

    public void cancel()
    {
        Bukkit.getScheduler().cancelTask(this.id);
    }

    public int getId()
    {
        return this.id;
    }

    public boolean isDelayed()
    {
        return delayed;
    }

    public boolean isRepeating()
    {
        return repeating;
    }
}
