package com.reliableplugins.printer.hook.territory;

import org.bukkit.entity.Player;

public interface TerritoryHook
{
    boolean isNonTerritoryMemberNearby(Player player);

    boolean isInATerritory(Player player);

    boolean isInOwnTerritory(Player player);
}
