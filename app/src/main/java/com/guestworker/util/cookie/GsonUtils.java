package com.guestworker.util.cookie;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * @author 莫小婷
 * @create 2019/4/19
 * @Describe
 */
public class GsonUtils {

    private static final Gson gson = new Gson();

    public static <T> T fromJson(String json, TypeToken<T> typeToken) {
        try {
            return gson.fromJson(json, typeToken.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> String toJson(Object object, TypeToken<T> typeToken) {
        return gson.toJson(object, typeToken.getType());
    }

}
