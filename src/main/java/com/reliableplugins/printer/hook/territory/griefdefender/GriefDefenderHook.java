package com.reliableplugins.printer.hook.territory.griefdefender;

import com.flowpowered.math.vector.Vector3i;
import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.Subject;
import com.griefdefender.api.Tristate;
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
        final Vector3i vector = Vector3i.from(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        final Claim claim = GriefDefender.getCore().getClaimManager(loc.getWorld().getUID()).getClaimAt(vector);

        return claim.isUserTrusted(accessor.getUniqueId(), TrustTypes.ACCESSOR);
    }

    @Override
    public boolean isInAClaim(Player player)
    {
        Location loc = player.getLocation();
        if (GriefDefender.getCore().isEnabled(loc.getWorld().getUID())) return false;
        final Vector3i vector = Vector3i.from(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        final Claim claim = GriefDefender.getCore().getClaimManager(loc.getWorld().getUID()).getClaimAt(vector);

        return !claim.isWilderness();
    }

    @Override
    public boolean isInOwnClaim(Player player)
    {
        Location loc = player.getLocation();
        final Vector3i vector = Vector3i.from(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        final Claim claim = GriefDefender.getCore().getClaimManager(loc.getWorld().getUID()).getClaimAt(vector);

        return player.getUniqueId().equals(claim.getOwnerUniqueId());
    }

    @Override
    public boolean canBuild(Player player, Location location, boolean allowWilderness)
    {
        final Vector3i vector = Vector3i.from(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        final Claim claim = GriefDefender.getCore().getClaimManager(location.getWorld().getUID()).getClaimAt(vector);
        final Subject subject = GriefDefender.getCore().getSubject(player.getUniqueId().toString());
        final Tristate canBuild = GriefDefender.getPermissionManager().getActiveFlagPermissionValue(claim, subject,
                com.griefdefender.api.permission.flag.Flags.BLOCK_PLACE, player, location.getBlock(), null, TrustTypes.BUILDER, true);

        if(claim.isWilderness())
        {
            return allowWilderness;
        }

        if(canBuild == Tristate.TRUE)
        {
            return true;
        }

        return player.getUniqueId().equals(claim.getOwnerUniqueId());
    }
}
