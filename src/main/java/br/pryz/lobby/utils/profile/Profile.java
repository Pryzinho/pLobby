package br.pryz.lobby.utils.profile;

import org.bukkit.entity.Player;

public class Profile {
    private Player player; //SQL > String
    private String email;
    private String discord;
    private long firstTimeOnline;
    private long lastTimeOnline;
    private StatusType status;

    public Profile(Player player, String email, String discord, long firstTimeOnline, long lastTimeOnline) {
        this.player = player;
        this.email = email;
        this.discord = discord;
        this.firstTimeOnline = firstTimeOnline;
        this.lastTimeOnline = lastTimeOnline;
    }

    public Player getPlayer() {
        return player;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType statustype) {
        status = statustype;
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

