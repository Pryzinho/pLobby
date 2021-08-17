package br.pryz.lobby.utils.profile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Profile {
    private UUID uuid;
    private String email;
    private String discord;
    private long firstTimeOnline;
    private long lastTimeOnline;
    private StatusType status;

    public Profile(UUID uuid, String email, String discord, long firstTimeOnline, long lastTimeOnline) {
        this.uuid = uuid;
        this.email = email;
        this.discord = discord;
        this.firstTimeOnline = firstTimeOnline;
        this.lastTimeOnline = lastTimeOnline;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType statustype) {
        status = statustype;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getDiscord() {
        return discord;
    }

    public String getEmail() {
        return email;
    }

    public long getFirstTimeOnline() {
        return firstTimeOnline;
    }

    public long getLastTimeOnline() {
        return lastTimeOnline;
    }
}

