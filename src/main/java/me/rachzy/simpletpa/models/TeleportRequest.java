package me.rachzy.simpletpa.models;

import me.rachzy.simpletpa.data.TeleportRequests;
import me.rachzy.simpletpa.functions.getStringFromConfig;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class TeleportRequest {
    Integer teleportId;
    Player playerSender;
    Player playerTarget;
    LocalDateTime expireDate;
    Boolean accepted;

    //Separate thread that that will check if the request got expired
    //and send a message to the sender if it is
    Thread expirationListener = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                //Delay of 2.05 seconds before checking
                Thread.sleep(120005);
                if (isExpired()) {
                    playerSender.sendMessage(getStringFromConfig.byName("sender_expired_teleport_request"));
                    playerTarget.sendMessage(getStringFromConfig.byName("target_expired_teleport_request", playerTarget.getDisplayName()));
                }
                interruptExpirationListener();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    });

    public TeleportRequest(Player playerSender, Player playerTarget) {
        this.teleportId = new Random().nextInt(999999);
        this.playerSender = playerSender;
        this.playerTarget = playerTarget;
        this.expireDate = LocalDateTime.now().plus(2, ChronoUnit.MINUTES);
        this.accepted = false;

        triggerExpirationListener();
    }

    //Function
    public void triggerExpirationListener() {
        expirationListener.start();
    }

    //Function to interrupt the listener
    public void interruptExpirationListener() {
        expirationListener.interrupt();
    }

    public Player getSender() {
        return playerSender;
    }

    public String getTargetUsername() {
        return playerTarget.getDisplayName();
    }

    public Boolean isExpired() {
        return LocalDateTime.now().isAfter(expireDate) && !this.accepted;
    }

    public void setAccepted(Boolean acceptedValue) {
        accepted = acceptedValue;

        //Interrupts the thread
        expirationListener.interrupt();

        //Remove the request from the collection of teleport requests
        TeleportRequests
                .getTeleportRequests()
                .removeIf(request -> request.teleportId.equals(this.teleportId));

    }
}
