// -----------------------
// Coded by Pandadoxo
// on 18.03.2021 at 08:38 
// -----------------------

package de.pandadoxo.melonsigns.core;

import de.pandadoxo.melonsigns.Main;
import de.pandadoxo.melonsigns.util.JsonUtil;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PingTask implements Runnable {

    private final Map<String, ServerPinger> pingers = new HashMap<>();

    @Override
    public void run() {
        for (Map.Entry<String, ServerInfo> entry : ProxyServer.getInstance().getServers().entrySet()) {
            if (!pingers.containsKey(entry.getKey())) {
                pingers.put(entry.getKey(), new ServerPinger(entry.getValue()));
            }
        }

        for (ServerPinger pinger : pingers.values()) {
            pinger.ping(2000);
        }

        for (Server server : Main.getServerConfig().servers) {
            if (!server.isLobby()) {
                continue;
            }
            if (ProxyServer.getInstance().getServerInfo(server.getName()).getPlayers().size() <= 0) {
                continue;
            }
            ProxyServer.getInstance().getServerInfo(server.getName()).sendData("melone:lobbysign",
                    JsonUtil.getServerGson().toJson(Main.getServerConfig().servers.stream().filter(s -> s.getStatus() != null).collect(Collectors.toList())).getBytes(StandardCharsets.UTF_8));
        }
    }
}
