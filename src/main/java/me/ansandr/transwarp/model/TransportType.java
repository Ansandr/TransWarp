package me.ansandr.transwarp.model;

/**
 * Содержит информацию об виде транспорта, которая задается в конфиге
 */
public class TransportType {

    private String name;
    private int maxDistance;
    private int minDistance;
    private double speed;
    private double price;

    public TransportType(String name, int maxDistance, int minDistance, double speed, double price) {
        this.name = name;
        this.maxDistance = maxDistance;
        this.minDistance = minDistance;
        this.speed = speed;
        this.price = price;
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
}
