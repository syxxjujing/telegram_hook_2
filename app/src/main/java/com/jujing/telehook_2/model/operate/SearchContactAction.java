package com.jujing.telehook_2.model.operate;

import android.os.SystemClock;
import android.text.TextUtils;

import com.jujing.telehook_2.Global;
import com.jujing.telehook_2.hook.HookActivity;
import com.jujing.telehook_2.model.SendMessage;
import com.jujing.telehook_2.model.UsersAndChats;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.HookUtil;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.RandomUtil;
import com.jujing.telehook_2.util.WriteFileUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static com.jujing.telehook_2.HookMain.classLoader;
import static com.jujing.telehook_2.model.UsersAndChats.sendM;

public class SearchContactAction {


    private static final String TAG = "SearchContactAction";
    public static List<Long> firstIdList = new ArrayList<>();//记录第一轮获取成功人的id

    public static void handle(String path) {
        try {
            if (UsersAndChats.isStart) {
                LoggerUtil.sendLog7("正在转发，请稍候...");
                return;
            }
            UsersAndChats.isStart = true;
            LoggerUtil.sendLog7("开始转发...");
            List<String> stringList = WriteFileUtil.readFile(path);
            LoggerUtil.logI(TAG, "stringList  38---->" + stringList.size());
            LoggerUtil.sendLog7("共有" + stringList.size() + "个好友");
            String replyJson = WriteFileUtil.read(Global.STORAGE_LOCAL_REPLY_JSON);
            if (judgeSayHiContent1(replyJson)) {
                return;
            }
            SendVideoInitAction.initSayHiVideo(replyJson);

            UsersAndChats.sentNum = 0;
            firstIdList.clear();
            for (int i = 0; i < stringList.size(); i++) {
                String friends = stringList.get(i);
                LoggerUtil.logI(TAG, "friends 55 :" + friends + "-----" + i);
                if (judgeSent(friends)) {
                    LoggerUtil.logI(TAG, "以前发送过了 51 :" + friends + "-----" + i);
                    LoggerUtil.sendLog7("第" + (i + 1) + "个好友：" + friends + "，以前发送过了");
                    continue;
                }
                //发送成功消息 就记录一下，下次就不发了
                String sent_messages_user = WriteFileUtil.read(Global.SENT_MESSAGES_USER);
                if (TextUtils.isEmpty(sent_messages_user)) {
                    WriteFileUtil.write(friends, Global.SENT_MESSAGES_USER);
                } else {
                    WriteFileUtil.write(sent_messages_user + "," + friends, Global.SENT_MESSAGES_USER);
                }

                LoggerUtil.sendLog7("开始搜索第" + (i + 1) + "个好友：" + friends);
                isSendFinished = false;
                SearchContactAction searchContactAction = new SearchContactAction();
                searchContactAction.seachUsers(friends);
                for (int j = 0; j < 60 * 10; j++) {
//                    LoggerUtil.logI(TAG, "isSendFinished 69 :" + isSendFinished + "-----" + j);
                    if (!isSendFinished) {
                        SystemClock.sleep(1000);
                        if (!UsersAndChats.isStart) {
                            LoggerUtil.logI(TAG, "任务停止了 60 :" + isSendFinished + "-----" + j + "---->" + friends + "---->" + i);
                            HookActivity.showToast("任务停止了！!!");
                            LoggerUtil.sendLog7("任务停止了！!!");
                            return;
                        }

                    } else {

                        break;
                    }
                }
                String interval_friends0 = WriteFileUtil.read(Global.INTERVAL_FRIENDS);
                int interval_friends = timeFormat(interval_friends0);

                LoggerUtil.logI(TAG, "interval_friends 97 :" + interval_friends);
//                SystemClock.sleep(interval_friends);
                if (sleepTime(interval_friends)) {
                    return;
                }

            }
            String say_hi_round_interval0 = WriteFileUtil.read(Global.SAY_HI_ROUND_INTERVAL);
            LoggerUtil.logI(TAG, "say_hi_round_interval0 102 :" + say_hi_round_interval0);
            int say_hi_round_interval = timeFormat(say_hi_round_interval0);
            LoggerUtil.sendLog7("第一轮结束，间隔" + say_hi_round_interval + "秒后，开始第二轮");
            LoggerUtil.logI(TAG, "say_hi_round_interval 106 :" + say_hi_round_interval);
            if (sleepTime(say_hi_round_interval)) {
                return;
            }

            String replyJson_2 = WriteFileUtil.read(Global.STORAGE_LOCAL_REPLY_JSON_2);
            if (judgeSayHiContent2(replyJson_2)) {
                return;
            }
            SendVideoInitAction.initSayHiVideo(replyJson_2);
            String is_only_unread = WriteFileUtil.read(Global.IS_ONLY_UNREAD);
            LoggerUtil.logI(TAG, "is_only_unread   113--->" + is_only_unread);
            List<Long> readList = UserReadAction.checkReadNum();
            LoggerUtil.logI(TAG, "firstIdList 112 :" + firstIdList.size() + "----->" + is_only_unread + "---->" + readList.size());
            LoggerUtil.sendLog7("第二轮共有" + firstIdList.size() + "个好友");
            UsersAndChats.sentNum = 0;
            for (int i = 0; i < firstIdList.size(); i++) {
                long firstId = firstIdList.get(i);
                if (is_only_unread.equals("true")) {
                    boolean contains = readList.contains(firstId);
                    LoggerUtil.logI(TAG, "firstId 117 :" + firstId + "--->" + contains + "--->" + i);
                    if (contains) {
                        LoggerUtil.sendLog7(firstId + "为已读了第一轮消息，故不发送第二轮");
                        continue;
                    }
                }
                sendMission(firstId, replyJson_2);
            }


        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee  36 ---->" + CrashHandler.getInstance().printCrash(e));
        }
//        UsersAndChats.isStart = false;

    }

    private static boolean sleepTime(int interval) {
        for (int k = 0; k < interval; k++) {
            if (!UsersAndChats.isStart) {
                LoggerUtil.logI(TAG, "任务停止了 115 :" + interval + "-----" + k);
                HookActivity.showToast("任务停止了！!!");
                LoggerUtil.sendLog7("任务停止了！!!");
                return true;
            }
            SystemClock.sleep(1000);
        }
        return false;
    }

    private static int timeFormat(String str) {
        int interval_friends = 0;
//        String interval_friends0 = WriteFileUtil.read(Global.INTERVAL_FRIENDS);
        if (str.contains("-")) {
            String[] split = str.split("-");
            int start = Integer.parseInt(split[0]);
            int end = Integer.parseInt(split[1]);
            interval_friends = RandomUtil.randomNumber(start, end);
        } else {
            interval_friends = Integer.parseInt(str);
        }
        return interval_friends;
    }


    private static boolean judgeSayHiContent1(String replyJson) {
        try {

            LoggerUtil.logI(TAG, "replyJson 50 :" + replyJson);
            JSONArray jsonArray = new JSONArray(replyJson);
            if (jsonArray.length() == 0) {
                LoggerUtil.sendLog7("第一轮打招呼消息为空，请去设置");
                return true;
            }
            boolean isHave = false;
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jsonObject = new JSONObject(jsonArray.getString(j));
                String content = jsonObject.getString("content");
                LoggerUtil.logI(TAG, "content 60 :" + content + "-----" + j + "---->");
                if (!TextUtils.isEmpty(content)) {
                    isHave = true;
                    break;
                }
            }
            LoggerUtil.logI(TAG, "isHave 67 :" + isHave);
            if (!isHave) {
                LoggerUtil.sendLog7("第一轮打招呼消息为空，请去设置！！！");
                return true;
            }

        } catch (Exception e) {
            LoggerUtil.sendLog7("第一轮打招呼消息为空，请去设置！");
            return true;
        }
        return false;
    }

    private static boolean judgeSayHiContent2(String replyJson) {
        try {

            LoggerUtil.logI(TAG, "replyJson 169 :" + replyJson);
            JSONArray jsonArray = new JSONArray(replyJson);
            if (jsonArray.length() == 0) {
                LoggerUtil.sendLog7("第二轮打招呼消息为空，请去设置");
                return true;
            }
            boolean isHave = false;
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jsonObject = new JSONObject(jsonArray.getString(j));
                String content = jsonObject.getString("content");
                LoggerUtil.logI(TAG, "content 179 :" + content + "-----" + j + "---->");
                if (!TextUtils.isEmpty(content)) {
                    isHave = true;
                    break;
                }
            }
            LoggerUtil.logI(TAG, "isHave 185 :" + isHave);
            if (!isHave) {
                LoggerUtil.sendLog7("第二轮打招呼消息为空，请去设置！！！");
                return true;
            }

        } catch (Exception e) {
            LoggerUtil.sendLog7("第二轮打招呼消息为空，请去设置！");
            return true;
        }
        return false;
    }

    public static boolean isSendFinished = false;

    public void seachUsers(final String txt) {
        try {

            // txt="@Kenn_Wu";
            Class TL_contacts_search = classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_contacts_search");
            Object req = XposedHelpers.newInstance(TL_contacts_search);
            XposedHelpers.setObjectField(req, "q", txt);
            XposedHelpers.setIntField(req, "limit", 5);

            Class UserConfig = classLoader.loadClass("org.telegram.messenger.UserConfig");
            final int currentAccount = XposedHelpers.getStaticIntField(UserConfig, "selectedAccount");

            Class ConnectionsManager = classLoader.loadClass("org.telegram.tgnet.ConnectionsManager");
            Object ConnectionsManagerIns = XposedHelpers.callStaticMethod(ConnectionsManager, "getInstance", currentAccount);

            Class RequestDelegate = classLoader.loadClass("org.telegram.tgnet.RequestDelegate");
            Object cb = Proxy.newProxyInstance(classLoader, new Class[]{RequestDelegate}, new InvocationHandler() {
                @Override
                public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                    String mName = method.getName();
                    if (mName.equals("run")) {

                        run(objects[0], objects[1]);
                    }
                    return null;
                }

                void run(Object response, Object error) {

                    try {
                        if (error != null) {
                            String text = (String) XposedHelpers.getObjectField(error, "text");
                            int code = XposedHelpers.getIntField(error, "code");
                            LoggerUtil.logI(TAG, "搜索出错  153:" + text + ":" + code + "----->" + txt);
                            LoggerUtil.sendLog7("搜索出错：" + text + ":" + code + "----->" + txt);
                            return;
                        }
                        if (response == null) {
                            LoggerUtil.logI(TAG, "搜索出错:response==null----》" + txt);
                            LoggerUtil.sendLog7("搜索出错:response==null---->" + txt);
                            return;
                        }

                        ArrayList users = (ArrayList) XposedHelpers.getObjectField(response, "users");
                        if (users == null || users.isEmpty()) {
                            LoggerUtil.logI(TAG, "搜索出错:users==null||users.isEmpty----》" + txt);
                            LoggerUtil.sendLog7("搜索出错:users==null||users.isEmpty---->" + txt);
                            isSendFinished = true;
                            return;
                        }

                        ArrayList chats = (ArrayList) XposedHelpers.getObjectField(response, "chats");

                        saveSearchRespondUsers(chats, users);
                        for (int a = 0; a < users.size(); a++) {
                            Object user = users.get(a);
//                            HookUtil.printAllFieldForSuperclass(user);
                            final long id = XposedHelpers.getLongField(user, "id");
                            LoggerUtil.logI(TAG, "user id:" + id);
                            String t = txt.replaceFirst("@", "");
                            String username = (String) XposedHelpers.getObjectField(user, "username");
                            LoggerUtil.logI(TAG, "user username:" + username);
                            String first_name = (String) XposedHelpers.getObjectField(user, "first_name");
                            LoggerUtil.logI(TAG, "user first_name:" + first_name);

                            if (t.equals(username)) {//忽略大小写
                                LoggerUtil.logI(TAG, "搜索到用户 179----->" + username + " id:" + id + " first_name:" + first_name);
                                LoggerUtil.sendLog7("搜索到用户:" + username + " id:" + id + " first_name:" + first_name);
                                firstIdList.add(id);
                                //保存用户
                                saveSearchUsers(id);
//                                SendMessage.sendMessage0(currentAccount, "12346", id);
//                                addContact(user);
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        SystemClock.sleep(5000);
//                                        SendMessage.sendMessage("hell",id);
//                                    }
//                                }).start();

                            } else if (t.equals(username.toLowerCase())) {
                                firstIdList.add(id);
                                LoggerUtil.logI(TAG, "搜索到用户 193----->" + username + " id:" + id + " first_name:" + first_name);
                                LoggerUtil.sendLog7("搜索到用户:" + username + " id:" + id + " first_name:" + first_name);

                                saveSearchUsers(id);
                            } else {
                                isSendFinished = true;
                                LoggerUtil.sendLog7("搜索失败！");
                            }

                        }
                    } catch (Exception e) {
                        LoggerUtil.logI(TAG, "eee  92---->" + CrashHandler.getInstance().printCrash(e));
                        isSendFinished = true;
                    }


                }
            });
            XposedHelpers.callMethod(ConnectionsManagerIns, "sendRequest", req, cb);

        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee  101---->" + CrashHandler.getInstance().printCrash(e));
        }
    }

    public static void addContact(Object user, long id) {

        try {

            Class UserConfig = classLoader.loadClass("org.telegram.messenger.UserConfig");
            int currentAccount = XposedHelpers.getStaticIntField(UserConfig, "selectedAccount");


            Class AccountInstance = classLoader.loadClass("org.telegram.messenger.AccountInstance");
            Object AccountInstanceIns = XposedHelpers.callStaticMethod(AccountInstance,
                    "getInstance", currentAccount);

            Object contactsController = XposedHelpers.callMethod(AccountInstanceIns, "getContactsController");
            XposedHelpers.callMethod(contactsController, "addContact", user, true);

            XposedHelpers.callMethod(AccountInstanceIns, "getNotificationCenter");
            int index = 0;
            try {
                index = Integer.parseInt(WriteFileUtil.read(Global.ADDED_CONTACTS_NUM));
            } catch (Exception e) {
            }
            LoggerUtil.logI(TAG, "index  236---> " + index);

            WriteFileUtil.write(index + 1 + "", Global.ADDED_CONTACTS_NUM);

//            LoggerUtil.logI(TAG, "添加用户到通讯录  124---->"+user);


            String added_contact = WriteFileUtil.read(Global.ADDED_CONTACT);
            if (TextUtils.isEmpty(added_contact)) {
                WriteFileUtil.write(id + "", Global.ADDED_CONTACT);
            } else {
                WriteFileUtil.write(added_contact + "," + id, Global.ADDED_CONTACT);
            }

        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee  141---->" + CrashHandler.getInstance().printCrash(e));
        }

    }

    void saveSearchUsers(final long user) {


        try {

            Class UserConfig = classLoader.loadClass("org.telegram.messenger.UserConfig");
            int currentAccount = XposedHelpers.getStaticIntField(UserConfig, "selectedAccount");


            Class MessagesStorage = classLoader.loadClass("org.telegram.messenger.MessagesStorage");
            Object MessagesStorageIns = XposedHelpers.callStaticMethod(MessagesStorage,
                    "getInstance", currentAccount);

            Object DispatchQueueObj = XposedHelpers.callMethod(MessagesStorageIns, "getStorageQueue");
            final Object sqLiteDatabase = XposedHelpers.callMethod(MessagesStorageIns, "getDatabase");
            XposedHelpers.callMethod(DispatchQueueObj, "postRunnable", new Runnable() {
                @Override
                public void run() {
                    LoggerUtil.logI(TAG, "saveSearchUsers   124====");
                    try {
                        Object state = XposedHelpers.callMethod(sqLiteDatabase, "executeFast", "REPLACE INTO search_recent VALUES(?, ?)");

                        XposedHelpers.callMethod(state, "requery");
                        XposedHelpers.callMethod(state, "bindLong", 1, user);
                        XposedHelpers.callMethod(state, "bindInteger", 2, (int) (System.currentTimeMillis() / 1000));
                        XposedHelpers.callMethod(state, "step");
                        XposedHelpers.callMethod(state, "dispose");
                        LoggerUtil.logI(TAG, "saveSearchUsers   133====");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String replyJson = WriteFileUtil.read(Global.STORAGE_LOCAL_REPLY_JSON);
                                sendMission(user, replyJson);
                            }
                        }).start();


                    } catch (Exception e) {
                        LoggerUtil.logI(TAG, "eee  133---->" + CrashHandler.getInstance().printCrash(e));
                    }
                }
            });
            LoggerUtil.logI(TAG, "saveSearchUsers   139====");

        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee  141---->" + CrashHandler.getInstance().printCrash(e));
        }


    }

    public static void sendMission(long user_id, String replyJson) {
        try {

            LoggerUtil.logI(TAG, "replyJson 289 :" + replyJson + "---->" + user_id);
            JSONArray jsonArray = new JSONArray(replyJson);
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jsonObject = new JSONObject(jsonArray.getString(j));
                String content = jsonObject.getString("content");
                LoggerUtil.logI(TAG, "content 256 :" + content + "-----" + j + "---->" + user_id);
                if (!UsersAndChats.isStart) {
                    LoggerUtil.logI(TAG, "任务停止了 300 :" + "-----" + j + "---->" + user_id);
                    HookActivity.showToast("任务停止了！");
                    LoggerUtil.sendLog7("任务停止了！");
                    return;
                }


                if (sendM(user_id, j, content, TAG)) {
                    continue;
                }

                int interval_messages = 0;
                String interval_messages0 = WriteFileUtil.read(Global.INTERVAL_MESSAGES);
                if (interval_messages0.contains("-")) {
                    String[] split = interval_messages0.split("-");
                    int start = Integer.parseInt(split[0]);
                    int end = Integer.parseInt(split[1]);
                    interval_messages = RandomUtil.randomNumber(start, end);
                } else {
                    interval_messages = Integer.parseInt(interval_messages0);
                }

                LoggerUtil.logI(TAG, "interval_messages 309 :" + interval_messages + "---->" + user_id);
//                for (int k = 0; k < interval_messages; k++) {
//                    if (!UsersAndChats.isStart) {
//                        LoggerUtil.logI(TAG, "任务停止了 285 :" + interval_messages + "-----" + k+"---->"+user_id);
//                        HookActivity.showToast("任务停止了！");
//                        return;
//                    }
//                    SystemClock.sleep(1000);
//                }

                SystemClock.sleep(interval_messages*1000);


            }
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "e 327 :" + CrashHandler.getInstance().printCrash(e) + "---->" + user_id);
        }
        isSendFinished = true;


    }

    void saveSearchRespondUsers(List chats, List users) {
        try {
            //保存搜索结果

            Class UserConfig = classLoader.loadClass("org.telegram.messenger.UserConfig");
            int currentAccount = XposedHelpers.getStaticIntField(UserConfig, "selectedAccount");
            Class MessagesController = classLoader.loadClass("org.telegram.messenger.MessagesController");
            Object MessagesControllerIns = XposedHelpers.callStaticMethod(MessagesController,
                    "getInstance", currentAccount);

            XposedHelpers.callMethod(MessagesControllerIns, "putChats", chats, false);
            XposedHelpers.callMethod(MessagesControllerIns, "putUsers", users, false);


            Class MessagesStorage = classLoader.loadClass("org.telegram.messenger.MessagesStorage");
            Object MessagesStorageIns = XposedHelpers.callStaticMethod(MessagesStorage,
                    "getInstance", currentAccount);

            XposedHelpers.callMethod(MessagesStorageIns, "putUsersAndChats", users, chats, true, true);

            LoggerUtil.logI(TAG, "saveSearchRespondUsers   167====");
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee  167---->" + CrashHandler.getInstance().printCrash(e));
        }
    }

    public static boolean judgeSent(String username) {
        boolean isHave = false;
        try {
            String added_friend_username = WriteFileUtil.read(Global.SENT_MESSAGES_USER);
            String[] split1 = added_friend_username.split(",");
            for (String wxid : split1) {
                if (wxid.equals(username)) {
                    isHave = true;
                    break;
                }
            }
        } catch (Exception e) {

        }
        return isHave;
    }
}
