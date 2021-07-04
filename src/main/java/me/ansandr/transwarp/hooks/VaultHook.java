package me.ansandr.transwarp.hooks;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {

    private Economy econ;

    public VaultHook() {
        this.econ = null;
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            this.econ = (Economy) rsp.getProvider();
        }
    }

    public boolean hasEnough(Player p, double amount) {
        return econ != null && econ.has((OfflinePlayer) p, amount);
    }

    public void takeMoney(Player p, double amount) {
        if (econ != null) {
            econ.withdrawPlayer((OfflinePlayer) p, amount);
        }
    }

    public void giveMoney(Player p, double amount) {
        if (econ != null) {
            econ.depositPlayer((OfflinePlayer) p, amount);
        }
    }

    public boolean hooked() {
        return this.econ != null;
    }

    public Economy getEconomy() {
        return econ;
    }
}
