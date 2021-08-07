package me.ansandr.transwarp;

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
import me.ansandr.utils.menu.MenuHolder;
import me.ansandr.utils.message.MessageManager;
import net.ess3.api.IEssentials;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
        instance = this;

        settings = new Settings(this);
        reload();

        setupEssentials();
        setupVault();
        setupGPS();

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        CommandSetTransport setTransport = new CommandSetTransport(this);
        getCommand("settransport").setExecutor(setTransport);
        getCommand("settransport").setTabCompleter(setTransport);
        CommandTranswarp transwarp = new CommandTranswarp(this);
        getCommand("transwarp").setExecutor(transwarp);
        getCommand("transwarp").setTabCompleter(transwarp);
    }

    public void reload() {
        saveDefaultConfig();
        settings.reloadConfig();
        config = getConfig();
        loadStorage();
        loadTransportTypes();
        messageManager = new MessageManager(this, config.getConfigurationSection("messages"));//TODO проверить на отказоустойчивость
    }

    public void loadStorage() {
        if (getSettings().getStorageType().equals("yaml")) {
            StorageConfig storageConfig = new StorageConfig(this);
            storage = new YamlStorageManager(storageConfig);
        } else if (getSettings().getStorageType().equals("sql")) {
            storage = new SQLStorageManager();//TODO
        }
    }

    public void loadTransportTypes() {
        ConfigurationSection transports = config.getConfigurationSection("transports");
        Map<String, TransportType> typeMap = new HashMap<>();
        for (String key : transports.getKeys(false)) {
            Set<String> transportSet = getStorage().getTransports(key);
            if (transportSet == null) {
                transportSet = new HashSet<>();
            }
            TransportType transportType = new TransportType(
                    key,
                    transports.getInt(key + ".max_distance"),
                    transports.getInt(key + ".min_distance"),
                    transports.getDouble(key + ".speed"),
                    transports.getDouble(key + ".cost"),
                    transportSet
            );
            typeMap.put(key, transportType);
        }
        typeManager = new TransportTypeManager(this, typeMap);
        loadTeleporterType();
    }

    private void loadTeleporterType() {
        ConfigurationSection teleporter = config.getConfigurationSection("teleporter");
        TransportType type = new TransportType(
                "teleporter",
                teleporter.getInt("time"),
                teleporter.getDouble("cost"),
                getStorage().getTransports("teleporter")
        );
        typeManager.addType("teleporter", type);
    }

    private boolean setupVault() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            LOGGER.info("Vault dependency not found!");
            LOGGER.info("Economy module disabled");
            return false;
        }
        vaultHook = new VaultHook();

        return vaultHook.hooked();
    }

    private boolean setupEssentials() {
        if (getServer().getPluginManager().getPlugin("Essentials") == null) {
            LOGGER.warning("Essentials dependency not found!");
            LOGGER.warning("Features cant working");
            return false;
        }
        essentialsHook = new EssentialsHook();
        return essentialsHook.hooked();
    }

    private boolean setupGPS() {
        if (getServer().getPluginManager().getPlugin("GPS") == null) {
            LOGGER.info("GPS dependency not found!");
            return false;
        }
        gpsHook = new GPSHook(this);
        return gpsHook.hooked();
    }

    public Settings getSettings() {
        return settings;
    }

    public static boolean isInTransport(Player player) {
        return transports.containsKey(player);
    }

    public static void putTransport(Player player, Transport transport) {
        transports.put(player, transport);
    }

    public static Transport getTransport(Player player) {
        return transports.get(player);
    }

    public static void removeTransport(Player player) {
        transports.remove(player);
    }

    /**
     * Создать holder и поместить его в список
     */
    public static MenuHolder createHolder(Player player) {
        MenuHolder holder = new MenuHolder(player);
        putHolder(holder);
        return holder;
    }

    /**
     * вставить holder в список
     */
    public static void putHolder(MenuHolder holder) {
        menuHolders.put(holder.getViewer(), holder);
    }

    public static MenuHolder getHolder(Player player) {
        return menuHolders.get(player);
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

    public VaultHook getVaultHook() {
        return this.vaultHook;
    }

    public GPSHook getGPSHook() {
        return gpsHook;
    }

    public static TransWarp getInstance() {
        return instance;
    }
}
