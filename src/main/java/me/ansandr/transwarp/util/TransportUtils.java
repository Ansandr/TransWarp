package me.ansandr.transwarp.util;

import me.ansandr.transwarp.TransWarp;
import me.ansandr.transwarp.model.Transport;
import me.ansandr.transwarp.task.TrasportingTask;
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
    public static Transport getTransport(Location playerLoc, Location targetLoc) throws TransportNotFoundException {

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
        Location transLoc = finder.findTransport(playerLoc, targetLoc);//TODO ищется дважды
        if (transLoc == null) {
            throw new TransportNotFoundException("");
        }
        //Получить объект транспорта
        return new Transport(finder.getTransportType(), transLoc, targetLoc,
                finder.distance);//TODO ищется дважды
    }

    public static void transport(Transport transport, Plugin plugin) {//TODO в utls
        Player p = transport.getPlayer();
        //Телепортировать в транспорт
        p.teleport(transport.getTransLocation());
        // дать зелье
        sendPotionEffect(p, plugin);
        new TrasportingTask(p, transport.getTargetLoc(), NumberConversions.toInt(transport.getTime())).runTaskTimer(plugin, 20, 20);
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
