package me.rachzy.simpletpa.data;

import me.rachzy.simpletpa.models.TeleportRequest;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeleportRequests {
    private static List<TeleportRequest> TeleportRequestsList = new ArrayList<TeleportRequest>();

    public static List<TeleportRequest> getTeleportRequests() {
        return TeleportRequestsList;
    }
    public static void create(Player playerSender, Player playerTarget) {
        TeleportRequestsList.add(new TeleportRequest(playerSender, playerTarget));
    }
}
