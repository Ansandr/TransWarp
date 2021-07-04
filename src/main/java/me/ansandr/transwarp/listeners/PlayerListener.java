package me.ansandr.transwarp.listeners;

import com.earth2me.essentials.commands.WarpNotFoundException;
import me.ansandr.transwarp.TransWarp;
import me.ansandr.transwarp.model.Transport;
import me.ansandr.transwarp.task.TrasportingTask;
import me.ansandr.transwarp.util.TransportFinder;
import me.ansandr.transwarp.util.TransportNotFoundException;
import net.ess3.api.IEssentials;
import net.ess3.api.InvalidWorldException;
import net.ess3.api.events.UserTeleportHomeEvent;
import net.ess3.api.events.UserWarpEvent;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;

public class PlayerListener implements Listener {

    private TransWarp plugin;
    private IEssentials ess;
    private FileConfiguration config;

    public PlayerListener(TransWarp plugin) {
        this.plugin = plugin;
        this.ess = plugin.getEssentials();
        this.config = plugin.getConfig();
    }

    @EventHandler
    public void onLeft(PlayerQuitEvent e) {

    }

    // When player use warp
    @EventHandler
    public void onWarp(UserWarpEvent e) throws WarpNotFoundException, InvalidWorldException {
        e.setCancelled(true);
        Player p = e.getUser().getBase();
        //Получить местоположение варпа
        String warpName = e.getWarp();
        Location warpLoc = ess.getWarps().getWarp(warpName);

        transport(p, warpLoc);
    }

    @EventHandler
    public void onHomeTeleport(UserTeleportHomeEvent e) {
        e.setCancelled(true);
        Player p = e.getUser().getBase();
        Location homeLoc = e.getHomeLocation();

        transport(p, homeLoc);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equals("transport")) {
            Player p = e.getPlayer();
            if (!p.isOp()) {
                p.sendMessage("Make by Ansandr");
            }
        }
    }

    private void transport(Player p, Location warpLoc) {
        Location playerLoc = p.getLocation();

        TransportFinder finder = new TransportFinder(plugin, playerLoc, warpLoc);
        //Получить местоположение машини
        Location transLoc = null;
        try {
            transLoc = finder.find(p, warpLoc);
        } catch (TransportNotFoundException ex) {
            p.sendMessage(ex.getMessage());
            return;
        }
        //Получить объект транспорта
        Transport transport = new Transport(finder.getTransportType(), p, transLoc, warpLoc,
                finder.getDistance(playerLoc, warpLoc));//TODO ищется дважды
        //Телепортировать в транспорт
        p.teleport(transLoc);
        // ждать время
        new BukkitRunnable() {
            @Override
            public void run() {
                p.addPotionEffect(
                        new PotionEffect(PotionEffectType.SLOW, 100, 1, false, false));
            }
        }.runTaskLater(plugin, 5);
        new TrasportingTask(p, warpLoc, NumberConversions.toInt(transport.getTime())).runTaskTimer(plugin, 20, 20);
    }
}