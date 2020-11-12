package me.jack.trello.trello;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Trello extends JavaPlugin {
    // Setup config variable
    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        // Initialise all three commands
        this.getCommand("trelloGet").setExecutor(new TrelloGet());
        this.getCommand("trelloPost").setExecutor(new TrelloPost());
        this.getCommand("trelloDelete").setExecutor(new TrelloDelete());

        // Initialise config file
        config.addDefault("boardID", "");
        config.addDefault("key", "");
        config.addDefault("token", "");
        config.options().copyDefaults(true);
        saveConfig();
    }
}