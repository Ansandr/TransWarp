package me.ansandr.transwarp.util;

import me.ansandr.transwarp.TransWarp;
import me.ansandr.transwarp.model.Transport;
import me.ansandr.transwarp.model.task.TransportingTask;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;

import static me.ansandr.transwarp.util.MessageManager.tl;

public class TransportUtils {

    /**
     * Get transport for given informations
     */
    public static Transport getTransport(Player player, Location targetLoc) throws TransportNotFoundException {
        Location playerLoc = player.getLocation();
        if (targetLoc.getWorld() != playerLoc.getWorld()) {//TODO придумать медленный делепортер через конфиг
            /*
            if (чета там) {
                тп телепорт потом target
            }
             */
            if (true) {
                throw new TransportNotFoundException(tl("invalid_world"));
            }
        }
        TransportFinder finder = new TransportFinder(playerLoc, targetLoc, TransWarp.getInstance());
        //Получить местоположение машини
        Location transLoc = finder.findTransport(playerLoc, targetLoc);
        if (transLoc == null) {
            throw new TransportNotFoundException("");
        }
        //Получить объект транспорта
        return new Transport(finder.getTransportType(), player, transLoc, targetLoc,
                finder.distance);
    }

    public static Transport getTeleporter(Player player, Location targetLoc) {
        return null;
    }//TODO
}
