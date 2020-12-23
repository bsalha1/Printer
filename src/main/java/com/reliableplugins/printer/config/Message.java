/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.config;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.command.CommandSender;

public enum Message
{
    PRINTER_ON("printer-on", "&7Printer has been activated"),
    PRINTER_OFF("printer-off", "&7Printer has been deactivated"),
    WITHDRAW_MONEY("withdraw-money", "&7${NUM} withdrawn"),
    VERSION_MESSAGE("version-message", "&7Printer v{NUM}"),
    RELOAD("reload", "&7Printer reloaded"),

    // Help Menu
    HELP_PRINTER_HEADER("help-printer-header", "&7&m----------&7[ &dPrinter &r&f{PAGE}&7/&f{NUM_PAGES}&7]&m----------"),
    HELP_PRINTER_ON("help-printer-on", "&d/printer on &7Turns printer on"),
    HELP_PRINTER_OFF("help-printer-off", "&d/printer off &7Turns printer off"),
    HELP_PRINTER_RELOAD("help-printer-reload", "&d/printer reload &7Reloads printer configs"),
    HELP_PRINTER_VERSION("help-printer-version", "&d/printer version &7Display printer version"),

    // Exploits
    ERROR_INVENTORY_OPEN_EXPLOIT("err-inventory-open-exploit", "&cOpening an inventory is not allowed while printer is on"),
    ERROR_DROP_ITEM_EXPLOIT("err-drop-item-exploit", "&cDropping items is not allowed while printer is on"),
    ERROR_ITEM_FRAME_EXPLOIT("err-item-frame-exploit", "&cUsing item frames is not allowed while printer is on"),
    ERROR_ARMOR_STAND_EXPLOIT("err-armor-stand-exploit", "&cUsing armor stands is not allowed while printer is on"),
    ERROR_ITEM_EXPLOIT("err-item-exploit", "&cUsing items is not allowed while printer is on"),
    ERROR_DAMAGE_EXPLOIT("err-damage-exploit", "&cDamaging entities is not allowed while printer is on"),
    ERROR_ENEMY_NEARBY_EXPLOIT("err-enemy-nearby-exploit", "&cEnemy or neutral player nearby - printer disabled"),
    ERROR_COMMAND_EXPLOIT("err-command-exploit", "&cEntering commands is not allowed while printer is on"),
    ERROR_PICKUP_EXPLOIT("err-pickup-exploit", "&cPicking up items is not allowed while printer is on"),
    ERROR_DISPENSE_ARMOR_EXPLOIT("err-dispense-armor-exploit", "&cDispensed armor activity is not allowed while printer is on"),
    ERROR_TELEPORT_EXPLOIT("err-teleport-exploit", "&cTeleported - printer has been turned off"),

    // Faction
    ERROR_NOT_IN_TERRITORY("err-not-in-territory", "&cYou may only use printer if you're in your own territory"),
    ERROR_NON_FACTION_MEMBER_NEARBY("err-non-faction-member-nearby", "&cSomeone who isn't from your faction is nearby"),

    // Skyblock
    ERROR_NOT_IN_ISLAND("err-not-in-island", "&cYou may only use printer if you're in your own island"),
    ERROR_NON_ISLAND_MEMBER_NEARBY("err-non-island-member-nearby", "&cSomeone who isn't from your island is nearby"),

    // Residence
    ERROR_NOT_IN_RESIDENCE("err-not-in-residence", "&cYou may only use printer if you're in your own residence"),
    ERROR_NON_RESIDENT_NEARBY("err-non-residence-member-nearby", "&cSomeone who isn't from your residence is nearby"),

    ERROR_BLOCK_PLACE_NOT_ALLOWED("err-block-not-allowed", "&cThis block is not allowed to be placed while printer is on"),
    ERROR_ITEM_PLACE_NOT_ALLOWED("err-item-not-allowed", "&cThis item is not allowed to be used while printer is on"),
    ERROR_BLOCK_BREAK_NOT_ALLOWED("err-block-break-not-allowed", "&cThis block is not allowed to be broken while printer is on"),
    ERROR_PRINTER_ALREADY_ON("err-printer-already-on", "&cPrinter is already activated"),
    ERROR_PRINTER_NOT_ON("err-printer-not-on", "&cPrinter is not activated"),
    ERROR_NON_EMPTY_INVENTORY("err-non-empty-inventory", "&cMust have empty inventory to activate printer"),
    ERROR_NOT_PLACED_IN_PRINTER("err-not-placed-in-printer", "&cCannot break blocks not placed in printer"),
    ERROR_NO_MONEY("err-not-enough-money", "&cNot enough money"),
    ERROR_NO_PERMS("err-no-perms", "&cYou do not have access to this command!"),
    ERROR_NOT_PLAYER("err-not-player", "&cOnly players may execute this command.");

    private String message;
    private final String configKey;
    private static final String header;

    static
    {
        header = Printer.INSTANCE.getMessageConfig().getString("message-header", "&8(&dPrinter&8) ");
    }

    Message(String configKey, String message)
    {
        this.configKey = configKey;
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public String getColoredMessage()
    {
        return BukkitUtil.color(header + message);
    }

    public String getColoredMessageWithoutHeader()
    {
        return BukkitUtil.color(message);
    }

    public static void sendMultilineMessage(CommandSender sender, String message)
    {
        while(message.contains("\\n"))
        {
            String temp = message.substring(0, message.indexOf("\\n"));
            sender.sendMessage(temp);
            message = message.substring(message.indexOf("\\n") + 2);
        }
        sender.sendMessage(message);
    }

    public void sendColoredMessage(CommandSender sender)
    {
        sendMultilineMessage(sender, getColoredMessage());
    }

    public void sendMessage(CommandSender sender)
    {
        sendMultilineMessage(sender, getMessage());
    }

    public void sendWithoutHeader(CommandSender sender)
    {
        sendMultilineMessage(sender, getColoredMessageWithoutHeader());
    }

    public void setMessage(String message)
    {
        this.message = BukkitUtil.color(message);
    }

    public String getConfigKey()
    {
        return configKey;
    }
}
