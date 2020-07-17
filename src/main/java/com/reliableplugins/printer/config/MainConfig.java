/*
 * Project: AntiSkid
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.config;

public class MainConfig extends Config
{
    private boolean scoreboard;
    private int costNotificationTime;

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

        useFactions = getBoolean("factions.support", true);
        allowInWilderness = getBoolean("factions.allow-in-wilderness", false);

        useShopGuiPlus = getBoolean("shopguiplus.support", true);

        useSuperiorSkyBlock = getBoolean("superior-skyblock.support", true);
        allowInNonIsland = getBoolean("superior-skyblock.allow-in-non-island", false);
        allowNearNonIslandMembers = getBoolean("superior-skyblock.allow-near-non-island-members", false);

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
