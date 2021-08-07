package me.ansandr.transwarp.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Содержит информацию об виде транспорта, которая задается в конфиге
 */
public class TransportType {

    private Set<String> transports;
    private String name;
    private int maxDistance;
    private int minDistance;
    private int time;
    private double speed;
    private double price;

    public TransportType(String name, int maxDistance, int minDistance, double speed, double price, Set<String> transports) {
        this.name = name;
        this.maxDistance = maxDistance;
        this.minDistance = minDistance;
        this.speed = speed;
        this.price = price;
        this.transports = transports;
    }

    /**
     * Constructor for teleporter
     */
    public TransportType(String name, int time, double price, Set<String> transports) {
        this.name = name;
        this.time = time;
        this.price = price;
        this.transports = transports;
    }

    /**
     * @return set of transports this type from storage
     */
    public Set<String> getTransports() {
        return transports;
    }

    /**
     *
     * @return
     */
    public String getTransport(String transportName) {
        for (String transport : transports) {
            if (transports.equals(transport)) {
                return transport;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public int getMinDistance() {
        return minDistance;
    }

    public double getSpeed() {
        return speed;
    }

    public double getPrice() {
        return price;
    }

    public int getTime() {
        return time;
    }
}
