package me.ansandr.transwarp;

import com.live.bemmamin.gps.api.GPSAPI;
import me.ansandr.transwarp.commands.CommandReload;
import me.ansandr.transwarp.commands.CommandSetTransport;
import me.ansandr.transwarp.configuration.StorageConfig;
import me.ansandr.transwarp.hooks.EssentialsHook;
import me.ansandr.transwarp.hooks.GPSHook;
import me.ansandr.transwarp.hooks.VaultHook;
import me.ansandr.transwarp.listeners.PlayerListener;
import me.ansandr.transwarp.model.TransportType;
import me.ansandr.transwarp.storage.SQLStorageManager;
import me.ansandr.transwarp.storage.StorageManager;
import me.ansandr.transwarp.storage.YamlStorageManager;
import me.ansandr.transwarp.util.MessageManager;
import me.ansandr.util.menu.Menu;
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
    private final Logger log = this.getLogger();
    private TransportTypeManager typeManager;
    public static String rawVersion;
    public static int version;
    private static Map<Player, MenuHolder> menuHolders = new HashMap<>();

    //Config
    private FileConfiguration config;
    //Localization
    private MessageManager messageManager;
    //Hooks
    private VaultHook vaultHook;
    private EssentialsHook essentialsHook;
    private GPSHook gpsHook;
    //Storage
    private StorageManager storage;

    private boolean menuEnabled;


    @Override
    public void onEnable() {

        onReload();

        messageManager = new MessageManager(this);
        messageManager.onEnable();

        setupEssentials();
        setupVault();
        setupGPS();

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        CommandSetTransport setTransport = new CommandSetTransport(this);
        getCommand("settransport").setExecutor(setTransport);
        getCommand("settransport").setTabCompleter(setTransport);
        getCommand("transwarp").setExecutor(new CommandReload(this));
        instance = this;
    }

    public void onReload() {
        saveDefaultConfig();
        this.reloadConfig();
        this.config = getConfig();
        Configuration def = getConfig().getDefaults();

        rawVersion = def.getString("config_version");
        version = calculateVersion(rawVersion);

        if (calculateVersion(config.getString("config_version")) < version) {
            log.warning("Config is outdated!");
        }

        if (getStorageType().equals("yaml")) {
            StorageConfig storageConfig = new StorageConfig(this);
            storage = new YamlStorageManager(storageConfig);
        } else if (getStorageType().equals("sql")) {
            storage = new SQLStorageManager();//TODO
        }
        loadTransportType();
        menuEnabled = config.getBoolean("menu.enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void loadTransportType() {
        ConfigurationSection transports = config.getConfigurationSection("transports");
        List<String> typeList = new ArrayList<>();
        Map<String, TransportType> typeMap = new HashMap<>();
        for (String key : transports.getKeys(false)) {//TODO Может быть null
            TransportType transportType = new TransportType(
                    key,
                    transports.getInt(key + ".max_distance"),
                    transports.getInt(key + ".min_distance"),
                    transports.getDouble(key + ".speed"),
                    transports.getDouble(key + ".cost")
            );
            typeList.add(key);
            typeMap.put(key, transportType);

        }
        typeManager = new TransportTypeManager(this, typeList, typeMap);
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
        if (!getServer().getPluginManager().getPlugin("GPS").isEnabled()) {
            return false;
        }
        gpsHook = new GPSHook(this);
        return gpsHook.hooked();
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

    public String getStorageType() {
        return config.getString("storage_type").toLowerCase(Locale.ROOT);
    }//TODO Рефакторинг

    public TransportTypeManager getTypeManager() {
        return typeManager;
    }

    public StorageManager getStorage() {
        return storage;
    }

    public boolean isMenuEnabled() {
        return menuEnabled;
    }//TODO Рефакторинг

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
