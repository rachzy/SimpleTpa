package me.rachzy.simpletpa.commands;

import me.rachzy.simpletpa.data.TeleportRequests;
import me.rachzy.simpletpa.models.TeleportRequest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaCommand implements CommandExecutor {
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

        //Check if there's no target
        if(args.length == 0) {
            sender.sendMessage("§cUse: /tpa (:player_name)");
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
            sender.sendMessage("§cYou already have a pending teleport request.");
            return true;
        }

        //Check if the player entered his own nickname as target player
        if(playerSender.getDisplayName().equals(args[0])) {
            sender.sendMessage("§cYou can't send a teleport request for yourself.");
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
        if(playerTarget == null) {
            playerSender.sendMessage(String.format("§cCouldn't find an online player with the nickname: %s", args[0]));
            return true;
        }

        //Check if the target player has permission to accept teleport requests
        if(!playerTarget.hasPermission("simpletpa.use")) {
            playerSender.sendMessage(String.format("§c%s doesn't have permission to use the teleport request system.", playerTarget.getDisplayName()));
            return true;
        }

        //Create a new teleport request
        TeleportRequests.create(playerSender, playerTarget);

        //Send the confirm message to the sender
        playerSender.sendMessage(String.format("§7You've sent a teleport request to §a%s", playerTarget.getDisplayName()));

        //Send the request message to the target
        playerTarget.sendMessage(String.format("§eYou've received a teleport request from §b%s", playerSender.getDisplayName()));
        playerTarget.sendMessage("§eUse §a/tpaccept §eto accept.");
        playerTarget.sendMessage("§eUse §c/tpdeny §eto deny.");

        return true;
    }
}
