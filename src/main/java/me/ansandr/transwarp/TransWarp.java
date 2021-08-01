package me.ansandr.transwarp;

import com.live.bemmamin.gps.api.GPSAPI;
import me.ansandr.transwarp.commands.CommandSetTransport;
import me.ansandr.transwarp.commands.CommandTranswarp;
import me.ansandr.transwarp.configuration.Settings;
import me.ansandr.transwarp.configuration.StorageConfig;
import me.ansandr.transwarp.hooks.EssentialsHook;
import me.ansandr.transwarp.hooks.GPSHook;
import me.ansandr.transwarp.hooks.VaultHook;
import me.ansandr.transwarp.listeners.PlayerListener;
import me.ansandr.transwarp.model.Transport;
import me.ansandr.transwarp.model.TransportType;
import me.ansandr.transwarp.storage.SQLStorageManager;
import me.ansandr.transwarp.storage.StorageManager;
import me.ansandr.transwarp.storage.YamlStorageManager;
import me.ansandr.transwarp.util.MessageManager;
import me.ansandr.util.menu.MenuHolder;
import net.ess3.api.IEssentials;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;

/**
 * TODO API for plugin
 */
public final class TransWarp extends JavaPlugin {

    private static TransWarp instance;
    private final Logger LOGGER = getLogger();
    private TransportTypeManager typeManager;

    private static Map<Player, MenuHolder> menuHolders = new HashMap<>();
    private static Map<Player, Transport> transports = new HashMap<>();

    //Config
    private FileConfiguration config;
    private Settings settings;
    //Localization
    private MessageManager messageManager;
    //Hooks
    private VaultHook vaultHook;
    private EssentialsHook essentialsHook;
    private GPSHook gpsHook;
    //Storage
    private StorageManager storage;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        settings = new Settings(this);
        messageManager = new MessageManager(this);
        messageManager.onEnable();

        setupEssentials();
        setupVault();
        setupGPS();

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        CommandSetTransport setTransport = new CommandSetTransport(this);
        getCommand("settransport").setExecutor(setTransport);
        getCommand("settransport").setTabCompleter(setTransport);
        getCommand("transwarp").setExecutor(new CommandTranswarp(this));
        instance = this;
    }

    public void reload() {
        if (getSettings().getStorageType().equals("yaml")) {
            StorageConfig storageConfig = new StorageConfig(this);
            storage = new YamlStorageManager(storageConfig);
        } else if (getSettings().getStorageType().equals("sql")) {
            storage = new SQLStorageManager();//TODO
        }

        loadTransportType();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void loadTransportType() {
        ConfigurationSection transports = config.getConfigurationSection("transports");
        Map<String, TransportType> typeMap = new HashMap<>();
        for (String key : transports.getKeys(false)) {//TODO Может быть null
            TransportType transportType = new TransportType(
                    key,
                    transports.getInt(key + ".max_distance"),
                    transports.getInt(key + ".min_distance"),
                    transports.getDouble(key + ".speed"),
                    transports.getDouble(key + ".cost")
            );
            typeMap.put(key, transportType);
        }
        typeManager = new TransportTypeManager(this, typeMap);
    }

    private boolean setupVault() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        vaultHook = new VaultHook();

        return vaultHook.hooked();
    }

    private boolean setupEssentials() {
        if (getServer().getPluginManager().getPlugin("Essentials") == null) {
            return false;
        }
        essentialsHook = new EssentialsHook();
        return essentialsHook.hooked();
    }

    private boolean setupGPS() {
        if (getServer().getPluginManager().getPlugin("GPS") == null) {
            return false;
        }
        gpsHook = new GPSHook(this);
        return gpsHook.hooked();
    }

    public Settings getSettings() {
        return settings;
    }

    public static void createHolder(Player player) {
        MenuHolder holder = new MenuHolder(player);
        menuHolders.put(player, holder);
    }

    public static MenuHolder getHolder(Player player) {
        if (menuHolders.containsKey(player)) {
            return menuHolders.get(player);
        }
        return null;
    }

    public static Map<Player, MenuHolder> getMenuHolders() {
        return menuHolders;
    }


    public TransportTypeManager getTypeManager() {
        return typeManager;
    }

    public StorageManager getStorage() {
        return storage;
    }

    public Economy getEconomy() {
        return vaultHook.getEconomy();
    }

    public IEssentials getEssentials() {
        return essentialsHook.getEssentials();
    }

    public GPSHook getGPSHook() {
        return gpsHook;
    }

    public GPSAPI getGPS() {
        return gpsHook.getApi();
    }

    public static TransWarp getInstance() {
        return instance;
    }
}
