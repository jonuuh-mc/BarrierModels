package io.jonuuh.barriermodels.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class Settings
{
    private static Settings instance;
    private final Configuration configuration;
    private boolean doRendering;
    private boolean doConnecting;
    private boolean doParticles;

    public static void createInstance(File configFile)
    {
        if (instance != null)
        {
            throw new IllegalStateException("Settings instance has already been created");
        }

        instance = new Settings(configFile);
    }

    public static Settings getInstance()
    {
        if (instance == null)
        {
            throw new NullPointerException("Settings instance has not been created");
        }

        return instance;
    }

    public boolean doRendering()
    {
        return doRendering;
    }

    public void setDoRendering(boolean doRendering)
    {
        this.doRendering = doRendering;
    }

    public boolean doConnecting()
    {
        return doConnecting;
    }

    public void setDoConnecting(boolean doConnecting)
    {
        this.doConnecting = doConnecting;
    }

    public boolean doParticles()
    {
        return doParticles;
    }

    public void setDoParticles(boolean doParticles)
    {
        this.doParticles = doParticles;
    }

    // Load settings (read data from Configuration, write it into each setting)
    private Settings(File configFile)
    {
        this.configuration = new Configuration(configFile);

        doRendering = getBoolProperty("doRendering", true).getBoolean();
        doConnecting = getBoolProperty("doConnecting", true).getBoolean();
        doParticles = getBoolProperty("doParticles", false).getBoolean();
    }

    // Save settings (read data from each setting, write it into Configuration)
    public void save()
    {
        getBoolProperty("doRendering", true).setValue(doRendering);
        getBoolProperty("doConnecting", true).setValue(doConnecting);
        getBoolProperty("doParticles", false).setValue(doParticles);

        if (configuration.hasChanged())
        {
            configuration.save();
        }
    }

    private Property getBoolProperty(String settingKey, boolean defaultVal)
    {
        return configuration.get("all", settingKey, defaultVal);
    }
}
