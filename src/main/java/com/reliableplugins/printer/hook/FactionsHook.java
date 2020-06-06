package com.reliableplugins.printer.hook;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.Relation;
import org.bukkit.entity.Player;

public class FactionsHook
{
    public static boolean sameFaction(Player player1, Player player2)
    {
        FPlayer fPlayer1 = FPlayers.getInstance().getByPlayer(player1);
        FPlayer fPlayer2 = FPlayers.getInstance().getByPlayer(player2);
        return fPlayer1.getFaction().equals(fPlayer2.getFaction());
    }

    public static boolean areNeutralOrEnemies(Player player1, Player player2)
    {
        FPlayer fPlayer1 = FPlayers.getInstance().getByPlayer(player1);
        FPlayer fPlayer2 = FPlayers.getInstance().getByPlayer(player2);
        return fPlayer1.getRelationTo(fPlayer2).equals(Relation.ENEMY) || fPlayer1.getRelationTo(fPlayer2).equals(Relation.NEUTRAL);
    }
}
