package me.alvsch.reporting.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtils {

    public static void add(JsonObject jsonObject, String property, JsonElement jsonElement) {
        jsonObject.add(property, jsonElement);
    }
    public static void addProperty(JsonObject jsonObject, String property, String object) {
        jsonObject.addProperty(property, object);
    }
    public static void addProperty(JsonObject jsonObject, String property, int object) {
        jsonObject.addProperty(property, object);
    }
    public static void addProperty(JsonObject jsonObject, String property, boolean object) {
            jsonObject.addProperty(property, object);
    }

    public static void removeProperty(JsonObject jsonObject, String property) {
        jsonObject.remove(property);
    }

    public static JsonElement getProperty(JsonObject jsonObject, String memberName) {
        return jsonObject.get(memberName);
    }

    public static boolean exists(JsonObject jsonObject, String memberName) {
        return jsonObject.has(memberName);
    }


}
