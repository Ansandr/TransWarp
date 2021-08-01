package me.ansandr.transwarp.listeners;

import com.earth2me.essentials.commands.WarpNotFoundException;
import me.ansandr.transwarp.TransWarp;
import me.ansandr.transwarp.menu.TransChosingMenu;
import me.ansandr.transwarp.model.Transport;
import me.ansandr.transwarp.util.TransportNotFoundException;
import me.ansandr.transwarp.util.TransportUtils;
import me.ansandr.util.menu.Menu;
import net.ess3.api.IEssentials;
import net.ess3.api.InvalidWorldException;
import net.ess3.api.events.UserTeleportHomeEvent;
import net.ess3.api.events.UserWarpEvent;
import net.essentialsx.api.v2.events.UserTeleportSpawnEvent;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;

public class PlayerListener implements Listener {

    private TransWarp plugin;
    private IEssentials ess;
    private FileConfiguration config;

    public PlayerListener(TransWarp plugin) {
        this.plugin = plugin;
        this.ess = plugin.getEssentials();
        this.config = plugin.getConfig();
    }

    @EventHandler//TODO commit
    public void onLeft(PlayerQuitEvent e) {

    }

    // When player use warp
    @EventHandler
    public void onWarp(UserWarpEvent e) throws WarpNotFoundException, InvalidWorldException {
        Player p = e.getUser().getBase();
        if (p.hasPermission("transwarp.bypass")) {
            return;
        }
        e.setCancelled(true);
        String warpName = e.getWarp();
        Location warpLoc = ess.getWarps().getWarp(warpName);
        Transport transport = null;
        try {
            transport = TransportUtils.getTransport(p.getLocation(), warpLoc);
        } catch (TransportNotFoundException ex) {
            p.sendMessage(ex.getMessage());
            return;
        }
        transport.setPassenger(p);
        double cost = transport.getCost();

        if (plugin.getSettings().isMenuEnable()) {
            TransWarp.createHolder(p);
            new TransChosingMenu(TransWarp.getHolder(p), cost, warpName, plugin);
        }
        TransportUtils.transport(transport, plugin);
    }

    @EventHandler
    public void onHomeTeleport(UserTeleportHomeEvent e) {
        Player p = e.getUser().getBase();
        if (p.hasPermission("transwarp.bypass")) {
            return;
        }
        e.setCancelled(true);
        Location homeLoc = e.getHomeLocation();
        Transport transport = null;
        try {
            transport = TransportUtils.getTransport(p.getLocation(), homeLoc);
        } catch (TransportNotFoundException ex) {
            p.sendMessage(ex.getMessage());
            return;
        }
        transport.setPassenger(p);
        double cost = transport.getCost();

        if (plugin.getSettings().isMenuEnable()) {
            TransWarp.createHolder(p);
            new TransChosingMenu(TransWarp.getHolder(p), cost, e.getHomeName(), plugin);
        }
        TransportUtils.transport(transport, plugin);
    }

    @EventHandler
    public void onSpawn(UserTeleportSpawnEvent e) {
        Player p = e.getUser().getBase();
        if (p.hasPermission("transwarp.bypass")) {
            return;
        }
        e.setCancelled(true);
        Location spawnLoc = e.getSpawnLocation();
        Transport transport = null;
        try {
            transport = TransportUtils.getTransport(p.getLocation(), spawnLoc);
        } catch (TransportNotFoundException ex) {
            p.sendMessage(ex.getMessage());
            return;
        }
        transport.setPassenger(p);
        double cost = transport.getCost();

        if (plugin.getSettings().isMenuEnable()) {
            TransWarp.createHolder(p);
            new TransChosingMenu(TransWarp.getHolder(p), cost, "Spawn", plugin);//TODO
        }
        TransportUtils.transport(transport, plugin);
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        InventoryHolder holder = e.getClickedInventory().getHolder();
        if (holder == plugin.getHolder(p)) {
            if (e.getCurrentItem() == null)
                return;
            // Choose menu
            Menu menu = plugin.getMenuHolders().get(p).getMenu();
            menu.handleMenu(e);
        }
    }


    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equals("transwarp")) {
            Player p = e.getPlayer();
            if (!p.isOp()) {
                p.sendMessage("Made by Ansandr");
            }
        }
    }
}