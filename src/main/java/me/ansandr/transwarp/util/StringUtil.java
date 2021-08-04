package me.ansandr.transwarp.util;

import static me.ansandr.utils.message.MessageManager.tl;

public class StringUtil {

    public static String formatDuration(int delay) {
        String time;

        if (delay > 60) {//Разделить по минутам
            int minute = delay/60;
            int second = delay%60;
            if (second == 0) {
                time = String.format("%d %s",
                        second,
                        ((second>1) ? tl("timer.seconds") : tl("timer.second")));
            } else {
                time = String.format("%d %s %d %s",
                        minute,
                        ((minute>1) ? tl("timer.minutes") : tl("timer.minute")),
                        second,
                        ((second>1) ? tl("timer.seconds") : tl("timer.second")));
            }
        } else {
            int second = delay;
            time = String.format("%d %s",
                    second,
                    ((second>1) ? tl("timer.seconds") : tl("timer.second")));
        }
        return time;
    }
}
