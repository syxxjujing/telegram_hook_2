package com.jujing.telehook_2.model;

import android.os.SystemClock;
import android.text.TextUtils;

import com.jujing.telehook_2.Global;
import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.bean.LocalReplyBean;
import com.jujing.telehook_2.hook.HookActivity;
import com.jujing.telehook_2.hook.HookSqlite;
import com.jujing.telehook_2.model.operate.SendForwardAction;
import com.jujing.telehook_2.model.operate.UserReadAction;
import com.jujing.telehook_2.model.operate.VoiceCallAction;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.HookUtil;
import com.jujing.telehook_2.util.LogTool;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.RandomUtil;
import com.jujing.telehook_2.util.WriteFileUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static com.jujing.telehook_2.HookMain.classLoader;
import static com.jujing.telehook_2.model.operate.SendForwardAction.getDatabase;

public class UsersAndChats {


    private static final String TAG = "UsersAndChats";

    public static Object getUser(long from_id) {
        Class<?> MessagesStorage = XposedHelpers.findClass("org.telegram.messenger.MessagesStorage", classLoader);
        Object getInstance = XposedHelpers.callStaticMethod(MessagesStorage, "getInstance", 0);
        return XposedHelpers.callMethod(getInstance, "getUser", from_id);
    }

    public static Object getUserFull(ClassLoader classLoader, long uid) {
        try {


            Object aaa = getAccountInstance(classLoader);
            Object messagesController = XposedHelpers.callMethod(aaa, "getMessagesController");
            LoggerUtil.logAll(TAG, "messagesController==================================" + messagesController);//1920502028
            Object user = XposedHelpers.callMethod(messagesController, "getUserFull", uid);
            LoggerUtil.logAll(TAG, "user==================================" + user);
            return user;
        } catch (Exception e) {
//            Tool.printException(e);
        }

        return null;
    }

    public static Object getAccountInstance(ClassLoader classLoader) {
        try {
            int currentAccount = getCurrentUserId(classLoader);

            Class AccountInstance = classLoader.loadClass("org.telegram.messenger.AccountInstance");
            Object AccountInstanceIns = XposedHelpers.callStaticMethod(AccountInstance, "getInstance", currentAccount);

            return AccountInstanceIns;
        } catch (Exception e) {
//            Tool.printException(e);
        }
        return null;
    }

    public static int getCurrentUserId(ClassLoader classLoader) {
        try {
            Class UserConfig = classLoader.loadClass("org.telegram.messenger.UserConfig");
            int currentAccount = XposedHelpers.getStaticIntField(UserConfig, "selectedAccount");
            return currentAccount;
        } catch (Exception e) {
//            Tool.printException(e);
        }
        return 0;
    }

//    public static Object getChat(int dialog_id) {
//        Class<?> MessagesStorage = XposedHelpers.findClass("org.telegram.messenger.MessagesStorage", classLoader);
//
//
//        Object getInstance = XposedHelpers.callStaticMethod(MessagesStorage, "getInstance", 0);
//        return XposedHelpers.callMethod(getInstance, "getChat", dialog_id);
////        return XposedHelpers.callMethod(getInstance, "getChatFull", dialog_id);
//    }

    public static Object getChat2(long dialog_id) {
        Class<?> MessagesController = XposedHelpers.findClass("org.telegram.messenger.MessagesController", classLoader);

        int selectedAccount = XposedHelpers.getStaticIntField(XposedHelpers.findClass("org.telegram.messenger.UserConfig", classLoader), "selectedAccount");

        Object getInstance = XposedHelpers.callStaticMethod(MessagesController, "getInstance", selectedAccount);
        return XposedHelpers.callMethod(getInstance, "getChat", dialog_id);
    }

    public static Object getInputChannel(Object chat) {
        Class<?> MessagesController = XposedHelpers.findClass("org.telegram.messenger.MessagesController", classLoader);

        int selectedAccount = XposedHelpers.getStaticIntField(XposedHelpers.findClass("org.telegram.messenger.UserConfig", classLoader), "selectedAccount");

        Object getInstance = XposedHelpers.callStaticMethod(MessagesController, "getInstance", selectedAccount);
        return XposedHelpers.callMethod(getInstance, "getInputChannel", chat);
    }

    public static Object getInputUser(Object user) {
        Class<?> MessagesController = XposedHelpers.findClass("org.telegram.messenger.MessagesController", classLoader);

        int selectedAccount = XposedHelpers.getStaticIntField(XposedHelpers.findClass("org.telegram.messenger.UserConfig", classLoader), "selectedAccount");

        Object getInstance = XposedHelpers.callStaticMethod(MessagesController, "getInstance", selectedAccount);
        return XposedHelpers.callMethod(getInstance, "getInputUser", user);
    }

    public static boolean getChat3(long dialog_id) {
        Class<?> MessagesController = XposedHelpers.findClass("org.telegram.messenger.MessagesController", classLoader);

        int selectedAccount = XposedHelpers.getStaticIntField(XposedHelpers.findClass("org.telegram.messenger.UserConfig", classLoader), "selectedAccount");

        Object getInstance = XposedHelpers.callStaticMethod(MessagesController, "getInstance", selectedAccount);
        return (boolean) XposedHelpers.callMethod(getInstance, "isJoiningChannel", dialog_id);
    }

    public static String getUserInfoId() {
        String id = WriteFileUtil.read(Global.USER_INFO_ID);
        if (TextUtils.isEmpty(id)) {
            Object currentUser = UsersAndChats.getCurrentUser();
            id = XposedHelpers.getLongField(currentUser, "id") + "";
            LoggerUtil.logI(TAG, "id  137---->" + id);
            WriteFileUtil.write(id + "", Global.USER_INFO_ID);

            String first_name = (String) XposedHelpers.getObjectField(currentUser, "first_name");
            String last_name = (String) XposedHelpers.getObjectField(currentUser, "last_name");
            WriteFileUtil.write(first_name + last_name, Global.USER_INFO_NICKNAME);
            LoggerUtil.logI(TAG, "first_name&last_name  142---->" + first_name + last_name);
            String phone = (String) XposedHelpers.getObjectField(currentUser, "phone");
            LoggerUtil.logI(TAG, "phone  144---->" + phone);
            WriteFileUtil.write(phone, Global.USER_INFO_PHONE);
        }
        return id;
    }

    /**
     * 获取自己的信息
     */
    public static Object getCurrentUser() {
        Class<?> UserConfig = XposedHelpers.findClass("org.telegram.messenger.UserConfig", classLoader);
        int currentAccount = XposedHelpers.getStaticIntField(UserConfig, "selectedAccount");
        Object getInstance1 = XposedHelpers.callStaticMethod(UserConfig, "getInstance", currentAccount);
        Object user = XposedHelpers.callMethod(getInstance1, "getCurrentUser");
        LoggerUtil.logI(TAG, "user   142=============>" + user);
        return user;
    }

    public static void getContactsInfo() {
        try {
            LoggerUtil.logI(TAG, "contacts  70 ");
            Class UserConfig = classLoader.loadClass("org.telegram.messenger.UserConfig");
            int currentAccount = XposedHelpers.getStaticIntField(UserConfig, "selectedAccount");


            Class ContactsController = classLoader.loadClass("org.telegram.messenger.ContactsController");

            Object ContactsControllerIns = XposedHelpers.callStaticMethod(ContactsController, "getInstance", currentAccount);
//            HashMap contactsByPhone = (HashMap) XposedHelpers.getObjectField(ContactsControllerIns, "contactsByPhone");
            List contactsByPhone = (List) XposedHelpers.getObjectField(ContactsControllerIns, "contacts");
//            List contactsByPhone = (List) XposedHelpers.getObjectField(ContactsControllerIns, "phoneBookContacts");

            LoggerUtil.logI(TAG, "联系人总共  89:" + contactsByPhone.size() + "人");

            for (int j = 0; j < contactsByPhone.size(); j++) {
                long user_id = XposedHelpers.getLongField(contactsByPhone.get(j), "user_id");
                Object user = UsersAndChats.getUser(user_id);
                Object username = XposedHelpers.getObjectField(user, "username");
                Object phone = XposedHelpers.getObjectField(user, "phone");
                Object first_name = XposedHelpers.getObjectField(user, "first_name");
                LoggerUtil.logI(TAG, "user_id:" + user_id + "---->" + username + "---->" + phone + "---->" + first_name + "---->" + j);


            }


        } catch (Exception e) {
            LoggerUtil.logI(TAG, "e   97------->" + CrashHandler.getInstance().printCrash(e));

        }

    }


    public static boolean isStart = false;

    public static void contacts(String inerval_friends0, String interval_messages0) {
        try {
            if (isStart) {
                HookActivity.showToast("正在转发，请稍候...");
                return;
            }
            isStart = true;
            HookActivity.showToast("开始转发...");
            LoggerUtil.logI(TAG, "contacts  57 ");
            Class UserConfig = classLoader.loadClass("org.telegram.messenger.UserConfig");

            int currentAccount = XposedHelpers.getStaticIntField(UserConfig, "selectedAccount");


            Class ContactsController = classLoader.loadClass("org.telegram.messenger.ContactsController");

            Object ContactsControllerIns = XposedHelpers.callStaticMethod(ContactsController, "getInstance", currentAccount);

//            HashMap contactsByPhone = (HashMap) XposedHelpers.getObjectField(ContactsControllerIns, "contactsByPhone");
            List contactsByPhone = (List) XposedHelpers.getObjectField(ContactsControllerIns, "contacts");

            LoggerUtil.logI(TAG, "联系人总共:" + contactsByPhone.size() + "人");
            int inerval_friends = 0;
//            try {
//                inerval_friends = Integer.parseInt(inerval_friends0);
//            } catch (Exception e) {
//
//            }

            try {
                if (inerval_friends0.contains("-")) {
                    String[] split = inerval_friends0.split("-");
                    int start = Integer.parseInt(split[0]);
                    int end = Integer.parseInt(split[1]);
                    inerval_friends = RandomUtil.randomNumber(start, end);
                } else {
                    inerval_friends = Integer.parseInt(inerval_friends0);
                }
            } catch (Exception e) {

            }


            int interval_messages = 0;
//            try {
//                interval_messages = Integer.parseInt(interval_messages0);
//            } catch (Exception e) {
//
//            }

            try {
                if (interval_messages0.contains("-")) {
                    String[] split = interval_messages0.split("-");
                    int start = Integer.parseInt(split[0]);
                    int end = Integer.parseInt(split[1]);
                    interval_messages = RandomUtil.randomNumber(start, end);
                } else {
                    interval_messages = Integer.parseInt(interval_messages0);
                }
            } catch (Exception e) {

            }

            LoggerUtil.logI(TAG, "inerval_friends 265 :" + inerval_friends + "-----" + interval_messages);
            for (int i = 0; i < contactsByPhone.size(); i++) {
                long user_id = XposedHelpers.getLongField(contactsByPhone.get(i), "user_id");
                Object user = UsersAndChats.getUser(user_id);
                Object username = XposedHelpers.getObjectField(user, "username");
                Object phone = XposedHelpers.getObjectField(user, "phone");
                Object first_name = XposedHelpers.getObjectField(user, "first_name");
                LoggerUtil.logI(TAG, "user_id  148:" + user_id + "---->" + username + "---->" + phone + "---->" + first_name + "---->" + i);

                String replyJson = WriteFileUtil.read(Global.STORAGE_LOCAL_REPLY_JSON);
                JSONArray jsonArray = new JSONArray(replyJson);
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject = new JSONObject(jsonArray.getString(j));
                    String content = jsonObject.getString("content");
                    LoggerUtil.logI(TAG, "content 109 :" + content + "-----" + j);
                    if (sendM(user_id, j, content, TAG)) {
                        continue;
                    }

                    for (int k = 0; k < interval_messages; k++) {
                        if (!isStart) {
                            LoggerUtil.logI(TAG, "任务停止了 109 :" + interval_messages + "-----" + k);
                            HookActivity.showToast("任务停止了！");
                            return;
                        }
                        SystemClock.sleep(1000);
                    }


                }
                for (int j = 0; j < inerval_friends; j++) {
                    if (!isStart) {
                        LoggerUtil.logI(TAG, "任务停止了 109 :" + inerval_friends + "-----" + j);
                        HookActivity.showToast("任务停止了！！！");
                        return;
                    }
                    SystemClock.sleep(1000);
                }

            }


//            String replyJson = WriteFileUtil.read(Global.STORAGE_LOCAL_REPLY_JSON);
//            JSONArray jsonArray = new JSONArray(replyJson);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
//                String content = jsonObject.getString("content");
//                LoggerUtil.logI(TAG, "content 109 :" + content + "-----" + i);
//                if (TextUtils.isEmpty(content)) {
//                    continue;
//                }
//
//                for (int j = 0; j < contactsByPhone.size(); j++) {
//                    long user_id = XposedHelpers.getLongField(contactsByPhone.get(j), "user_id");
//                    Object user = UsersAndChats.getUser(user_id);
//                    Object username = XposedHelpers.getObjectField(user, "username");
//                    Object phone = XposedHelpers.getObjectField(user, "phone");
//                    Object first_name = XposedHelpers.getObjectField(user, "first_name");
//                    LoggerUtil.logI(TAG, "user_id:" + user_id + "---->" + username + "---->" + phone + "---->" + first_name + "---->" + j);
//                    if (content.startsWith("语音")) {
//                        String[] array = content.split(":");
//                        String path = array[1].trim();
//                        if (TextUtils.isEmpty(path)) {
//                            continue;
//                        }
//                        boolean exists = new File(path).exists();
//                        LoggerUtil.logI(TAG, "path 120 :" + path + "-----" + j + "---->" + exists);
//                        if (!exists) {
//                            continue;
//                        }
//                        SendMessage.sendVoice(user_id, path);
//                    } else {
//                        SendMessage.sendMessage(content, user_id);
//                    }
//
//                    SystemClock.sleep(inerval_friends * 1000);
//
//                }
//
//                SystemClock.sleep(interval_messages * 1000);
//
//
//            }


        } catch (Exception e) {
            LoggerUtil.logI(TAG, "e   95------->" + CrashHandler.getInstance().printCrash(e));

        }
//        isStart = false;

    }

    public static boolean sendM(long user_id, int j, String content, String TAG) {
        try {
            WriteFileUtil.write(user_id + "", Global.SENT_UID + user_id);


            UserReadAction.checkSendSucceedNum(false);

            if (TextUtils.isEmpty(content)) {
                return true;
            }
            LoggerUtil.sendLog7("开始发送消息：" + content + "--->" + user_id);
            if (content.startsWith("语音") || content.startsWith("图片")
                    || content.startsWith("gif") || content.startsWith("视频")) {
                String[] array = content.split(":");
                String path = array[1].trim();
                if (TextUtils.isEmpty(path)) {
                    return true;
                }
                boolean exists = new File(path).exists();
                LoggerUtil.logI(TAG, "path 294 :" + path + "-----" + j + "---->" + exists + "---->" + content);
                if (!exists) {
                    return true;
                }
                if (content.startsWith("语音")) {
                    SendMessage.sendVoice(user_id, path);
                } else if (content.startsWith("图片")) {
                    SendMessage.sendImage(false, user_id, path);
                } else if (content.startsWith("gif")) {
                    SendMessage.sendGif(user_id, path);
                } else if (content.startsWith("视频")) {
                    SendMessage.sendVideo(false, user_id, path);
                }

            } else if (content.startsWith("弹语音")) {
                String[] array = content.split(":");
                String time = array[1].trim();
                LoggerUtil.logI(TAG, "time 166 :" + time + "-----" + j);
                VoiceCallAction.startCall(user_id, time);
            } else if (content.startsWith("收藏")) {
                String[] array = content.split(":");
                int index = 0;
                try {
                    index = Integer.parseInt(array[1].trim());
                } catch (Exception e) {
                    return true;
                }
                LoggerUtil.logI(TAG, "index 318 :" + index + "-----" + j);
                SendForwardAction.sendForwardMessages(user_id, index);
            } else {
                SendMessage.sendMessage(content, user_id);
            }
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee 328 :" + CrashHandler.getInstance().printCrash(e) + "-----" + j);
        }
        return false;
    }

    public static void getChatsInfos() {
        Object database = getDatabase(classLoader);


        Object[] obj = new Object[]{};
        String sql = "SELECT uid,info, pinned, online FROM chat_settings_v2";
//        String sql = "SELECT uid,data FROM chats";
//        String sql = "SELECT data FROM messages";
        Object cursor = XposedHelpers.callMethod(database, "queryFinalized", sql, obj);
        LoggerUtil.logI(TAG, "cursor   131------->" + cursor);
        int getColumnCount = (int) XposedHelpers.callMethod(cursor, "getColumnCount");
        LoggerUtil.logI(TAG, "getColumnCount   58------->" + getColumnCount);
//        boolean next = (boolean) XposedHelpers.callMethod(cursor, "next");
//        LoggerUtil.logI(TAG,"next   60------->" + next);

//        XposedHelpers.setBooleanField(cursor,"inRow",true);

        while ((boolean) XposedHelpers.callMethod(cursor, "next")) {
            LoggerUtil.logI(TAG, "getColumnCount   61------->" + getColumnCount);
            long uid = (long) XposedHelpers.callMethod(cursor, "longValue", 0);
            LoggerUtil.logI(TAG, "uid   133------->" + uid);

            Object byteBufferValue = XposedHelpers.callMethod(cursor, "byteBufferValue", 1);
            LoggerUtil.logI(TAG, "byteBufferValue   57------->" + byteBufferValue);
            Object readInt32 = XposedHelpers.callMethod(byteBufferValue, "readInt32", false);
            LoggerUtil.logI(TAG, "readInt32   70------->" + readInt32);
            Object chatFull = XposedHelpers.callStaticMethod(XposedHelpers.findClass("org.telegram.tgnet.TLRPC$ChatFull", HookMain.classLoader)
//            Object chatFull = XposedHelpers.callStaticMethod(XposedHelpers.findClass("org.telegram.tgnet.TLRPC$Chat", classLoader)
                    , "TLdeserialize"
                    , byteBufferValue
                    , readInt32
                    , false
            );
            LoggerUtil.logI(TAG, "chatFull   146------->" + chatFull);

            XposedHelpers.callMethod(byteBufferValue, "reuse");
            HookUtil.printAllFieldForSuperclass(chatFull);//megagroup
            int participants_count = XposedHelpers.getIntField(chatFull, "participants_count");//参与者
            LoggerUtil.logI(TAG, "participants_count  ------->" + participants_count);
            if (participants_count == 0) {
                //TODO 如果数是零，去chats表里找


            }

            Object chat = UsersAndChats.getChat2(uid);
            LoggerUtil.logI(TAG, "chat   172------->" + chat);
            if (chat == null) {
//                continue;
            }

//            HookUtil.printAllFieldForSuperclass(chat);
            boolean megagroup = XposedHelpers.getBooleanField(chat, "megagroup");

            boolean isChannel = (boolean) XposedHelpers.callStaticMethod(XposedHelpers.findClass("org.telegram.messenger.ChatObject", classLoader), "isChannel", (int) uid, 0);
            boolean isNotInChat = (boolean) XposedHelpers.callStaticMethod(XposedHelpers.findClass("org.telegram.messenger.ChatObject", classLoader), "isNotInChat", chat);
            Object title = XposedHelpers.getObjectField(chat, "title");//群名
            LogTool.d("isNotInChat  96--->" + isNotInChat + "---->" + title);
            if (isChannel && !megagroup) {
                //满足这个条件才是channel的
            }


            LoggerUtil.logI(TAG, "isChannel 98 ------->" + isChannel + "----->" + megagroup + "----->" + title);
//            if (title.toString().contains("菠菜大佬人脉交流圈")){
//                LoggerUtil.logI(TAG,"有 此title  103------------>"+title);
//                HookUtil.printAllFieldForSuperclass(chatFull);
//            }else if (title.toString().contains("test_icc_1")){
//                LoggerUtil.logI(TAG,"有 此title  106------------>"+title);
//                HookUtil.printAllFieldForSuperclass(chatFull);
//            }

        }
        XposedHelpers.callMethod(cursor, "dispose");

        LoggerUtil.logI(TAG, "cursor   end  116------->" + cursor);
    }
}
