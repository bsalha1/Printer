/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook.territory.factions;

import com.massivecraft.factions.*;
import com.massivecraft.factions.iface.RelationParticipator;
import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.hook.territory.ClaimHook;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

public class FactionsHook_UUID implements ClaimHook
{
    private Method relationToMethod;

    public FactionsHook_UUID()
    {
        try
        {
            this.relationToMethod = Faction.class.getMethod("getRelationTo", RelationParticipator.class);
        }
        catch (NoSuchMethodException e)
        {
            Printer.INSTANCE.getLogger().log(Level.SEVERE, "Unsupported Factions hook! Contact support.");
        }
    }

    @Override
    public boolean isClaimFriend(Player owner, Player accessor)
    {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(owner);
        FPlayer nearbyFPlayer = FPlayers.getInstance().getByPlayer(accessor);

        // Either of the players aren't registered
        if (nearbyFPlayer == null || fPlayer == null)
        {
            return false;
        }

        // Owner is in the same faction as the nearby player
        if(fPlayer.getFaction().equals(nearbyFPlayer.getFaction()))
        {
            return true;
        }

        // If printer allowed near allies, and nearby player is an ally or truce
        return Printer.INSTANCE.getMainConfig().allowNearAllies() && !isEnemyOrNeutral(fPlayer, nearbyFPlayer);
    }

    @Override
    public boolean isInAClaim(Player player)
    {
        return !Board.getInstance().getFactionAt(new FLocation(player.getLocation())).isWilderness();
    }

    @Override
    public boolean isInOwnClaim(Player player)
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
    public boolean canBuild(Player player, Location location, boolean allowWilderness)
    {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        Faction faction = Board.getInstance().getFactionAt(new FLocation(location));

        if(faction == null || faction.isWilderness())
        {
            return allowWilderness;
        }

        if(fPlayer == null || !fPlayer.hasFaction())
        {
            return false;
        }

        return faction.equals(fPlayer.getFaction());
    }

    private boolean isEnemyOrNeutral(FPlayer fPlayer1, FPlayer fplayer2)
    {
        try
        {
            String relation = this.relationToMethod.invoke(fPlayer1.getFaction(), fplayer2)
                    .toString();

            return relation.equalsIgnoreCase("enemy") || relation.equalsIgnoreCase("neutral");
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            Printer.INSTANCE.getLogger().log(Level.SEVERE, "Unsupported Factions hook! Contact support.");
        }
        return true;
    }
}
