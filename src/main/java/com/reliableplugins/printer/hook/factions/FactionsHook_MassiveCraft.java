/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook.factions;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.entity.Player;

import java.util.List;

public class FactionsHook_MassiveCraft implements FactionsHook
{
    public boolean areNeutralOrEnemies(Player player1, Player player2)
    {
        MPlayer mPlayer1 = MPlayer.get(player1);
        MPlayer mPlayer2 = MPlayer.get(player2);
        return mPlayer1.getRelationTo(mPlayer2).equals(Rel.ENEMY) || mPlayer1.getRelationTo(mPlayer2).equals(Rel.NEUTRAL);
    }

    public boolean isEnemyOrNeutralNearby(Player player)
    {
        List<Player> nearbyPlayers = BukkitUtil.getNearbyPlayers(player);
        for(Player nearbyPlayer : nearbyPlayers)
        {
            if(areNeutralOrEnemies(player, nearbyPlayer))
            {
                return true;
            }
        }
        return false;
    }

    public boolean inOwnTerritory(Player player)
    {
        MPlayer fPlayer = getMPlayer(player);
        Faction faction = BoardColl.get().getFactionAt(PS.valueOf(player.getLocation()));
        return faction.equals(fPlayer.getFaction()) && !faction.isNone();
    }

    public boolean inWilderness(Player player)
    {
        return BoardColl.get().getFactionAt(PS.valueOf(player.getLocation())).isNone();
    }

    public boolean canBuild(Player player)
    {
        MPlayer fPlayer = getMPlayer(player);
        return BoardColl.get().getFactionAt(PS.valueOf(player.getLocation())).equals(fPlayer.getFaction());
    }

    public static MPlayer getMPlayer(Player player)
    {
        return MPlayer.get(player);
    }
}
