package me.ansandr.transwarp.menu;

import me.ansandr.transwarp.TransWarp;
import me.ansandr.transwarp.model.Transport;
import me.ansandr.utils.menu.Menu;
import me.ansandr.utils.menu.MenuHolder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static me.ansandr.utils.message.MessageManager.tl;
import static me.ansandr.utils.message.MessageManager.tla;

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
    }

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
                    holder.getViewer().closeInventory();
                }
                break;
            }
            case CHEST_MINECART: {
                holder.getViewer().closeInventory();
                transport.transport(plugin);
                break;
            }
        }
    }

    @Override
    public void createMenuItems(Player player) {
        ItemStack fast;

        if (player.hasPermission("transwarp.fast")) {
            fast = createItemStack(Material.ENDER_EYE, tl("menu.item_title.fast"), tla("menu.lore.fast"));
        } else {
            fast = createItemStack(Material.BARRIER, tl("menu.item_title.fast_lock"), tla("menu.lore.fast_lock"));
        }
        ItemStack gps;
        if (plugin.getGPSHook() != null) {
            gps = createItemStack(Material.COMPASS, tl("menu.item_title.gps"), tla("menu.lore.gps"));
        } else {
            gps = null;//TODO предмет "пешком"
        }

        ItemStack transport = createItemStack(Material.CHEST_MINECART, tl("menu.item_title.transport"), format("menu.item_title.transport", cost));

        addItem(11, fast);
        addItem(13, gps);
        addItem(15, transport);
    }

    @Override
    public ItemStack setFillerItem() {
        return null;
    }

    private static String[] format(String path, double cost) {
        String[] array = tla("menu.lore.transport");
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].replace("{cost}", String.valueOf(cost));
        }
        return array;
    }
}
