/*
 * Project: AntiSkid
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.config;

public class MainConfig extends Config
{
    private boolean debug;
    private boolean scoreboard;
    private boolean allowInWilderness;
    private boolean useFactions;
    private boolean useShopGuiPlus;
    private boolean useSuperiorSkyBlock;

    public MainConfig()
    {
        super("config.yml");
    }

    @Override
    public void load()
    {
        debug = getBoolean("debug-mode-enabled", false);
        scoreboard = getBoolean("scoreboard-enabled", true);

        useFactions = getBoolean("factions.support", true);
        allowInWilderness = getBoolean("factions.allow-in-wilderness", false);

        useShopGuiPlus = getBoolean("shopguiplus.support", true);

        useSuperiorSkyBlock = getBoolean("superior-skyblock.support", true);

        save();
    }

    public boolean isScoreboard()
    {
        return scoreboard;
    }

    public boolean allowInWilderness()
    {
        return allowInWilderness;
    }

    public boolean isDebug()
    {
        return debug;
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
