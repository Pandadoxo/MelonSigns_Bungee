// -----------------------
// Coded by Pandadoxo
// on 18.03.2021 at 08:12 
// -----------------------

package de.pandadoxo.melonsigns.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ServerConfig {

    public List<Server> servers = new ArrayList<>();
    public int interval = 1;
    public TimeUnit timeUnit = TimeUnit.SECONDS;

    public Server getServer(String name) {
        for (Server s : servers) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }

    public Server getLobby() {
        for (Server s : servers) {
            if (s.isLobby()) {
                return s;
            }
        }
        return null;
    }

}
