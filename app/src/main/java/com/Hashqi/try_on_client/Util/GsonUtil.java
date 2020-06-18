package com.Hashqi.try_on_client.Util;

import com.Hashqi.try_on_client.Page.messagePage.Contacts;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GsonUtil {
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    public GsonUtil() {
    }

    public static String BeanToJson(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }

    public static <T> T GsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    public static <T> List<T> GsonToList(String gsonString, Class<T> cls) {
        List<T> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }
    public static List<Contacts> GsonToContactsList(String gsonString) {
        List<Contacts> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString, new TypeToken<List<Contacts>>() {
            }.getType());
        }
        return list;
    }

    public <T> List<T> jsonToList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }


    public static <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    public static Map<String, String> GsonToMaps(String gsonString) {
        Map<String, String> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, String>>() {
            }.getType());
        }
        return map;
    }

    public static HttpAttribute GsonToAttribute(String gsonString) {
        HttpAttribute attribute = new HttpAttribute();
        if(gson != null) {
            Map<String, String> maps = GsonToMaps(gsonString);
            attribute.setCode(Integer.valueOf(maps.get("code")));
            attribute.setMsg(maps.get("msg"));
            attribute.setData(maps.get("data"));
        }
        return attribute;
    }
}