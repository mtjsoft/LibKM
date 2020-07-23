package com.mtjsoft.www.kotlinmvputils.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析数据
 *
 * @author mtj
 */
public class AndModelUtils {
    private static final String tag = AndModelUtils.class.getName();

    /**
     * 解析数据到一个Model中,用Gosn解析方式
     *
     * @param codeName  状态code对应的名称
     * @param codeValue 状态code的值，表示在什么值得时候去解析数据部分，一般情况下是0
     * @param dataName  数据对应的值
     * @param clazz     解析出来的Model
     * @param data      需要解析的数据
     * @return 如果数据为null，返回null；否则的话返回解析出来的Model
     */
    public static <T> T getModel2Gson(String codeName, String codeValue, String dataName, Class<T> clazz, String data) {
        T t = null;
        if (data != null) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                if (codeValue.equals(jsonObject.optString(codeName))) {
                    Gson gson = new Gson();
                    t = gson.fromJson(jsonObject.optString(dataName), clazz);
                }
            } catch (Exception e) {
            }
        }
        return t;
    }

    public static <T> T getModel2Gson(Class<T> clazz, String data) {
        T t = null;
        if (data != null) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                Gson gson = new Gson();
                t = gson.fromJson(jsonObject.toString(), clazz);
            } catch (Exception e) {
            }
        }
        return t;
    }


    /**
     * 解析数据到一个集合,用Gosn解析方式
     *
     * @param codeValue code的值，表示在什么值得时候去解析数据部分，一般情况下是0
     * @param data      需要解析的数据
     * @param codeName  code 对应的名称
     * @param dataName  数据对应的名称
     * @return 如果需要解析的数据是null，返回null；否则返回一个类型为clazz的实例的集合
     */
    public static <T> List<T> getModelList2Gson(String codeName, String codeValue, String dataName, Class<T> clzss, String data) {
        List<T> list = null;
        if (data != null) {
            try {
                list = new ArrayList<T>();
                JSONObject jsonObject = new JSONObject(data);
                if (codeValue.equals(jsonObject.optString(codeName))) {
                    Gson gson = new Gson();
                    list = gson.fromJson(jsonObject.optString(dataName), TypeToken.getParameterized(ArrayList.class, clzss).getType());
                }
            } catch (Exception e) {
            }
        }
        return list;
    }

    public static <T> List<T> getModelList2Gson(Class<T> clzss, String data) {
        List<T> list = null;
        if (data != null) {
            try {
                list = new ArrayList<T>();
                JSONArray jsonObject = new JSONArray(data);
                Gson gson = new Gson();
                list = gson.fromJson(jsonObject.toString(), TypeToken.getParameterized(ArrayList.class, clzss).getType());
            } catch (Exception e) {
            }
        }
        return list;
    }

}
