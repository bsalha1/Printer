package com.reliableplugins.printer.hook;

import com.massivecraft.factions.*;
import com.massivecraft.factions.struct.Relation;
import org.bukkit.entity.Player;

public class FactionsHook
{
    public static boolean areNeutralOrEnemies(Player player1, Player player2)
    {
        FPlayer fPlayer1 = FPlayers.getInstance().getByPlayer(player1);
        FPlayer fPlayer2 = FPlayers.getInstance().getByPlayer(player2);
        return fPlayer1.getRelationTo(fPlayer2).equals(Relation.ENEMY) || fPlayer1.getRelationTo(fPlayer2).equals(Relation.NEUTRAL);
    }

    public static boolean inOwnTerritory(Player player)
    {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        FLocation fLocation = new FLocation(player.getWorld().getName(), player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
        Faction faction = Board.getInstance().getFactionAt(fLocation);

        return faction.equals(fPlayer.getFaction());
    }
}
