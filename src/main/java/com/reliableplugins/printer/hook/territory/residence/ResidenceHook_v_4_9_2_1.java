package com.reliableplugins.printer.hook.territory.residence;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.ResidencePlayer;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.reliableplugins.printer.hook.territory.TerritoryHook;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.entity.Player;

public class ResidenceHook_v_4_9_2_1 implements TerritoryHook
{

    @Override
    public boolean isNonTerritoryMemberNearby(Player player)
    {
        ResidencePlayer rPlayer = Residence.getInstance().getPlayerManager().getResidencePlayer(player);
        for(Player nearbyPlayer : BukkitUtil.getNearbyPlayers(player))
        {
            ResidencePlayer nearbyRPlayer = Residence.getInstance().getPlayerManager().getResidencePlayer(nearbyPlayer);

            // If nearby player isn't from residence
            if(rPlayer.getMainResidence() == null || nearbyRPlayer.getMainResidence() == null ||
                    !rPlayer.getMainResidence().equals(nearbyRPlayer.getMainResidence()))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInATerritory(Player player)
    {
        ClaimedResidence currentResidence = Residence.getInstance().getResidenceManager().getByLoc(player);
        return currentResidence != null;
    }

    @Override
    public boolean isInOwnTerritory(Player player)
    {
        ResidencePlayer rPlayer = Residence.getInstance().getPlayerManager().getResidencePlayer(player);
        ClaimedResidence currentResidence = Residence.getInstance().getResidenceManager().getByLoc(player);

        // If player isn't in a residence or doesn't have a residence, they aren't in their own residence
        if(currentResidence == null || rPlayer.getMainResidence() == null)
        {
            return false;
        }

        return rPlayer.getMainResidence() == null || rPlayer.getMainResidence().getResidenceName().equals(currentResidence.getResidenceName());
    }
}
