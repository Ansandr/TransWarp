package me.ansandr.transwarp.configuration;

import me.ansandr.transwarp.TransWarp;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Класс для генерации и доступа к файлу storage.yml
 */
public class StorageConfig {
    
    private FileConfiguration storageConfig;
    private File storageFile;
    
    private TransWarp plugin;
    
    public StorageConfig(TransWarp plugin) {
        this.plugin = plugin;
        File dataFolder = plugin.getDataFolder();
        if(!dataFolder.exists()) {
            dataFolder.mkdirs();
            plugin.getLogger().info("Creating of plugin folder...");
        }

        if (dataFolder == null) throw new IllegalStateException();
        
        this.storageFile = new File(dataFolder, "storage.yml");
        create();
    }
    
    public void reloadConfig() {
        if (storageFile == null) {
            storageFile = new File(plugin.getDataFolder(), "storage.yml");
            create();
        }
        storageConfig = YamlConfiguration.loadConfiguration(storageFile);

    }

    public FileConfiguration getConfig() {
        if (storageConfig == null) {
            reloadConfig();
        }
        return storageConfig;
    }

    public void saveConfig() {
        if (storageConfig != null && storageFile != null) {
            try {
                getConfig().save(storageFile);
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not save config to " + storageFile, ex);
            }
        }
    }

    public void create() {
        if (!storageFile.exists()) {
            try {
                storageFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
