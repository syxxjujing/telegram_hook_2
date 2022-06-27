package com.jujing.telehook_2.model.operate;

import android.content.Context;
import android.text.TextUtils;

import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.hook.HookActivity;
import com.jujing.telehook_2.model.UsersAndChats;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.MatchUtil;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import de.robv.android.xposed.XposedHelpers;

public class LoadFullUser implements InvocationHandler {

    private static final String TAG = "LoadFullUser";
    Context context;
    ClassLoader classLoader;
    long chatId;
//    String TAG = getClass().getSimpleName();

    public  LoadFullUser(Context context, long chatId) {
        this.chatId = chatId;
        this.context = context;
        classLoader = HookMain.classLoader;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

        String mName = method.getName();
        LoggerUtil.logI(TAG+chatId,  " invoke mName  38:" + mName);

        switch (mName) {
            case "run":
                run(objects[0], objects[1]);
        }
        return null;
    }

    public static void newReq(final long chatId, long newId) {
        try {
            LoggerUtil.sendLog4("正在修改群简介:" + " id:" + newId);
            Object chat = UsersAndChats.getChat2(chatId);
            ClassLoader classLoader = HookMain.classLoader;
            Object AccountInstanceIns = Tools.getAccountInstance(classLoader);

            Object connectionsManager = XposedHelpers.callMethod(AccountInstanceIns, "getConnectionsManager");

            Class TL_channels_getFullChannel = classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_channels_getFullChannel");
            Object req = XposedHelpers.newInstance(TL_channels_getFullChannel);

            // Object user=com.hook.telegram.v25949.Tools.getUser(classLoader,chatId);
//            long id = XposedHelpers.getLongField(user, "id");
//            long access_hash = XposedHelpers.getLongField(user, "access_hash");

//            Class TL_inputUser = classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_inputUser");
//            Object inputUser = XposedHelpers.newInstance(TL_inputUser);
//            XposedHelpers.setLongField(inputUser, "user_id", id);
//            XposedHelpers.setLongField(inputUser, "access_hash", access_hash);
            Object inputChannel = UsersAndChats.getInputChannel(chat);

            XposedHelpers.setObjectField(req, "channel", inputChannel);

            Class RequestDelegate = classLoader.loadClass("org.telegram.tgnet.RequestDelegate");
//            Object callback= Proxy.newProxyInstance(classLoader,new Class[]{RequestDelegate},new LoadFullUser(context,id));
//            LoggerUtil.logI(TAG+chatId, " user  72---->" + user);
            Object callback = Proxy.newProxyInstance(classLoader, new Class[]{RequestDelegate}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {
                    String mName = method.getName();
                    LoggerUtil.logI(TAG, " invoke mName  77:" + mName);

                    switch (mName) {
                        case "run":
                            Object response = objects[0];
                            Object error = objects[1];
                            try {
                                if (error != null) {
                                    int code = XposedHelpers.getIntField(error, "code");
                                    String text = (String) XposedHelpers.getObjectField(error, "text");
                                    LoggerUtil.logI(TAG, "获取用户信息错误code  84---->" + code + " text:" + text);
                                    return null;
                                }
                                Object full_chat = XposedHelpers.getObjectField(response, "full_chat");
                                long id = XposedHelpers.getLongField(full_chat, "id");

                                String about = (String) XposedHelpers.getObjectField(full_chat, "about");
                                LoggerUtil.logI(TAG, "about  94:" + about+"---->"+id);

//                                UpdateChatAbout.newReq(HookActivity.baseActivity,id,about);

                                if (!TextUtils.isEmpty(about)) {
                                    UpdateChatAbout.newReq(HookActivity.baseActivity,newId*-1,about);

                                }


                            } catch (Exception e) {
                                LoggerUtil.logI(TAG, "ee  100:" + CrashHandler.getInstance().printCrash(e));
                            }
                    }
                    return null;
                }
            });
            XposedHelpers.callMethod(connectionsManager, "sendRequest", req, callback);
//            LoggerUtil.logI(TAG+chatId, " user  107---->" + user);
//            for (int i = 0; i < 20; i++) {
//                LoggerUtil.logI(TAG+chatId, "iiii  106:" + about[0]+"---->"+i);
//                if (TextUtils.isEmpty(about[0])){
//                    SystemClock.sleep(500);
//                }else{
//                    break;
//                }
//
//            }

        } catch (Exception e) {
            LoggerUtil.logI(TAG, " eee  124---->" + CrashHandler.getInstance().printCrash(e));
        }

    }

    void run(Object response, Object error) {
        try {


            if (error != null) {

                int code = XposedHelpers.getIntField(error, "code");
                String text = (String) XposedHelpers.getObjectField(error, "text");
                LoggerUtil.logI(TAG+chatId, "获取用户信息错误code:" + code + " text:" + text);
                return;
            }

            Object full_user = XposedHelpers.getObjectField(response, "full_user");
            long id = XposedHelpers.getLongField(full_user, "id");


            String about = (String) XposedHelpers.getObjectField(full_user, "about");
            if (!TextUtils.isEmpty(about)) {
//                Tool.showMsg(context,"about:"+about);
                LoggerUtil.logI(TAG+chatId, "about:" + about);
            }

//            Object user=Tools.getUser(classLoader,id);
//            if(user!=null)
//            {
//                String username=(String) XposedHelpers.getObjectField(user,"username");
//                LoggerUtil.logI(TAG+chatId,"username:"+username);
//            }
//
//
//            LoggerUtil.logI(TAG+chatId,"full_user:"+new Gson().toJson(full_user));
//
//          Object profile_photo=  XposedHelpers.getObjectField(full_user,"profile_photo");
//          byte[]file_reference=(byte[]) XposedHelpers.getObjectField(profile_photo,"file_reference");
//          LoggerUtil.logI(TAG+chatId,"file_reference:"+new String(file_reference));


            // Object bot_info=XposedHelpers.getObjectField(full_user,"bot_info");

            //   String description=(String) XposedHelpers.getObjectField(bot_info,"description");
            //  Tools.showMsg(context,"description:"+description);

        } catch (Exception e) {
//            Tool.printException(e);
        }
    }
}
