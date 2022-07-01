package com.jujing.telehook_2.model.operate;

import static com.jujing.telehook_2.HookMain.classLoader;
import static com.jujing.telehook_2.model.operate.Tools.getCurrentUserId;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.Toast;

import com.jujing.telehook_2.Global;
import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.bean.GroupUserBean;
import com.jujing.telehook_2.hook.HookActivity;
import com.jujing.telehook_2.model.UsersAndChats;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.ExecutorUtil;
import com.jujing.telehook_2.util.FileUtils;
import com.jujing.telehook_2.util.HookUtil;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.RandomUtil;
import com.jujing.telehook_2.util.WriteFileUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.tgnet.TLRPC;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddAndInviteGroupAction {
    private static final String TAG = "AddAndInviteGroupAction";

    public static boolean isStart = false;

    public static void hook() {
        Class<?> aClass = XposedHelpers.findClass("org.telegram.messenger.NotificationCenter", classLoader);
        XposedBridge.hookAllMethods(aClass, "postNotificationName", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
//                String s = HookUtil.printParams(param);
//                LoggerUtil.logI(TAG, "sss  48---> " + s);
//                HookUtil.frames();
                Object[] arg = (Object[]) param.args[1];
//                LoggerUtil.logI(TAG, "chatId  54---> " + arg[0] + "---->" );
                int id = (int) param.args[0];
//                int didReceiveNewMessages = XposedHelpers.getStaticIntField(aClass, "didReceiveNewMessages");
////                if (id == chatDidCreated) {
//                LoggerUtil.logI(TAG, "id  67---> " + id + "---->" + didReceiveNewMessages);
                if (id == 1) {
//                    HookUtil.frames();

                    long dialogId = (long) arg[0];
                    LoggerUtil.logI(TAG, "dialogId  72---> " + dialogId);

                }//
//                Object[] arg = (Object[]) param.args[1];
//                long chatId0 = (long) arg[0];
////                    long chatId = (long) arg;
//                LoggerUtil.logI(TAG, "chatId0  58---> " + chatId0 + "---->" + chatDidCreated + "---->" + id);
                if (true) {
                    return;
                }

                long chatId = Long.parseLong(WriteFileUtil.read(Global.GROUP_ADD_INVITE_ID));
                LoggerUtil.logI(TAG, "chatId  62---> " + chatId);
                Object chat = UsersAndChats.getChat2(chatId);
                String title0 = (String) XposedHelpers.getObjectField(chat, "title");
                Object photo = XposedHelpers.getObjectField(chat, "photo");
                Object photo_big = XposedHelpers.getObjectField(photo, "photo_big");
                LoggerUtil.logI(TAG, "photo_big  82---> " + photo_big + "----》" + title0);
                long volume_id = XposedHelpers.getLongField(photo_big, "volume_id");
                File file = new File("/sdcard/Android/data/org.telegram.messenger/cache");
                File[] files = file.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        return file.getName().contains(volume_id + "");
                    }
                });
                if (files != null && files.length != 0) {
                    String absolutePath = files[0].getAbsolutePath();
                    LoggerUtil.logI(TAG, "absolutePath  69---> " + absolutePath);

//                    ChangeChatAvtar.newReq(HookActivity.baseActivity, absolutePath, chatId0);
                }


//                }


            }
        });

    }

    public static boolean isCreating = false;

    public static void createGroup() {
        try {
            if (isCreating) {
                LoggerUtil.sendLog4("正在建群，若长时间没成功，请杀死重开，再建！");
                return;
            }
            isCreating = true;
            String added_contact = WriteFileUtil.read(Global.ADDED_CONTACT);
            if (TextUtils.isEmpty(added_contact)) {
                LoggerUtil.sendLog4("请先加人！！");
                return;
            }
            String[] split = added_contact.split(",");
            ArrayList<Long> selectedContacts = new ArrayList<>();
//            selectedContacts.add(5134062503L);
//            selectedContacts.add(5297812200L);
            for (int i = 0; i < split.length; i++) {
                long l = Long.parseLong(split[i]);
                selectedContacts.add(l);
            }

            LoggerUtil.sendLog4("本次建群应拉" + selectedContacts.size() + "人。");
//            MessagesController.getInstance(UserConfig.selectedAccount)
//                    .createChat2(title, selectedContacts, null, 0, false, null, null, null);
            long chatId = Long.parseLong(WriteFileUtil.read(Global.GROUP_ADD_INVITE_ID));
            LoggerUtil.logI(TAG, "chatId  111---> " + chatId);
            Object chat = UsersAndChats.getChat2(chatId);
            String title0 = (String) XposedHelpers.getObjectField(chat, "title");

            int currentId = getCurrentUserId(classLoader);
            Class MessagesController = classLoader.loadClass("org.telegram.messenger.MessagesController");
            Object MessagesControllerIns = XposedHelpers.callStaticMethod(MessagesController, "getInstance", currentId);


            Class TL_messages_createChat = classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_messages_createChat");
            Object req = XposedHelpers.newInstance(TL_messages_createChat);
            XposedHelpers.setObjectField(req, "title", title0);
            for (int a = 0; a < selectedContacts.size(); a++) {
//                LoggerUtil.logI(TAG, "aaa  129---->" + selectedContacts.get(a) + "---->" + a);
                Object user = XposedHelpers.callMethod(MessagesControllerIns, "getUser", selectedContacts.get(a));
//                TLRPC.User user = getUser(selectedContacts.get(a));
                if (user == null) {
                    continue;
                }
                Object getInputUser = XposedHelpers.callMethod(MessagesControllerIns, "getInputUser", user);
                Object users = XposedHelpers.getObjectField(req, "users");
                XposedHelpers.callMethod(users, "add", getInputUser);
            }
            List users = (List) XposedHelpers.getObjectField(req, "users");
            LoggerUtil.logI(TAG, "users  140---->" + users.size());
            LoggerUtil.sendLog4("本次建群实际拉" + users.size() + "人，准备开始。");

            Object AccountInstanceIns = Tools.getAccountInstance(classLoader);
            Object connectionsManager = XposedHelpers.callMethod(AccountInstanceIns, "getConnectionsManager");
            Class RequestDelegate = classLoader.loadClass("org.telegram.tgnet.RequestDelegate");
            Object callback = Proxy.newProxyInstance(classLoader, new Class[]{RequestDelegate}, new InvocationHandler() {
                @Override
                public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                    String mName = method.getName();
                    LoggerUtil.logI(TAG, " createGroup mName  147:" + mName);
                    if (mName.equals("run")) {
                        isCreating = false;
                        Object response = objects[0];
                        Object error = objects[1];
                        LoggerUtil.logI(TAG, " response or error  151:" + response + "--->" + error);
                        if (error != null) {
                            int code = XposedHelpers.getIntField(error, "code");
                            String text = (String) XposedHelpers.getObjectField(error, "text");
                            LoggerUtil.logI(TAG, "建群错误code  155:" + code + " text:" + text);
                            LoggerUtil.sendLog4("建群错误code:" + code + " text:" + text);
                            return null;
                        }

                        List chats = (List) XposedHelpers.getObjectField(response, "chats");
                        long id = XposedHelpers.getLongField(chats.get(0), "id");
                        LoggerUtil.sendLog4("建群成功:" + " id:" + id);
                        WriteFileUtil.write("", Global.ADDED_CONTACT);

//                        HookActivity.baseActivity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Object getNotificationCenter = XposedHelpers.callMethod(AccountInstanceIns, "getNotificationCenter");
//                                Object[] arg = new Object[]{id};
//                                XposedHelpers.callMethod(getNotificationCenter,"postNotificationName",24,arg);
//                            }
//                        });


                        ExecutorUtil.doExecute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    SystemClock.sleep(1000);
                                    long chatId = Long.parseLong(WriteFileUtil.read(Global.GROUP_ADD_INVITE_ID));
                                    LoggerUtil.logI(TAG, "chatId  171---> " + chatId);
                                    Object chat = UsersAndChats.getChat2(chatId);
                                    String title0 = (String) XposedHelpers.getObjectField(chat, "title");
                                    Object photo = XposedHelpers.getObjectField(chat, "photo");
                                    Object photo_big = XposedHelpers.getObjectField(photo, "photo_big");
                                    LoggerUtil.logI(TAG, "photo_big  176---> " + photo_big + "----》" + title0);
                                    long volume_id = XposedHelpers.getLongField(photo_big, "volume_id");
                                    File file = new File("/sdcard/Android/data/org.telegram.messenger/cache");
                                    File[] files = file.listFiles(new FileFilter() {
                                        @Override
                                        public boolean accept(File file) {
                                            return file.getName().contains(volume_id + "");
                                        }
                                    });
                                    if (files != null && files.length != 0) {
                                        String absolutePath = files[0].getAbsolutePath();
                                        LoggerUtil.logI(TAG, "absolutePath  187---> " + absolutePath);

                                        ChangeChatAvtar.newReq(HookActivity.baseActivity, absolutePath, id);
                                    }

                                    LoadFullUser.newReq(chatId, id);


                                    int admin_id = Integer.parseInt(WriteFileUtil.read(Global.GROUP_ADD_INVITE_ADMIN_ID));
                                    LoggerUtil.logI(TAG, "admin_id  216---> " + admin_id);

                                    JoinToGroupAction.addUserToChat(id, admin_id);
                                    LoggerUtil.sendLog4("拉管理员入群成功:" + " id:" + chatId);
                                    LoggerUtil.sendLog4("正在设置管理员:" + " id:" + chatId);
                                    SystemClock.sleep(3000);
                                    SetAdminAction.setUserAdminRole(id, admin_id);

                                } catch (Exception e) {
                                    LoggerUtil.logI(TAG, "ee  219---> " + CrashHandler.getInstance().printCrash(e));
                                }


                            }
                        });


                    }
                    return null;
                }
            });

            XposedHelpers.callMethod(connectionsManager, "sendRequest", req, callback);

//            XposedHelpers.callMethod(MessagesControllerIns, "createChat", title0, selectedContacts, null, 0, false, null, null, null);


        } catch (Exception e) {
            LoggerUtil.logI(TAG, "e  89---> " + CrashHandler.getInstance().printCrash(e));
        }
    }

    public static void handle(long chatId) {
        if (isStart) {
            LoggerUtil.sendLog4("正在添加好友，请稍后。。。");
            return;
        }
        isStart = true;
        LoggerUtil.sendLog4("开始添加好友到通讯录，chatId:" + chatId);
        checkGroupUsers(chatId);
    }


    public static void checkGroupUsers(long chatId) {
        try {
            WriteFileUtil.write(chatId + "", Global.GROUP_ADD_INVITE_ID);


            LoggerUtil.logI(TAG + chatId, "newReq  46---> " + chatId);
            Object chat = UsersAndChats.getChat2(chatId);
//            HookUtil.printAllFieldForSuperclass(chat);
            String title = (String) XposedHelpers.getObjectField(chat, "title");
            LoggerUtil.sendLog4("正在添加好友到通讯录，群:" + title);
            LoggerUtil.logI(TAG + chatId, "title  108---> " + title + "---->" + chatId);
//            HookUtil.printAllFieldForSuperclass(photo_big);
//            if (true){
//                return;
//            }

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
                index = Integer.parseInt(WriteFileUtil.read(Global.GOT_INFO_INDEX_2 + chatId));
            } catch (Exception e) {
            }
            LoggerUtil.logI(TAG + chatId, "index  304---> " + index);
            if (index == 0) {
                index = RandomUtil.randomNumber(0, 4000);
            }
            LoggerUtil.logI(TAG + chatId, "index  308---> " + index);
            XposedHelpers.setIntField(req, "offset", index);//从第几个开始获取，群员太多的有必要分批获取
            XposedHelpers.setIntField(req, "limit", 200);//数量
            Class RequestDelegate = classLoader.loadClass("org.telegram.tgnet.RequestDelegate");
            int finalIndex = index;
            Object callback = Proxy.newProxyInstance(classLoader, new Class[]{RequestDelegate}, new InvocationHandler() {
                @Override
                public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                    String mName = method.getName();
                    LoggerUtil.logI(TAG + chatId, " checkGroupUsers mName  57:" + mName);
                    switch (mName) {
                        case "run":

                            try {
                                if (!isStart) {
                                    LoggerUtil.logI(TAG + chatId, "isStart  124---> " + finalIndex);
                                    return null;
                                }

                                Object response = objects[0];
                                Object error = objects[1];

                                if (error != null) {

                                    int code = XposedHelpers.getIntField(error, "code");
                                    String text = (String) XposedHelpers.getObjectField(error, "text");
                                    LoggerUtil.logI(TAG + chatId, "获取群用户信息错误code:" + code + " text:" + text);
                                    LoggerUtil.sendLog4("获取群用户信息错误code:" + code + " text:" + text);
                                    isStart = false;
                                    return null;
                                }

                                List users = (List) XposedHelpers.getObjectField(response, "users");
                                int count = XposedHelpers.getIntField(response, "count");
                                LoggerUtil.logI(TAG + chatId, "count  91:" + count);
                                if (users == null || users.isEmpty()) {
                                    LoggerUtil.logI(TAG + chatId, "获取群用户信息错误为空");
                                    LoggerUtil.sendLog4("获取群用户信息错误为空");
                                    isStart = false;
                                    return null;
                                }
                                Collections.shuffle(users);
                                LoggerUtil.logI(TAG + chatId, "获取群用户信息成功，数量:" + users.size());
                                ExecutorUtil.doExecute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            int interval = Integer.parseInt(WriteFileUtil.read(Global.GROUP_ADD_INVITE_INTERVAL));
                                            int interval_2 = Integer.parseInt(WriteFileUtil.read(Global.GROUP_ADD_INVITE_INTERVAL_2));
                                            String added_contact = WriteFileUtil.read(Global.ADDED_CONTACT);
                                            int count = 0;
                                            if (TextUtils.isEmpty(added_contact)) {
                                                count = 0;
                                            } else {
                                                String[] split = added_contact.split(",");
                                                count = split.length;
                                            }

                                            int i1 = Integer.parseInt(WriteFileUtil.read(Global.GROUP_ADD_INVITE_NUM));
                                            int i2 = Integer.parseInt(WriteFileUtil.read(Global.GROUP_ADD_INVITE_NUM_2));
                                            LoggerUtil.logI(TAG + chatId, "run  336---->" + count
                                                    + "---->" + interval + "---->" + i1 + "---->" + users.size() + "---->" + interval_2 + "---->" + i2);


                                            for (int i = 0; i < users.size(); i++) {
                                                Object user = users.get(i);
                                                boolean self = XposedHelpers.getBooleanField(user, "self");
                                                boolean contact = XposedHelpers.getBooleanField(user, "contact");
                                                if (self || contact) {
                                                    continue;
                                                }
                                                long id = XposedHelpers.getLongField(user, "id");


                                                int expires = 0;
                                                int online_day = 0;
                                                boolean isContinue = false;
                                                Object status = XposedHelpers.getObjectField(user, "status");
                                                if (status != null) {
                                                    expires = XposedHelpers.getIntField(status, "expires");

                                                    if (expires != 0) {
                                                        online_day = Integer.parseInt(WriteFileUtil.read(Global.ONLINE_DAY2));
                                                        int k = expires + online_day * 24 * 60 * 60;

                                                        if (k < System.currentTimeMillis() / 1000) {
                                                            isContinue = true;
                                                        }
                                                    }

                                                }

                                                if (isContinue) {
                                                    if (status != null) {
                                                        if (status.toString().contains("TL_userStatusRecently") || status.toString().contains("TLRPC$TL_userStatusOnline")) {
                                                            LoggerUtil.logI(TAG + chatId, "expires ==0 Recently or online  352---->" + id + "---->" + expires + "---->" + status);

                                                        } else {
                                                            continue;
                                                        }
                                                    } else {
                                                        continue;
                                                    }
                                                }

                                                if (expires == 0) {
                                                    LoggerUtil.logI(TAG + chatId, "expires ==0  354---->" + id + "---->" + expires + "---->" + status + "---->");
                                                    if (status != null) {
                                                        if (status.toString().contains("TL_userStatusRecently") || status.toString().contains("TLRPC$TL_userStatusOnline")) {
                                                            LoggerUtil.logI(TAG + chatId, "expires ==0 Recently or online  357---->" + id + "---->" + "---->" + expires + "---->" + status);

                                                        } else {
                                                            continue;
                                                        }
                                                    } else {
                                                        continue;
                                                    }
                                                }
                                                boolean b = judgeRepeat(id, chatId);
                                                if (b) {
                                                    LoggerUtil.sendLog4("加第" + (count + 1) + "个好友重复了，群:" + title + " id:" + id);
                                                    continue;
                                                }


                                                String first_name = (String) XposedHelpers.getObjectField(user, "first_name");
                                                String last_name = (String) XposedHelpers.getObjectField(user, "last_name");

                                                LoggerUtil.logI(TAG + chatId, "nick & id 148 ---->" + id
                                                        + "---->" + first_name + "---->" + last_name
                                                        + "---->" + i + "--->" + count + "---->" + interval);
                                                XposedHelpers.setObjectField(user, "first_name", first_name + " ");
                                                XposedHelpers.setObjectField(user, "last_name", last_name + " ");
                                                count++;
                                                LoggerUtil.sendLog4("加第" + count + "个好友，群:" + title);
                                                SearchContactAction.addContact(user, id);

                                                WriteFileUtil.write(finalIndex + i + "", Global.GOT_INFO_INDEX_2 + chatId);
                                                for (int j = 0; j < interval; j++) {
                                                    if (!isStart) {
                                                        LoggerUtil.logI(TAG + chatId, "isStart  168---> " + finalIndex);
                                                        return;
                                                    }

                                                    SystemClock.sleep(1000);
                                                }
                                                if (count >= i1) {
                                                    isStart = false;

                                                    String is_group_create = WriteFileUtil.read(Global.IS_GROUP_CREATE);
                                                    LoggerUtil.logI(TAG + chatId, "is_group_create  454---> " + is_group_create);
                                                    if (is_group_create.equals("true")) {
                                                        LoggerUtil.sendLog4("人数加够了,开始自动建群");
                                                        // 自动建群
                                                        createGroup();
                                                    }

                                                    return;
                                                }

                                                if (count % i2 == 0) {
                                                    LoggerUtil.sendLog4("添加指定人数完成，等待" + interval_2 + "秒后，再加");
                                                    for (int j = 0; j < interval_2; j++) {
                                                        if (!isStart) {
                                                            LoggerUtil.logI(TAG + chatId, "isStart  437---> " + finalIndex);
                                                            return;
                                                        }
                                                        SystemClock.sleep(1000);
                                                    }
                                                }


                                            }

                                            checkGroupUsers(chatId);
                                        } catch (Exception e) {
                                            LoggerUtil.logI(TAG + chatId, "e  171---> " + CrashHandler.getInstance().printCrash(e));
                                        }
                                    }
                                });


                            } catch (Exception e) {
                                LoggerUtil.logI(TAG + chatId, "e  104---> " + CrashHandler.getInstance().printCrash(e));
                            }


                            break;
                    }
                    return null;
                }
            });
            XposedHelpers.callMethod(connectionsManager, "sendRequest", req, callback);
        } catch (Exception e) {
            LoggerUtil.logI(TAG + chatId, "e  141---> " + CrashHandler.getInstance().printCrash(e));
        }
    }

    private static boolean judgeRepeat(long id, long chatId) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", id + "");
        String account = WriteFileUtil.read(Global.ACCOUNT);
        jsonObject.put("account", account);
        LoggerUtil.logI(TAG + chatId, "jsonObject 89 :" + jsonObject);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject));
        final String[] result = {""};
        OkGo.post("http://ec2-13-214-196-63.ap-southeast-1.compute.amazonaws.com:8081/account/username/check")
                .requestBody(body)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LoggerUtil.logI(TAG + chatId, "ttt 69: " + s + "-------------------->" + jsonObject.toString());
                        result[0] = s;
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        result[0] = "error";
                        try {
                            LoggerUtil.logI(TAG + chatId, "eee 76: " + e + "---->" + response + "---->" + jsonObject);
                        } catch (Exception e1) {
                            LoggerUtil.logI(TAG + chatId, "e1 78: " + e + "---->" + jsonObject);
                        }
                    }
                });

        for (int j = 0; j < 10; j++) {
            String s = result[0];
            LoggerUtil.logI(TAG + chatId, "sss  93---->" + s + "---->" + j);
            if (TextUtils.isEmpty(s)) {
                SystemClock.sleep(1000);
            } else {
                break;
            }
        }

        String s = result[0];
        LoggerUtil.logI(TAG + chatId, "s 103: " + s);
        JSONObject jsonObject1 = new JSONObject(s);
        int code = jsonObject1.getInt("code");
        if (code == 200) {
            boolean is_exist = jsonObject1.getBoolean("is_exist");
            if (is_exist) {

                LoggerUtil.logI(TAG + chatId, "有了，停止 120: " + s);
//                    LoggerUtil.sendLog("采集重复了，username:" + username + " id:" + id);
//                LoggerUtil.sendLog("采集重复了，当前任务第" + GroupSendAction.sentCount + "人，username," + username + " id:" + id);
                return true;
            }
        }
        OkGo.post("http://ec2-13-214-196-63.ap-southeast-1.compute.amazonaws.com:8081/account/username/save")
                .requestBody(body)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LoggerUtil.logI(TAG + chatId, "ttt 158: " + s + "-------------------->" + jsonObject.toString());
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        try {
                            LoggerUtil.logI(TAG + chatId, "eee 166: " + e + "---->" + response + "---->" + jsonObject);
                        } catch (Exception e1) {
                            LoggerUtil.logI(TAG + chatId, "e1 165: " + e + "---->" + jsonObject);
                        }
                    }
                });
        return false;


    }


}
