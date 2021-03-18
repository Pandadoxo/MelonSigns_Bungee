package de.pandadoxo.melonsigns;

import de.pandadoxo.melonsigns.commands.SetplayersCmd;
import de.pandadoxo.melonsigns.core.PingTask;
import de.pandadoxo.melonsigns.core.ServerConfig;
import de.pandadoxo.melonsigns.listener.MessageListener;
import de.pandadoxo.melonsigns.util.FilesUtil;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public final class Main extends Plugin {

    public static final String PREFIX = "§7| §f§lMeloneoderso.de §8» §r";

    private static Main instance;
    private static ServerConfig serverConfig;
    private static FilesUtil filesUtil;
    private static Doxperm doxperm;

    private static int task;

    public static Main getInstance() {
        return instance;
    }

    public static ServerConfig getServerConfig() {
        return serverConfig;
    }

    public static void setServerConfig(ServerConfig serverConfig) {
        Main.serverConfig = serverConfig;
    }

    public static FilesUtil getFilesUtil() {
        return filesUtil;
    }

    public static Doxperm getDoxperm() {
        return doxperm;
    }

    @Override
    public void onEnable() {
        instance = this;
        doxperm = new Doxperm(PREFIX);
        serverConfig = new ServerConfig();
        filesUtil = new FilesUtil();


        ProxyServer.getInstance().registerChannel("melone:lobbysign");

        task = ProxyServer.getInstance().getScheduler().schedule(this, new PingTask(), 1, serverConfig.interval, serverConfig.timeUnit).getId();

        getProxy().getPluginManager().registerCommand(this, new SetplayersCmd("setplayers"));
        getProxy().getPluginManager().registerListener(this, new MessageListener());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        filesUtil.save();
    }
}
