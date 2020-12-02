package com.reliableplugins.printer.hook.residence;

import org.bukkit.entity.Player;

public interface ResidenceHook
{
    boolean isNonResidenceMemberNearby(Player player);

    boolean hasResidence(Player player);

    boolean isInAResidence(Player player);

    boolean isInOwnResidence(Player player);
}
