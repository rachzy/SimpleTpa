package me.rachzy.simpletpa.functions;

import me.rachzy.simpletpa.SimpleTpa;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class getStringFromConfig {
    public static String byName(String key) {
        FileConfiguration config = SimpleTpa.getPlugin(SimpleTpa.class).getConfig();
        return ChatColor.translateAlternateColorCodes('&', config.getString(key));
    }

    public static List<String> byListName(String key) {
        FileConfiguration config = SimpleTpa.getPlugin(SimpleTpa.class).getConfig();

        List<String> listWithColors = new ArrayList<>();
        for(String s : config.getStringList(key)) {
            listWithColors.add(ChatColor.translateAlternateColorCodes('&', s));
        }

        return listWithColors;
    }
}
