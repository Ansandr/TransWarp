package me.ansandr.transwarp.commands;

import me.ansandr.transwarp.TransWarp;
import me.ansandr.transwarp.TransportTypeManager;
import me.ansandr.transwarp.model.Transport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.earth2me.essentials.I18n.tl;

public class CommandTranswarp implements CommandExecutor {

    private final TransWarp plugin;

    public CommandTranswarp(TransWarp plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Incorrect usage");
            return false;
        }
        if (args[0].equals("reload")) {
            if (!sender.isOp()) {//TODO Permission
                sender.sendMessage(tl("no_perm"));
                return true;
            }
            plugin.reload();
            sender.sendMessage(tl("reloaded"));
            return true;
            }
        if (args[0].equals("list")) {
            if (!sender.isOp()) {//TODO Permission
                sender.sendMessage(tl("no_perm"));
                return true;
            }
            sendTransportList((Player) sender);
            return true;
        }
        if (args[0].equals("skip")) {
            if (!sender.hasPermission("transwarp.skip")) {
                sender.sendMessage(tl("no_perm"));
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
}
