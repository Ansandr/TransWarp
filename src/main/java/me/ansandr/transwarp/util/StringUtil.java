package me.ansandr.transwarp.util;

import net.md_5.bungee.api.ChatColor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static me.ansandr.transwarp.util.MessageManager.tl;

public class StringUtil {

    private static final Pattern HEX_PATTERN = Pattern.compile("&(#[a-f0-9]{6})", 2);//TODO

    public static String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static List<String> color(List<String> input){
        List<String> clore = new ArrayList<>();
        for(String s : input){
            clore.add(color(s));
        }
        return clore;
    }

    public static String formatDuration(int delay) {
        String time;

        if (delay > 60) {//Разделить по минутам
            int minute = delay/60;
            int second = delay%60;
            if (second == 0) {
                time = String.format("%01d %01s",
                        second,
                        ((second>1) ? tl("timer.seconds") : tl("timer.second")));
            } else {
                time = String.format("%01d %01s %02d %02s",
                        minute,
                        ((minute>1) ? tl("timer.minutes") : tl("timer.minute")),
                        second,
                        ((second>1) ? tl("timer.seconds") : tl("timer.second")));
            }
        } else {
            int second = delay;
            time = String.format("%01d %01s",
                    second,
                    ((second>1) ? tl("timer.seconds") : tl("timer.second")));
        }
        return time;
    }
}
