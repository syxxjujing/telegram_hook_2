package com.jujing.telehook_2.model.operate;

import static com.jujing.telehook_2.HookMain.classLoader;
import static com.jujing.telehook_2.model.UsersAndChats.getAccountInstance;
import static com.jujing.telehook_2.model.UsersAndChats.getCurrentUserId;
import static com.jujing.telehook_2.model.operate.SendForwardAction.getDatabase;

import android.os.SystemClock;
import android.text.TextUtils;

import com.jujing.telehook_2.Global;
import com.jujing.telehook_2.bean.ChatBean;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.ExecutorUtil;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.WriteFileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.robv.android.xposed.XposedHelpers;

public class GetMemberAction {


    private static final String TAG = "GetMemberAction";

    public static void handle() {
        ExecutorUtil.doExecute(new Runnable() {
            @Override
            public void run() {
                List<ChatBean> groupIdList = UserReadAction.getGroupIdList();
                LoggerUtil.logI(TAG, "groupIdList  30:" + groupIdList.size());
                for (int i = 0; i < groupIdList.size(); i++) {

//                    loadUsers(groupIdList.get(i).chatId);
                    loadChatInfoInternal(groupIdList.get(i).chatId);
                    SystemClock.sleep(5000);

                    loadUsers(groupIdList.get(i).chatId);


                }
            }
        });


    }

    public static void loadChatInfoInternal(long chatId){
        try {
            Class UserConfig = classLoader.loadClass("org.telegram.messenger.UserConfig");
            int currentAccount = XposedHelpers.getStaticIntField(UserConfig, "selectedAccount");


            Class MessagesStorage = classLoader.loadClass("org.telegram.messenger.MessagesStorage");
            Object MessagesStorageIns = XposedHelpers.callStaticMethod(MessagesStorage,
                    "getInstance", currentAccount);
            XposedHelpers.callMethod(MessagesStorageIns,"loadChatInfo",chatId,true,new CountDownLatch(1),false,true,0);


        } catch (Exception e) {
            LoggerUtil.logI(TAG + chatId, "eee  55===>" + CrashHandler.getInstance().printCrash(e));
        }
    }
    public static void loadUsers(long chatId) {
        try {
            ArrayList loadedUsers = new ArrayList<>();
            Object database = getDatabase(classLoader);
            Object accountInstanceIns = getAccountInstance(classLoader);
            Object userConfig = XposedHelpers.callMethod(accountInstanceIns, "getUserConfig");
            long clientUserId = XposedHelpers.getLongField(userConfig, "clientUserId");
            LoggerUtil.logI(TAG + chatId, "clientUserId  86:" + clientUserId);
            String sql = "SELECT us.data, us.status, cu.data, cu.date FROM channel_users_v2 as cu LEFT JOIN users as us ON us.uid = cu.uid WHERE cu.did = " + (-chatId) + " ORDER BY cu.date DESC";
            Object[] aaaa = new Object[]{sql, new Object[]{}};
            Object cursor = XposedHelpers.callMethod(database, "queryFinalized", aaaa);
            while ((boolean) XposedHelpers.callMethod(cursor, "next")) {
                try {
                    Object user = null;
                    Object participant = null;
//                    LoggerUtil.logI(TAG + chatId, "come  54-------");
                    Object data = XposedHelpers.callMethod(cursor, "byteBufferValue", 0);
                    if (data != null) {
                        int i = (int) XposedHelpers.callMethod(data, "readInt32", false);
                        Class User = classLoader.loadClass("org.telegram.tgnet.TLRPC$User");
                        user = XposedHelpers.callStaticMethod(User, "TLdeserialize", data, i, false);
                        XposedHelpers.callMethod(data, "reuse");
                    }

                    data = XposedHelpers.callMethod(cursor, "byteBufferValue", 2);
                    if (data != null) {
                        int i = (int) XposedHelpers.callMethod(data, "readInt32", false);
                        Class ChannelParticipant = classLoader.loadClass("org.telegram.tgnet.TLRPC$ChannelParticipant");
                        participant = XposedHelpers.callStaticMethod(ChannelParticipant, "TLdeserialize", data, i, false);
                        XposedHelpers.callMethod(data, "reuse");
                    }
                    if (participant != null) {
                        long user_id = (long) XposedHelpers.getLongField(participant, "user_id");
                        if (user_id == clientUserId) {
                            user = XposedHelpers.callMethod(userConfig, "getCurrentUser");
                        }
                    }

                    if (user != null && participant != null) {

                        Object status = XposedHelpers.getObjectField(user, "status");
                        if (status != null) {
//                            int expires = (int) XposedHelpers.callMethod(cursor, "intValue", 1);
//                            XposedHelpers.setIntField(status, "expires", expires);
                        }

                        loadedUsers.add(user);


//                        StringBuilder sbTxt = new StringBuilder();

                        String first_name = (String) XposedHelpers.getObjectField(user, "first_name");
                        String nick = first_name;
                        String last_name = (String) XposedHelpers.getObjectField(user, "last_name");

                        if (!TextUtils.isEmpty(last_name)) {
                            nick = first_name + last_name;
                        }

                        int expires = 0;
                        long uid = XposedHelpers.getLongField(user, "id");
                        if (status != null) {
                            expires = XposedHelpers.getIntField(status, "expires");
                        }
                        LoggerUtil.logI(TAG + chatId, "uid  174---->" + uid + "---->" + nick + "---->" + expires);

                    }
                } catch (Exception e) {

                    LoggerUtil.logI(TAG + chatId, "ee  181------->" + CrashHandler.getInstance().printCrash(e));
                }
            }

            LoggerUtil.logI(TAG + chatId, "loadedUsers  122:" + loadedUsers.size());

        } catch (Exception e) {
            LoggerUtil.logI(TAG + chatId, "eee  57===>" + CrashHandler.getInstance().printCrash(e));
        }
    }
}
