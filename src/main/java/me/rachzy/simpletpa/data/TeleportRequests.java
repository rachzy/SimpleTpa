package me.rachzy.simpletpa.data;

import me.rachzy.simpletpa.models.TeleportRequest;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeleportRequests {

    private static final List<TeleportRequest> teleportRequests = new ArrayList<>();

    public static List<TeleportRequest> getTeleportRequests() {
        return teleportRequests;
    }

    public static void create(Player playerSender, Player playerTarget) {
        teleportRequests.add(new TeleportRequest(playerSender, playerTarget));
    }
}
