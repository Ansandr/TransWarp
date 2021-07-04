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
    public void setTransport(String transportName, String transportType, Location loc) {
        data.set(transportType + "." + transportName, loc);
        storageConfigc.saveConfig();

    }


   @Override
    public Location getLocation(String path) {
       return data.getLocation(path);

    }


    @Override//TODO Оптимизировать с помощью передачи пути к транспорту в конфиге (или генерация списка)
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

        return cs.getKeys(false);
    }
}
