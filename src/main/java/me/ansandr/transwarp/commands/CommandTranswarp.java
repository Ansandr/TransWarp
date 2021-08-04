package me.ansandr.transwarp.commands;

import me.ansandr.transwarp.TransWarp;
import me.ansandr.transwarp.TransportTypeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static me.ansandr.utils.message.MessageManager.tl;

public class CommandTranswarp implements TabExecutor {

    private final TransWarp plugin;

    public CommandTranswarp(TransWarp plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(tl("common.invalid_usage"));
            return false;
        }
        if (args[0].equals("reload")) {
            if (!sender.isOp()) {
                sender.sendMessage(tl("common.no_permissions"));
                return true;
            }
            plugin.reload();
            sender.sendMessage(tl("common.reloaded"));
            return true;
            }
        if (args[0].equals("list")) {
            if (!sender.isOp()) {
                sender.sendMessage(tl("common.no_permissions"));
                return true;
            }
            sendTransportList((Player) sender);
            return true;
        }
        if (args[0].equals("skip")) {
            if (!sender.hasPermission("transwarp.skip")) {
                sender.sendMessage(tl("common.no_permissions"));
                return true;
            }
            if (!TransWarp.isInTransport((Player) sender)) {
                sender.sendMessage("Вы не в транспорте");
                return true;
            }
            TransWarp.getTransport((Player) sender).cancelTransporting();
        }
        return true;
    }

    private void sendTransportList(Player player) {
        player.sendMessage("Available transports:");
        for (String name : TransportTypeManager.typeMap.keySet()) {
            player.sendMessage(name); //TODO локализация json
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> tabs = new ArrayList<>();
            if (sender.hasPermission("transwarp.skip")) {
                tabs.add("skip");
            }
            if (sender.isOp()) {
                tabs.add("reload");
                tabs.add("list");
            }
        }
        return new ArrayList<>();
    }
}
