package me.rachzy.simpletpa.commands;

import me.rachzy.simpletpa.data.TeleportRequests;
import me.rachzy.simpletpa.functions.getStringFromConfig;
import me.rachzy.simpletpa.models.TeleportRequest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpDenyCommand implements CommandExecutor {

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
        TeleportRequest senderTeleportRequest = TeleportRequests.getTeleportRequests().stream().filter(request -> request.getTargetUsername().equals(playerSender.getDisplayName())).findFirst().orElse(null);

        //Check if there is a Teleport Request for the player
        if (senderTeleportRequest == null) {
            playerSender.sendMessage(getStringFromConfig.byName("no_requests_message"));
            return true;
        }

        //Set the request as not accepted
        senderTeleportRequest.setAccepted(false);

        Player playerRequester = senderTeleportRequest.getSender();

        //Sends the confirmation message for both players
        playerSender.sendMessage(getStringFromConfig.byName("target_declined_request_message", playerRequester.getDisplayName()));
        playerRequester.sendMessage(getStringFromConfig.byName("sender_declined_request_message", playerSender.getDisplayName()));

        return true;
    }
}
