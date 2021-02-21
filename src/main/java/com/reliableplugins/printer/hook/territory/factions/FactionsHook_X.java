package com.reliableplugins.printer.hook.territory.factions;

import com.massivecraft.factions.FPlayers;
import com.reliableplugins.printer.utils.BukkitUtil;
import net.prosavage.factionsx.core.FPlayer;
import net.prosavage.factionsx.core.Faction;
import net.prosavage.factionsx.manager.GridManager;
import net.prosavage.factionsx.manager.PlayerManager;
import net.prosavage.factionsx.util.Relation;
import org.bukkit.entity.Player;

public class FactionsHook_X implements FactionsHook {

    @Override
    public boolean isNonTerritoryMemberNearby(Player player) {
        FPlayer mPlayer = PlayerManager.INSTANCE.getFPlayer(player.getUniqueId());
        for(Player nearbyPlayer : BukkitUtil.getNearbyPlayers(player))
        {
            FPlayer nearbyMPlayer = PlayerManager.INSTANCE.getFPlayer(nearbyPlayer.getUniqueId());
            if(nearbyMPlayer == null || mPlayer == null || mPlayer.getFaction().isWilderness() ||
                    !mPlayer.getFaction().equals(nearbyMPlayer.getFaction()))
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
        for(Player nearbyPlayer : BukkitUtil.getNearbyPlayers(player))
        {
            FPlayer nearbyFPlayer = PlayerManager.INSTANCE.getFPlayer(nearbyPlayer);
            if(nearbyFPlayer == null ||
                    fPlayer == null ||
                    fPlayer.getFaction().isWilderness() ||
                    (!fPlayer.getFaction().equals(nearbyFPlayer.getFaction()) && fPlayer.getFaction().getRelationTo(nearbyFPlayer.getFaction()) != Relation.ALLY))
            {
                return true;
            }
        }
        return false;
    }
}
