package me.rachzy.simpletpa.functions;

import com.avaje.ebean.validation.NotNull;
import me.rachzy.simpletpa.SimpleTpa;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;

public class getStringFromConfig {
    public static @NotNull String @NotNull [] byName(String key, Object... values) {
        FileConfiguration config = SimpleTpa.getPlugin(SimpleTpa.class).getConfig();

        Object obj = config.get(key);
        if (obj == null) {
            return new String[]{"Missing key: " + key};
        }

        String[] lines;
        if (obj instanceof List) {
            List<String> stringList = config.getStringList(key);
            lines = stringList.toArray(new String[0]);
        } else {
            String message = obj.toString();
            lines = message.split("\n");
        }

        return Arrays.stream(lines).map(s -> String.format(s, values)).map(s -> ChatColor.translateAlternateColorCodes('&', s)).toArray(String[]::new);
    }

}
