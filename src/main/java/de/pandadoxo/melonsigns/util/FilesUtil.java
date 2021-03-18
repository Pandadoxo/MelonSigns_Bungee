// -----------------------
// Coded by Pandadoxo
// on 17.03.2021 at 20:44 
// -----------------------

package de.pandadoxo.melonsigns.util;

import com.google.gson.stream.JsonReader;
import de.pandadoxo.melonsigns.Main;
import de.pandadoxo.melonsigns.core.ServerConfig;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class FilesUtil {

    private static final File server = new File(Main.getInstance().getDataFolder(), "ServerConfig.json");

    public FilesUtil() {

        create();
        load();
    }

    public void create() {
        try {
            if (!server.exists()) {
                server.getParentFile().mkdirs();
                server.createNewFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            JsonReader serverSignR = new JsonReader(new FileReader(server));
            Main.setServerConfig(JsonUtil.getGson().fromJson(serverSignR, ServerConfig.class));
            if (Main.getServerConfig() == null) Main.setServerConfig(new ServerConfig());
            serverSignR.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            PrintWriter serverSignR = new PrintWriter(server, "UTF-8");
            serverSignR.println(JsonUtil.getGson().toJson(Main.getServerConfig()));
            serverSignR.flush();
            serverSignR.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
