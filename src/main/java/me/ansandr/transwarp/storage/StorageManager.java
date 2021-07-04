package me.ansandr.transwarp.storage;

import org.bukkit.Location;

import java.util.Set;

/**
 * Интерфейс для связи с базой данных с местоположением Транспортов
 */
public interface StorageManager {

    /**
     * Set to storage transport coordinates
     * @param transportName
     * @param transportType
     */
    void setTransport(String transportName, String transportType, Location loc);

    /**
     *
     * @param transportName
     * @return Type of transport, if transport find
     */
    String getTransportType(String transportName);//TODO add exceptions

    /**
     *
     * @param transportName
     * @return Location of transport, if exists
     */
    Location getLocation(String transportName);

    /**
     * Get all transportTypes in config
     * @return
     */
    Set<String> getTransportTypes();

    /**
     *
     * @param transportType
     * @return set of all transports of transport type
     */
    Set<String> getTransports(String transportType);
}
