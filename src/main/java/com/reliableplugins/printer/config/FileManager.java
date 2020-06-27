package com.reliableplugins.printer.config;

import com.reliableplugins.printer.Printer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class FileManager
{
    private final List<Config> files = new ArrayList<>();

    public FileManager()
    {
        if(!Printer.INSTANCE.getDataFolder().exists() && !Printer.INSTANCE.getDataFolder().mkdir())
        {
            Printer.INSTANCE.getLogger().log(Level.SEVERE, "Failed to create plugins/" + Printer.INSTANCE.getDescription().getName());
        }
    }

    public void addFile(Config file)
    {
        files.add(file);
        Printer.INSTANCE.getLogger().log(Level.INFO, file.getConfigFile().getName() + " has initialized.");
        file.load();
    }

    public List<Config> getFiles()
    {
        return files;
    }

    public void loadAll()
    {
        for(Config file : files)
        {
            file.load();
        }
    }

    public void saveAll()
    {
        for(Config file : files)
        {
            file.save();
        }
    }
}
