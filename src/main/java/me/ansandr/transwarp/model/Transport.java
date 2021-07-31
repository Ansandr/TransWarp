package me.ansandr.transwarp.model;

import org.bukkit.Location;
import org.bukkit.entity.Player;

// Объект искуственного транспорта (машина, автобус, самолет)
public class Transport {

    private TransportType type;
    private Player player;//Nullable
    private Location transLoc;
    private Location targetLoc;
    /** distance to target in blocks*/
    private double distance;
    private double time;
    private double cost;

    public Transport(TransportType type, Player passenger, Location transLoc, Location targetLoc, double distance) {
        this.transLoc = transLoc;
        this.targetLoc = targetLoc;
        this.type = type;
        this.player = passenger;
        this.distance = distance;
        this.time = calculateTime();
        this.cost = calculateCost();
    }

    public Transport(TransportType type, Location transLoc, Location targetLoc, double distance) {
        this.transLoc = transLoc;
        this.targetLoc = targetLoc;
        this.type = type;
        this.distance = distance;
        this.time = calculateTime();
        this.cost = calculateCost();
    }

    /**
     * Calculate time of seconds in how need for transport
     */
    public double calculateTime() {
        // Время = расстояние/скорость
        return (distance)/(type.getSpeed()*3.6);//in m/s
    }

    /**
     * Calculate how many cost the trip on a transport
     */
    public double calculateCost() {
        return type.getPrice() * (distance/1000);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Location getTransLocation() {
        return transLoc;
    }

    public Location getTargetLoc() {
        return targetLoc;
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

    public double getCost() {
        return cost;
    }
}
