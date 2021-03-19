/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook.territory.factions;

import com.massivecraft.factions.*;
import com.massivecraft.factions.iface.RelationParticipator;
import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;

public class FactionsHook_UUID implements FactionsHook
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
    public boolean isNonTerritoryMemberNearby(Player player)
    {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        List<Player> nearbyPlayers = BukkitUtil.getNearbyPlayers(player);

        // If there are >0 players and this player is not in a faction, there must be someone not from their faction nearby
        if(nearbyPlayers.size() > 0 && (fPlayer == null || fPlayer.getFaction().isWilderness()))
        {
            return true;
        }

        for(Player nearbyPlayer : nearbyPlayers)
        {
            FPlayer nearbyMPlayer = FPlayers.getInstance().getByPlayer(nearbyPlayer);
            if(nearbyMPlayer == null || !fPlayer.getFaction().equals(nearbyMPlayer.getFaction()))
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
    public boolean canBuild(Player player, Location location, boolean allowWilderness)
    {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        Faction faction = Board.getInstance().getFactionAt(new FLocation(location));

        if(faction == null || faction.isWilderness())
        {
            return allowWilderness;
        }

        if(fPlayer == null || fPlayer.hasFaction())
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

        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        List<Player> nearbyPlayers = BukkitUtil.getNearbyPlayers(player);

        // If there are >0 players and this player is not in a faction, there must be a non-ally or non-member nearby
        if(nearbyPlayers.size() > 0 && (fPlayer == null || fPlayer.getFaction().isWilderness()))
        {
            return true;
        }

        for(Player nearbyPlayer : nearbyPlayers)
        {
            FPlayer nearbyFPlayer = FPlayers.getInstance().getByPlayer(nearbyPlayer);
            if (nearbyFPlayer == null || isEnemyOrNeutral(fPlayer, nearbyFPlayer))
            {
                return true;
            }
        }
        return false;
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
