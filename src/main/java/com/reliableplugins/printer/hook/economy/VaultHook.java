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

    @Override
    public void withdraw(Player player, double amount)
    {
        this.economy.withdrawPlayer(player, amount);
    }

    public double getBalance(Player player)
    {
        return this.economy.getBalance(player);
    }
}
