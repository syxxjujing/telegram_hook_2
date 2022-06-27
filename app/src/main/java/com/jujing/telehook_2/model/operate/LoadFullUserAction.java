package com.jujing.telehook_2.model.operate;

import android.os.SystemClock;

import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.LoggerUtil;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class LoadFullUserAction {

    public static void hook() {
        Class<?> MessagesStorage = XposedHelpers.findClass("org.telegram.messenger.MessagesStorage", HookMain.classLoader);
        XposedBridge.hookAllMethods(MessagesStorage, "updateUserInfo", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
//                String s = HookUtil.printParams(param);
//                LoggerUtil.logI(TAG, "updateUserInfo  235---->"+s);

                Object arg = param.args[0];
                long id = XposedHelpers.getLongField(arg, "id");
//                LoggerUtil.logAll(TAG+cid, "isStart  240---->"+isStart+"---->"+id);
//                if (!GroupSendAction.isStart) {
//                    return;
//                }

                noId = id;
//                LoggerUtil.logI(TAG+cid, "id  243---->"+id);

            }
        });
    }

    public static long noId = -1;

    //    public static long cid = -1;
    public static long loadFullUser(Object user, String TAG) {
        try {
            noId = -1;

//            cid = chatId;
//            Class TL_users_getFullUser = HookMain.classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_users_getFullUser");
//            Object req = XposedHelpers.newInstance(TL_users_getFullUser);
            Object AccountInstanceIns = Tools.getAccountInstance(HookMain.classLoader);
            Object messagesController = XposedHelpers.callMethod(AccountInstanceIns, "getMessagesController");
            XposedHelpers.callMethod(messagesController, "loadFullUser", user, 10, true);
            for (int i = 0; i < 10; i++) {
                if (noId == -1) {
                    SystemClock.sleep(1000);
                } else {
                    break;
                }
            }
            LoggerUtil.logI(TAG, "noId  270:" + noId);
            if (noId == -1) {
            }
//            SendCommentTask sendCommentTask = new SendCommentTask("", noId);
//            int size = TaskCommentQueue.getInstance().add(sendCommentTask);
//            LoggerUtil.logI(TAG, "sendCommentTask.size  234------>" + size + "---->" + noId);

        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee  258---->" + CrashHandler.getInstance().printCrash(e));
        }
        return noId;
    }
}
