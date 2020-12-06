/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook.territory.factions;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.entity.Player;

public class FactionsHook_MassiveCraft implements FactionsHook
{
    @Override
    public boolean isNonTerritoryMemberNearby(Player player)
    {
        MPlayer mPlayer = MPlayer.get(player);
        for(Player nearbyPlayer : BukkitUtil.getNearbyPlayers(player))
        {
            MPlayer nearbyMPlayer = MPlayer.get(nearbyPlayer);
            if(nearbyMPlayer == null || mPlayer == null || mPlayer.getFaction().isNone() ||
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
        return !BoardColl.get().getFactionAt(PS.valueOf(player.getLocation())).isNone();
    }

    @Override
    public boolean isInOwnTerritory(Player player)
    {
        MPlayer fPlayer = MPlayer.get(player);
        Faction faction = BoardColl.get().getFactionAt(PS.valueOf(player.getLocation()));

        if(fPlayer == null || faction == null || faction.isNone())
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
        MPlayer mPlayer = MPlayer.get(player);
        for(Player nearbyPlayer : BukkitUtil.getNearbyPlayers(player))
        {
            MPlayer nearbyMPlayer = MPlayer.get(nearbyPlayer);
            if(nearbyMPlayer == null ||
                    mPlayer == null ||
                    mPlayer.getFaction().isNone() ||
                    (!mPlayer.getFaction().equals(nearbyMPlayer.getFaction()) && !mPlayer.getFaction().getRelationTo(nearbyMPlayer).equals(Rel.ALLY)))
            {
                return true;
            }
        }
        return false;
    }
}
