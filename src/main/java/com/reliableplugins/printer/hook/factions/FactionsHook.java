/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook.factions;

import org.bukkit.entity.Player;

public interface FactionsHook
{
    boolean areNeutralOrEnemies(Player player1, Player player2);

    boolean isEnemyOrNeutralNearby(Player player);

    boolean inOwnTerritory(Player player);

    boolean inWilderness(Player player);

    boolean canBuild(Player player);
}
