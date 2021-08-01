package me.ansandr.transwarp.util;

import me.ansandr.transwarp.TransWarp;
import me.ansandr.transwarp.model.TransportType;
import me.ansandr.transwarp.storage.StorageManager;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.NumberConversions;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * Класс для поиска оптимального транспорта
*/
public class TransportFinder {

    private final TransWarp plugin;//TODO может сделать статик?

    private FileConfiguration config;
    private StorageManager storage;

    private Location playerLoc;
    private TransportType type;
    public double distance;

    public TransportFinder(Location playerLoc, Location targetLoc, TransWarp plugin) {
        this.plugin = plugin;
        this.storage = plugin.getStorage();
        this.playerLoc = playerLoc;
        distance = getDistance(playerLoc, targetLoc);
    }

    /**
     * Получить местоположение машини
     */
    public Location findTransport(Location playerLoc, Location warpLoc) throws TransportNotFoundException {
        //Выбрать тип транспорта исходя из длины
        type = plugin.getTypeManager().getByDistance(distance);
        if (type == null) {
            throw new TransportNotFoundException();//TODO локализация
        }
        //Собрать все транспорта одного типа
        Set<String> transportSet = storage.getTransports(type.getName());
        if (transportSet == null) {
            throw new TransportNotFoundException();//TODO локализация
        }
        //Выбрать случайный транспорт из списка
        String transportName = getRandom(transportSet);

        return storage.getLocation(type.getName() + "." + transportName);
    }

    public TransportType getTransportType() {
        return type;
    }

    private String getRandom(Set<String> set) {
        Random rand = new Random();

        int index = rand.nextInt(set.size());//Случайное число от размера массива

        Iterator<String> iter = set.iterator();
        for (int i = 0; i < index; i++) {
            iter.next();
        }
        return iter.next();
    }

    public double getDistanceSquared(Location firstLoc, Location secondLoc) {
        if (secondLoc == null) {
            throw new IllegalArgumentException("Cannot measure distance to a null location");
        } else if (secondLoc.getWorld() == null || firstLoc.getWorld() == null) {
            throw new IllegalArgumentException("Cannot measure distance to a null world");
        } else if (secondLoc.getWorld() != firstLoc.getWorld()) {
            throw new IllegalArgumentException("Cannot measure distance between " + firstLoc.getWorld().getName() + " and " + secondLoc.getWorld().getName());
        }
        //First coordinates
        double fx = firstLoc.getX();
        double fz = firstLoc.getZ();
        //Seconds coordinates
        double sx = secondLoc.getX();
        double sz = secondLoc.getZ();

        return NumberConversions.square(fx - sx) + NumberConversions.square(fz - sz);
    }

    /**
     * Метод для нахождение расстояния между локациями по площади
     * @param firstLoc
     * @param secondLoc
     * @return расстояние по площади
     */
    public double getDistance(Location firstLoc, Location secondLoc) {
        return Math.sqrt(getDistanceSquared(firstLoc, secondLoc));
    }
}