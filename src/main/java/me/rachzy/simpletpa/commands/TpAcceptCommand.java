package me.rachzy.simpletpa.commands;

import me.rachzy.simpletpa.data.TeleportRequests;
import me.rachzy.simpletpa.functions.getStringFromConfig;
import me.rachzy.simpletpa.models.TeleportRequest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpAcceptCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //Check if the command sender is not a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(getStringFromConfig.byName("not_a_player_message"));
            return true;
        }

        Player playerSender = (Player) sender;

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
        playerSender.sendMessage(getStringFromConfig.byName("target_accepted_request_message", playerRequester.getDisplayName()));
        playerRequester.sendMessage(getStringFromConfig.byName("sender_accepted_request_message", playerSender.getDisplayName()));

        return true;
    }
}
