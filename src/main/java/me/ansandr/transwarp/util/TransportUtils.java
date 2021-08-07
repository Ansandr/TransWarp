package me.ansandr.transwarp.util;

import me.ansandr.transwarp.TransWarp;
import me.ansandr.transwarp.model.Transport;
import me.ansandr.transwarp.model.TransportType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class TransportUtils {

    /**
     * Get transport for given informations
     */
    public static Transport getTransport(Player player, Location targetLoc) throws TransportNotFoundException {
        Location playerLoc = player.getLocation();
        TransportFinder finder = new TransportFinder(playerLoc, targetLoc, TransWarp.getInstance());
        //Получить местоположение машини
        Location transLoc = finder.findTransport(playerLoc, targetLoc);
        if (transLoc == null) {
            throw new TransportNotFoundException("");
        }
        TransportType type = finder.getTransportType();

        double cost = type.getPrice();
        if (!TransWarp.getInstance().getSettings().costIsStatic()) {
            cost = calculateCost(finder.distance, cost);
        }
        return new Transport(type, player, transLoc, targetLoc,
                finder.distance,
                calculateTime(finder.distance, type.getSpeed()),
                cost);
    }

    public static Transport getTeleporter(Player player, Location targetLoc) throws TransportNotFoundException {
        TransWarp plugin = TransWarp.getInstance();
        TransportType type = plugin.getTypeManager().getByName("teleporter");
        if (type == null) {
            throw new TransportNotFoundException();
        }

        String transName = getRandom(type.getTransports());
        Location transLoc = plugin.getStorage().getLocation(type.getName(), transName);

        return new Transport(type, player, transLoc, targetLoc,
                type.getTime(),
                type.getPrice()
        );
    }

    public static String getRandom(Set<String> set) {
        Random rand = new Random();

        int index = rand.nextInt(set.size());//Случайное число от размера массива
        Iterator<String> iter = set.iterator();
        for (int i = 0; i < index; i++) {
            iter.next();
        }
        return iter.next();
    }

    /**
     * Calculate time of seconds in how need for transport
     */
    public static int calculateTime(double distance, double speed) {
        // Время = расстояние/скорость
        return NumberConversions.toInt((distance)/(speed*3.6));//in m/s
    }

    /**
     * Calculate how many cost the trip on a transport
     */
    public static double calculateCost(double distance, double price) {
        return price * (distance/1000);
    }
}
