package com.kzh.sys.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JsonUtil {

    private static Gson gson = new Gson();
    private static Gson disableHtmlEscapingGson = null;//不转义html字符

    static {
        GsonBuilder gb =new GsonBuilder();
        gb.disableHtmlEscaping();
        disableHtmlEscapingGson = gb.create();
    }

    public static final String EMPTY_JSON_STRING = "[]";

    public static Map<String, String> jsonToStringMap(String json) {
        if (StringUtils.isBlank(json)) {
            return new HashMap<String, String>();
        }
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> result = gson.fromJson(json, type);
        return result;
    }

    public static <T> T fromJson(String json, Class<T> clazz, Class type) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        Type objectType = type(clazz, type);
        return gson.fromJson(json, objectType);
    }

    public static String objectToJson(Object object) {
        return gson.toJson(object);
    }

    public static String objectToJson(Object object, boolean disableHtmlEscaping) {
        if(disableHtmlEscaping){
            return disableHtmlEscapingGson.toJson(object);
        }else {
            return gson.toJson(object);
        }

    }

    public static <T> T jsonToObject(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)||EMPTY_JSON_STRING.equals(json)) {
            return null;
        }
        return gson.fromJson(json, clazz);
    }

    public static <K, V, T> Map<K, V> jsonToMap(String json, Class<K> classOfK, Class<V> classOfV) {
        Type type = type(Map.class, classOfK, classOfV);
        return gson.fromJson(json, type);
    }
    public static <T> List<T> jsonToList(String jsonStr, Class<T> clazz) {
        Type type = type(List.class, clazz);
        return gson.fromJson(jsonStr, type);
    }

    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }
}
