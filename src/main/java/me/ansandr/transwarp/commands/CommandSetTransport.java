package me.ansandr.transwarp.commands;

import me.ansandr.transwarp.TransWarp;
import me.ansandr.transwarp.TransportTypeManager;
import me.ansandr.transwarp.storage.StorageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

import static me.ansandr.utils.message.MessageManager.tl;

public class CommandSetTransport implements TabExecutor {

    private final StorageManager storage;
    private final TransWarp plugin;
    private List<String> transportTypes;
    public CommandSetTransport(TransWarp plugin) {
        this.storage = plugin.getStorage();
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(tl("common.for_player"));
            return false;
        }
        Player p = (Player) sender;

        if (args.length == 2) {
            //settrans <transName> <transportType>
            if (!p.isOp()) {
                p.sendMessage(tl("common.no_permissions"));
                return true;
            }
            String transportName = args[0];
            String transportType = args[1];

            if (plugin.getTypeManager().isTypeExist(transportType)) {
                storage.setTransport(transportName, transportType, p.getLocation());
                p.sendMessage(tl("command.transport_set"));
            } else {
                p.sendMessage(tl("error.transport_type_not_initialized"));
            }
            return true;
        }
        //If invalid usage
        sender.sendMessage(tl("common.invalid_usage"));
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2) {
            return plugin.getTypeManager().getTypeMap().keySet().stream().toList();
        }

        return null;
    }
}
