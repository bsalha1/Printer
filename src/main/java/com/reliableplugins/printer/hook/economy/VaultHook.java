package com.reliableplugins.printer.hook.economy;

import com.reliableplugins.printer.Printer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

import java.util.concurrent.Executors;

public class VaultHook implements EconomyHook
{
    private final Economy economy;

    public VaultHook(Economy economy)
    {
        this.economy = economy;
    }

    public boolean queueWithdraw(Player player, double amount)
    {
        if(this.economy.getBalance(player) - amount >= 0)
        {
            Printer.INSTANCE.getAsyncTaskManager().enqueueTask(() ->
                    Printer.INSTANCE.getEconomyHook().withdraw(player, amount));

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
