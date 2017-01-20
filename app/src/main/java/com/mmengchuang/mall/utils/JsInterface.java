package com.mmengchuang.mall.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by 11655 on 2017/1/19.
 */

public class JsInterface {
    private Context context;
    public JsInterface(Context context) {
        super();
        this.context = context;
    }

    /**
     * js中通过window.jsObj.saveToken("参数") 可以调用此方法并且把js中input中的值作为参数传入，
     * 但这是在点击js中的按钮得到的，若实现点击java中的按钮得到，需要方法 clickView(View v)
     *
     * @param token 用于与服务器交互的令牌
     * @param uid   用户的Id
     */
    @JavascriptInterface
    public void saveToken(final String token, final String uid) {//保存token和用户id
        Toast.makeText(context, ""+token+uid, Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = context.getSharedPreferences("users",MODE_PRIVATE).edit();
        editor.putString("token",token);
        editor.putString("uid",uid);
        editor.putBoolean("isLogin",true);
        editor.commit();
    }

    /**
     * Js 通过window.jsObj.jsonImeiToHtml() 获取设备的信息(用于生成token);
     *
     * @return
     */
    @JavascriptInterface
    public String jsonimeitohtml() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        /*
        * 唯一的设备ID：
        * GSM手机的 IMEI 和 CDMA手机的 MEID.
        * Return null if device ID is not available.
        */
        final String IMEI = tm.getDeviceId();//String
        final String IP = tm.getDeviceId()+"2016";
        JSONObject map;
        JSONArray array = new JSONArray();
        try {
            map = new JSONObject();
            map.put("IMEI", IMEI);
            map.put("IP", IP);
            array.put(map);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array.toString();
    }
    /**
     * js通过获取window.jsObj.jsonUidToHtmltoken和用户id
     * @return
     */
    @JavascriptInterface
    public String jsonUidToHtml(){

        //读取文件中的token 和id
        SharedPreferences preferences = context.getSharedPreferences("users",MODE_PRIVATE);
//        preferences.getString("")
        final boolean FLAG = preferences.getBoolean("isLogin",false);//false为未登录
        final String TOKEN = preferences.getString("token",null);
        final String UID = preferences.getString("uid",null);
        JSONObject map;
        JSONArray array = new JSONArray();
        try {
            map = new JSONObject();
            map.put("TOKEN", TOKEN);
            map.put("UID", UID);
            map.put("FLAG", FLAG);
            array.put(map);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array.toString();
   }

    @JavascriptInterface
    public void loginOut(){
        Toast.makeText(context, "我要退出登陆", Toast.LENGTH_SHORT).show();
        context.getSharedPreferences("users",MODE_PRIVATE).edit().putBoolean("isLogin",false).commit();
    }
}
