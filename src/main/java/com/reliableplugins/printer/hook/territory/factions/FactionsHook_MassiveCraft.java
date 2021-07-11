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
import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.hook.territory.ClaimHook;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FactionsHook_MassiveCraft implements ClaimHook
{

    @Override
    public boolean isClaimFriend(Player owner, Player accessor)
    {
        MPlayer mPlayer = MPlayer.get(owner);
        MPlayer nearbyMPlayer = MPlayer.get(accessor);

        // Either of the players aren't registered or the owner is not in a faction
        if(nearbyMPlayer == null || mPlayer == null || mPlayer.getFaction().isNone())
        {
            return false;
        }

        // Owner is in the same faction as the nearby player
        if(mPlayer.getFaction().equals(nearbyMPlayer.getFaction()))
        {
            return true;
        }

        // If printer allowed near allies, and nearby player is an ally
        return Printer.INSTANCE.getMainConfig().allowNearAllies() && mPlayer.getFaction().getRelationTo(nearbyMPlayer).equals(Rel.ALLY);
    }

    @Override
    public boolean isInAClaim(Player player)
    {
        return !BoardColl.get().getFactionAt(PS.valueOf(player.getLocation())).isNone();
    }

    @Override
    public boolean isInOwnClaim(Player player)
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
    public boolean canBuild(Player player, Location location, boolean allowWilderness)
    {
        MPlayer fPlayer = MPlayer.get(player);
        Faction faction = BoardColl.get().getFactionAt(PS.valueOf(location));

        if(faction == null || faction.isNone())
        {
            return allowWilderness;
        }

        if(fPlayer == null || !fPlayer.hasFaction())
        {
            return false;
        }

        return faction.equals(fPlayer.getFaction());
    }
}
