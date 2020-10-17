package com.reliableplugins.printer.hook.superiorskyblock;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.task.BukkitTask;
import com.reliableplugins.printer.type.PrinterPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SuperiorSkyblockScanner extends BukkitTask
{
    public SuperiorSkyblockScanner(long delay, long period)
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
                    // If printing is allowed on a non island and the player cannot build
                    // Or if that player is not on their own island, turn printer off
                    if((Printer.INSTANCE.getMainConfig().allowInNonIsland() && !Printer.INSTANCE.getSuperiorSkyBlockHook().canPlayerBuild(player, player.getLocation()))
                            || !Printer.INSTANCE.getSuperiorSkyBlockHook().isOnOwnIsland(player))
                    {
                        printerPlayer.printerOff();
                        player.sendMessage(Message.ERROR_NOT_IN_ISLAND.getMessage());
                    }
                    else if(Printer.INSTANCE.getMainConfig().allowNearNonIslandMembers() && Printer.INSTANCE.getSuperiorSkyBlockHook().isNonIslandMemberNearby(player))
                    {
                        printerPlayer.printerOff();
                        player.sendMessage(Message.ERROR_NON_ISLAND_MEMBER_NEARBY.getMessage());
                    }
                }
            }
        }
    }
}