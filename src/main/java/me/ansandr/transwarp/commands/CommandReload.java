package me.ansandr.transwarp.commands;

import me.ansandr.transwarp.TransWarp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandReload implements CommandExecutor {

    private final TransWarp plugin;

    public CommandReload(TransWarp plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (args[0].equals("reload")) {
                if (sender.isOp()) {
                    plugin.onReload();
                    sender.sendMessage("Config reloaded!");
                    return true;
                }
                sender.sendMessage("No perm");
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            sender.sendMessage("Incorrect usage");
            return false;
        }

        return false;
    }
}
