package me.rachzy.simpletpa.commands;

import me.rachzy.simpletpa.data.TeleportRequests;
import me.rachzy.simpletpa.models.TeleportRequest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaAcceptCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //Check if the command sender is not a player
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can execute this command!");
            return true;
        }

        Player playerSender = (Player) sender;

        //Check if the player doesn't have permission to execute the command
        if(!playerSender.hasPermission("simpletpa.use")) {
            playerSender.sendMessage("§cYou don't have permission to use the teleport request system.");
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
            playerSender.sendMessage("§cThere are no teleport requests for you!");
            return true;
        }

        Player playerRequester = senderTeleportRequest.getSender();

        //Teleports the requester player to the target
        playerRequester.teleport(playerSender.getLocation());

        //Set the teleport request as accepted
        senderTeleportRequest.setAccepted(true);

        //Send the confirmation messages to both players
        playerSender.sendMessage(String.format("§eYou've accepted a teleport request from §7%s.", playerRequester.getDisplayName()));
        playerRequester.sendMessage(String.format("§a%s §eaccepted your teleport request.", playerSender.getDisplayName()));

        return true;
    }
}
