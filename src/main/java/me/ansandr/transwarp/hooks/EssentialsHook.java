package me.ansandr.transwarp.hooks;

import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;

public class EssentialsHook {

    private IEssentials essentials;

    public EssentialsHook() {
        essentials = (IEssentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");

    }

    public IEssentials getEssentials() {
        return essentials;
    }

    public boolean hooked() {
        return essentials != null;
    }
}
