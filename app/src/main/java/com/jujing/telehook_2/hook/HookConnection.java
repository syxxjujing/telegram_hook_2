package com.jujing.telehook_2.hook;

import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.util.HookUtil;
import com.jujing.telehook_2.util.LogTool;

import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class HookConnection {

    public static void hook() {

        Class<?> ConnectionsManager = XposedHelpers.findClass("org.telegram.tgnet.ConnectionsManager", HookMain.classLoader);
        XposedBridge.hookAllMethods(ConnectionsManager, "sendRequest", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String s = HookUtil.printParams(param);
                LogTool.e("sendRequest  21---->" + s);//
                if (s.contains("TLRPC$TL_messages_sendMessage")) {
//                if (s.contains("TLRPC$TL_account_updateStatus 666")) {
//                if (s.contains("TLRPC$TL_messages_addChatUser")) {
//                    LogTool.e("sendRequest  23---->" + s);//
                    HookUtil.printAllField(param.args[0]);
                    HookUtil.printAllField(param.args[1]);

//                    Object arg = param.args[1];HookUtil.frames();
//                    LogTool.e("arg  29---->" +  arg.getClass().getName());
//                    LogTool.e("arg  30---->" + Arrays.toString(arg.getClass().getTypeParameters()));
//                    LogTool.e("arg  30---->" + Arrays.toString(arg.getClass().getInterfaces()));
//                    HookUtil.frames();
                }

            }
        });


//        Class<?> DialogsSearchAdapter$$Lambda$0 = XposedHelpers.findClass("org.telegram.ui.Adapters.DialogsSearchAdapter$$Lambda$0", HookMain.classLoader);
//        Class<?> DialogsSearchAdapter$$Lambda$0 = XposedHelpers.findClass("org.telegram.ui.Adapters.SearchAdapterHelper$$Lambda$1", HookMain.classLoader);
//        XposedBridge.hookAllMethods(DialogsSearchAdapter$$Lambda$0, "run", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                String s = HookUtil.printParams(param);
//
//                LogTool.e("run  41---->" + s);//
////
//                Object arg = param.args[0];
//                List chats = (List) XposedHelpers.getObjectField(arg, "chats");
//
//                HookUtil.printAllField(chats.get(0));
//
//            }
//        });

    }
}
