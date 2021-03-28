/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook.territory.lands;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.hook.territory.TerritoryHook;
import com.reliableplugins.printer.utils.BukkitUtil;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Land;
import me.angeschossen.lands.api.player.LandPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class LandsHook implements TerritoryHook
{
    private final LandsIntegration landsIntegration;

    public LandsHook()
    {
        this.landsIntegration = new LandsIntegration(Printer.INSTANCE);
    }

    @Override
    public boolean isNonTerritoryMemberNearby(Player player)
    {
        LandPlayer landPlayer = this.landsIntegration.getLandPlayer(player.getUniqueId());
        List<Player> nearbyPlayers = BukkitUtil.getNearbyPlayers(player);

        // If there are >0 players and this player doesn't own land, there must be a non-land member nearby
        if(nearbyPlayers.size() > 0 && (landPlayer == null || !landPlayer.ownsLand() || landPlayer.getOwningLand() == null))
        {
            return true;
        }

        for(Player nearbyPlayer : nearbyPlayers)
        {
            LandPlayer nearbyLandPlayer = this.landsIntegration.getLandPlayer(nearbyPlayer.getUniqueId());
            if(nearbyLandPlayer == null || !landPlayer.getOwningLand().equals(nearbyLandPlayer.getOwningLand()))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isInATerritory(Player player)
    {
        return this.landsIntegration.isClaimed(player.getLocation());
    }


    @Override
    public boolean isInOwnTerritory(Player player)
    {
        LandPlayer landPlayer = this.landsIntegration.getLandPlayer(player.getUniqueId());
        Land currentLand = this.landsIntegration.getLand(player.getLocation());
        return landPlayer != null && landPlayer.getLands().contains(currentLand);
    }

    @Override
    public boolean canBuild(Player player, Location location, boolean allowWilderness)
    {
        LandPlayer landPlayer = this.landsIntegration.getLandPlayer(player.getUniqueId());
        Land currentLand = this.landsIntegration.getLand(location);

        if(currentLand == null || currentLand.exists())
        {
            return allowWilderness;
        }

        if(landPlayer == null)
        {
            return false;
        }

        return currentLand.equals(landPlayer.getOwningLand());
    }
}
