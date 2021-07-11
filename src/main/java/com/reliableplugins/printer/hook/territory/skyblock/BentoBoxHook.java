package com.reliableplugins.printer.hook.territory.skyblock;

import com.reliableplugins.printer.hook.territory.ClaimHook;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;

import java.util.Optional;

public class BentoBoxHook implements ClaimHook
{
    @Override
    public boolean isClaimFriend(Player owner, Player accessor)
    {
        Island playerIsland = BentoBox.getInstance().getIslands().getIsland(owner.getWorld(), owner.getUniqueId());
        Island nearbyPlayerIsland = BentoBox.getInstance().getIslands().getIsland(accessor.getWorld(), accessor.getUniqueId());
        if(playerIsland == null)
        {
            return false;
        }

        return playerIsland.equals(nearbyPlayerIsland);
    }

    @Override
    public boolean isInAClaim(Player player)
    {
        return BentoBox.getInstance().getIslands().getIslandAt(player.getLocation()).isPresent();
    }

    @Override
    public boolean isInOwnClaim(Player player)
    {
        Island playerIsland = BentoBox.getInstance().getIslands().getIsland(player.getWorld(), player.getUniqueId());
        Optional<Island> currentIsland = BentoBox.getInstance().getIslands().getIslandAt(player.getLocation());
        if(playerIsland == null || !currentIsland.isPresent())
        {
            return false;
        }

        return currentIsland.get().equals(playerIsland);
    }

    @Override
    public boolean canBuild(Player player, Location location, boolean allowWilderness)
    {
        Island playerIsland = BentoBox.getInstance().getIslands().getIsland(player.getWorld(), player.getUniqueId());
        Optional<Island> currentIsland = BentoBox.getInstance().getIslands().getIslandAt(location);

        if(!currentIsland.isPresent())
        {
            return allowWilderness;
        }

        if(playerIsland == null)
        {
            return false;
        }

        return currentIsland.get().equals(playerIsland);
    }
}
