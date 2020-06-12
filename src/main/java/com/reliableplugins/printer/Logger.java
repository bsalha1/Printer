package com.reliableplugins.printer;

import org.bukkit.Bukkit;

public class Logger
{
    private LogType logType;

    public Logger(LogType logType)
    {
        this.logType = logType;
    }

    public void logDebug(String message)
    {
        if(logType.equals(LogType.DEBUG))
        {
            Bukkit.broadcastMessage(message);
        }
    }
}
