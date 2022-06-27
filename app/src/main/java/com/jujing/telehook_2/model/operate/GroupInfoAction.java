package com.jujing.telehook_2.model.operate;

import android.text.TextUtils;

import com.jujing.telehook_2.Global;
import com.jujing.telehook_2.hook.HookActivity;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.ExecutorUtil;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.WriteFileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.robv.android.xposed.XposedHelpers;

import static com.jujing.telehook_2.HookMain.classLoader;
import static com.jujing.telehook_2.model.UsersAndChats.getAccountInstance;
import static com.jujing.telehook_2.model.UsersAndChats.getCurrentUserId;
import static com.jujing.telehook_2.model.operate.SendForwardAction.getDatabase;

public class GroupInfoAction {


    private static final String TAG = "GroupInfoAction";
    public static boolean isStart = false;

    public static void handle() {
        ExecutorUtil.doExecute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isStart) {
                        HookActivity.showToast("正在采集，请稍候...");
                        return;
                    }
                    isStart = true;
                    String read = WriteFileUtil.read(Global.GROUP_CHAT_ID);
                    if (read.contains(",")){
                        String[] split = read.split(",");
                        for (int i = 0; i < split.length; i++) {
                            LoggerUtil.logI(TAG, "split  45 :" + split[i]+"---->"+i);
                            loadUser(Long.parseLong(split[i]));
                        }
                    }else{
                        long chat_id = Long.parseLong(WriteFileUtil.read(Global.GROUP_CHAT_ID));
                        loadUser(chat_id);
                    }


                } catch (Exception e) {
                    LoggerUtil.logI(TAG, "ee 42:" + CrashHandler.getInstance().printCrash(e));
                }

                isStart = false;

            }
        });


    }


    public static void loadUser(long chatId) {


        try {

            WriteFileUtil.write("", Global.GROUP_CHAT_INFO);
//            Thread.sleep(5000);
//            long chatId = 0;//群ID

            ArrayList loadedUsers = new ArrayList<>();


            Object database = getDatabase(classLoader);


            Object accountInstanceIns = getAccountInstance(classLoader);
            Object userConfig = XposedHelpers.callMethod(accountInstanceIns, "getUserConfig");
            long clientUserId = XposedHelpers.getLongField(userConfig, "clientUserId");


            LoggerUtil.logI(TAG, "clientUserId  86:" + clientUserId);
            String sql = "SELECT us.data, us.status, cu.data, cu.date FROM channel_users_v2 as cu LEFT JOIN users as us ON us.uid = cu.uid WHERE cu.did = " + (-chatId) + " ORDER BY cu.date DESC";

            Object[] aaaa = new Object[]{sql, new Object[]{}};

            Object cursor = XposedHelpers.callMethod(database, "queryFinalized", aaaa);


            int cuid = getCurrentUserId(classLoader);

            Class ConnectionsManager = classLoader.loadClass("org.telegram.tgnet.ConnectionsManager");
            Object ConnectionsManagerIns = XposedHelpers.callStaticMethod(ConnectionsManager, "getInstance", cuid);
            int ct = (int) XposedHelpers.callMethod(ConnectionsManagerIns, "getCurrentTime");

//            List<String> list_user_info = new ArrayList<>();
            while ((boolean) XposedHelpers.callMethod(cursor, "next")) {
                try {
                    Object user = null;
                    Object participant = null;

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
                            int expires = (int) XposedHelpers.callMethod(cursor, "intValue", 1);
                            XposedHelpers.setIntField(status, "expires", expires);
                        }

                        loadedUsers.add(user);


//                        StringBuilder sbTxt = new StringBuilder();

                        String first_name = (String) XposedHelpers.getObjectField(user, "first_name");
                        String nick = first_name;
                        String last_name = (String) XposedHelpers.getObjectField(user, "last_name");
//                        sbTxt.append("user:" + first_name);

                        if (!TextUtils.isEmpty(last_name)) {
//                            sbTxt.append(last_name);
                            nick = first_name + last_name;
                        }
                        String is_group_nick_chinese = WriteFileUtil.read(Global.IS_GROUP_NICK_CHINESE);
                        if (is_group_nick_chinese.equals("true")) {
                            try {
                                Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
                                Matcher m = p.matcher(nick);
                                if (m.find()) {
                                } else {
                                    continue;
                                }
                            } catch (Exception e) {
                                continue;
                            }
                        }


                        long uid = XposedHelpers.getLongField(user, "id");
//                        sbTxt.append(" uid:" + uid);
                        if (status != null) {
                            int expires = XposedHelpers.getIntField(status, "expires");
//                            sbTxt.append(" expires:" + expires);
                            String is_group_online = WriteFileUtil.read(Global.IS_GROUP_ONLINE);
                            if (is_group_online.equals("true")) {
                                if (expires > ct) {
//                                sbTxt.append(" online");
                                } else {
                                    continue;
                                }
                            }
                        }
                        LoggerUtil.logI(TAG, "uid  174---->" + uid + "---->" + nick);

//                        LoggerUtil.logI(TAG,sbTxt.toString());
                        int i = Integer.parseInt(WriteFileUtil.read(Global.IS_GROUP_DAY));

                        loadMsg(uid, chatId, i, nick, "");


                    }
                } catch (Exception e) {

                    LoggerUtil.logI(TAG, "ee  181------->" + CrashHandler.getInstance().printCrash(e));
                }
            }

            XposedHelpers.callMethod(cursor, "dispose");

            LoggerUtil.logI(TAG, "loadedUsers size:" + loadedUsers.size());
            HookActivity.showToast("获取成功");
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "ee  140------->" + CrashHandler.getInstance().printCrash(e));
        }


    }

    public static void loadMsg(long fiterUser, long groupId, int day, String nick, String title) {
        try {


//            long timeLimit = 60 * 60 * 24 * 3;//三天
            long timeLimit = 60 * 60 * 24 * day;//三天

            long timeM = System.currentTimeMillis() / 1000 - timeLimit;
//            timeM = 0;//时间段查询

//            long fiterUser = 0;//用户ID
//            long groupId = 0;//群ID

//            Thread.sleep(5000);


            //SQLiteDatabase database= MessagesStorage.getInstance(UserConfig.selectedAccount).getDatabase();

            Object database = getDatabase(classLoader);
            ;


            String sql = "SELECT  data, date FROM messages_v2 where  uid=" + (-groupId) + " and date>" + timeM;
            Object[] aaaa = new Object[]{sql, new Object[]{}};
            Object cursor = XposedHelpers.callMethod(database, "queryFinalized", aaaa);
            // SQLiteCursor cursor = database.queryFinalized(sql);

//            LoggerUtil.logI(TAG, "sql:" + sql);
            if (cursor == null) {
                LoggerUtil.logI(TAG, "cursor==null  240====");
                return;
            }


            List msgList = new ArrayList();


            while ((boolean) XposedHelpers.callMethod(cursor, "next")) {


                // NativeByteBuffer data = cursor.byteBufferValue(0);
                Object data = XposedHelpers.callMethod(cursor, "byteBufferValue", 0);
                if (data != null) {

                    int i = (int) XposedHelpers.callMethod(data, "readInt32", false);
                    Class Message = classLoader.loadClass("org.telegram.tgnet.TLRPC$Message");
                    Object message = XposedHelpers.callStaticMethod(Message, "TLdeserialize", data, i, false);

                    // TLRPC.Message message = TLRPC.Message.TLdeserialize(data, data.readInt32(false), false);

                    Class MessageObject = classLoader.loadClass("org.telegram.messenger.MessageObject");
                    long fromUid = (long) XposedHelpers.callStaticMethod(MessageObject, "getFromChatId", message);

                    // long fromUid = MessageObject.getFromChatId(message);
                    LoggerUtil.logI(TAG,"loadMsg fromUid  265:" + fromUid+"--->"+title);

                    if (fromUid == fiterUser) {
                        msgList.add(message);
                    }
                }


                int date = (int) XposedHelpers.callMethod(cursor, "intValue", 1);
                //int date = cursor.intValue(1);
                //  LoggerUtil.logI(TAG,"date:"+date);


            }


            String sts = "uid:" + fiterUser + " 昵称:" + nick + " 聊天数量:" + msgList.size() + "\n";
            LoggerUtil.logI(TAG, sts);
//            WriteFileUtil.writeAppend(sts, Global.GROUP_CHAT_INFO);


        } catch (Exception e) {
            LoggerUtil.logI(TAG, "ee  276------->" + CrashHandler.getInstance().printCrash(e));
        }
    }
}
