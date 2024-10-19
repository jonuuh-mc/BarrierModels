package io.jonuuh.barriermodels;

import io.jonuuh.barriermodels.config.Command;
import io.jonuuh.barriermodels.config.Settings;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = BarrierModels.modID, version = BarrierModels.version, acceptedMinecraftVersions = "[1.8.9]")
public class BarrierModels
{
    public static final String modID = "barriermodels";
    public static final String version = "1.2.0";

    @Mod.EventHandler
    public void FMLPreInit(FMLPreInitializationEvent event)
    {
        Settings.createInstance(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void FMLInit(FMLInitializationEvent event)
    {
        ClientCommandHandler.instance.registerCommand(new Command());
    }
}
