package com.reliableplugins.printer.type;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PrinterPlayer
{
    private GameMode initialGamemode;
    private ItemStack[] initialInventory;
    private final Player player;
    private boolean printing = false;

    public PrinterPlayer(Player player)
    {
        this.player = player;
        this.initialGamemode = player.getGameMode();
        this.initialInventory = player.getInventory().getContents();
    }

    // Cache initial gamemode and inventory to give back on printer off
    public void printerOn()
    {
        if(!printing)
        {
            initialInventory = player.getInventory().getContents();
            initialGamemode = player.getGameMode();

            if(player.getOpenInventory() != null)
            {
                player.getOpenInventory().close();
            }
            player.getInventory().clear();
            player.setGameMode(GameMode.CREATIVE);
            printing = true;
        }
    }

    // Set gamemode and inventory back to initials
    public void printerOff()
    {
        if(printing)
        {
            player.setGameMode(initialGamemode);
            player.getInventory().setContents(initialInventory);
            printing = false;
        }
    }

    public Player getPlayer()
    {
        return player;
    }

    public boolean isPrinting()
    {
        return printing;
    }
}
