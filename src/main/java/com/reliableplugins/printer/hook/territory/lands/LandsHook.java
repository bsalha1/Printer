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
        if(nearbyPlayers.size() > 0 && (landPlayer == null || !landPlayer.ownsLand()))
        {
            return true;
        }

        for(Player nearbyPlayer : nearbyPlayers)
        {
            LandPlayer nearbyLandPlayer = this.landsIntegration.getLandPlayer(nearbyPlayer.getUniqueId());
            if(nearbyLandPlayer == null || !hasCommonLand(landPlayer, nearbyLandPlayer))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isInATerritory(Player player)
    {
        Land currentLand = this.landsIntegration.getLand(player.getLocation());
        return currentLand != null && currentLand.exists();
    }

    @Override
    public boolean isInOwnTerritory(Player player)
    {
        LandPlayer landPlayer = this.landsIntegration.getLandPlayer(player.getUniqueId());
        Land currentLand = this.landsIntegration.getLand(player.getLocation());
        return landPlayer != null && landPlayer.getLands().contains(currentLand);
    }

    private boolean hasCommonLand(LandPlayer landPlayer1, LandPlayer landPlayer2)
    {
        for(Land land : landPlayer1.getLands())
        {
            if(landPlayer2.getLands().contains(land))
            {
                return true;
            }
        }
        return false;
    }
}
