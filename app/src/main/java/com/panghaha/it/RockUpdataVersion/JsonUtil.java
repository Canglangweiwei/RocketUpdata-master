package com.panghaha.it.RockUpdataVersion;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 封装的是使用Gson解析json的方法
 *
 * @author tzy
 */
@SuppressWarnings("ALL")
public class JsonUtil {

    /**
     * 把一个map变成json字符串
     */
    public static String parseMapToJson(Map<?, ?> map) {
        try {
            Gson gson = new Gson();
            return gson.toJson(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把一个json字符串变成对象
     */
    public static <T> T parseJsonToBean(String json, Class<T> cls) {
        Gson gson = new Gson();
        T t = null;
        try {
            String data = json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1);
            t = gson.fromJson(data, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 把json字符串变成map
     */
    public static HashMap<String, Object> parseJsonToMap(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();

        HashMap<String, Object> map = null;
        try {
            String data = json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1);
            map = gson.fromJson(data, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 把json字符串变成集合
     * params: new TypeToken<List<yourbean>>(){}.getType(),
     */
    public static List<?> parseJsonToList(String json, Type type) {
        Gson gson = new Gson();
        String data = json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1);
        return gson.fromJson(data, type);
    }

    /**
     * 获取json串中某个字段的值，注意，只能获取同一层级的value
     */
    public static String getFieldValue(String json, String key) {
        if (TextUtils.isEmpty(json))
            return null;
        if (!json.contains(key))
            return "";
        JSONObject jsonObject = null;
        String value = null;
        try {
            String data = json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1);
            jsonObject = new JSONObject(data);
            value = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }
}
