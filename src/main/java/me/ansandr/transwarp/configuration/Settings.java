package me.ansandr.transwarp.configuration;

import me.ansandr.transwarp.TransWarp;
import me.ansandr.transwarp.storage.SQLStorageManager;
import me.ansandr.transwarp.storage.YamlStorageManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Locale;
import java.util.logging.Logger;

public class Settings {

    private static final Logger LOGGER = TransWarp.getInstance().getLogger();
    private final FileConfiguration config;
    private final TransWarp plugin;

    public static String rawVersion;
    public static int version;

    private String storageType;
    private boolean costIsStatic;
    private boolean isMenuEnable;

    public Settings(final TransWarp plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();

        rawVersion = config.getDefaults().getString("config_version");
        version = calculateVersion(rawVersion);

        if (calculateVersion(config.getString("config_version")) < version) {
            LOGGER.warning("Config is outdated!");//TODO update
        }

        storageType = config.getString("storage_type".toLowerCase(Locale.ROOT), "yaml");
        costIsStatic = config.getBoolean("static_cost");
        isMenuEnable = config.getBoolean("menu.enabled", false);
    }

    public String getStorageType() {
        return storageType;
    }

    public boolean costIsStatic() {
        return costIsStatic;
    }

    public boolean isMenuEnable() {
        return isMenuEnable;
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
