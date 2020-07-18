/*
 * Project: AntiSkid
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.config;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainConfig extends Config
{
    private boolean scoreboard;
    private int costNotificationTime;
    private List<Material> unbreakables;

    private boolean allowInWilderness;
    private boolean useFactions;

    private boolean useShopGuiPlus;

    private boolean useSuperiorSkyBlock;
    private boolean allowInNonIsland;
    private boolean allowNearNonIslandMembers;

    public MainConfig()
    {
        super("config.yml");
    }

    @Override
    public void load()
    {
        scoreboard = getBoolean("scoreboard-enabled", true);
        costNotificationTime = getInt("cost-notification-time", 5);
        List<String> unbreakableStrings = getStringList("unbreakable-blocks", Arrays.asList("BEDROCK", "BARRIER", "ENDER_PORTAL_FRAME", "DRAGON_EGG"));

        useFactions = getBoolean("factions.support", true);
        allowInWilderness = getBoolean("factions.allow-in-wilderness", false);

        useShopGuiPlus = getBoolean("shopguiplus.support", true);

        useSuperiorSkyBlock = getBoolean("superior-skyblock.support", true);
        allowInNonIsland = getBoolean("superior-skyblock.allow-in-non-island", false);
        allowNearNonIslandMembers = getBoolean("superior-skyblock.allow-near-non-island-members", false);

        unbreakables = new ArrayList<>();
        for(String unbreakableString : unbreakableStrings)
        {
            Material unbreakable;
            try
            {
                unbreakable = Material.valueOf(unbreakableString);
            }
            catch (IllegalArgumentException e)
            {
                continue;
            }

            unbreakables.add(unbreakable);
        }

        save();
    }

    public int getCostNotificationTime()
    {
        return costNotificationTime;
    }

    public boolean isScoreboardEnabled()
    {
        return scoreboard;
    }

    public List<Material> getUnbreakables()
    {
        return unbreakables;
    }

    public boolean allowInWilderness()
    {
        return allowInWilderness;
    }

    public boolean allowInNonIsland()
    {
        return allowInNonIsland;
    }

    public boolean allowNearNonIslandMembers()
    {
        return allowNearNonIslandMembers;
    }

    public boolean useShopGuiPlus()
    {
        return useShopGuiPlus;
    }

    public boolean useFactions()
    {
        return useFactions;
    }

    public boolean useSuperiorSkyBlock()
    {
        return useSuperiorSkyBlock;
    }
}
