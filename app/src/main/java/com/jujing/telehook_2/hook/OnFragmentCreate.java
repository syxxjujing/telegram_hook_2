package com.jujing.telehook_2.hook;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;


import com.jujing.telehook_2.Global;
import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.model.operate.GetChannelParticipants;
import com.jujing.telehook_2.util.ExecutorUtil;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.WriteFileUtil;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class OnFragmentCreate extends XC_MethodHook {

    private static final String TAG = "OnFragmentCreate";

    public static void hook() {
        try {
            Class ChatActivity1 = HookMain.classLoader.loadClass("org.telegram.ui.ChatActivity");
            XposedBridge.hookAllMethods(ChatActivity1, "onFragmentCreate", new OnFragmentCreate(HookActivity.baseActivity));
        } catch (ClassNotFoundException e) {

        }
    }

    Context context;

    public OnFragmentCreate(Context context) {
        this.context = context;
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        Bundle arguments = (Bundle) XposedHelpers.getObjectField(param.thisObject, "arguments");
//        Tool.printBundle(arguments,"xxx");
        final long userId = arguments.getLong("user_id", 0);
        final long chat_id = arguments.getLong("chat_id", 0);
        LoggerUtil.logAll(TAG,"chat_id  ---- >"+chat_id);
        HookActivity.showLongToast("userId:" + userId + "\nchat_id:" + chat_id);
        if (chat_id==0){
            return;
        }


//        ExecutorUtil.doExecute(new Runnable() {
//            @Override
//            public void run() {
//                if (isHave(chat_id+"")) {
//                    LoggerUtil.logI(TAG, "以前获取过了 51 :" + chat_id);
//                }else{
//                    String chat_ids = WriteFileUtil.read(Global.CHAT_IDS);
//                    if (TextUtils.isEmpty(chat_ids)) {
//                        WriteFileUtil.write(chat_id+"", Global.CHAT_IDS);
//                    } else {
//                        WriteFileUtil.write(chat_ids + "," + chat_id, Global.CHAT_IDS);
//                    }
//                }
//            }
//        });




    }


//    public static boolean isHave( String chatid) {
//        boolean isHave = false;
//        try {
//            String added_friend_username = WriteFileUtil.read(Global.CHAT_IDS);
//            String[] split1 = added_friend_username.split(",");
//            for (String wxid : split1) {
//                if (wxid.equals(chatid)) {
//                    isHave = true;
//                    break;
//                }
//            }
//        } catch (Exception e) {
//
//        }
//        return isHave;
//    }
}
