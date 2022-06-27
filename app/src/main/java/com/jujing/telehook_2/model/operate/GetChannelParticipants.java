package com.jujing.telehook_2.model.operate;

import static com.jujing.telehook_2.HookMain.classLoader;

import android.content.Context;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;


import com.jujing.telehook_2.Global;
import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.HttpApi;
import com.jujing.telehook_2.bean.ChatBean;
import com.jujing.telehook_2.bean.GroupUserBean;
import com.jujing.telehook_2.hook.HookActivity;
import com.jujing.telehook_2.model.SendMessage;
import com.jujing.telehook_2.model.UsersAndChats;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.DateUtils;
import com.jujing.telehook_2.util.ExecutorUtil;
import com.jujing.telehook_2.util.HookUtil;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.MatchUtil;
import com.jujing.telehook_2.util.WriteFileUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XposedHelpers;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetChannelParticipants implements InvocationHandler {

    private static final String TAG = "GetChannelParticipants";
    Context context;
    ClassLoader classLoader;
    long chatId;
    String tag = getClass().getSimpleName();
    String title;

    public static List<Long> chatidList = new ArrayList<>();

    public GetChannelParticipants(Context context, long chatId, String title) {
        this.chatId = chatId;
        this.context = context;
        classLoader = HookMain.classLoader;
        this.title = title;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

        String mName = method.getName();
        LoggerUtil.logI(TAG + chatId, tag + " invoke mName:" + mName);

        switch (mName) {
            case "run":

                run(objects[0], objects[1]);
        }
        return null;
    }

    public static boolean Stop = false;

    public static boolean isStart = false;

    public static void handleAll(final boolean isRe) {
        if (isStart) {
            HookActivity.showToast("正在采集，请稍后...");
            return;
        }

        ExecutorUtil.doExecute(new Runnable() {
            @Override
            public void run() {
                try {

                    WriteFileUtil.write("1", Global.IS_START_CAIJI);
                    isStart = true;
                    Stop = false;
//                    String chat_ids = WriteFileUtil.read(Global.CHAT_IDS);
                    List<ChatBean> groupIdList = UserReadAction.getGroupIdList();

                    LoggerUtil.logI(TAG, "chat_ids  59---> " + groupIdList.size() + "---->" + isRe);
                    if (groupIdList.size() == 0) {
                        HookActivity.showToast("请先入群！");
                        LoggerUtil.sendLog("请先入群！");
                        isStart = false;
                        WriteFileUtil.write("0", Global.IS_START_CAIJI);
                        return;
                    }

//                    if (chat_ids.contains(",")) {
//                        String[] split = chat_ids.split(",");
                    int count = 0;
                    for (int i = groupIdList.size() - 1; i >= 0; i--) {
                        long chat_id = groupIdList.get(i).getChatId();
                        String index = WriteFileUtil.read(Global.GOT_INFO_INDEX + chat_id);
                        LoggerUtil.logI(TAG, "handleAll  87---> " + chat_id + "---->" + i + "---->" + count + "---->" + index + "--->" + isRe + "---->" + i);
                        if (index.equals("-1")) {
                            if (isRe) {
                                WriteFileUtil.write("0", Global.GOT_INFO_INDEX + chat_id);
                            } else {
                                continue;
                            }

                        }
                        handle(HookActivity.baseActivity, chat_id, i);
                        for (int j = 0; j < 10; j++) {
                            SystemClock.sleep(1000);
                            if (Stop) {
                                LoggerUtil.logI(TAG + chat_id, "Stop  76===");
                                return;
                            }
                        }
                        count++;
                        if (count % 5 == 0) {
                            for (int j = 0; j < 60; j++) {
                                SystemClock.sleep(1000);
                                if (Stop) {
                                    LoggerUtil.logI(TAG + chat_id, "Stop  89===");
                                    return;
                                }
                            }
                        }
                    }

//                    } else {
//                        handle(HookActivity.baseActivity, Long.parseLong(chat_ids));
//                    }
                } catch (Exception e) {
                    LoggerUtil.logI(TAG, "eee  86----》" + CrashHandler.getInstance().printCrash(e));
                }

//                isStart = false;
            }
        });


    }

    public static void handle(final Context context, final long chatId, int i) {
        if (Stop) {
            LoggerUtil.logI(TAG + chatId, "Stop  84");
            return;
        }
        if (chatidList.contains(chatId)) {
            LoggerUtil.logI(TAG, "chatId  正在进行---> " + chatId);
            return;
        }
        chatidList.add(chatId);
        ExecutorUtil.doExecute(new Runnable() {
            @Override
            public void run() {
                try {
                    Object chat = UsersAndChats.getChat2(chatId);

                    String title = (String) XposedHelpers.getObjectField(chat, "title");
                    LoggerUtil.logI(TAG, "title  169---> " + title + "---->" + chatId + "---->" + i);


                    GetChannelParticipants.newReq(context, chatId);

                } catch (Exception e) {
                    WriteFileUtil.write("-1", Global.GOT_INFO_INDEX + chatId);
                }

            }
        });


    }

    public static int newReqCount = 0;

    public static void newReq(Context context, long chatId) {
        try {

            if (!chatidList.contains(chatId)) {
                chatidList.add(chatId);
            }
            LoggerUtil.logI(TAG + chatId, "newReq  46---> " + chatId);
            Object chat = UsersAndChats.getChat2(chatId);
//            HookUtil.printAllFieldForSuperclass(chat);
            String title = (String) XposedHelpers.getObjectField(chat, "title");
            LoggerUtil.sendLog("正在采集群:" + title);
            LoggerUtil.logI(TAG + chatId, "title  108---> " + title + "---->" + chatId);
            LoggerUtil.logI(TAG, "title  197---> " + title + "---->" + chatId);
            //限制次数！！
            LoggerUtil.logI(TAG, "newReqCount  203---->" + newReqCount + "--->" + chatId + "---->" + title);
//            if (newReqCount > 3) {
//                LoggerUtil.logI(TAG, "读起来了  206---> " + title + "---->" + chatId);
//                return;
//            }
            newReqCount++;

            ClassLoader classLoader = HookMain.classLoader;
            Object AccountInstanceIns = Tools.getAccountInstance(classLoader);

            Object connectionsManager = XposedHelpers.callMethod(AccountInstanceIns, "getConnectionsManager");
            Object messagesController = XposedHelpers.callMethod(AccountInstanceIns, "getMessagesController");
            Object channel = XposedHelpers.callMethod(messagesController, "getInputChannel", chatId);
            Class TL_channels_getParticipants = classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_channels_getParticipants");
            Object req = XposedHelpers.newInstance(TL_channels_getParticipants);
            LoggerUtil.logI(TAG + chatId, "newReq  119---> " + chatId);
            Class TL_channelParticipantsRecent = classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_channelParticipantsRecent");
            Object filter = XposedHelpers.newInstance(TL_channelParticipantsRecent);
            XposedHelpers.setObjectField(req, "channel", channel);
            XposedHelpers.setObjectField(req, "filter", filter);
            int index = 0;
            try {
                index = Integer.parseInt(WriteFileUtil.read(Global.GOT_INFO_INDEX + chatId));
            } catch (Exception e) {
            }
            LoggerUtil.logI(TAG + chatId, "index  129---> " + index);

            XposedHelpers.setIntField(req, "offset", index);//从第几个开始获取，群员太多的有必要分批获取
            XposedHelpers.setIntField(req, "limit", 200);//数量
            Class RequestDelegate = classLoader.loadClass("org.telegram.tgnet.RequestDelegate");
            Object callback = Proxy.newProxyInstance(classLoader, new Class[]{RequestDelegate}, new GetChannelParticipants(context, chatId, title));
            XposedHelpers.callMethod(connectionsManager, "sendRequest", req, callback);
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "e  141---> " + CrashHandler.getInstance().printCrash(e));
            WriteFileUtil.write("-1", Global.GOT_INFO_INDEX + chatId);
//            Tool.printException(e);
        }
    }

    void run(Object response, Object error) {
        ExecutorUtil.doExecute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Stop) {
                        LoggerUtil.logI(TAG + chatId, "Stop  144");
                        return;
                    }

                    LoggerUtil.logI(TAG, "newReqCount  241---->" + newReqCount + "--->" + chatId);
                    newReqCount--;
                    Class ConnectionsManager = classLoader.loadClass("org.telegram.tgnet.ConnectionsManager");
                    int cuid = Tools.getCurrentUserId(classLoader);
                    Object ConnectionsManagerIns = XposedHelpers.callStaticMethod(ConnectionsManager, "getInstance", cuid);
                    int ct = (int) XposedHelpers.callMethod(ConnectionsManagerIns, "getCurrentTime");

                    if (error != null) {

                        int code = XposedHelpers.getIntField(error, "code");
                        String text = (String) XposedHelpers.getObjectField(error, "text");
                        LoggerUtil.logI(TAG + chatId, "获取群用户信息错误code:" + code + " text:" + text);
//                WriteFileUtil.write("-1", Global.GOT_INFO_INDEX + chatId);
                        WriteFileUtil.write("0", Global.GOT_INFO_INDEX + chatId);

                        LoggerUtil.sendLog("获取群用户信息错误,code:" + code + " text:" + text + ",群:" + title);
                        LoggerUtil.sendLog("等2分后，重新采集,群:" + title);
                        for (int j = 0; j < 60 * 2; j++) {
                            SystemClock.sleep(1000);
                            if (Stop) {
                                LoggerUtil.logI(TAG + chatId, "Stop  89===");
                                return;
                            }
                        }

                        LoggerUtil.sendLog("重新开始采集，群为：" + title);
                        GetChannelParticipants.newReq(context, chatId);
                        return;
                    }

                    List users = (List) XposedHelpers.getObjectField(response, "users");
                    int count = XposedHelpers.getIntField(response, "count");
                    LoggerUtil.logI(TAG + chatId, "count  91:" + count);
                    if (users == null || users.isEmpty()) {
                        LoggerUtil.logI(TAG + chatId, "获取群用户信息错误为空");

//                WriteFileUtil.write("-1", Global.GOT_INFO_INDEX + chatId);
                        WriteFileUtil.write("0", Global.GOT_INFO_INDEX + chatId);

                        LoggerUtil.sendLog("采集完毕,群:" + title);
                        for (int j = 0; j < 60 * 2; j++) {
                            SystemClock.sleep(1000);
                            if (Stop) {
                                LoggerUtil.logI(TAG + chatId, "Stop  89===");
                                return;
                            }
                        }

                        LoggerUtil.sendLog("重新开始采集,群:" + title);
                        GetChannelParticipants.newReq(context, chatId);
                        return;
                    }

                    LoggerUtil.logI(TAG + chatId, "获取群用户信息成功，数量:" + users.size());

                    int checkCount = 0;

                    List<GroupUserBean> groupUserBeanList = new ArrayList<>();
                    for (int a = 0; a < users.size(); a++) {
                        Object user = users.get(a);
//                HookUtil.printAllFieldForSuperclass(user);
                        String username = (String) XposedHelpers.getObjectField(user, "username");
                        if (TextUtils.isEmpty(username)) {
                            continue;
                        }


                        long id = XposedHelpers.getLongField(user, "id");
//                if (judgeGot(id + "", chatId)) {
//                    LoggerUtil.logI(TAG + chatId, "以前获取过了 116 :" + id + "-----" + a + "---->" + chatId);
//                    continue;
//                }

                        String first_name = (String) XposedHelpers.getObjectField(user, "first_name");
                        String nick = first_name;
                        String last_name = (String) XposedHelpers.getObjectField(user, "last_name");
                        if (!TextUtils.isEmpty(last_name)) {
                            nick = first_name + last_name;
                        }
                        LoggerUtil.logAll(TAG, "nick  195----" + a + "---->" + nick);
                        String is_group_nick_chinese = WriteFileUtil.read(Global.IS_GROUP_NICK_CHINESE);
                        if (is_group_nick_chinese.equals("true")) {

                            boolean hasChinese = MatchUtil.hasChinese(nick);
//                    LoggerUtil.logI(TAG + chatId, "hasChinese  259---->" + is_group_nick_chinese + "---->" + nick + "---->" + hasChinese);
                            if (!hasChinese) {
                                continue;
                            }
                        }
                        int expires = 0;
                        int online_day = 0;
                        boolean isContinue = false;
                        Object status = XposedHelpers.getObjectField(user, "status");
                        if (status != null) {
                            expires = XposedHelpers.getIntField(status, "expires");
                            //                    LoggerUtil.logAll(TAG, "expires  119---->"+expires+"---->"+nick);


                            if (expires > ct) {
                                //在线
                            }
//                    if (expires == 0) {//org.telegram.tgnet.TLRPC$TL_userStatusOffline@95d4a
////                            HookUtil.printAllField(status);
////                            HookUtil.printAllFieldForSuperclass(status);
//                        LoggerUtil.logI(TAG + chatId, "expires ==0  332---->" + id + "---->" + nick + "---->" + expires + "---->" + online_day+"----->"+isContinue);
//
//
//                        continue;
//                    }

                            if (expires != 0) {
                                online_day = Integer.parseInt(WriteFileUtil.read(Global.ONLINE_DAY));
//                            LoggerUtil.logAll(TAG, "expires  212---->" + expires + "---->" + nick + "---->" + online_day);
                                int i = expires + online_day * 24 * 60 * 60;

                                if (i < System.currentTimeMillis() / 1000) {
                                    isContinue = true;
//                            continue;
                                }
                            }

                        }
                        if (isContinue) {
                            if (status != null) {
                                if (status.toString().contains("TL_userStatusRecently") || status.toString().contains("TLRPC$TL_userStatusOnline")) {
                                    LoggerUtil.logI(TAG + chatId, "expires ==0 Recently or online  352---->" + id + "---->" + nick + "---->" + expires + "---->" + status + "---->" + username);

                                } else {
                                    continue;
                                }
                            } else {
                                continue;
                            }
                        }

                        if (expires == 0) {
                            LoggerUtil.logI(TAG + chatId, "expires ==0  354---->" + id + "---->" + nick + "---->" + expires + "---->" + status + "---->" + username);
                            if (status != null) {
                                if (status.toString().contains("TL_userStatusRecently") || status.toString().contains("TLRPC$TL_userStatusOnline")) {
                                    LoggerUtil.logI(TAG + chatId, "expires ==0 Recently or online  357---->" + id + "---->" + nick + "---->" + expires + "---->" + status + "---->" + username);

                                } else {
                                    continue;
                                }
                            } else {
                                continue;
                            }
                        }

                        if (judgeGotUsername(username)) {
                            LoggerUtil.logI(TAG + chatId, "以前获取过了 344 :" + username + "-----" + a + "---->" + chatId);
                            continue;
                        }

                        String black_path = WriteFileUtil.read(Global.BLACK_PATH);
                        List<String> stringList = WriteFileUtil.readFile(black_path);
                        if (stringList.contains(username)) {
                            LoggerUtil.logI(TAG, "在黑名单之中  351---->" + stringList.size() + "---->" + username + "--->" + black_path);
                            continue;
                        }
                        LoggerUtil.logI(TAG + chatId, "expires  372---->" + is_group_nick_chinese + "---->" + nick + "---->" + expires + "---->" + online_day + "----->" + isContinue + "---->" + id + "---->" + username);

//                String is_group_about_chinese = WriteFileUtil.read(Global.IS_GROUP_ABOUT_CHINESE);
//                if (is_group_about_chinese.equals("true")) {
//                    LoadFullUser.newReq(user, username, nick, title, chatId, users.size());
//                } else {
                        checkCount++;
//                long id = XposedHelpers.getLongField(user, "id");
//                LoggerUtil.logI(TAG + chatId, "id  351--->" + id);
//                saveSearchUsers(id);

                        saveMMM(nick, username, title, chatId, users.size());
//                }
                        try {
                            GroupUserBean bean = new GroupUserBean();
                            bean.setUserName(username);
                            bean.setNickName(nick);
                            bean.setGroup(title);
                            bean.setCollectTime(DateUtils.formatDate(System.currentTimeMillis()));
                            groupUserBeanList.add(bean);
                        } catch (Exception e) {

                        }
//                SystemClock.sleep(5000);
                    }
                    try {
                        LoggerUtil.sendLog("检测到符合条件用户的数量为:" + checkCount + ",群:" + title);


                        ArrayList<ArrayList<GroupUserBean>> lists = new ArrayList<>();
                        ArrayList<GroupUserBean> list = new ArrayList<>();
                        for (int i = 0; i < groupUserBeanList.size(); i++) {
                            if (i % 100 == 0 && i != 0) {

                                lists.add(list);
                                list = new ArrayList<>();
                            }
                            list.add(groupUserBeanList.get(i));
                        }
                        lists.add(list);

                        LoggerUtil.logI(TAG + chatId, "list  350--->" + list.size());
                        LoggerUtil.logI(TAG + chatId, "groupUserBeanList  351--->" + groupUserBeanList.size());//1357
                        LoggerUtil.logI(TAG + chatId, "lists  352--->" + lists.size());

                        for (int i = 0; i < lists.size(); i++) {
                            ArrayList<GroupUserBean> list1 = lists.get(i);
                            LoggerUtil.logI(TAG + chatId, "list1  358--->" + list1.size() + "---->" + i + "---->" + list1.get(0));

                            postJson(list1);
                        }
                    } catch (Exception e) {

                    }

//            String is_group_nick_chinese = WriteFileUtil.read(Global.IS_GROUP_NICK_CHINESE);
//            if (is_group_nick_chinese.equals("true")) {
                    for (int i = 0; i < 40; i++) {
                        SystemClock.sleep(1000);
                        if (Stop) {
                            LoggerUtil.logI(TAG + chatId, "Stop  229");
                            return;
                        }
                    }
//            }
                    int index = 0;
                    try {
                        index = Integer.parseInt(WriteFileUtil.read(Global.GOT_INFO_INDEX + chatId));
                    } catch (Exception e) {
                    }
                    LoggerUtil.logI(TAG + chatId, "index  174:" + index + "---->" + (index + users.size()));
                    WriteFileUtil.write(index + users.size() + "", Global.GOT_INFO_INDEX + chatId);


                    for (int i = 0; i < 60 * 10; i++) {
                        LoggerUtil.logI(TAG + chatId, "newReqCount  488:" + newReqCount + "---->" + i);
                        if (Stop) {
                            LoggerUtil.logI(TAG + chatId, "Stop  490");
                            return;
                        }
                        if (newReqCount > 3) {
                            SystemClock.sleep(1000);
                        } else {
                            break;
                        }
                    }
                    LoggerUtil.logI(TAG, "newReqCount  494---> " + newReqCount + "---->" + title + "---->" + chatId);
                    if (newReqCount > 3) {
                        LoggerUtil.logI(TAG, "堵起来了  206---> " + title + "---->" + chatId);
                    }
                    GetChannelParticipants.newReq(HookActivity.baseActivity, chatId);


                } catch (Exception e) {
//            Tool.printException(e);
                    LoggerUtil.logI(TAG + chatId, "eee  258---->" + CrashHandler.getInstance().printCrash(e));
                }

            }
        });
    }

    private void postJson(List<GroupUserBean> groupUserBeanList) {
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < groupUserBeanList.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userName", groupUserBeanList.get(i).getUserName());
                jsonObject.put("nickName", groupUserBeanList.get(i).getNickName());
                jsonObject.put("group", groupUserBeanList.get(i).getGroup());
                jsonObject.put("collectTime", groupUserBeanList.get(i).getCollectTime());
                jsonArray.put(i, jsonObject);
            }
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("appId", "tg");
            jsonObject.put("appSecret", "PQoTESG6rQkA2wwU");
            jsonObject.put("data", jsonArray);
            LoggerUtil.logI(TAG + chatId, "jsonObject  327---->" + jsonObject);

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject));
            OkGo.post("https://tgcollect.crazy-customer.com/api/TgInfo/Add")
                    .requestBody(body)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            LoggerUtil.logI(TAG + chatId, "ttt 409: " + s + "---->" + jsonObject);
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            LoggerUtil.logI(TAG + chatId, "eee 415: " + e);

                        }
                    });

        } catch (Exception e) {
            LoggerUtil.logI(TAG + chatId, "eee  421---->" + CrashHandler.getInstance().printCrash(e));
        }
    }

    public static void saveMMM(String nick, String username, String title, long chatId, int size) {
        String result = "username:" + username + " 昵称:" + nick + "\n";
        LoggerUtil.logI(TAG + chatId, "result  279---->" + result);
        title = title.replace("/", "");
        LoggerUtil.sendLog2(result);

        WriteFileUtil.writeAppend("@" + username + "\n", Global.GROUP_CHAT_INFO + title + ".txt");

        LoggerUtil.logI(TAG + "saveMMM", "saveMMM  493---->" + username + "---->" + nick + "---->" + title + "----->" + chatId);

//        String got_info_username = WriteFileUtil.read(Global.GOT_INFO_USERNAME);
//        if (TextUtils.isEmpty(got_info_username)) {
//            WriteFileUtil.write(username + "", Global.GOT_INFO_USERNAME);
//        } else {
//            WriteFileUtil.write(got_info_username + "," + username, Global.GOT_INFO_USERNAME);
//        }


        WriteFileUtil.write("1", Global.GOT_USERNAME + username);

        WriteFileUtil.write(System.currentTimeMillis() + "", Global.IS_STOP_CAIJI);


    }

//    public static boolean judgeGot(String username, long chatid) {
//        boolean isHave = false;
//        try {
//            String added_friend_username = WriteFileUtil.read(Global.GOT_INFO + chatid);
//            String[] split1 = added_friend_username.split(",");
//            for (String wxid : split1) {
//                if (wxid.equals(username)) {
//                    isHave = true;
//                    break;
//                }
//            }
//        } catch (Exception e) {
//
//        }
//        return isHave;
//    }

    public static boolean judgeGotUsername(String username) {
        boolean isHave = false;
//        try {
//            String added_friend_username = WriteFileUtil.read(Global.GOT_INFO_USERNAME);
//            String[] split1 = added_friend_username.split(",");
//            for (String wxid : split1) {
//                if (wxid.equals(username)) {
//                    isHave = true;
//                    break;
//                }
//            }
//        } catch (Exception e) {
//
//        }

        try {
            String read = WriteFileUtil.read(Global.GOT_USERNAME + username);
            if (read.equals("1")) {
                isHave = true;
            }
        } catch (Exception e) {

        }


        return isHave;
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
                    LoggerUtil.logI(TAG, "saveSearchUsers   552====");
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
                                SendMessage.sendMessage("hihihi", user);
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

}
