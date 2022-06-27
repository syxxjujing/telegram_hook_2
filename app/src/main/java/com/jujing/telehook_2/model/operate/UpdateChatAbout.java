package com.jujing.telehook_2.model.operate;

import android.content.Context;


import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.model.XCMethodHookBase;
import com.jujing.telehook_2.util.LoggerUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import de.robv.android.xposed.XposedHelpers;

public class UpdateChatAbout extends XCMethodHookBase implements InvocationHandler {


    private static final String TAG = "UpdateChatAbout";
    long chatId;

    public UpdateChatAbout(Context context, long chatId) {
        super(context);
        this.chatId = chatId;
    }


    public static void newReq(Context context, long chatId, String content) {
        try {

            ClassLoader classLoader = HookMain.classLoader;
            Class TL_messages_editChatAbout = classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_messages_editChatAbout");
            Object editChatAboutObj = XposedHelpers.newInstance(TL_messages_editChatAbout);
            Object peer = Tools.getInputPeer(classLoader, chatId);
            XposedHelpers.setObjectField(editChatAboutObj, "peer", peer);
            XposedHelpers.setObjectField(editChatAboutObj, "about", content);
            Object mConnectionsManager = Tools.getConnectionsManager(classLoader);

            InvocationHandler invocationHandler = new UpdateChatAbout(context, chatId);

            Class RequestDelegate = classLoader.loadClass("org.telegram.tgnet.RequestDelegate");
            Object cb = Proxy.newProxyInstance(classLoader, new Class[]{RequestDelegate}, invocationHandler);
            XposedHelpers.callMethod(mConnectionsManager, "sendRequest", editChatAboutObj, cb, 64);
            LoggerUtil.logI(TAG,"newReq  44--->"+chatId);
        } catch (Exception e) {
        }
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {


        String mName = method.getName();
        LoggerUtil.logI(TAG,"mName  54--->"+mName);
        if (mName.equals("run")) {

            run(objects[0], objects[1]);
        }
        return null;
    }

    void run(Object response, Object error) {
        LoggerUtil.logI(TAG,"run  62--->"+response+"---->"+error);
        if (error != null) {
            int code = XposedHelpers.getIntField(error, "code");
            String text = (String) XposedHelpers.getObjectField(error, "text");
            LoggerUtil.logI(TAG, "获取用户信息错误code  67---->" + code + " text:" + text);
        }
        if (response == null) {
//            Tool.showMsg(context,"修改群简介失败");
            return;
        }

        if (!response.getClass().getName().equals("org.telegram.tgnet.TLRPC$TL_boolTrue")) {
//            Tool.showMsg(context,"修改群简介失败");
            return;
        }

//        Tool.showMsg(context,"修改群简介成功");
        LoggerUtil.sendLog4("修改群简介成功:"  + " id:" + chatId);
        return;
    }
}
