package com.reliableplugins.printer.task;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.type.PrinterPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryScanner extends BukkitTask
{
    public InventoryScanner(long delay, long period)
    {
        super(delay, period);
    }

    @Override
    public void run()
    {
        // Removes externally added items
        for(Player player : Bukkit.getOnlinePlayers())
        {
            if(Printer.INSTANCE.printerPlayers.containsKey(player))
            {
                PrinterPlayer printerPlayer = Printer.INSTANCE.printerPlayers.get(player);
                if(printerPlayer.isPrinting())
                {
                    for(ItemStack item : player.getInventory().getContents())
                    {
                        if(item != null && !printerPlayer.isInternalItem(item))
                        {
                            player.getInventory().remove(item);
                        }
                    }
                }
            }
        }
    }
}
