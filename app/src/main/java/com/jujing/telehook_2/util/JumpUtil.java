package com.jujing.telehook_2.util;

import android.content.Context;
import android.content.Intent;


import com.jujing.telehook_2.Global;
import com.jujing.telehook_2.ProgressDialogActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class JumpUtil {


//    public static void  jumpToProgressDialog(String content){
//
//        Intent redIntent=new Intent();
//        redIntent.putExtra("content",content);
//        redIntent.setClassName("com.jujing.xreply", "com.jujing.xreply.dialogui.ProgressDialogActivity");
//        HookActivity.baseActivity.startActivity(redIntent);
//
//    }

    public static void  jumpToProgressDialog(String content, Context context){

        Intent redIntent=new Intent();
        redIntent.putExtra("content",content);
        redIntent.setClassName("com.jujing.telehook_2", "com.jujing.telehook_2.ProgressDialogActivity");
        context.startActivity(redIntent);

    }




//    public static void  jumpToShowRingActivity(String content){
//        Intent redIntent=new Intent();
//        redIntent.putExtra("json",content);
//        redIntent.setClassName("com.jujing.xreply", "com.jujing.xreply.media.ShowRingActivity");
//        HookActivity.baseActivity.startActivity(redIntent);
//
//    }



//    public static void sendMessageToProgressDialog(String content){
//        try {
//            Intent intent = new Intent();
//            intent.setAction(ProgressDialogActivity.ACTION_XREPLY_PROGRESS);
//            intent.putExtra("content",content);
//            HookActivity.baseActivity.sendBroadcast(intent);
//        } catch (Exception e) {
//
//        }
//    }
    public static void sendMessageToProgressDialog(String content,Context context){
        Intent intent = new Intent();
        intent.setAction(ProgressDialogActivity.ACTION_XREPLY_PROGRESS);
        intent.putExtra("content",content);
        context.sendBroadcast(intent);
    }

    public static boolean judgePower(String power) throws JSONException {
        String login_json = WriteFileUtil.read(Global.LOGIN_JSON);
        JSONObject jsonObject = new JSONObject(login_json);
        JSONObject dataObj = jsonObject.getJSONObject("data");
        return dataObj.getBoolean(power);
    }

}
