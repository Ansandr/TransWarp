package me.ansandr.transwarp.hooks;

import com.live.bemmamin.gps.api.GPSAPI;
import me.ansandr.transwarp.TransWarp;

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
}
