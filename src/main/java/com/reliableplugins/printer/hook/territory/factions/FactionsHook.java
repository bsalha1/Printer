package com.reliableplugins.printer.hook.territory.factions;

import com.reliableplugins.printer.hook.territory.TerritoryHook;
import org.bukkit.entity.Player;

public interface FactionsHook extends TerritoryHook
{
    boolean isNonTerritoryMemberNearby(Player player, boolean allowAllies);
}
