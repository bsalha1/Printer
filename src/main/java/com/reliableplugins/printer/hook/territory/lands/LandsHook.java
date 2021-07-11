/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook.territory.lands;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.hook.territory.ClaimHook;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Land;
import me.angeschossen.lands.api.player.LandPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LandsHook implements ClaimHook
{
    private final LandsIntegration landsIntegration;

    public LandsHook()
    {
        this.landsIntegration = new LandsIntegration(Printer.INSTANCE);
    }

    @Override
    public boolean isClaimFriend(Player owner, Player accessor)
    {
        LandPlayer landPlayer = this.landsIntegration.getLandPlayer(owner.getUniqueId());
        LandPlayer nearbyLandPlayer = this.landsIntegration.getLandPlayer(accessor.getUniqueId());
        if(landPlayer == null || nearbyLandPlayer == null || landPlayer.getOwningLand() == null)
        {
            return false;
        }

        return landPlayer.getOwningLand().equals(nearbyLandPlayer.getOwningLand());
    }

    @Override
    public boolean isInAClaim(Player player)
    {
        return this.landsIntegration.isClaimed(player.getLocation());
    }


    @Override
    public boolean isInOwnClaim(Player player)
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
