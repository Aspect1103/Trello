package me.jack.TrelloForge;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = TrelloForge.MODID, name = TrelloForge.NAME, version = TrelloForge.VERSION)
public class TrelloForge {
    public static final String MODID = "trelloforge";
    public static final String NAME = "Trello Forge";
    public static final String VERSION = "1.0";

    // Runs first thing to initialise mod
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }

    // Runs during game initialisation to register world generators and event handlers
    @EventHandler
    public void init(FMLInitializationEvent event) {

    }

    // Runs after game initialisation for dependencies on other mods
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

    // Register commands
    @EventHandler
    public void init(FMLServerStartingEvent event) {
        event.registerServerCommand(new TrelloGet());
    }
}
