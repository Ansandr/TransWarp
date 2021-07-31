package me.ansandr.transwarp.hooks;

import com.live.bemmamin.gps.api.GPSAPI;
import me.ansandr.transwarp.TransWarp;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GPSHook {

    private GPSAPI gpsapi;

    public GPSHook(TransWarp plugin) {
        gpsapi = new GPSAPI(plugin);
    }

    public GPSAPI getApi() {
        return gpsapi;
    }

    public boolean hooked() {
        return gpsapi != null;
    }

    public boolean launchCompass(Player player, Location location) {
        if (getApi().gpsIsActive(player)) return false;
        gpsapi.startCompass(player, location);
        return true;
    }

    public void stopCompass(Player player) {
        gpsapi.stopGPS(player);
    }

}
