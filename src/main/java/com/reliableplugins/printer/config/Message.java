package com.reliableplugins.printer.config;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.utils.BukkitUtil;

public enum Message
{
    PRINTER_ON("printer-on", "&7Printer has been activated"),
    PRINTER_OFF("printer-off", "&7Printer has been deactivated"),
    WITHDRAW_MONEY("withdraw-money", "&7${NUM} withdrawn"),
    RELOAD("reload", "&7Printer reloaded"),


    // Exploits
    ERROR_INVENTORY_OPEN_EXPLOIT("err-inventory-open-exploit", "&cOpening an inventory is not allowed while printer is on"),
    ERROR_DROP_ITEM_EXPLOIT("err-drop-item-exploit", "&cDropping items is not allowed while printer is on"),
    ERROR_ITEM_FRAME_EXPLOIT("err-item-frame-exploit", "&cUsing item frames is not allowed while printer is on"),
    ERROR_ARMOR_STAND_EXPLOIT("err-armor-stand-exploit", "&cUsing armor stands is not allowed while printer is on"),
    ERROR_ITEM_EXPLOIT("err-item-exploit", "&cUsing items is not allowed while printer is on"),
    ERROR_DAMAGE_EXPLOIT("err-damage-exploit", "&cDamaging entities is not allowed while printer is on"),
    ERROR_ENEMY_NEARBY_EXPLOIT("err-enemy-nearby-exploit", "&cEnemy or neutral player nearby - printer disabled"),

    // Factions
    ERROR_NOT_IN_TERRITORY("err-not-in-territory", "&cYou may only use printer if you're in your own territory"),
    ERROR_BLOCK_NOT_ALLOWED("err-block-not-allowed", "&cThis block is not allowed to be placed while printer is on"),
    ERROR_ENEMY_NEARBY("err-enemy-nearby", "&cEnemy or neutral player nearby"),
    ERROR_PRINTER_ALREADY_ON("err-printer-already-on", "&cPrinter is already activated"),
    ERROR_PRINTER_NOT_ON("err-printer-not-on", "&cPrinter is not activated"),

    // Skyblock
    ERROR_NOT_IN_ISLAND("err-not-in-island", "&cYou may only use printer if you're in your own island"),
    ERROR_NON_ISLAND_MEMBER_NEARBY("err-non-island-member-nearby", "&cNon island member nearby"),

    ERROR_NO_MONEY("err-not-enough-money", "&cNot enough money"),
    ERROR_NO_PERMS("err-no-perms", "&cYou do not have access to this command!"),
    ERROR_NOT_PLAYER("err-not-player", "&cOnly players may execute this command.");

    private String message;
    private String configKey;
    private static String header;

    static
    {
        header = Printer.INSTANCE.getMessageConfig().getString("message-header", "&8(&dPrinter&8) ");
    }

    Message(String configKey, String message)
    {
        this.configKey = configKey;
        this.message = message;
    }

    public String getLoneMessage()
    {
        return message;
    }

    public String getRawMessage()
    {
        return header + message;
    }

    public String getMessage()
    {
        return BukkitUtil.color(header + message);
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
