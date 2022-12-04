package me.rachzy.simpletpa.commands;

import me.rachzy.simpletpa.SimpleTpa;
import me.rachzy.simpletpa.data.TeleportRequests;
import me.rachzy.simpletpa.models.TeleportRequest;
import me.rachzy.simpletpa.functions.getStringFromConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TpaAcceptCommand implements CommandExecutor {

    FileConfiguration config = SimpleTpa.getPlugin(SimpleTpa.class).getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //Check if the command sender is not a player
        if(!(sender instanceof Player)) {
            sender.sendMessage(getStringFromConfig.byName("not_a_player_message"));
            return true;
        }

        Player playerSender = (Player) sender;

        //Check if the player doesn't have permission to execute the command
        if(!playerSender.hasPermission("simpletpa.use") && config.getBoolean("requires_use_permission")) {
            playerSender.sendMessage(getStringFromConfig.byName("no_permission_message"));
            return true;
        }

        //Get the sender teleport request
        //If there's not one, return null
        TeleportRequest senderTeleportRequest = TeleportRequests
                .getTeleportRequests()
                .stream()
                .filter(request -> request.getTargetUsername().equals(playerSender.getDisplayName()))
                .findFirst()
                .orElse(null);

        //Check if there is a Teleport Request for the player
        if(senderTeleportRequest == null) {
            playerSender.sendMessage(getStringFromConfig.byName("no_requests_message"));
            return true;
        }

        Player playerRequester = senderTeleportRequest.getSender();

        //Teleports the requester player to the target
        playerRequester.teleport(playerSender.getLocation());

        //Set the teleport request as accepted
        senderTeleportRequest.setAccepted(true);

        //Send the confirmation messages to both players
        playerSender.sendMessage(String.format(getStringFromConfig.byName("target_accepted_request_message"), playerRequester.getDisplayName()));
        playerRequester.sendMessage(String.format(getStringFromConfig.byName("sender_accepted_request_message"), playerSender.getDisplayName()));

        return true;
    }
}
