package me.ansandr.transwarp.util;

import me.ansandr.transwarp.TransWarp;
import org.bukkit.configuration.ConfigurationSection;

import java.text.MessageFormat;

public class MessageManager {

    private static MessageManager instance;
    private ConfigurationSection messages;
    public String prefix;


    public MessageManager(TransWarp plugin) {
        messages = plugin.getConfig().getConfigurationSection("messages");
        prefix = messages.getString("prefix");
    }

    public void onEnable() {
        instance = this;
    }

    public static String tl(String path, Object... objects) {
        if (instance == null) {
            return  "";
        }

        if (objects.length == 0) {
            return StringUtil.color(instance.get(path));
        } else {
            return StringUtil.color(instance.format(path, objects));
        }
    }

    private String format(String path, Object... objects) {

        String message = get(path);
        MessageFormat messageFormat = new MessageFormat(message);

        messageFormat.format(objects).replace("\u00a0", " ");
        return message;
    }

    public String get(String path) {
        String s = messages.getString(path);

        if (s == null) {
            TransWarp.getInstance().getLogger().warning(String.format("Missing translation key \"%s\" in file config.yml", path));
            return path; //иначе
        }

        return s;
    }
}
