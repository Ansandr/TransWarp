package me.ansandr.transwarp.configuration;

import com.tchristofferson.configupdater.ConfigUpdater;
import me.ansandr.transwarp.TransWarp;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Logger;

public class Settings {

    private static final Logger LOGGER = TransWarp.getInstance().getLogger();
    private final TransWarp plugin;
    private FileConfiguration config;

    public static String rawVersion;
    public static int version;

    private String storageType;
    private boolean isEconomyEnabled;
    private boolean costIsStatic;
    private boolean isMenuEnable;
    //TODO private boolean isGpsEnable
    private boolean interWorldTransitEnabled;

    public Settings(final TransWarp plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public void reloadConfig() {
        rawVersion = config.getDefaults().getString("config_version");
        version = calculateVersion(rawVersion);

        if (calculateVersion(config.getString("config_version")) < version) {
            LOGGER.warning("Config is outdated! Updating now");
            try {
                ConfigUpdater.update(plugin, "config.yml", new File(plugin.getDataFolder(), "config.yml"), new ArrayList<>());
                config.set("config_version", rawVersion);
                plugin.saveConfig();//TODO save with comment
                LOGGER.info("Update successful");
            } catch (IOException ex) {
                LOGGER.warning("Update failed");
                ex.printStackTrace();
            }
        }

        plugin.reloadConfig();
        config = plugin.getConfig();

        storageType = config.getString("storage_type".toLowerCase(Locale.ROOT), "yaml");
        isEconomyEnabled = config.getBoolean("economy_enabled", false) && plugin.getEconomy() != null;
        costIsStatic = config.getBoolean("static_cost", true);
        isMenuEnable = config.getBoolean("menu.enabled", false);
        interWorldTransitEnabled = config.getBoolean("inter_world_transit", true);
    }

    public String getStorageType() {
        return storageType;
    }

    public boolean costIsStatic() {
        return costIsStatic;
    }

    public boolean isEconomyEnabled() {
        return isEconomyEnabled;
    }

    public boolean isMenuEnable() {
        return isMenuEnable;
    }

    public boolean interWorldTransitEnabled() {
        return interWorldTransitEnabled;
    }

    public static int calculateVersion(String s) {
        int ver = 0;
        String[] version = s.split("\\.");
        try {
            ver += Integer.parseInt(version[0]) * 1000;
            ver += Integer.parseInt(version[1]) * 100;
            ver += Integer.parseInt(version[2]);
        } catch (NumberFormatException ignored) {}

        return ver;
    }
}
