package me.ansandr.transwarp.storage;

import me.ansandr.transwarp.configuration.StorageConfig;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;

public class YamlStorageManager implements StorageManager {

    private FileConfiguration data;
    private StorageConfig storageConfigc;

    public YamlStorageManager(StorageConfig sc) {
        this.storageConfigc = sc;
        this.data = sc.getConfig();
    }

    @Override
    public void setTransport(String transportType, String transportName, Location loc) {
        data.set(transportType + "." + transportName, loc);
        storageConfigc.saveConfig();
    }

   @Override
    public Location getLocation(String path) {
       return data.getLocation(path);

    }

    @Override
    public Location getLocation(String transportType, String transportName) {
        return data.getLocation(transportType + "." + transportName);
    }


    @Override
    public String getTransportType(String transportName) {
        ConfigurationSection cs = data.getConfigurationSection("");

        for (String key : cs.getKeys(true)) {
            if (key.equals(transportName)) {
                String path = cs.getConfigurationSection(key).getCurrentPath(); //bus.1
                String[] data = path.split(".");
                return data[1]; // "bus"
            }
        }
        return null;
    }

    @Override
    public Set<String> getTransportTypes() {
        ConfigurationSection cs = data.getConfigurationSection("");

        return cs.getKeys(false);
    }

    @Override
    public Set<String> getTransports(String transportType) {
        ConfigurationSection cs = data.getConfigurationSection(transportType);
        if (cs == null)
            return null;
        return cs.getKeys(false);
    }

    @Override
    public String getTransport(String transportType, String transportName) {
        return data.getConfigurationSection(transportType + "." + transportName).getName();
    }

    @Override
    public String getTransport(String transportType) {
        return null;
    }
}
