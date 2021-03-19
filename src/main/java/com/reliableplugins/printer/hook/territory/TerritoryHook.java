package com.reliableplugins.printer.hook.territory;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface TerritoryHook
{
    boolean isNonTerritoryMemberNearby(Player player);

    boolean isInATerritory(Player player);

    boolean isInOwnTerritory(Player player);

    boolean canBuild(Player player, Location location, boolean allowWilderness);
}
