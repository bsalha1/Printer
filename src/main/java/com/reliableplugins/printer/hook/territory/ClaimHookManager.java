/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook.territory;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ClaimHookManager
{
    public static String PRINTER_HIDE_PERMISSION_NODE = "printer.hide";

    private final ArrayList<ClaimHook> claimHooks;

    public ClaimHookManager()
    {
        this.claimHooks = new ArrayList<>();
    }

    public void registerClaimHook(ClaimHook claimHook)
    {
        this.claimHooks.add(claimHook);
    }



    /**
     * Determines if a non-claim-member is nearby
     * @param player player to search from
     * @return true if a non-claim member is nearby, else false
     */
    public boolean isNonClaimMemberNearby(Player player)
    {
        for(Player nearbyPlayer : BukkitUtil.getNearbyPlayers(player))
        {
            if(nearbyPlayer.hasPermission(PRINTER_HIDE_PERMISSION_NODE))
            {
                continue;
            }

            for(ClaimHook claimHook : claimHooks)
            {
                if(!claimHook.isClaimFriend(player, nearbyPlayer))
                {
                    return true;
                }
            }
        }
        return false;
    }



    /**
     * Determines if player is restricted from placing a block
     * @param player player placing the block
     * @param location location to place the block
     * @return true if they cannot place it, else false
     */
    public boolean isTerritoryRestricted(Player player, Location location)
    {
        for(ClaimHook hook : this.claimHooks)
        {
            if(!hook.canBuild(player, location, Printer.INSTANCE.getMainConfig().allowInWilderness()))
            {
                return true;
            }
        }
        return false;
    }



    /**
     * Determines if player is restricted from using printer in their location
     * @param player player to use printer
     * @return true if they cannot be in that location with printer on, false otherwise
     */
    public boolean isTerritoryRestricted(Player player)
    {
        for(ClaimHook hook : this.claimHooks)
        {
            // If player isn't in their own territory and they aren't in wilderness (if they're allowed)
            if(!hook.isInOwnClaim(player) &&
                    (hook.isInAClaim(player) || !Printer.INSTANCE.getMainConfig().allowInWilderness()))
            {
                Message.ERROR_NOT_IN_TERRITORY.sendColoredMessage(player);
                return true;
            }
            else if(!Printer.INSTANCE.getMainConfig().allowNearNonMembers() && this.isNonClaimMemberNearby(player))
            {
                Message.ERROR_NON_TERRITORY_MEMBER_NEARBY.sendColoredMessage(player);
                return true;
            }
        }
        return false;
    }
}
