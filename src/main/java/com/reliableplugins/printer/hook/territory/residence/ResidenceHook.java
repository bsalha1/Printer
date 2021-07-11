package com.reliableplugins.printer.hook.territory.residence;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.ResidencePlayer;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.reliableplugins.printer.hook.territory.ClaimHook;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ResidenceHook implements ClaimHook
{
    @Override
    public boolean isClaimFriend(Player owner, Player accessor)
    {
        ResidencePlayer rPlayer = Residence.getInstance().getPlayerManager().getResidencePlayer(owner);
        ResidencePlayer nearbyRPlayer = Residence.getInstance().getPlayerManager().getResidencePlayer(accessor);

        if(rPlayer.getMainResidence() == null)
        {
            return false;
        }

        return rPlayer.getMainResidence().equals(nearbyRPlayer.getMainResidence());
    }

    @Override
    public boolean isInAClaim(Player player)
    {
        ClaimedResidence currentResidence = Residence.getInstance().getResidenceManager().getByLoc(player);
        return currentResidence != null;
    }

    @Override
    public boolean isInOwnClaim(Player player)
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

    @Override
    public boolean canBuild(Player player, Location location, boolean allowWilderness)
    {
        ResidencePlayer rPlayer = Residence.getInstance().getPlayerManager().getResidencePlayer(player);
        ClaimedResidence currentResidence = Residence.getInstance().getResidenceManager().getByLoc(location);

        if(currentResidence == null)
        {
            return allowWilderness;
        }

        if(rPlayer == null)
        {
            return false;
        }

        return currentResidence.equals(rPlayer.getMainResidence());
    }
}
