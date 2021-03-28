/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.commands;

import com.google.common.collect.Sets;
import org.bukkit.command.CommandSender;

import java.util.Set;

public abstract class Command
{
    private final String label;
    private final String[] alias;
    private final String permission;
    private final String description;
    private final boolean playerRequired;

    public Command(String label, String permission, String description, boolean playerRequired, String[] alias)
    {
        this.label = label;
        this.permission = permission;
        this.description = description;
        this.playerRequired = playerRequired;
        this.alias = alias;
    }

    public abstract void execute(CommandSender executor, String[] args);

    public String getLabel()
    {
        return label;
    }

    public Set<String> getAlias()
    {
        return Sets.newHashSet(alias);
    }

    public String getPermission()
    {
        return permission;
    }

    public String getDescription()
    {
        return description;
    }

    public boolean hasPermission()
    {
        return permission.length() != 0;
    }

    public boolean isPlayerRequired()
    {
        return playerRequired;
    }
}
