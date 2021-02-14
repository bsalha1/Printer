package com.reliableplugins.printer.hook.economy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

import java.util.concurrent.Executors;

public class VaultHook implements EconomyHook
{
    private final Economy economy;
    private final EconomyManager economyManager;

    public VaultHook(Economy economy)
    {
        this.economy = economy;
        this.economyManager = new EconomyManager();

        Executors.newSingleThreadExecutor().submit(this.economyManager);
    }

    public boolean queueWithdraw(Player player, double amount)
    {
        if(this.economy.getBalance(player) - amount >= 0)
        {
            this.economyManager.enqueueRequest(player, amount);
            return true;
        }
        else
        {
            return false;
        }
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
