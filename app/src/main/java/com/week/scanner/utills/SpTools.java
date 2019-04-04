package com.week.scanner.utills;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;
//将存取缓存信息的类封装一下
public class SpTools {
    public static String Get(Activity activity, String key){
        SharedPreferences sp=activity.getSharedPreferences("User", Context.MODE_PRIVATE);
        String value=sp.getString(key, key);
        return value;
    }
    //批量缓存
    public static void Set(Activity activity, List<String> key, List<String> content){
        @SuppressLint("WrongConstant") SharedPreferences sp = activity.getSharedPreferences("User", Context.MODE_APPEND);
        SharedPreferences.Editor edit = sp.edit();
        for(int i=0;i<key.size();i++){
            //通过editor对象写入数据
            edit.putString(key.get(i),content.get(i));
        }
        edit.commit();
    }
    //单个缓存
    public static void Set(Activity activity, String key, String content){
        @SuppressLint("WrongConstant") SharedPreferences sp = activity.getSharedPreferences("User", Context.MODE_APPEND);
        SharedPreferences.Editor edit = sp.edit();

            edit.putString(key,content);

        edit.commit();
    }
}
