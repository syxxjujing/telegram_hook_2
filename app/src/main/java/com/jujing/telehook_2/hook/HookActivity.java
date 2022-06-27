package com.jujing.telehook_2.hook;

import android.app.Activity;
import android.widget.Toast;


import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.LoggerUtil;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

/**
 * Created by Administrator on 2019/3/23.
 */

public class HookActivity {
    private static final String TAG = "HookActivity";
    static String title = "";
    public static Activity baseActivity;
    public static boolean isRun = false;

    public static void hook() {


        XposedHelpers.findAndHookMethod(Activity.class, "onResume", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                try {
                    Activity activity = (Activity) param.thisObject;
                    baseActivity = activity;
                    LoggerUtil.logAll(TAG, "activityName  87----> " + activity+"----->");

                } catch (Exception e) {
                    LoggerUtil.logI(TAG, "eee  104--->" + CrashHandler.getInstance().printCrash(e));
//                    e.printStackTrace();
                }
            }
        });
    }
    public static void showToast(final String content) {
        try {
            HookActivity.baseActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Toast.makeText(HookActivity.baseActivity, content, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {

        }
    }


    public static void showLongToast(final String content) {
        try {
            HookActivity.baseActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Toast.makeText(HookActivity.baseActivity, content, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {

        }
    }
}
