package me.rachzy.simpletpa;

import me.rachzy.simpletpa.commands.TpaAcceptCommand;
import me.rachzy.simpletpa.commands.TpaCommand;
import me.rachzy.simpletpa.commands.TpaDenyCommand;
import me.rachzy.simpletpa.models.TeleportRequest;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class SimpleTpa extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Enabling SimpleTpa plugin...");

        getCommand("tpa").setExecutor(new TpaCommand());
        getCommand("tpaccept").setExecutor(new TpaAcceptCommand());
        getCommand("tpdeny").setExecutor(new TpaDenyCommand());
    }

    @Override
    public void onDisable() {
        System.out.println("Disabling SimpleTpa plugin...");
    }
}
