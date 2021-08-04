package me.ansandr.transwarp.model;

import me.ansandr.transwarp.TransWarp;
import me.ansandr.transwarp.model.task.TransportingTask;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

// Объект искусственного транспорта (машина, автобус, самолет)
public class Transport {

    private TransportType type;
    private Player passenger;
    private TransportingTask task;
    private Location startLoc;
    private Location transLoc;
    private Location targetLoc;
    /** distance from start to target in blocks*/
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
        this.task = new TransportingTask(this);
    }

    public Transport(TransportType type, Player passenger, Location transLoc, Location targetLoc, double distance, double cost) {
        this.type = type;
        this.passenger = passenger;
        this.startLoc = passenger.getLocation();
        this.transLoc = transLoc;
        this.targetLoc = targetLoc;
        this.distance = distance;
        this.time = calculateTime();
        this.cost = type.getPrice();
        this.task = new TransportingTask(this);
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

    public void setPassenger(Player player) {
        this.passenger = player;
    }

    public Player getPassenger() {
        return passenger;
    }

    public void setTask(BukkitRunnable task) {
        this.task = (TransportingTask) task;
    }

    public TransportingTask getTask() {
        return task;
    }

    public void transport(TransWarp plugin) {
        sendPotionEffect(passenger, plugin);
        passenger.teleport(transLoc);
        task.runTaskTimer(plugin, 20, 20);
    }

    public void teleportToTarget() {
        passenger.teleport(targetLoc);
    }

    public void cancelTransporting() {
        task.cancel();
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

    /**
     * @return Return a object of transport type
     */
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

    private static void sendPotionEffect(Player p, Plugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                p.addPotionEffect(
                        new PotionEffect(PotionEffectType.SLOW, 80, 2, false, false));
            }
        }.runTaskLater(plugin, 5);
    }
}
