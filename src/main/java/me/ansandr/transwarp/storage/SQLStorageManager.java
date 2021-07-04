package me.ansandr.transwarp.storage;

import org.bukkit.Location;

import java.util.Set;

public class SQLStorageManager implements StorageManager {
    @Override
    public void setTransport(String transportName, String transportType, Location loc) {

    }

    @Override
    public String getTransportType(String transportName) {
        return null;
    }

    @Override
    public Location getLocation(String transportName) {
        return null;
    }

    @Override
    public Set<String> getTransportTypes() {
        return null;
    }

    @Override
    public Set<String> getTransports(String transportType) {
        return null;
    }
}
