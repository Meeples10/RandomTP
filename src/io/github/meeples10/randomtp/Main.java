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
    private static final int MAXIMUM_DISTANCE = 10000000;

    private static File df, cfg;
    private static List<String> worlds;
    private static int maximumDistance;
    private static long cooldown;
    private static HashMap<UUID, Long> lastUses = new HashMap<UUID, Long>();

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

    public static boolean isEnabled(String world) {
        return worlds.contains(world);
    }

    public static int getMaximumDistance() {
        return maximumDistance;
    }

    public static long getCooldown() {
        return cooldown;
    }

    public static long getLastUse(UUID uuid) {
        if(lastUses.containsKey(uuid)) {
            return lastUses.get(uuid);
        } else {
            return cooldown;
        }
    }

    public static void setLastUse(UUID uuid) {
        lastUses.put(uuid, System.currentTimeMillis());
    }

    public static HashMap<UUID, Long> getLastUses() {
        return lastUses;
    }
}
