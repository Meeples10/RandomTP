package io.github.meeples10.randomtp;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.meeples10.meepcore.I18n;

public class Main extends JavaPlugin {

    public static final String NAME = "RandomTP";
    private static final int MAXIMUM_DISTANCE = 30000000;

    private static File df, cfg;
    public static List<String> worlds;
    public static int maximumDistance;
    public static long cooldown;
    public static final HashMap<UUID, Long> LAST_USES = new HashMap<UUID, Long>();

    @Override
    public void onEnable() {
        df = Bukkit.getServer().getPluginManager().getPlugin(NAME).getDataFolder();
        cfg = new File(df, "config.yml");
        this.getCommand("randomtp").setExecutor(new CommandRandomTeleport());
        loadConfig();
        try {
            I18n.loadMessages(NAME);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean loadConfig() {
        try {
            if(!df.exists()) {
                df.mkdirs();
            }
            if(!cfg.exists()) {
                Bukkit.getServer().getPluginManager().getPlugin(NAME).saveDefaultConfig();
            }
            FileConfiguration c = YamlConfiguration.loadConfiguration(cfg);
            worlds = c.getStringList("enabled-worlds");
            maximumDistance = c.getInt("maximum-distance");
            if(maximumDistance > MAXIMUM_DISTANCE) {
                maximumDistance = MAXIMUM_DISTANCE;
                c.set("maximum-distance", MAXIMUM_DISTANCE);
                c.save(cfg);
            }
            cooldown = c.getLong("cooldown");
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
