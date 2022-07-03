package com.jujing.telehook_2.model.operate;

import android.os.Handler;
import android.os.Looper;

import com.jujing.telehook_2.hook.HookActivity;
import com.jujing.telehook_2.model.UsersAndChats;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.LoggerUtil;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static com.jujing.telehook_2.HookMain.classLoader;

public class VoiceCallAction {
    private static final String TAG = "VoiceCallAction";

    public static void startCall(long toUid, String time) {
        try {
            LoggerUtil.logI(TAG, "startCall     20 --->" + toUid + "---->" + time);
            int i = Integer.parseInt(time);

            Thread.sleep(i * 1000);
//            long toUid=1801589112;
            final Object user = UsersAndChats.getUser(toUid);


            Object fuser = UsersAndChats.getUserFull(classLoader, toUid);
            LoggerUtil.logI(TAG, "fuser     28 ---" + fuser);
            if (fuser == null) {
                long noId = LoadFullUserAction.loadFullUser(user, TAG);
                LoggerUtil.logI(TAG, "noId  32---->" + noId);
                fuser = UsersAndChats.getUserFull(classLoader, toUid);
                LoggerUtil.logI(TAG, "fuser     32 ---" + fuser);
            }
            if (fuser == null) {
                return;
            }

            boolean phone_calls_private = XposedHelpers.getBooleanField(fuser, "phone_calls_private");
            LoggerUtil.logI(TAG, "phone_calls_private     29 ---" + toUid + "---->" + phone_calls_private);
            if (fuser != null && phone_calls_private) {
                return;
            }


            final Object accounIns = UsersAndChats.getAccountInstance(classLoader);

//            if(LaunchActivityOnCreate.activity==null)
//            {
//                XposedBridge.log("LaunchActivityOnCreate.activity==null");
//                return;
//            }
            final Class VoIPHelper = classLoader.loadClass("org.telegram.ui.Components.voip.VoIPHelper");
            LoggerUtil.logI(TAG, "VoIPHelper     56 ---" + toUid + "---->" + VoIPHelper);
            Object finalFuser = fuser;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    XposedHelpers.callStaticMethod(VoIPHelper, "startCall", user, false, true, HookActivity.baseActivity, finalFuser, accounIns);


                }
            });


            Class VoIPService = classLoader.loadClass("org.telegram.messenger.voip.VoIPService");
            final Object VoIPServiceIns = XposedHelpers.callStaticMethod(VoIPService, "getSharedInstance");
            LoggerUtil.logI(TAG, "VoIPServiceIns     70 ---" + toUid + "---->" + VoIPServiceIns);
            if (VoIPServiceIns != null) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            XposedHelpers.callMethod(VoIPServiceIns, "hangUp");

                            LoggerUtil.logI(TAG, "hangUp   55 ---");
                        } catch (Exception e) {

                        }
                    }
                });

            }


            LoggerUtil.logI(TAG, "startCall end    62 ---");
        } catch (Exception e) {
//            Tool.printException(e);
            LoggerUtil.logI(TAG, "eee  89---->"+ CrashHandler.getInstance().printCrash(e));
        }
    }
}
