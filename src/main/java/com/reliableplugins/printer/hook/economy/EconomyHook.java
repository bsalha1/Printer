package com.reliableplugins.printer.hook.economy;

import org.bukkit.entity.Player;

public interface EconomyHook
{
    double getBalance(Player player);

    boolean queueWithdraw(Player player, double amount);

    void withdraw(Player player, double amount);
}
