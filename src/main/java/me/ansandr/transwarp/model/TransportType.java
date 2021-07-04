package me.ansandr.transwarp.model;

/**
 * Содержит информацию об виде транспорта, которая задается в конфиге
 */
public class TransportType {

    private String name;
    private int maxDistance;
    private int minDistance;
    private double speed;

    public TransportType(String name, int maxDistance, int minDistance, double speed) {
        this.name = name;
        this.maxDistance = maxDistance;
        this.minDistance = minDistance;
        this.speed = speed;
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
}
