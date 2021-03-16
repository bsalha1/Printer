package com.reliableplugins.printer.hook.territory.factions;

import com.reliableplugins.printer.utils.BukkitUtil;
import net.prosavage.factionsx.core.FPlayer;
import net.prosavage.factionsx.core.Faction;
import net.prosavage.factionsx.manager.GridManager;
import net.prosavage.factionsx.manager.PlayerManager;
import net.prosavage.factionsx.util.Relation;
import org.bukkit.entity.Player;

import java.util.List;

public class FactionsHook_X implements FactionsHook {

    @Override
    public boolean isNonTerritoryMemberNearby(Player player) {
        FPlayer fPlayer = PlayerManager.INSTANCE.getFPlayer(player.getUniqueId());
        List<Player> nearbyPlayers = BukkitUtil.getNearbyPlayers(player);

        // If there are >0 players and this player is not in a faction, there must be someone not from their faction nearby
        if(nearbyPlayers.size() > 0 && (fPlayer == null || fPlayer.getFaction().isWilderness()))
        {
            return true;
        }

        for(Player nearbyPlayer : nearbyPlayers)
        {
            FPlayer nearbyFPlayer = PlayerManager.INSTANCE.getFPlayer(nearbyPlayer.getUniqueId());
            if(nearbyFPlayer == null || !fPlayer.getFaction().equals(nearbyFPlayer.getFaction()))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInATerritory(Player player) {
        return !GridManager.INSTANCE.getFactionAt(player.getLocation().getChunk()).isWilderness();
    }

    @Override
    public boolean isInOwnTerritory(Player player) {
        FPlayer fPlayer = PlayerManager.INSTANCE.getFPlayer(player.getUniqueId());
        Faction faction = GridManager.INSTANCE.getFactionAt(player.getLocation().getChunk());

        if(fPlayer == null || faction == null || faction.isWilderness())
        {
            return false;
        }

        return faction.equals(fPlayer.getFaction());
    }

    @Override
    public boolean isNonTerritoryMemberNearby(Player player, boolean allowAllies) {
        if(!allowAllies)
        {
            return isNonTerritoryMemberNearby(player);
        }

        FPlayer fPlayer = PlayerManager.INSTANCE.getFPlayer(player.getUniqueId());
        List<Player> nearbyPlayers = BukkitUtil.getNearbyPlayers(player);

        // If there are >0 players and this player is not in a faction, there must be someone not from their faction nearby
        if(nearbyPlayers.size() > 0 && (fPlayer == null || fPlayer.getFaction().isWilderness()))
        {
            return true;
        }

        for(Player nearbyPlayer : nearbyPlayers)
        {
            FPlayer nearbyFPlayer = PlayerManager.INSTANCE.getFPlayer(nearbyPlayer);
            if(nearbyFPlayer == null || (!fPlayer.getFaction().equals(nearbyFPlayer.getFaction()) && fPlayer.getFaction().getRelationTo(nearbyFPlayer.getFaction()) != Relation.ALLY))
            {
                return true;
            }
        }
        return false;
    }
}
