package com.jujing.telehook_2.model.operate;

import static com.jujing.telehook_2.HookMain.classLoader;

import android.content.DialogInterface;
import android.os.SystemClock;
import android.text.TextUtils;

import com.jujing.telehook_2.Global;
import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.hook.HookActivity;
import com.jujing.telehook_2.model.UsersAndChats;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.HookUtil;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.WriteFileUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class SwitchAccountAction {

    private static final String TAG = "SwitchAccountAction";

    public static void hook() {
        Class<?> LaunchActivity = XposedHelpers.findClass("org.telegram.ui.LaunchActivity", HookMain.classLoader);
        LoggerUtil.logI(TAG, "LaunchActivity 20---->" + LaunchActivity);
        XposedHelpers.findAndHookMethod(LaunchActivity, "switchToAccount", int.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String s = HookUtil.printParams(param);
                LoggerUtil.logI(TAG, "sss 35---->" + s);
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
        LoggerUtil.logI(TAG, "LaunchActivity 26---->" + LaunchActivity);
    }

    public static boolean handleSwitch(int num) {
        String nums = WriteFileUtil.read(Global.SWITCH_NUMS + num);
        LoggerUtil.logI(TAG, "nums 55---->" + nums + "---->" + num);
        if (nums.equals("switched")) {
            LoggerUtil.logI(TAG, "switched 58---->" + nums + "---->" + num);
            LoggerUtil.sendLog7("????????????????????????");
            return false;
        }


        HookActivity.baseActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    LoggerUtil.logI(TAG, "switchToAccount 48----->" + "---->" + HookActivity.baseActivity);
                    Method switchToAccount = HookActivity.baseActivity.getClass().getMethod("switchToAccount", int.class, boolean.class);
                    switchToAccount.invoke(HookActivity.baseActivity, num, true);
//                                        HookActivity.baseActivity.
//                                        XposedHelpers.callMethod(HookActivity.baseActivity,"switchToAccount",1,true);
                    LoggerUtil.logI(TAG, "switchToAccount 53----->");
                } catch (Exception e) {
                    LoggerUtil.logI(TAG, "eee 55----->" + CrashHandler.getInstance().printCrash(e));
                }
            }
        });

        for (int i = 0; i < 20; i++) {
            int currentUserId = UsersAndChats.getCurrentUserId(classLoader);
            LoggerUtil.logI(TAG, "iiii  83---->" + num + "----->" + currentUserId + "---->" + i);
            if (currentUserId != num) {
                SystemClock.sleep(1000);
            } else {
                break;
            }
        }
        WriteFileUtil.write("switched", Global.SWITCH_NUMS + num);
        Object currentUser = UsersAndChats.getCurrentUser();
//                HookUtil.printAllFieldForSuperclass(currentUser);
        long id = XposedHelpers.getLongField(currentUser, "id");
        LoggerUtil.logI(TAG, "id  80---->" + id);
        WriteFileUtil.write(id + "", Global.USER_INFO_ID);
        String first_name = (String) XposedHelpers.getObjectField(currentUser, "first_name");
        String last_name = (String) XposedHelpers.getObjectField(currentUser, "last_name");
        WriteFileUtil.write(first_name + last_name, Global.USER_INFO_NICKNAME);
        LoggerUtil.logI(TAG, "first_name&last_name  85---->" + first_name + last_name);
        String phone = (String) XposedHelpers.getObjectField(currentUser, "phone");
        LoggerUtil.logI(TAG, "phone  87---->" + phone);
        WriteFileUtil.write(phone, Global.USER_INFO_PHONE);
        return true;

//        String replyJson = WriteFileUtil.read(Global.STORAGE_LOCAL_REPLY_JSON);
//        LoggerUtil.logI(TAG, "replyJson  92---->" + replyJson);
//        boolean b = SendVideoInitAction.initSayHi(replyJson);
//        LoggerUtil.logI(TAG, "bbb  94---->" + b);
//        if (!b) {
//            LoggerUtil.sendLog7("?????????????????????????????????????????????");
//            HookActivity.baseActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HookActivity.baseActivity)
//                            .setTitle("?????????????????????????????????????????????")
//                            .setPositiveButton("??????", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    LoggerUtil.logI(TAG, "?????????????????????????????????????????????   66--->");
//                                }
//                            })
//                            .setNegativeButton("??????", null);
//                    builder.show();
//                }
//            });
//            UsersAndChats.isStart = false;
//            return;
//        }
    }

    public static boolean isSwitch = false;

    public static boolean handle() {

        boolean flag = false;
        try {
            if (isSwitch) {
                LoggerUtil.logI(TAG, "isSwitch 120----->" + isSwitch);
                LoggerUtil.sendLog7("?????????????????????");
                flag = false;
                return false;
            }
            isSwitch = true;
            LoggerUtil.sendLog7("???????????????" + UsersAndChats.sentNum + "???????????????????????????");
            Class UserConfig = classLoader.loadClass("org.telegram.messenger.UserConfig");
            int getActivatedAccountsCount = (int) XposedHelpers.callStaticMethod(UserConfig, "getActivatedAccountsCount");
            LoggerUtil.logI(TAG, "getActivatedAccountsCount 377----->" + getActivatedAccountsCount);//????????????3???
            LoggerUtil.sendLog7("???????????????" + getActivatedAccountsCount + "?????????");
            if (getActivatedAccountsCount == 1) {
                LoggerUtil.sendLog7("??????????????????????????????????????????????????????");
            } else if (getActivatedAccountsCount == 2) {
                int currentUserId = UsersAndChats.getCurrentUserId(classLoader);
                LoggerUtil.logI(TAG, "currentUserId 380----->" + currentUserId);
                WriteFileUtil.write("switched", Global.SWITCH_NUMS + currentUserId);
                if (currentUserId == 0) {
                    LoggerUtil.sendLog7("???????????????????????????????????????????????????????????????");
                    flag = SwitchAccountAction.handleSwitch(1);
                } else if (currentUserId == 1) {
                    LoggerUtil.sendLog7("???????????????????????????????????????????????????????????????");
                    flag = SwitchAccountAction.handleSwitch(0);
                }
                LoggerUtil.sendLog7("???????????????");
            } else if (getActivatedAccountsCount == 3) {
                int currentUserId = UsersAndChats.getCurrentUserId(classLoader);
                LoggerUtil.logI(TAG, "currentUserId 115----->" + currentUserId);
                WriteFileUtil.write("switched", Global.SWITCH_NUMS + currentUserId);
                if (currentUserId == 0) {
                    LoggerUtil.sendLog7("???????????????????????????????????????????????????????????????");
                    flag = SwitchAccountAction.handleSwitch(1);
                } else if (currentUserId == 1) {
                    LoggerUtil.sendLog7("???????????????????????????????????????????????????????????????");
                    flag =   SwitchAccountAction.handleSwitch(2);
                } else if (currentUserId == 2) {
                    LoggerUtil.sendLog7("???????????????????????????????????????????????????????????????");
                    flag = SwitchAccountAction.handleSwitch(0);
                }
                LoggerUtil.sendLog7("???????????????");
            }
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee 90----->" + CrashHandler.getInstance().printCrash(e));
        }

        isSwitch = false;
        return flag;
    }
}
