/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.hook.superiorskyblock;

import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface SuperiorSkyblockHook
{
    boolean canPlayerBuild(Player player, Location location);

    boolean isMemberOfIsland(Player player, Island island);

    boolean isOnOwnIsland(Player player);

    SuperiorPlayer getSuperiorPlayer(Player player);

    Island getIsland(Player player);
    
    boolean isNonIslandMemberNearby(Player player);


}
