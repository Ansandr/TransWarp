package me.ansandr.transwarp.model.task;

import me.ansandr.transwarp.model.Transport;
import me.ansandr.transwarp.util.StringUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;

import static me.ansandr.utils.message.MessageManager.tl;

public class TransportingTask extends BukkitRunnable {

    private Transport transport;
    private Player player;
    private int delay;

    public TransportingTask(Transport transport) {
        this.transport = transport;
        this.delay = transport.getTime();
        this.player = transport.getPassenger();
    }

    @Override
    public void run() {
        if (delay == 0) {
            end();
            this.cancel();
            return;
        }

        String time = StringUtil.formatDuration(delay);

        player.sendTitle(tl("titles.time_left_title").replace("{time}", time)
                , tl("titles.time_left_subtitle").replace("{time}", time), 5, 20, 5);

        delay--;
    }

    private void end() {//Тп на варв
        transport.teleportToTarget();
        player.sendTitle(tl("titles.got_title"), tl("titles.got_subtitle"), 10, 300, 30);
        player.sendMessage(tl("transport.you_got"));
    }


}
