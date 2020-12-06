package com.reliableplugins.printer.hook.economy;

import org.bukkit.entity.Player;

public interface EconomyHook
{
    double getBalance(Player player);

    boolean withdraw(Player player, double amount);
}
