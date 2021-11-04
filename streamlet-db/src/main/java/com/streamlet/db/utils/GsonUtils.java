package com.streamlet.db.utils;

import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Linxz
 * 创建日期：2021年11月02日 6:55 下午
 * version：
 * 描述：
 */
public class GsonUtils {

    public static <T> List<T> json2List(String jsonStr, Class<T> tC) {
        if(TextUtils.isEmpty(jsonStr)) return null;
         List<T> listT = null;
        try {
            T t =  tC.newInstance();
            Class<T> classT = (Class<T>) Class.forName(t.getClass().getName());
            List<Object> listObj;
            listObj = new GsonBuilder().create().fromJson(jsonStr, new TypeToken<List<Object>>(){}.getType());
            if(listObj == null || listObj.isEmpty()) return null;
            listT = new ArrayList<>();
            for (Object obj : listObj) {
                T perT = new GsonBuilder().create().fromJson(obj.toString(), classT);
                listT.add(perT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listT;
    }

} 