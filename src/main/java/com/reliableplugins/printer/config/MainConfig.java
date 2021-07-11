/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.config;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class MainConfig extends Config
{
    private boolean scoreboard;
    private boolean tooltipNotification;
    private int tooltipNotificationTime;
    private boolean onlyBreakPlaced;
    private boolean requireEmptyInventory;
    private int noFallDamageSeconds;
    private int checkRadius;
    private List<Material> unbreakables;
    private List<Material> unplaceables;
    private List<String> allowedCommands;

    private boolean useFactions;

    private boolean allowInWilderness;
    private boolean allowNearAllies;
    private boolean allowNearNonMembers;

    private boolean useShopGuiPlus;
    private boolean useZShop;
    private boolean useDynamicShop;

    private int scoreboardMargin;
    private String scoreboardTitle;
    private String costScoreTitle;
    private String blocksScoreTitle;
    private String balanceScoreTitle;
    private String costFormat;
    private String blocksFormat;
    private String balanceFormat;
    private boolean useSuperiorSkyBlock;
    private boolean useIridiumSkyblock;
    private boolean useResidence;
    private boolean useLands;
    private boolean useGriefDefender;
    private boolean useBentoBox;
    private boolean useASkyBlock;
    private boolean useClaimChunk;

    public MainConfig()
    {
        super("config.yml");
    }

    @Override
    public void load()
    {
        noFallDamageSeconds = getInt("no-fall-damage-seconds", 5);
        onlyBreakPlaced = getBoolean("only-break-placed", true);
        requireEmptyInventory = getBoolean("require-empty-inventory", false);
        checkRadius = getInt("player-check-radius", 64);
        allowInWilderness = getBoolean("allow-in-wilderness", false);
        allowNearNonMembers = getBoolean("allow-near-non-members", false);
        unbreakables = getMaterialList("unbreakable-blocks", Arrays.asList(Material.BEDROCK, Material.BARRIER, Material.ENDER_PORTAL_FRAME, Material.DRAGON_EGG));
        unplaceables = getMaterialList("unplaceable-blocks", Arrays.asList(Material.POTION, Material.MONSTER_EGG));
        allowedCommands = getStringList("allowed-commands", Arrays.asList("f*", "printer*", "bal*", "tp*", "etp*", "msg % %"));

        useFactions = getBoolean("factions.support", true);
        allowNearAllies = getBoolean("factions.allow-near-allies", true);

        useShopGuiPlus = getBoolean("shop.shopguiplus-support", true);
        useZShop = getBoolean("shop.zshop-support", false);
        useDynamicShop = getBoolean("shop.dynamicshop-support", false);

        useResidence = getBoolean("residence.support", false);
        useLands = getBoolean("lands.support", false);
        useGriefDefender = getBoolean("griefdefender.support", false);
        useSuperiorSkyBlock = getBoolean("skyblock.superior-skyblock-support", false);
        useBentoBox = getBoolean("skyblock.bento-box-support", false);
        useIridiumSkyblock = getBoolean("skyblock.iridiumskyblock-support", false);
        useASkyBlock = getBoolean("skyblock.askyblock-support", false);
        useClaimChunk = getBoolean("claimchunk.support", false);

        tooltipNotification = getBoolean("tooltip-notification.enabled", true);
        tooltipNotificationTime = getInt("tooltip-notification.seconds", 5);

        scoreboard = getBoolean("scoreboard.enabled", true);
        scoreboardMargin = getInt("scoreboard.margin", 32);
        scoreboardTitle = getColoredString("scoreboard.title", "&d&lPrinter");
        costScoreTitle = getColoredString("scoreboard.cost-score-title", "&7Cost&f:");
        costFormat = getColoredString("scoreboard.cost-format", "&c${NUM}");
        blocksScoreTitle = getColoredString("scoreboard.blocks-score-title", "&7Blocks&f:");
        blocksFormat = getColoredString("scoreboard.blocks-format", "&a{NUM}");
        balanceScoreTitle = getColoredString("scoreboard.balance-score-title", "&7Balance&f:");
        balanceFormat = getColoredString("scoreboard.balance-format", "&a${NUM}");

        save();
    }

    public int getTooltipNotificationTime()
    {
        return tooltipNotificationTime;
    }

    public boolean isScoreboardEnabled()
    {
        return scoreboard;
    }

    public boolean isTooltipNotificationEnabled()
    {
        return tooltipNotification;
    }

    public boolean isUnbreakable(Material material)
    {
        return unbreakables.contains(material);
    }

    public boolean isUnplaceable(Material material)
    {
        return unplaceables.contains(material);
    }

    public boolean isAllowedCommand(String command)
    {
        command = command.substring(1); // Take off '/'
        String[] commandArguments = command.split(" ");
        for(String allowedCommand : allowedCommands)
        {
            if(allowedCommand.contains("*") && command.startsWith(allowedCommand.replaceAll("\\*", "")))
            {
                return true;
            }
            else if(allowedCommand.contains("%"))
            {
                String[] allowedCommandArguments = allowedCommand.split(" ");
                if(commandArguments.length != allowedCommandArguments.length)
                {
                    continue;
                }

                int numMatches;
                for(numMatches = 0; numMatches < commandArguments.length; numMatches++)
                {
                    if(!allowedCommandArguments[numMatches].equalsIgnoreCase(commandArguments[numMatches])
                        && !allowedCommandArguments[numMatches].equals("%"))
                    {
                        break;
                    }
                }

                if(numMatches == commandArguments.length)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean allowInWilderness()
    {
        return allowInWilderness;
    }

    public boolean allowNearAllies()
    {
        return allowNearAllies;
    }

    public boolean allowNearNonMembers()
    {
        return allowNearNonMembers;
    }

    public boolean useShopGuiPlus()
    {
        return useShopGuiPlus;
    }

    public boolean useFactions()
    {
        return useFactions;
    }

    public boolean useZShop()
    {
        return useZShop;
    }

    public boolean useDynamicShop()
    {
        return useDynamicShop;
    }

    public boolean useSuperiorSkyBlock()
    {
        return useSuperiorSkyBlock;
    }

    public boolean useBentoBox()
    {
        return useBentoBox;
    }

    public boolean useIridiumSkyblock()
    {
        return useIridiumSkyblock;
    }

    public boolean useASkyBlock()
    {
        return useASkyBlock;
    }

    public boolean useResidence()
    {
        return useResidence;
    }

    public boolean useLands()
    {
        return useLands;
    }

    public boolean useGriefDefender()
    {
        return useGriefDefender;
    }

    public boolean useClaimChunk()
    {
        return useClaimChunk;
    }

    public String getScoreboardTitle()
    {
        return scoreboardTitle;
    }

    public String getBlocksScoreTitle()
    {
        return blocksScoreTitle;
    }

    public String getCostScoreTitle()
    {
        return costScoreTitle;
    }

    public String getBalanceScoreTitle()
    {
        return balanceScoreTitle;
    }

    public int getScoreboardMargin()
    {
        return scoreboardMargin;
    }

    public String getBalanceFormat()
    {
        return balanceFormat;
    }

    public String getBlocksFormat()
    {
        return blocksFormat;
    }

    public String getCostFormat()
    {
        return costFormat;
    }

    public boolean onlyBreakPlaced()
    {
        return onlyBreakPlaced;
    }


    public boolean requireEmptyInventory()
    {
        return requireEmptyInventory;
    }

    public int getNoFallDamageSeconds()
    {
        return noFallDamageSeconds;
    }

    public int getCheckRadius()
    {
        return checkRadius;
    }

}
