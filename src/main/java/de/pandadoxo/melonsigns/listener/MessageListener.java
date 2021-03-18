// -----------------------
// Coded by Pandadoxo
// on 18.03.2021 at 08:54 
// -----------------------

package de.pandadoxo.melonsigns.listener;

import de.pandadoxo.melonsigns.Main;
import de.pandadoxo.melonsigns.core.Server;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MessageListener implements Listener {

    private static final HashMap<ProxiedPlayer, ServerInfo> connectQueue = new HashMap<>();
    BaseComponent[] full = new ComponentBuilder("§c§oDieser Server ist voll!").create();
    BaseComponent[] kick = new ComponentBuilder("§7Du wurdest gekickt um einem §6Ranghöheren §7Spieler Platz zu machen!").create();
    BaseComponent[] noPerm = new ComponentBuilder("§c§oDu hast keine Berechtigung diesen Server zu betreten!").create();

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase("BungeeCord")) {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        try {
            String channel = in.readUTF();

            if (channel.equalsIgnoreCase("connectrequest")) {
                ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();

                String serverName = in.readUTF(); //Get Server
                ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(serverName);
                if (serverInfo == null) {
                    return;
                }
                connectRequest(player, serverInfo);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onConnected(ServerConnectedEvent event) {
        if (connectQueue.containsKey(event.getPlayer())) {
            if (connectQueue.get(event.getPlayer()).equals(event.getServer().getInfo())) {
                connectQueue.remove(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        connectQueue.remove(event.getPlayer());
    }


    public boolean isNumber(String tocheck) {
        try {
            Integer.parseInt(tocheck);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    public void connectRequest(ProxiedPlayer player, ServerInfo serverInfo) {
        String joinPerm = serverInfo.getName(); //Get Required Permission
        if (joinPerm.split("-").length == 2 && isNumber(joinPerm.split("-")[1])) {
            joinPerm = joinPerm.split("-")[0];
        }

        if (!Main.getDoxperm().has(player, "bungeecord.join." + joinPerm, false)) {
            player.sendMessage(noPerm);
            return;
        }

        Server server = Main.getServerConfig().getServer(serverInfo.getName());

        if (server == null) {
            return;
        }

        int inqueue = 0;
        for (ServerInfo si : connectQueue.values()) {
            if (si.equals(serverInfo)) inqueue++;
        }

        if (serverInfo.getPlayers().size() + inqueue < server.getMaxplayers()) {
            player.connect(serverInfo);
            connectQueue.put(player, serverInfo);
            return;
        }

        if (serverInfo.getPlayers().size() + inqueue >= server.getRealplayers()) {
            player.sendMessage(full);
            return;
        }

        if (server.getStatus().equals(Server.Status.INGAME) || server.isLobby()) {
            player.connect(serverInfo);
            return;
        }

        if (!Main.getDoxperm().has(player, "bungeecord.kick." + joinPerm, false)) {
            player.sendMessage(full);
            return;
        }

        List<ProxiedPlayer> kickable = new ArrayList<>();
        for (ProxiedPlayer pp : serverInfo.getPlayers()) {
            if (!Main.getDoxperm().has(pp, "bungeecord.ignorekick." + joinPerm, false)) {
                kickable.add(pp);
            }
        }

        if (kickable.isEmpty()) {
            player.sendMessage(full);
            return;
        }

        player.connect(serverInfo);
        connectQueue.put(player, serverInfo);
        ProxiedPlayer toKick = kickable.get(new Random().nextInt(kickable.size()));
        Server serverToKick = Main.getServerConfig().getLobby();
        if (serverToKick == null) {
            toKick.disconnect(kick);
        } else {
            ServerInfo serverInfoToKick = ProxyServer.getInstance().getServerInfo(serverToKick.getName());
            if (serverInfoToKick == null) {
                toKick.disconnect(kick);
            } else {
                toKick.connect(ProxyServer.getInstance().getServerInfo(serverToKick.getName()));
                toKick.sendMessage(kick);
            }
        }
    }

}
