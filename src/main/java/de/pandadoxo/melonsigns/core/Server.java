// -----------------------
// Coded by Pandadoxo
// on 18.03.2021 at 08:12 
// -----------------------

package de.pandadoxo.melonsigns.core;

import de.pandadoxo.melonsigns.util.Exclude;
import net.md_5.bungee.api.ChatColor;

public class Server {

    private String name;
    private boolean lobby;
    private int maxplayers;

    @Exclude
    private Status status;
    @Exclude
    private int currplayers;
    @Exclude
    private int realplayers;


    public Server(String name, Status status, boolean lobby, int currplayers, int maxplayers, int realplayers) {
        this.name = name;
        this.status = status;
        this.lobby = lobby;
        this.currplayers = currplayers;
        this.maxplayers = maxplayers;
        this.realplayers = realplayers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isLobby() {
        return lobby;
    }

    public void setLobby(boolean lobby) {
        this.lobby = lobby;
    }

    public int getCurrplayers() {
        return currplayers;
    }

    public void setCurrplayers(int currplayers) {
        this.currplayers = currplayers;
    }

    public int getMaxplayers() {
        return maxplayers;
    }

    public void setMaxplayers(int maxplayers) {
        this.maxplayers = maxplayers;
    }

    public int getRealplayers() {
        return realplayers;
    }

    public void setRealplayers(int realplayers) {
        this.realplayers = realplayers;
    }

    public enum Status {
        ONLINE(ChatColor.GREEN), RESTARTING(ChatColor.RED), OFFLINE(ChatColor.DARK_RED), INGAME(ChatColor.GOLD), STARTING(ChatColor.GOLD);

        private final ChatColor statusColor;

        Status(ChatColor statusColor) {
            this.statusColor = statusColor;
        }

        public ChatColor get() {
            return this.statusColor;
        }
    }
}
