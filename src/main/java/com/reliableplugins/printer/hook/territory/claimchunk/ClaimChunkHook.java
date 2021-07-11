package com.reliableplugins.printer.hook.territory.claimchunk;

import com.cjburkey.claimchunk.ClaimChunk;
import com.reliableplugins.printer.hook.territory.ClaimHook;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClaimChunkHook implements ClaimHook
{

    @Override
    public boolean isClaimFriend(Player owner, Player accessor)
    {
        return ClaimChunk.getInstance().getPlayerHandler().hasAccess(owner.getUniqueId(), accessor.getUniqueId());
    }

    public boolean isClaimed(Location location)
    {
        return ClaimChunk.getInstance().getChunkHandler().isClaimed(location.getChunk());
    }

    @Override
    public boolean isInAClaim(Player player)
    {
        return this.isClaimed(player.getLocation());
    }


    @Override
    public boolean isInOwnClaim(Player player)
    {
        UUID currentChunkOwner = ClaimChunk.getInstance().getChunkHandler().getOwner(player.getLocation().getChunk());
        if(currentChunkOwner == null)
        {
            return false;
        }

        return player.getUniqueId().equals(currentChunkOwner) || ClaimChunk.getInstance().getPlayerHandler().hasAccess(currentChunkOwner, player.getUniqueId());
    }

    @Override
    public boolean canBuild(Player player, Location location, boolean allowWilderness)
    {
        if(!this.isClaimed(location))
        {
            return allowWilderness;
        }

        UUID owner = ClaimChunk.getInstance().getChunkHandler().getOwner(location.getChunk());
        return player.getUniqueId().equals(owner) || ClaimChunk.getInstance().getPlayerHandler().hasAccess(player.getUniqueId(), owner);
    }
}
