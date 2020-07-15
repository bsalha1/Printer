package com.reliableplugins.printer.task;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.hook.SuperiorSkyblockHook;
import com.reliableplugins.printer.type.PrinterPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SuperiorSkyBlockScanner extends BukkitTask
{
    public SuperiorSkyBlockScanner(long delay, long period)
    {
        super(delay, period);
    }

    @Override
    public void run()
    {
        for(Player player : Bukkit.getOnlinePlayers())
        {
            if(Printer.INSTANCE.printerPlayers.containsKey(player))
            {
                PrinterPlayer printerPlayer = Printer.INSTANCE.printerPlayers.get(player);
                if(printerPlayer.isPrinting())
                {
                    if(SuperiorSkyblockHook.canPlayerBuild(player, player.getLocation()))
                    {
                        printerPlayer.printerOff();
                        player.sendMessage(Message.ERROR_NOT_IN_ISLAND.getMessage());
                    }
                }
            }
        }
    }
}
