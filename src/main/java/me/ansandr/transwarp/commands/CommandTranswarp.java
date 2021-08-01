package me.ansandr.transwarp.commands;

import me.ansandr.transwarp.TransWarp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
                sender.sendMessage("No perm");
                return true;
            }
            plugin.reload();
            sender.sendMessage("Config reloaded!");
            return true;
            }
        if (args[0].equals("list")) {
            if (!sender.isOp()) {//TODO Permission
                sender.sendMessage("No perm");
                return true;
            }
            //TODO show list method
            return true;
        }
        return true;
    }
}
