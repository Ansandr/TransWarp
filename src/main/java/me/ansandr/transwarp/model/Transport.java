package me.ansandr.transwarp.model;

import org.bukkit.Location;
import org.bukkit.entity.Player;

// Объект искуственного транспорта (машина, автобус, самолет)
public class Transport {

    private TransportType type;
    private Player player;
    private Location transLoc;
    private Location warpLoc;
    private double distance;
    private double time;

    public Transport(TransportType type, Player passenger, Location transLoc, Location warpLoc, double distance) {
        this.transLoc = transLoc;
        this.warpLoc = warpLoc;
        this.type = type;
        this.player = passenger;
        this.distance = distance;
        this.time = calculateTime();
    }

    public double calculateTime() {
        // Время = расстояние/скорость
        return (distance)/(type.getSpeed()*3.6);//in m/s
    }

    public Location getTransLocation() {
        return transLoc;
    }

    public TransportType getType() {
        return type;
    }

    public double getDistance() {
        return distance;
    }

    public double getTime() {
        return time;
    }
}
