package me.ansandr.transwarp.util;

import me.ansandr.transwarp.TransWarp;
import me.ansandr.transwarp.model.Transport;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static me.ansandr.utils.message.MessageManager.tl;

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
        if (TransWarp.getInstance().getSettings().costIsStatic()) {
            return new Transport(finder.getTransportType(), player, transLoc, targetLoc,
                    finder.distance, finder.getTransportType().getPrice());//TODO рефакторинг
        }
        return new Transport(finder.getTransportType(), player, transLoc, targetLoc,
                finder.distance);
    }

    public static Transport getTeleporter(Player player, Location targetLoc) {
        return null;
    }//TODO
}
