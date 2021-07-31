package me.ansandr.transwarp.task;

import me.ansandr.transwarp.util.StringUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static me.ansandr.transwarp.util.MessageManager.tl;

public class TrasportingTask extends BukkitRunnable {

    private Player player;
    private Location targetLoc;
    private int delay;

    public TrasportingTask(Player player, Location targetLoc, int delay) {
        this.player = player;
        this.targetLoc = targetLoc;
        this.delay = delay;
    }

    @Override
    public void run() {
        if (delay == 0) {
            end();
            this.cancel();
            return;
        }

        String time = StringUtil.formatDuration(delay);

        player.sendTitle(tl("time_left_title").replace("{time}", time)
                , tl("time_left_subtitle").replace("{time}", time), 5, 20, 5);

        delay--;
    }

    private void end() {//Тп на варв
        player.teleport(targetLoc);
        player.sendTitle(tl("got_title"), tl("got_subtitle"), 10, 300, 30);
        player.sendMessage(tl("you_got"));
    }


}
