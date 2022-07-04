package com.jujing.telehook_2.hook;

import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.util.HookUtil;
import com.jujing.telehook_2.util.LogTool;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class HookRequest {
    public static void hook(){
        Class<?> ConnectionsManager = XposedHelpers.findClass("org.telegram.tgnet.ConnectionsManager", HookMain.classLoader);
        XposedBridge.hookAllMethods(ConnectionsManager, "sendRequest", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                int length = param.args.length;
                if (length!=2){
                    return;
                }
                HookUtil.frames();

//                String s = HookUtil.printParams(param);
//                LogTool.e("sss  22---->" + s);
            }
        });

        Class<?> View = XposedHelpers.findClass("android.view.View", HookMain.classLoader);
        XposedBridge.hookAllMethods(View, "performClick", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
//                HookUtil.frames();
            }
        });
    }
}
