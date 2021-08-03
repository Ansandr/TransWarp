package me.ansandr.transwarp.menu;

import me.ansandr.transwarp.TransWarp;
import me.ansandr.transwarp.model.Transport;
import me.ansandr.transwarp.util.TransportUtils;
import me.ansandr.util.menu.Menu;
import me.ansandr.util.menu.MenuHolder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TransChosingMenu extends Menu {

    private final TransWarp plugin;

    @NotNull
    private Transport transport;

    private String name;
    private double cost;

    public TransChosingMenu(InventoryHolder holder, Transport transport, String targetName, TransWarp plugin) {
        super((MenuHolder) holder);
        this.transport = transport;
        this.cost = transport.getCost();
        this.name = targetName;
        this.plugin = plugin;
    }

    public TransChosingMenu(Player viewer, Transport transport, String targetName, TransWarp plugin) {
        super(viewer);
        this.transport = transport;
        this.cost = transport.getCost();
        this.name = targetName;
        this.plugin = plugin;
    }

    @Override
    public String getTitle() {
        return (name);
    }//TODO локализация + имя варпа в название

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        switch (event.getCurrentItem().getType()) {
            case BARRIER: {
                return;
            }
            case ENDER_EYE: {
                if (p.hasPermission("transwarp.fast")) {
                    transport.teleportToTarget();
                }
                break;
            }
            case COMPASS: {
                if (plugin.getGPSHook().getApi() != null) {
                    plugin.getGPSHook().launchCompass(p, transport.getTargetLocation());
                }
                break;
            }
            case CHEST_MINECART: {
                transport.transport(plugin);
                break;
            }
        }
    }

    @Override
    public void createMenuItems(Player player) {
        ItemStack fast;

        if (player.hasPermission("transwarp.fast")) {
            fast = createItemStack(Material.ENDER_EYE, "Быстрый телепорт");
        } else {
            fast = createItemStack(Material.BARRIER, "НЕДОСТУПНО", "","Купите донат, чтобы открыть возможность телепортации");//TODO локализация
        }

        ItemStack gps = createItemStack(Material.COMPASS, "GPS", "","Нажмите, чтобы использовать навигатор");//TODO локализация

        ItemStack transport = createItemStack(Material.CHEST_MINECART, "Трансопрт",("Цена поезди " + cost),"Нажмите, чтобы приехать на автобусе");//TODO локализация

        addItem(11, fast);
        addItem(13, gps);
        addItem(15, transport);
    }

    @Override
    public ItemStack setFillerItem() {
        return null;
    }
}
