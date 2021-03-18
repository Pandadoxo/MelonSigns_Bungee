// -----------------------
// Coded by Pandadoxo
// on 18.03.2021 at 08:28 
// -----------------------

package de.pandadoxo.melonsigns.core;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.pandadoxo.melonsigns.Main;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerPinger {

    private static final List<UUID> cancel = new ArrayList<>();
    private final ServerInfo serverInfo;
    private Server server;
    private long lastOnline = System.currentTimeMillis();

    public ServerPinger(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
        this.server = Main.getServerConfig().getServer(serverInfo.getName());
    }

    public Server ping(long timeout) {
        if (this.server == null) {
            if ((this.server = Main.getServerConfig().getServer(serverInfo.getName())) == null) {
                Main.getServerConfig().servers.add((this.server = new Server(this.serverInfo.getName(), Server.Status.OFFLINE, false, 0, 0, 0)));
            }
        }
        UUID request = UUID.randomUUID();
        this.serverInfo.ping((result, error) -> {
            if (cancel.contains(request)) {
                return;
            }

            if (error == null) {
                //Set Players
                this.server.setCurrplayers(result.getPlayers().getOnline());
                this.server.setRealplayers(result.getPlayers().getMax());
                if (this.server.getMaxplayers() == 0) this.server.setMaxplayers(result.getPlayers().getMax());

                this.lastOnline = System.currentTimeMillis();
                try {
                    //Get Status
                    JsonObject statusJson = new Gson().fromJson(result.getDescriptionComponent().toPlainText(), JsonObject.class);
                    if (statusJson.has("status")) {
                        String status = statusJson.get("status").getAsString();
                        if (!status.equalsIgnoreCase("ingame") && !status.equalsIgnoreCase("starting")
                                && !status.equalsIgnoreCase("online") && !status.equalsIgnoreCase("restarting")) {
                            this.server.setStatus(Server.Status.ONLINE);
                        } else {
                            this.server.setStatus(Server.Status.valueOf(status.toUpperCase()));
                        }
                    }
                } catch (Exception e) {
                    server.setStatus(Server.Status.ONLINE);
                }
            } else {
                //Set Players
                this.server.setCurrplayers(0);
                this.server.setRealplayers(0);

                //Restarting or Offline?
                if ((System.currentTimeMillis() - lastOnline) < (1000 * 60 * 5)) {
                    server.setStatus(Server.Status.RESTARTING);
                } else {
                    server.setStatus(Server.Status.OFFLINE);
                }
            }
            synchronized (this) {
                this.notifyAll();
            }
        });
        synchronized (this) {
            try {
                this.wait(timeout);
            } catch (InterruptedException ignored) {
            }
        }
        cancel.add(request);
        return server;
    }
}
