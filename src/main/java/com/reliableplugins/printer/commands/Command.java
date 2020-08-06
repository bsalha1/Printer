/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.commands;

import com.google.common.collect.Sets;
import com.reliableplugins.printer.annotation.CommandBuilder;
import org.bukkit.command.CommandSender;

import java.util.Set;

public abstract class Command
{
    private String label;
    private String[] alias;
    private String permission;
    private String description;
    private boolean playerRequired;

    public Command()
    {
        this.label = getClass().getAnnotation(CommandBuilder.class).label();
        this.alias = getClass().getAnnotation(CommandBuilder.class).alias();
        this.permission = getClass().getAnnotation(CommandBuilder.class).permission();
        this.description = getClass().getAnnotation(CommandBuilder.class).description();
        this.playerRequired = getClass().getAnnotation(CommandBuilder.class).playerRequired();
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
