package me.ansandr.transwarp.model;

import org.bukkit.Location;
import org.bukkit.entity.Player;

// Объект искуственного транспорта (машина, автобус, самолет)
public class Transport {

    private TransportType type;
    private Player passenger;//Nullable
    private Location startLoc;//TODO commit
    private Location transLoc;
    private Location targetLoc;
    /** distance to target in blocks*/
    private double distance;
    private double time;
    private double cost;

    public Transport(TransportType type, Player passenger, Location transLoc, Location targetLoc, double distance) {
        this.type = type;
        this.passenger = passenger;
        this.startLoc = passenger.getLocation();
        this.transLoc = transLoc;
        this.targetLoc = targetLoc;
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

    public Player getPassenger() {
        return passenger;
    }

    public void setPassenger(Player player) {
        this.passenger = player;
    }

    /**
     * @return Location where player has been before teleported to transport
     */
    public Location getStartLocation() {
        return startLoc;
    }

    /**
     * @return The transport location
     */
    public Location getTransLocation() {
        return transLoc;
    }

    /**
     * @return A location where player will be teleported after transport
     */
    public Location getTargetLocation() {
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
