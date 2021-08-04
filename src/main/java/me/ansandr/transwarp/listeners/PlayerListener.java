package me.ansandr.transwarp.listeners;

import com.earth2me.essentials.commands.WarpNotFoundException;
import me.ansandr.transwarp.TransWarp;
import me.ansandr.transwarp.menu.TransChosingMenu;
import me.ansandr.transwarp.model.Transport;
import me.ansandr.transwarp.util.TransportNotFoundException;
import me.ansandr.transwarp.util.TransportUtils;
import me.ansandr.utils.menu.Menu;
import me.ansandr.utils.menu.MenuHolder;
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

import static me.ansandr.utils.message.MessageManager.tl;

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
        Player p = e.getPlayer();
        if (TransWarp.isInTransport(p)) {
            p.teleport(TransWarp.getTransport(e.getPlayer()).getStartLocation());
            TransWarp.removeTransport(p);
        }
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
        Location warpLoc;
        try {
            warpLoc = ess.getWarps().getWarp(warpName);
        } catch (WarpNotFoundException ex) {
            p.sendMessage(ex.getMessage());
            return;
        }
        doTransport(p, warpLoc, tl("transport_to_warp").replace("{warp}", warpName));
    }

    @EventHandler
    public void onHomeTeleport(UserTeleportHomeEvent e) {
        Player p = e.getUser().getBase();
        if (p.hasPermission("transwarp.bypass")) {
            return;
        }
        e.setCancelled(true);
        Location homeLoc = e.getHomeLocation();
        doTransport(p, homeLoc, tl("transport_to_home").replace("{home}", e.getHomeName()));
    }

    @EventHandler
    public void onSpawn(UserTeleportSpawnEvent e) {
        Player p = e.getUser().getBase();
        if (p.hasPermission("transwarp.bypass")) {
            return;
        }
        e.setCancelled(true);
        Location spawnLoc = e.getSpawnLocation();
        doTransport(p, spawnLoc, tl("transport_to_spawn").replace("{spawnGroup}", e.getSpawnGroup()));
    }

    /**
     *
     * @param player
     * @param targetLoc
     * @param title for menu
     */
    private void doTransport(Player player, Location targetLoc, String title) {
        Transport transport = null;
        try {
            transport = TransportUtils.getTransport(player, targetLoc);
        } catch (TransportNotFoundException ex) {
            player.sendMessage(ex.getMessage());
            return;
        }

        TransWarp.putTransport(player, transport);
        if (plugin.getSettings().isMenuEnable()) {
            MenuHolder holder = TransWarp.createHolder(player);
            TransChosingMenu menu = new TransChosingMenu(holder, transport, title, plugin);
            menu.createMenuItems(player);
            menu.open();
            return;
        }
        transport.transport(plugin);
    }

    /**
     *
     */
    private void doTransport(Player player, Location targetLoc) {
        doTransport(player, targetLoc, null);
    }


    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        InventoryHolder holder = e.getClickedInventory().getHolder();
        if (holder == plugin.getHolder(p)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            // Choose menu
            Menu menu = plugin.getHolder(p).getMenu();
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