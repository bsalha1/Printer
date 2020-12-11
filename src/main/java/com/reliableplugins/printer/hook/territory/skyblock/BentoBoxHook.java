package com.reliableplugins.printer.hook.territory.skyblock;

import com.reliableplugins.printer.hook.territory.TerritoryHook;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;

import java.util.Optional;

public class BentoBoxHook implements TerritoryHook
{
    @Override
    public boolean isNonTerritoryMemberNearby(Player player)
    {
        Island playerIsland = BentoBox.getInstance().getIslands().getIsland(player.getWorld(), player.getUniqueId());

        for(Player nearbyPlayer : BukkitUtil.getNearbyPlayers(player))
        {
            Island nearbyPlayerIsland = BentoBox.getInstance().getIslands().getIsland(nearbyPlayer.getWorld(), nearbyPlayer.getUniqueId());
            if(playerIsland == null || nearbyPlayerIsland == null || !playerIsland.equals(nearbyPlayerIsland))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isInATerritory(Player player)
    {
        return BentoBox.getInstance().getIslands().getIslandAt(player.getLocation()).isPresent();
    }

    @Override
    public boolean isInOwnTerritory(Player player)
    {
        Island playerIsland = BentoBox.getInstance().getIslands().getIsland(player.getWorld(), player.getUniqueId());
        Optional<Island> currentIsland = BentoBox.getInstance().getIslands().getIslandAt(player.getLocation());
        if(playerIsland == null || !currentIsland.isPresent())
        {
            return false;
        }

        return currentIsland.get().equals(playerIsland);
    }
}
