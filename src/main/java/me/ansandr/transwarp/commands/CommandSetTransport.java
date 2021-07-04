package me.ansandr.transwarp.commands;

import me.ansandr.transwarp.TransWarp;
import me.ansandr.transwarp.TransportTypeManager;
import me.ansandr.transwarp.storage.StorageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

import static me.ansandr.transwarp.util.MessageManager.tl;

public class CommandSetTransport implements TabExecutor {

    private final StorageManager storage;
    private List<String> transportTypes;
    public CommandSetTransport(TransWarp plugin) {
        this.storage = plugin.getStorage();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only for player");
            return false;
        }
        Player p = (Player) sender;

        try {
            if (p.isOp()) {
                //settrans <transName> <transportType>
                String transportName = args[0];
                String transportType = args[1];

                if (TransportTypeManager.isTypeExist(transportType)) {
                    storage.setTransport(transportName, transportType, p.getLocation());
                    p.sendMessage(tl("transport_set"));
                } else {
                    p.sendMessage(tl("transport_type_not_initialized"));
                }

                return true;
            }
            p.sendMessage(tl("no_perm"));

        } catch(IndexOutOfBoundsException ex) {
            p.sendMessage("Incorrect usage");
            //TODO sendHelp
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2) {
            return TransportTypeManager.typeList;
        }

        return null;
    }
}
