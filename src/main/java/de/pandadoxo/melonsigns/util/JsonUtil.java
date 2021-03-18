// -----------------------
// Author ~ IllgiLP
// Rewritten by Pandadoxo
// on 16.08.2020 at 16:40 
// -----------------------

package de.pandadoxo.melonsigns.util;

import com.google.gson.*;
import de.pandadoxo.melonsigns.core.Server;

public class JsonUtil {

    private static final Gson gson;
    private static final Gson serverGson;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getAnnotation(Exclude.class) != null;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });

        gson = gsonBuilder.create();
        gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        gsonBuilder.registerTypeAdapter(Server.class, (JsonSerializer<Server>) (server, type, jsonSerializationContext) -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", server.getName());
            jsonObject.addProperty("status", server.getStatus().toString());
            jsonObject.addProperty("currplayers", server.getCurrplayers());
            jsonObject.addProperty("maxplayers", server.getMaxplayers());
            return jsonObject;
        });

        serverGson = gsonBuilder.create();

    }

    public static Gson getGson() {
        return gson;
    }

    public static Gson getServerGson() {
        return serverGson;
    }
}
