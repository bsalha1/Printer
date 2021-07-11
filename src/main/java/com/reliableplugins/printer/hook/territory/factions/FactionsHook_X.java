package com.reliableplugins.printer.hook.territory.factions;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.hook.territory.ClaimHook;
import net.prosavage.factionsx.core.FPlayer;
import net.prosavage.factionsx.core.Faction;
import net.prosavage.factionsx.manager.GridManager;
import net.prosavage.factionsx.manager.PlayerManager;
import net.prosavage.factionsx.util.Relation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FactionsHook_X implements ClaimHook
{
    @Override
    public boolean isClaimFriend(Player owner, Player accessor)
    {
        FPlayer fPlayer = PlayerManager.INSTANCE.getFPlayer(owner.getUniqueId());
        FPlayer nearbyFPlayer = PlayerManager.INSTANCE.getFPlayer(accessor);

        // Either of the players aren't registered
        if(nearbyFPlayer == null || fPlayer == null)
        {
            return false;
        }

        // Owner is in the same faction as the nearby player
        if(fPlayer.getFaction().equals(nearbyFPlayer.getFaction()))
        {
            return true;
        }

        // If printer allowed near allies, and nearby player is an ally or truce
        return Printer.INSTANCE.getMainConfig().allowNearAllies() && fPlayer.getFaction().getRelationTo(nearbyFPlayer.getFaction()) == Relation.ALLY;
    }

    @Override
    public boolean isInAClaim(Player player) {
        return !GridManager.INSTANCE.getFactionAt(player.getLocation().getChunk()).isWilderness();
    }

    @Override
    public boolean isInOwnClaim(Player player) {
        FPlayer fPlayer = PlayerManager.INSTANCE.getFPlayer(player.getUniqueId());
        Faction faction = GridManager.INSTANCE.getFactionAt(player.getLocation().getChunk());

        if(fPlayer == null || faction == null || faction.isWilderness())
        {
            return false;
        }

        return faction.equals(fPlayer.getFaction());
    }

    @Override
    public boolean canBuild(Player player, Location location, boolean allowWilderness)
    {
        FPlayer fPlayer = PlayerManager.INSTANCE.getFPlayer(player.getUniqueId());
        Faction faction = GridManager.INSTANCE.getFactionAt(location.getChunk());

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
}
