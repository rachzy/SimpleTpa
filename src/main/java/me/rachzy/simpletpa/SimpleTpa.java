package me.rachzy.simpletpa;

import me.rachzy.simpletpa.commands.TpAcceptCommand;
import me.rachzy.simpletpa.commands.TpDenyCommand;
import me.rachzy.simpletpa.commands.TpaCommand;
import me.rachzy.simpletpa.functions.getStringFromConfig;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleTpa extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Enabling SimpleTpa plugin...");

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        PluginCommand command = getCommand("tpa");
        command.setExecutor(new TpaCommand(this));
        if (getConfig().getBoolean("requires_use_permission")) {
            command.setPermission("simpletpa.use");
            command.setPermissionMessage(String.join("\n", getStringFromConfig.byName("no_permission_message")));
        }

        command = getCommand("tpaccept");
        command.setExecutor(new TpAcceptCommand());
        if (getConfig().getBoolean("requires_use_permission")) {
            command.setPermission("simpletpa.use");
            command.setPermissionMessage(String.join("\n", getStringFromConfig.byName("no_permission_message")));
        }

        command = getCommand("tpdeny");
        command.setExecutor(new TpDenyCommand());
        if (getConfig().getBoolean("requires_use_permission")) {
            command.setPermission("simpletpa.use");
            command.setPermissionMessage(String.join("\n", getStringFromConfig.byName("no_permission_message")));
        }


    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling SimpleTpa plugin...");
    }
}
