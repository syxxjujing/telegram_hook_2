package com.jujing.telehook_2.model.operate;

import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.util.HookUtil;
import com.jujing.telehook_2.util.LoggerUtil;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class SwitchAccountAction {

    private static final String TAG = "SwitchAccountAction";

    public static void hook(){
        Class<?> LaunchActivity = XposedHelpers.findClass("org.telegram.ui.LaunchActivity", HookMain.classLoader);
        LoggerUtil.logI(TAG,"LaunchActivity 20---->"+LaunchActivity);
        XposedHelpers.findAndHookMethod(LaunchActivity, "switchToAccount", int.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String s = HookUtil.printParams(param);
                LoggerUtil.logI(TAG,"sss 23---->"+s);
//                sss 23---->1,true,
//                        sss 23---->0,true,
            }
        });
//        XposedBridge.hookAllMethods(LaunchActivity, "switchToAccount", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                String s = HookUtil.printParams(param);
//                LoggerUtil.logI(TAG,"sss 20---->"+s);
//            }
//        });
        LoggerUtil.logI(TAG,"LaunchActivity 26---->"+LaunchActivity);
    }
}
