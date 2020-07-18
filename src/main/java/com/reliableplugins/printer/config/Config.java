package com.reliableplugins.printer.config;

import com.reliableplugins.printer.Printer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public abstract class Config
{
    private File configFile;
    protected YamlConfiguration config;
    protected boolean isNew = false;

    public Config(String filename)
    {
        this.configFile = new File(Printer.INSTANCE.getDataFolder().getAbsolutePath() + "/" +  filename);
        try
        {
            isNew = configFile.createNewFile();
        }
        catch(IOException e)
        {
            Bukkit.getLogger().log(Level.WARNING, "Failed to create config: " + configFile.getName());
            e.printStackTrace();
            return;
        }

        config = new YamlConfiguration();
        try
        {
            config.load(configFile);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        config.options().copyDefaults(true);
    }

    public abstract void load();

    public void save()
    {
        try
        {
            config.save(configFile);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean isNew()
    {
        return isNew;
    }

    public void set(String path, Object val)
    {
        config.set(path, val);

        try
        {
            config.save(configFile);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public Set<String> getKeys(String path)
    {
        if(!config.isConfigurationSection(path))
        {
            config.createSection(path);
            return new HashSet<>();
        }

        return config.getConfigurationSection(path).getKeys(false);
    }

    public boolean getBoolean(String path, boolean def)
    {
        config.addDefault(path, def);
        return config.getBoolean(path, config.getBoolean(path));
    }

    public double getDouble(String path, double def)
    {
        config.addDefault(path, def);
        return config.getDouble(path, config.getDouble(path));
    }

    public int getInt(String path, int def)
    {
        config.addDefault(path, def);
        return config.getInt(path, config.getInt(path));
    }

    public int getNonNegativeInt(String path, int def)
    {
        int get = getInt(path, def);
        if(get < 0)
        {
            return def;
        }
        return get;
    }

    public List<String> getStringList(String path, List<String> def)
    {
        config.addDefault(path, def);
        return config.getStringList(path);
    }

    public String getString(String path, String def)
    {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    public YamlConfiguration getConfig()
    {
        return config;
    }

    public File getConfigFile()
    {
        return configFile;
    }
}
