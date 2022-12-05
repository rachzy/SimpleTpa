package me.rachzy.simpletpa.commands;

import me.rachzy.simpletpa.SimpleTpa;
import me.rachzy.simpletpa.data.TeleportRequests;
import me.rachzy.simpletpa.functions.getStringFromConfig;
import me.rachzy.simpletpa.models.TeleportRequest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TpaCommand implements CommandExecutor {

    private final SimpleTpa plugin;

    public TpaCommand(SimpleTpa plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //Check if the command sender is not a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(getStringFromConfig.byName("not_a_player_message"));
            return true;
        }

        Player playerSender = (Player) sender;
        FileConfiguration config = this.plugin.getConfig();

        //Check if there's no target
        if(args.length == 0) {
            sender.sendMessage(getStringFromConfig.byName("wrong_usage_message"));
            return true;
        }

        //Check if the player doesn't already have a teleport request which is not expired
        TeleportRequest getSenderTeleportRequests = TeleportRequests
                .getTeleportRequests()
                .stream()
                .filter(request -> request.getSender() == playerSender)
                .findFirst()
                .orElse(null);

        if(getSenderTeleportRequests != null && !getSenderTeleportRequests.isExpired()) {
            sender.sendMessage(getStringFromConfig.byName("pending_request_message"));
            return true;
        }

        //Check if the player entered his own nickname as target player
        if(playerSender.getDisplayName().equals(args[0])) {
            sender.sendMessage(getStringFromConfig.byName("auto_request_message"));
            return true;
        }

        //Filter every online player in the server and find one with the entered username.
        //If there are no players with that username, return null
        Player playerTarget = playerSender
                .getServer()
                .getOnlinePlayers()
                .stream()
                .filter(player -> player.getDisplayName().equals(args[0]))
                .findFirst()
                .orElse(null);

        //Check if the target player could not be found
        if (playerTarget == null) {
            playerSender.sendMessage(getStringFromConfig.byName("could_not_find_target_message", args[0]));
            return true;
        }

        //Check if the target player has permission to accept teleport requests
        if (!playerTarget.hasPermission("simpletpa.use") && config.getBoolean("requires_use_permission")) {
            playerSender.sendMessage(getStringFromConfig.byName("target_has_no_permission_message", playerTarget.getDisplayName()));
            return true;
        }

        //Create a new teleport request
        TeleportRequests.create(playerSender, playerTarget);

        //Send the confirmation message to the sender
        playerSender.sendMessage(getStringFromConfig.byName("sent_request_message", playerTarget.getDisplayName()));

        //Send the request message to the target
        playerTarget.sendMessage(getStringFromConfig.byName("request_confirmation_message", playerSender.getDisplayName()));

        return true;
    }
}
