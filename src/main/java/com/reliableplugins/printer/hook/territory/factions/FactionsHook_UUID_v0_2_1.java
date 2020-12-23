/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook.territory.factions;

import com.massivecraft.factions.*;
import com.massivecraft.factions.struct.Relation;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.entity.Player;

public class FactionsHook_UUID_v0_2_1 implements FactionsHook
{
    @Override
    public boolean isNonTerritoryMemberNearby(Player player)
    {
        FPlayer mPlayer = FPlayers.getInstance().getByPlayer(player);
        for(Player nearbyPlayer : BukkitUtil.getNearbyPlayers(player))
        {
            FPlayer nearbyMPlayer = FPlayers.getInstance().getByPlayer(nearbyPlayer);
            if(nearbyMPlayer == null || mPlayer == null || mPlayer.getFaction().isWilderness() ||
                    !mPlayer.getFaction().equals(nearbyMPlayer.getFaction()))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInATerritory(Player player)
    {
        return !Board.getInstance().getFactionAt(new FLocation(player.getLocation())).isWilderness();
    }

    @Override
    public boolean isInOwnTerritory(Player player)
    {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        Faction faction = Board.getInstance().getFactionAt(new FLocation(player.getLocation()));

        if(fPlayer == null || faction == null || faction.isWilderness())
        {
            return false;
        }

        return faction.equals(fPlayer.getFaction());
    }

    @Override
    public boolean isNonTerritoryMemberNearby(Player player, boolean allowAllies)
    {
        if(!allowAllies)
        {
            return isNonTerritoryMemberNearby(player);
        }

        // If all nearby players are either in same faction or in an ally faction
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        for(Player nearbyPlayer : BukkitUtil.getNearbyPlayers(player))
        {
            FPlayer nearbyFPlayer = FPlayers.getInstance().getByPlayer(nearbyPlayer);
            if(nearbyFPlayer == null ||
                    fPlayer == null ||
                    fPlayer.getFaction().isWilderness() ||
                    (!fPlayer.getFaction().equals(nearbyFPlayer.getFaction()) && !fPlayer.getFaction().getRelationTo(nearbyFPlayer).equals(Relation.ALLY)))
            {
                return true;
            }
        }
        return false;
    }
}
