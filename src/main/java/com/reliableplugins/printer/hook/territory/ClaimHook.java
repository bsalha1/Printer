package com.reliableplugins.printer.hook.territory;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface ClaimHook
{

    boolean isClaimFriend(Player owner, Player accessor);



    /**
     * Determines if a player is in some sort of claim.
     * @param player player to search from
     * @return true if player is in a claim, else false
     */
    boolean isInAClaim(Player player);



    /**
     * Determines if player is in their own claim.
     * @param player player to search from
     * @return true if player is in their own claim, else false
     */
    boolean isInOwnClaim(Player player);



    /**
     * Determines if player can place a block at a certain location.
     * @param player player placing block
     * @param location block-place location
     * @param allowWilderness if block was placed in wilderness, allowWilderness determines whether this block place is allowed
     * @return true if they can place a block at the location, else false
     */
    boolean canBuild(Player player, Location location, boolean allowWilderness);
}
