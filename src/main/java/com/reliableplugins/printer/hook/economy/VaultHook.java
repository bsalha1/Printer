package com.reliableplugins.printer.hook.economy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class VaultHook implements EconomyHook
{
    private final Economy economy;

    public VaultHook(Economy economy)
    {
        this.economy = economy;
    }

    public boolean withdraw(Player player, double amount)
    {
        if(economy.getBalance(player) - amount >= 0)
        {
            economy.withdrawPlayer(player, amount);
            return true;
        }
        else
        {
            return false;
        }
    }

    public double getBalance(Player player)
    {
        return economy.getBalance(player);
    }
}
