package com.reliableplugins.printer.hook.territory.griefdefender;

import com.flowpowered.math.vector.Vector3i;
import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;
import com.griefdefender.api.claim.TrustTypes;
import com.reliableplugins.printer.hook.territory.ClaimHook;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GriefDefenderHook implements ClaimHook
{
    @Override
    public boolean isClaimFriend(Player owner, Player accessor)
    {
        Location loc = owner.getLocation();
        Vector3i vector = Vector3i.from(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        Claim claim = GriefDefender.getCore().getClaimManager(loc.getWorld().getUID()).getClaimAt(vector);
        if(claim == null)
        {
            return false;
        }

        return claim.getUserTrusts(TrustTypes.BUILDER).contains(accessor.getUniqueId());
    }

    @Override
    public boolean isInAClaim(Player player)
    {
        Location loc = player.getLocation();
        Vector3i vector = Vector3i.from(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        Claim claim = GriefDefender.getCore().getClaimManager(loc.getWorld().getUID()).getClaimAt(vector);

        return !claim.isWilderness();
    }

    @Override
    public boolean isInOwnClaim(Player player)
    {
        Location loc = player.getLocation();
        Vector3i vector = Vector3i.from(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        Claim claim = GriefDefender.getCore().getClaimManager(loc.getWorld().getUID()).getClaimAt(vector);

        return player.getUniqueId().equals(claim.getOwnerUniqueId());
    }

    @Override
    public boolean canBuild(Player player, Location location, boolean allowWilderness)
    {
        Vector3i vector = Vector3i.from(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        Claim claim = GriefDefender.getCore().getClaimManager(location.getWorld().getUID()).getClaimAt(vector);

        if(claim.isWilderness())
        {
            return allowWilderness;
        }

        if(claim.getUserTrusts(TrustTypes.BUILDER).contains(player.getUniqueId()))
        {
            return true;
        }

        return player.getUniqueId().equals(claim.getOwnerUniqueId());
    }
}
