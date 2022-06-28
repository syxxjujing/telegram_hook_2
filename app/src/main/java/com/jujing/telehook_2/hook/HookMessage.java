package com.jujing.telehook_2.hook;

import android.os.SystemClock;
import android.text.TextUtils;

import com.jujing.telehook_2.Global;
import com.jujing.telehook_2.GroupAddMemberActivity;
import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.HttpApi;
import com.jujing.telehook_2.model.SendMessage;
import com.jujing.telehook_2.model.UsersAndChats;
import com.jujing.telehook_2.model.operate.GroupAddMemberAction;
import com.jujing.telehook_2.model.operate.ImportContactsAction;
import com.jujing.telehook_2.model.operate.JoinToGroupAction;
import com.jujing.telehook_2.model.operate.JudgeCountryAndLangAction;
import com.jujing.telehook_2.model.operate.LoadFullUser;
import com.jujing.telehook_2.model.operate.SearchContactAction;
import com.jujing.telehook_2.model.operate.SetAdminAction;
import com.jujing.telehook_2.model.operate.TranslateAction;
import com.jujing.telehook_2.model.operate.UpdateChatAbout;
import com.jujing.telehook_2.util.Aes;
import com.jujing.telehook_2.util.CompressUtil;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.DownloadUtil;
import com.jujing.telehook_2.util.ExecutorUtil;
import com.jujing.telehook_2.util.HookUtil;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.MatchUtil;
import com.jujing.telehook_2.util.UploadFileUtil;
import com.jujing.telehook_2.util.WriteFileUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import okhttp3.Call;
import okhttp3.Response;


public class HookMessage {

    private static final String TAG = "HookMessage";
    private static List<Integer> listMessageIds = new ArrayList<>();
    private static List<String> listTalker = new ArrayList<>();

    /**
     * hook接收消息
     */
    public static void hook() {
//        Class<?> MessagesController = XposedHelpers.findClass("org.telegram.messenger.MessagesController", HookMain.classLoader);
//        XposedBridge.hookAllMethods(MessagesController, "loadFullChat", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                String s = HookUtil.printParams(param);
//                LoggerUtil.logI(TAG,"loadFullChat  24--->" + s);
//            }
//        });
//        XposedBridge.hookAllMethods(MessagesController, "addUserToChat", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                String s = HookUtil.printParams(param);
////                addUserToChat  24--->1353481412,org.telegram.tgnet.TLRPC$TL_user@1b8bb12,null,0,null,org.telegram.ui.ChatActivity@69352e3,null,
//                LoggerUtil.logI(TAG,"addUserToChat  33--->" + s);
//            }
//        });
//        LoggerUtil.logI(TAG,"hook start 44");
//        Class<?> aClass1 = XposedHelpers.findClass("android.widget.TextView", HookMain.classLoader);
//        XposedBridge.hookAllMethods(aClass1, "setText", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                String text = (String) param.args[0];
////                LoggerUtil.logI(TAG,"text  51---->"+text);
//                if (text.contains("2520")){
//                    LoggerUtil.logI(TAG,"text  47---->"+text);
//                    frames();
//                }
//
//            }
//        });
        LoggerUtil.logI(TAG, "hook end 58");

//        Class<?> SQLiteDatabase = XposedHelpers.findClass("org.telegram.SQLite.SQLiteDatabase", HookMain.classLoader);
//        XposedBridge.hookAllMethods(SQLiteDatabase, "executeFast", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                try {
//                    String sql = (String) param.args[0];
//
//                    if (sql.contains("UPDATE messages_v2 SET read_state")) {
//
//                        String[] arr = sql.split(" ");
//                        String uid = arr[11];
//                        String mid = arr[19];
//                        if (uid.equals("?")) {
//                            return;
//                        }
//                        if (uid.startsWith("-")) {
//                            return;
//                        }
//                        if (mid.equals("0")) {
//                            return;
//                        }
//                        if (mid.equals("?")) {
//                            return;
//                        }
//                        LoggerUtil.logI(TAG + uid, "uid & mid 113---->" + uid + "---->" + mid + "---->" + sql);
//
//                        String user_id_ = WriteFileUtil.read(Global.USER_ID);
//                        String token_ = WriteFileUtil.read(Global.TOKEN);
//                        if (user_id_.equals("") || token_.equals("")) {
//                            return;
//                        }
//                        JSONObject jsonObjectCurrent = new JSONObject();
//                        jsonObjectCurrent.put("user_id", Integer.parseInt(user_id_));
//                        jsonObjectCurrent.put("app_wx_id", UsersAndChats.getUserInfoId());
//                        jsonObjectCurrent.put("wx_id", uid + "");//
//                        jsonObjectCurrent.put("index", 0);
//                        jsonObjectCurrent.put("platform", "telegram");
//                        jsonObjectCurrent.put("content", "123456789");
//                        String user_info_phone = WriteFileUtil.read(Global.USER_INFO_PHONE);
//                        LoggerUtil.logI(TAG + uid, "user_info_phone 131----->" + user_info_phone);
//                        jsonObjectCurrent.put("phone_number", user_info_phone);
//                        jsonObjectCurrent.put("msg_type", "text");
//
//                        if (listTalker.contains(uid + "")) {//
//                            LoggerUtil.logI(TAG + uid, "listTalker.contains  136 ---------------------------->" + uid);
//                            return;
//                        }
//                        listTalker.add(uid + "");
//                        JSONObject jsonObjectMsg = new JSONObject();
//                        jsonObjectMsg.put("current", jsonObjectCurrent);
//                        final JSONObject jsonObject0 = new JSONObject();
//                        jsonObject0.put("msg", jsonObjectMsg);
//                        LoggerUtil.logI(TAG + uid, "jsonObject  144--->" + jsonObject0.toString() + "---->" + token_);
//                        String json = Aes.buildReqStr(Aes.Jia_Mi(jsonObject0.toString()));
//                        LoggerUtil.logAll(TAG + uid, "json  146 : " + json);
//                        OkGo.post(HttpApi.ReplyMsg)
//                                .headers("Authorization", token_)
//                                .upJson(json)
//                                .execute(new StringCallback() {
//                                    @Override
//                                    public void onSuccess(String s, Call call, Response response) {
//                                        LoggerUtil.logI(TAG + uid, "ttt 153: " + s + "-------------------->" + jsonObject0.toString());
//                                        try {
//                                            final JSONObject jsonObject = new JSONObject(s);
//                                            String ret = jsonObject.getString("ret");
//                                            if (ret.equals("success")) {
//                                                ExecutorUtil.doExecute(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        try {
//
//
//                                                            String data = jsonObject.getString("data");
//                                                            String s1 = Aes.Jie_Mi(data);
//                                                            LoggerUtil.logI(TAG + uid, "sss 166: " + s1 + "------>" + jsonObject0.toString());
//                                                            JSONObject jsonObject1 = new JSONObject(s1);
//                                                            boolean key_word_reply = jsonObject1.getBoolean("key_word_reply");
//                                                            if (!key_word_reply) {
////                                                            if (finalIsDang){
//////                                                                removeOneTalker(from_id);
//////                                                                LoggerUtil.logI(TAG + from_id, "不是关键词被挡  185-------------> " + from_id + "----->" + content);
////////                                                                return;
//////                                                            }
//                                                            }
//
//                                                            JSONArray msg_list = null;
//                                                            try {
//                                                                msg_list = jsonObject1.getJSONArray("msg_list");
//                                                            } catch (Exception e) {
//                                                            }
//                                                            LoggerUtil.logI(TAG + uid, "msg_list 182: " + msg_list + "------>" + jsonObject0.toString());
//                                                            if (msg_list != null && msg_list.length() > 0) {
//                                                                for (int i = 0; i < msg_list.length(); i++) {
//                                                                    String string = msg_list.getString(i);
//                                                                    JSONObject msgObj = new JSONObject(string);
//                                                                    LoggerUtil.logI(TAG + uid, "msgObj  187----->" + i + "---->" + msg_list.length() + "----->" + msgObj + "----->" + uid);
//                                                                    handleMessages(msgObj, Long.parseLong(uid), 0L, key_word_reply, "");
//                                                                }
//                                                            } else {
//                                                                JSONObject msgObj = jsonObject1.getJSONObject("msg");
//                                                                LoggerUtil.logI(TAG + uid, "msgObj  192----->" + "----->" + msgObj + "----->" + uid);
//                                                                handleMessages(msgObj, Long.parseLong(uid), null, key_word_reply, "");
//                                                            }
//                                                            removeOneTalker(Long.parseLong(uid));
//                                                        } catch (Exception e) {
//                                                            LoggerUtil.logI(TAG + uid, "eee 241---->" + CrashHandler.getInstance().printCrash(e));
//                                                            removeOneTalker(Long.parseLong(uid));
//                                                        }
//                                                    }
//                                                });
//
//                                            } else {
//                                                removeOneTalker(Long.parseLong(uid));
//                                            }
//                                        } catch (Exception e) {
//                                            LoggerUtil.logI(TAG + uid, "eee 78--->" + CrashHandler.getInstance().printCrash(e));
//                                            removeOneTalker(Long.parseLong(uid));
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onError(Call call, Response response, Exception e) {
//                                        super.onError(call, response, e);
//                                        try {
//                                            LoggerUtil.logI(TAG + uid, "eee 216: " + e + "---->" + response);
//                                        } catch (Exception e1) {
//                                            LoggerUtil.logI(TAG + uid, "e1 218: " + e);
//                                        }
//                                        removeOneTalker(Long.parseLong(uid));
//                                    }
//                                });
//                    }
//                } catch (Exception e) {
//                    LoggerUtil.logI(TAG, "eee 118--->" + CrashHandler.getInstance().printCrash(e));
//                }
//            }
//        });

        Class<?> MessagesStorage = XposedHelpers.findClass("org.telegram.messenger.MessagesStorage", HookMain.classLoader);
        XposedBridge.hookAllMethods(MessagesStorage, "putMessagesInternal", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                try {
                    List list = (List) param.args[0];
//                    LoggerUtil.logI(TAG, "list.size  74----->" + list.size());
//                    for (int i = 0; i < list.size(); i++) {
                    Object messageObj = list.get(list.size() - 1);
                    long dialog_id = XposedHelpers.getLongField(messageObj, "dialog_id");
                    if (dialog_id < 0) {//过滤群的
                        return;
                    }
//                    HookUtil.printAllFieldForSuperclass(messageObj);
                    int messageId = XposedHelpers.getIntField(messageObj, "id");
//                    LoggerUtil.logAll(TAG, "messageId  93--->" + messageId);
                    if (listMessageIds.contains(messageId)) {
                        LoggerUtil.logI(TAG, "hook 过了 ----->" + messageId + "----->" + listMessageIds.size());
                        if (listMessageIds.size() == 100) {
                            for (int i = 0; i < 50; i++) {
                                listMessageIds.remove(i);
                            }
                        }

                        return;
                    }
                    listMessageIds.add(messageId);
                    LoggerUtil.logI(TAG, "dialog_id  260------>" + dialog_id);
                    ExecutorUtil.doExecute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //                    String s = HookUtil.printParams(param);
                                //                    LoggerUtil.logI(TAG, "putMessagesInternal  22--->" + s);


                                final Object from_id_obj = XposedHelpers.getObjectField(messageObj, "from_id");//对方id
                                final long from_id = XposedHelpers.getLongField(from_id_obj, "user_id");
                                LoggerUtil.logI(TAG, "from_id  78--->" + from_id + "---->" + dialog_id);
                                try {
                                    Object user = UsersAndChats.getUser(from_id);
                                    Object username = XposedHelpers.getObjectField(user, "username");
                                    Object phone = XposedHelpers.getObjectField(user, "phone");
                                    Object first_name = XposedHelpers.getObjectField(user, "first_name");
                                    LoggerUtil.logI(TAG, "user_id  125:" + from_id + "---->" + username + "---->" + phone + "---->" + first_name);
                                } catch (Exception e) {
                                    LoggerUtil.logI(TAG, "ee  128--->" + CrashHandler.getInstance().printCrash(e));
                                }


                                Object currentUser = UsersAndChats.getCurrentUser();
                                //                    HookUtil.printAllFieldForSuperclass(currentUser,TAG);
                                long id = XposedHelpers.getLongField(currentUser, "id");
                                LoggerUtil.logI(TAG + from_id, "id  116--->" + id + "---->" + messageId);
                                if (id == from_id) {//过滤自己发送的消息


                                    return;
                                }
                                //                    HookUtil.printAllFieldForSuperclass(messageObj, TAG);
                                Object action = XposedHelpers.getObjectField(messageObj, "action");
                                if (action != null) {
                                    LoggerUtil.logI(TAG + from_id, "action  74---------------->" + action);
                                    //                            action  74---------------->org.potato.tgnet.TLRPC$TL_messageActionChatAddUser@b23d29d
                                    //群通知消息
                                    //                            HookUtil.printAllFieldForSuperclass(action);
                                    if (action.toString().contains("TLRPC$TL_messageActionChatAddUser")) {
                                        //                            int from_id = XposedHelpers.getIntField(messageObj, "from_id");//邀请人的id， 如果自己主动加群，那么此id和被邀请人的id一样
                                        //                            XposedHelpers.getIntField(messageObj, "dialog_id");//群id
                                        //                            Object user = UsersAndChats.getUser(from_id);
                                        //                            String name = (String) XposedHelpers.getObjectField(user, "first_name");//
                                        //                            ArrayList<Integer> users = (ArrayList<Integer>) XposedHelpers.getObjectField(action, "users");//为被邀请人的id集合
                                    }
                                    return;
                                }

                                String msg_type = "";
                                Object media = XposedHelpers.getObjectField(messageObj, "media");
                                LoggerUtil.logI(TAG + from_id, "media  146---------------->" + media);
                                if (media == null) {
                                    msg_type = "text";
                                }
                                try {
                                    if (media.toString().contains("TL_messageMediaEmpty")) {
                                        //是文字消息
                                        msg_type = "text";
                                    } else {
                                        LoggerUtil.logI(TAG + from_id, "media  98---->" + media);
                                        if (media.toString().contains("TL_messageMediaPhoto")) {
                                            msg_type = "image";
                                            //                                HookUtil.printAllFieldForSuperclass(media);
                                            //                                Object photo = XposedHelpers.getObjectField(media, "photo");
                                            //                                HookUtil.printAllFieldForSuperclass(photo);
                                        } else {
                                            //                                HookUtil.printAllFieldForSuperclass(media);
                                            Object document = XposedHelpers.getObjectField(media, "document");
                                            //                                HookUtil.printAllFieldForSuperclass(document);
                                            Object attributes = XposedHelpers.getObjectField(document, "attributes");
                                            LoggerUtil.logI(TAG + from_id, "attributes 105 --->" + attributes);
                                            //                        byte[] bytes = (byte[]) XposedHelpers.getObjectField(media, "bytes");//图片等 数据源
                                            if (attributes.toString().contains("TL_documentAttributeAudio")) {
                                                msg_type = "voice";
                                            }
                                            if (attributes.toString().contains("TL_documentAttributeVideo")) {
                                                msg_type = "video";
                                            }
                                        }

                                    }
                                } catch (Exception e) {

                                }
                                //                    media:org.potato.tgnet.TLRPC$TL_messageMediaPhoto@23a1d02
                                Object message = XposedHelpers.getObjectField(messageObj, "message");//消息内容
                                LoggerUtil.logI(TAG + from_id, "message  197---->" + message);
                                if (msg_type.equals("text")) {
                                    if (message.toString().contains("@")) {
                                        List<String> atUsernameList = MatchUtil.getAtUsername0(message.toString());
                                        LoggerUtil.logI(TAG + from_id, "atUsernameList  201------>" + atUsernameList + "---->" + atUsernameList.size());
                                        for (int i = 0; i < atUsernameList.size(); i++) {
                                            String atUsername = atUsernameList.get(i);
                                            //                            LoggerUtil.logAll(TAG+from_id, "atUsername  201------>" + atUsername+"---->"+message.toString());
                                            if (!TextUtils.isEmpty(atUsername.trim())) {
                                                LoggerUtil.logI(TAG + from_id, "atUsername  204------>" + atUsername + "---->" + message.toString());

                                                if (SearchContactAction.judgeSent(atUsername.trim())) {
                                                    LoggerUtil.logI(TAG + from_id, "以前发送过了 208 :" + atUsername.trim());

                                                } else {
                                                    //发送成功消息 就记录一下，下次就不发了
                                                    String sent_messages_user = WriteFileUtil.read(Global.SENT_MESSAGES_USER);
                                                    if (TextUtils.isEmpty(sent_messages_user)) {
                                                        WriteFileUtil.write(atUsername.trim(), Global.SENT_MESSAGES_USER);
                                                    } else {
                                                        WriteFileUtil.write(sent_messages_user + "," + atUsername.trim(), Global.SENT_MESSAGES_USER);
                                                    }
                                                    UsersAndChats.isStart = true;
                                                    SearchContactAction searchContactAction = new SearchContactAction();
                                                    searchContactAction.seachUsers(atUsername);
                                                }

                                            }
                                        }

                                    }
                                }


                                String user_id_ = WriteFileUtil.read(Global.USER_ID);
                                String token_ = WriteFileUtil.read(Global.TOKEN);
                                LoggerUtil.logI(TAG + from_id, "user_id_ & token_ 377----->" + user_id_ + "---->" + token_);
                                if (user_id_.equals("") || token_.equals("")) {
                                    return;
                                }
                                JSONObject jsonObjectCurrent = new JSONObject();
                                jsonObjectCurrent.put("user_id", Integer.parseInt(user_id_));
                                jsonObjectCurrent.put("app_wx_id", UsersAndChats.getUserInfoId());
                                jsonObjectCurrent.put("wx_id", from_id + "");//
                                jsonObjectCurrent.put("index", 0);
                                jsonObjectCurrent.put("platform", "telegram");
                                jsonObjectCurrent.put("content", message.toString());
                                String user_info_phone = WriteFileUtil.read(Global.USER_INFO_PHONE);
                                LoggerUtil.logI(TAG + from_id, "user_info_phone 185----->" + user_info_phone);
                                jsonObjectCurrent.put("phone_number", user_info_phone);
                                LoggerUtil.logI(TAG + from_id, "msg_type 178----->" + msg_type);
                                boolean isCountry = false;
                                if (!msg_type.equals("text")) {
                                    String country = WriteFileUtil.read(Global.COUNTRY_JUDGE + from_id);
                                    LoggerUtil.logI(TAG + from_id, "country 395----->" + country);
                                    String lang = WriteFileUtil.read(Global.LANG_JUDGE + from_id);
                                    LoggerUtil.logI(TAG + from_id, "lang 397----->" + lang);

                                    if (!TextUtils.isEmpty(country)) {
                                        if (!country.equals("fail")) {
                                            isCountry = true;
                                        }
                                    }
                                    boolean isLang = false;
                                    if (!TextUtils.isEmpty(lang)) {
                                        if (!lang.equals("error")) {
                                            isLang = true;
                                        }
                                    }
                                    LoggerUtil.logI(TAG + from_id, "isCountry &  isLang 411----->" + isCountry + "---->" + isLang);
                                    if (isCountry && isLang) {
                                        return;//都识别出了，不走下面
                                    }

                                }


                                if (msg_type.equals("text")) {//文字  emoji
                                    boolean b = MatchUtil.isEmoji(message.toString());
                                    if (b) {
                                        jsonObjectCurrent.put("msg_type", "emoji");
                                    } else {
                                        jsonObjectCurrent.put("msg_type", "text");
                                    }
                                    if (message.toString().contains("jujing999")) {
                                        //                            SendMessage.sendVoice(939531867,"/sdcard/1xreply/msg_031119092019f8217167028103.mp3");
                                        //                            SendMessage.sendImage(false,939531867,"/sdcard/1xreply/jjj.png");

                                        //                            SendMessage.seachUsers("@MT667788");

//                                        SearchContactAction searchContactAction = new SearchContactAction();
//                                        searchContactAction.seachUsers("+639565471115");

//                                        ImportContactsAction.importContact("+63 916 517 1456");
                                    }
//                                    boolean isCountry = false;
                                    String country = JudgeCountryAndLangAction.judgeCountry(from_id, message.toString());
                                    LoggerUtil.logI(TAG + from_id, "country 441----->" + country + "---->" + message);
                                    if (!country.equals("fail")) {
                                        isCountry = true;
                                    }
                                    boolean isLang = false;
                                    String lang = TranslateAction.detect(from_id + "", message.toString());
                                    LoggerUtil.logI(TAG + from_id, "lang 446--->" + lang + "----->" + message.toString());
                                    if (!lang.equals("error")) {
                                        isLang = true;
                                    }
                                    LoggerUtil.logI(TAG + from_id, "isCountry &  isLang 451----->" + isCountry + "---->" + isLang);
                                    if (isCountry && isLang) {
                                        return;//都识别出了，不走下面
                                    }


                                    String result = TranslateAction.post(from_id + "", message.toString(), "zh");
                                    LoggerUtil.logI(TAG + from_id, "result 419--->" + result + "----->" + message.toString());
                                    jsonObjectCurrent.put("content", result);
                                } else if (msg_type.equals("image")) {//图片
                                    jsonObjectCurrent.put("msg_type", "image");
                                } else if (msg_type.equals("voice")) {//语音
                                    jsonObjectCurrent.put("msg_type", "voice");
                                } else if (msg_type.equals("video")) {//视频
                                    jsonObjectCurrent.put("msg_type", "video");
                                } else {
                                    jsonObjectCurrent.put("msg_type", "other");
                                }

                                if (listTalker.contains(from_id + "")) {//
                                    LoggerUtil.logI(TAG + from_id, "listTalker.contains  209 ---------------------------->" + from_id + "---->" + message);
                                    return;
                                }
                                listTalker.add(from_id + "");
                                JSONObject jsonObjectMsg = new JSONObject();
                                jsonObjectMsg.put("current", jsonObjectCurrent);
                                final JSONObject jsonObject0 = new JSONObject();
                                jsonObject0.put("msg", jsonObjectMsg);
                                LoggerUtil.logI(TAG + from_id, "jsonObject  217--->" + jsonObject0.toString() + "---->" + token_);
                                String json = Aes.buildReqStr(Aes.Jia_Mi(jsonObject0.toString()));
                                LoggerUtil.logAll(TAG + from_id, "json  219 : " + json);
                                boolean finalIsCountry = isCountry;
                                OkGo.post(HttpApi.ReplyMsg)
                                        .headers("Authorization", token_)
                                        .upJson(json)
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onSuccess(String s, Call call, Response response) {
                                                LoggerUtil.logI(TAG + from_id, "ttt 134: " + s + "-------------------->" + jsonObject0.toString());
                                                try {
                                                    if (Global.IS_DEBUG) {
                                                        //测试语音
                                                        s = "{\"code\":0,\"data\":\"UiTS3A59rLWYy9keNptJmHGCgCr5B1nhKgXqwu0VwOQuxkoIsu4MxXq+KpzPXfAUUDqpiJ5NzerVfOr5R4siOIPiF7dM2HNMMGjGDdjeucKiP2EW4DMwn0lHmnz1qClnkAn4qNj7VbHv3QPRq+L865TLziC2DluyAljG8Z3t5koWl/gGZmmeQ5JDeT1HOVoTM1/2/U58zuD4Uo4WiuXvXy07tlVkk7Hbtaaa9q2zmGU+cJ7ElPZD1U/SOr4w7XSUc7Dc6ORVTrIvr9E8kRq3H7/Fe5cTlz9aqti4Bai0akRU/i/76L0wgXkbpU9tCRjy/IuW8dFClz1tN1Ez6c6LVQQhbUZcWJFN1p4MmgINCerS1HmyQCv+S486mgKRZIw8c1yNeV6wGalA5Cn+HuoMQrRM8T8hawWijuuBZJ0Yd6NYGCrNVbqKzYVDc47MvorTgR8CyUuWUNRUAYKxvWCvWiwSyPR4YYvpXbCo3uEREecltFL/yyYwr4NLSiFcXshYcmoHLyRi6uNV7lR3MPGSRRRJfNBOxqFkouNW3S2gdqGjNHkKYR/yVOwYM1LDZFfsHlw0gMi31J28etPEe2uik442Vax/PZBNf2IfLSb1gWzAVYhnxz+sgHMOubvcPnveu5xNYpmYp2eZpT9K2kTqTSYN03lx4snd02178ZrtpbStbcSPUQCMCQog1d68n2NVGD04lRu96g6PMzABGporDg==\",\"msg\":\"req success\",\"ret\":\"success\"}\n";
                                                    }
                                                    final JSONObject jsonObject = new JSONObject(s);
                                                    String ret = jsonObject.getString("ret");
                                                    if (ret.equals("failed")) {
                                                        String msg = jsonObject.getString("msg");
                                                        if (msg.contains("没有对应")) {//没有识别出国家，但脚本走完的情况

                                                            LoggerUtil.logI(TAG + from_id, "isCountry 505: " + finalIsCountry);
                                                            WriteFileUtil.write("default", Global.COUNTRY_JUDGE + from_id);
                                                        }
                                                    }
                                                    if (ret.equals("success")) {
                                                        ExecutorUtil.doExecute(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {


                                                                    String data = jsonObject.getString("data");
                                                                    String s1 = Aes.Jie_Mi(data);
                                                                    LoggerUtil.logI(TAG + from_id, "sss 63: " + s1 + "------>" + jsonObject0.toString());
                                                                    JSONObject jsonObject1 = new JSONObject(s1);
                                                                    boolean key_word_reply = jsonObject1.getBoolean("key_word_reply");
                                                                    boolean need_translate = false;
                                                                    try {
                                                                        need_translate = jsonObject1.getBoolean("need_translate");
                                                                    } catch (Exception e) {

                                                                    }
                                                                    if (!key_word_reply) {
                                                                        //                                                            if (finalIsDang){
                                                                        ////                                                                removeOneTalker(from_id);
                                                                        ////                                                                LoggerUtil.logI(TAG + from_id, "不是关键词被挡  185-------------> " + from_id + "----->" + content);
                                                                        //////                                                                return;
                                                                        ////                                                            }
                                                                    }

                                                                    JSONArray msg_list = null;
                                                                    try {
                                                                        msg_list = jsonObject1.getJSONArray("msg_list");
                                                                    } catch (Exception e) {
                                                                    }
                                                                    LoggerUtil.logI(TAG + from_id, "msg_list 230: " + msg_list + "------>" + jsonObject0.toString());
                                                                    if (msg_list != null && msg_list.length() > 0) {
                                                                        for (int i = 0; i < msg_list.length(); i++) {
                                                                            String string = msg_list.getString(i);
                                                                            JSONObject msgObj = new JSONObject(string);
                                                                            LoggerUtil.logI(TAG + from_id, "msgObj  225----->" + i + "---->" + msg_list.length() + "----->" + msgObj + "----->" + from_id);
                                                                            handleMessages(msgObj, from_id, 0L, key_word_reply, "", need_translate);
                                                                        }
                                                                    } else {
                                                                        JSONObject msgObj = jsonObject1.getJSONObject("msg");
                                                                        LoggerUtil.logI(TAG + from_id, "msgObj  230----->" + "----->" + msgObj + "----->" + from_id);
                                                                        handleMessages(msgObj, from_id, null, key_word_reply, "", need_translate);
                                                                    }
                                                                    removeOneTalker(from_id);
                                                                } catch (Exception e) {
                                                                    LoggerUtil.logI(TAG + from_id, "eee 241---->" + CrashHandler.getInstance().printCrash(e));
                                                                    removeOneTalker(from_id);
                                                                }
                                                            }
                                                        });

                                                    } else {
                                                        removeOneTalker(from_id);
                                                    }
                                                } catch (Exception e) {
                                                    LoggerUtil.logAll(TAG + from_id, "eee 78--->" + CrashHandler.getInstance().printCrash(e));
                                                    removeOneTalker(from_id);
                                                }
                                            }

                                            @Override
                                            public void onError(Call call, Response response, Exception e) {
                                                super.onError(call, response, e);
                                                try {
                                                    LoggerUtil.logI(TAG + from_id, "eee 167: " + e + "---->" + response);
                                                } catch (Exception e1) {
                                                    LoggerUtil.logI(TAG + from_id, "e1 169: " + e);
                                                }
                                                removeOneTalker(from_id);
                                            }
                                        });


                            } catch (Exception e) {
                                e.printStackTrace();
                                LoggerUtil.logI(TAG, "eee 293--->" + CrashHandler.getInstance().printCrash(e));
                            }

                        }
                    });
                } catch (Exception e) {

                }


//                putMessagesInternal  22--->[org.potato.tgnet.TLRPC$TL_message@17008f],true,false,218959117,false,

//                putMessagesInternal  22--->[org.potato.tgnet.TLRPC$TL_message@18390fa],true,false,218959117,false,


            }
        });


        Class<?> aClass = XposedHelpers.findClass("org.telegram.messenger.SendMessagesHelper", HookMain.classLoader);
        XposedBridge.hookAllMethods(aClass, "sendMessage", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String s = HookUtil.printParams(param);
//                sss  54--->vvv,746055308,null,null,true,null,null,null,

//                sss  54--->org.telegram.tgnet.TLRPC$TL_photo@f102266,null,746055308,null,null,null,null,{groupId=0, originalPath=/storage/emulated/0/11Siri/bgBottomBitmap.png30971_1561946476000, final=1},0,null,

                //群 文字
//                sss  54--->😍,-334129000,null,null,true,null,null,null,

//                LoggerUtil.logI(TAG,"sss  116--->" + s);


//                sss  116--->org.telegram.tgnet.TLRPC$TL_document@ff80ef9,org.telegram.messenger.VideoEditedInfo@f0521c0,/storage/emulated/0/Android/data/org.telegram.messenger/cache/-2147483648_-210014.mp4,-343928937,null,null,null,null,{ve=-1_-1_-1_90_1280_720_900000_640_360_24_/storage/emulated/0/360/1.mp4, groupId=0, originalPath=/storage/emulated/0/360/1.mp426615176_158415336300014784_-1_-1_640, final=1},0,sent_0_262,

//                sss  116--->111,598957033,null,null,null,true,[],null,null,true,0,org.telegram.messenger.MessageObject$SendAnimationData@34f4616,
//                sss  116--->1111,939531867,null,null,null,true,[],null,null,true,0,org.telegram.messenger.MessageObject$SendAnimationData@bddad63,

//                HookUtil.frames();


            }
        });
//        if (true) {
//            return;
//        }
//        XposedBridge.hookAllMethods(aClass, "getInstance", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                String s = HookUtil.printParams(param);
////                sss  71--->0,
//                LoggerUtil.logI(TAG,"sss  71--->" + s);
//            }
//        });
//
//        XposedBridge.hookAllMethods(aClass, "prepareSendingPhoto", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                String s = HookUtil.printParams(param);
//
//
//                LoggerUtil.logI(TAG,"sss  83--->" + s);
//                frames();
//
//
//            }
//        });
//
//
//        XposedBridge.hookAllMethods(aClass, "generatePhotoSizes", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                String s = HookUtil.printParams(param);
//
//                LoggerUtil.logI(TAG,"sss  100--->" + s);
////                HookUtil.frames();
//
////                /storage/emulated/0/wechat/data/image/02101074a86df930770975e36ff18ba7.jpg,null,
//
//
//            }
//        });
    }

    public static void removeOneTalker(long id) {

        listTalker.remove(id + "");

    }

    public static void handleMessages(JSONObject msgObj, long talker_id, Long postTime, boolean key_word_reply, String key_id, boolean need_translate) throws JSONException {
        try {
            String msg_type = msgObj.getString("msg_type");
            int delay = msgObj.getInt("delay");
            if (Global.IS_DEBUG) {
                delay = 10;
            }
            LoggerUtil.logI(TAG + talker_id, "msg_type & delay 200: " + msg_type + "---->" + delay);
            SystemClock.sleep(1000 * delay);
            SendMessage.markDialogAsRead(talker_id);

            String is_open_check_reply = WriteFileUtil.read(Global.IS_OPEN_CHECK_REPLY);
            LoggerUtil.logI(TAG + talker_id, "is_open_check_reply   176--->" + is_open_check_reply);
            if (is_open_check_reply.equals("false")) {
                return;
            }

            if (msg_type.equals("text")) {//文字
                LoggerUtil.logI(TAG + talker_id, "要回复文字了  687--->" + msgObj + "------------------->" + talker_id + "---->" + key_word_reply + "---->" + need_translate);
                String content = msgObj.getString("content");
                if (need_translate) {
                    String lang = WriteFileUtil.read(Global.LANG_JUDGE + talker_id);
                    LoggerUtil.logI(TAG + talker_id, "content  691---->" + content + "---->" + delay + "---->" + lang);
                    if (!TextUtils.isEmpty(lang)) {
                        if (!lang.equals("error")) {
                            content = TranslateAction.post(talker_id + "", content, lang);
                            content = content.replace("&#39;", "'");
                        }
                    }
                }
                LoggerUtil.logI(TAG + talker_id, "content  695---->" + content + "---->" + delay);
//                content = TranslateAction.post0(talker_id + "", content, false);
//                content = content.replace("&#39;", "'");
//                LoggerUtil.logI(TAG + talker_id, "content  719---->" + content + "---->" + delay);
                SendMessage.sendMessage(content, talker_id);


            }
            if (msg_type.equals("image")) {//图片
                handleImage(msgObj, delay, talker_id, TAG);

            }
            if (msg_type.equals("voice")) {//语音
                handleVoice(msgObj, delay, talker_id, TAG);
            }
            if (msg_type.equals("video")) {//视频
                handleVideo(msgObj, delay, talker_id, TAG);
            }

            if (msg_type.equals("file")) {//文件
//                handleFile(msgObj, delay, talker_id, TAG);
            }

        } catch (Exception e) {
            LoggerUtil.logI(TAG + talker_id, "eee 347 --->" + CrashHandler.getInstance().printCrash(e));
        }
    }

    public static void handleImage(final JSONObject msgObj, final long delay, final long talker_id, final String TAG) throws JSONException {
        try {

            final String media_url = msgObj.getString("media_url");

            LoggerUtil.logI(TAG + talker_id, "media_url  703--->" + media_url);

            final String s = MatchUtil.splitFileName(media_url);
            LoggerUtil.logI(TAG + talker_id, "s  515--->" + s);
            String path = Global.STORAGE_FILE + s;
            File fileD = new File(path);
            LoggerUtil.logI(TAG + talker_id, "path  519--->" + fileD.exists());
            if (!fileD.exists()) {
//                HookActivity.baseActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        DownloadUtil.downloadFile(media_url, Global.STORAGE_FILE, s, 0);
//                    }
//                });
                DownloadUtil.downloadFileByOkGo(media_url, Global.STORAGE_FILE, s, 0);
                long length0 = 0;
                for (int i = 0; i < 30; i++) {
                    File file0 = new File(path);
                    LoggerUtil.logI(TAG + talker_id, "file0.exists()  714--->" + file0.exists() + "=====" + i);
                    if (!file0.exists()) {
                        SystemClock.sleep(1000);
                    } else {
                        File file = new File(path);
                        long length = file.length();
                        LoggerUtil.logI(TAG + talker_id, "length 720--->" + length + "---->" + length0 + "---->" + file.exists() + "----->" + i + "---->" + path);
                        if (length > length0) {
                            if (length0 != 0) {
                                SystemClock.sleep(1000);
                            }
                            length0 = length;
                        } else {
                            break;
                        }
                    }
                }
            }
//            for (int i = 0; i < delay; i++) {
//                SystemClock.sleep(1000);
//            }
            SystemClock.sleep(3000);
            File file = new File(path);
            if (!file.exists()) {
                LoggerUtil.logI(TAG + talker_id, "文件没有下载成功 130!!" + path);//
                return;
            }
            LoggerUtil.logI(TAG + talker_id, "要回复图片了  610--->" + msgObj + "------------------->" + talker_id);
            SendMessage.sendImage(false, talker_id, file.getPath());
//            SendMessageAction.sendPicture(talker_id, file.getPath());
//            SendMessageAction2.sendMessageForQueue("2", toTalker, file.getPath());
//            Intent intent = new Intent();
//            intent.setAction(WAReceiver.ACTION_XWA_SEND_MESSAGE);
//            intent.putExtra("content", path);
//            intent.putExtra("type", 1);
//            intent.putExtra("talker_id", talker_id);
//            MyApp.context.sendBroadcast(intent);
        } catch (Exception e) {
            LoggerUtil.logI(TAG + talker_id, "eee 265--->" + CrashHandler.getInstance().printCrash(e));
        }
    }

    public static void handleVoice(final JSONObject msgObj, final int delay, final long talker_id, final String TAG) throws JSONException {
        try {
            final String media_url = msgObj.getString("media_url");
            final String s = MatchUtil.splitFileName(media_url);
            LoggerUtil.logI(TAG + talker_id, "s  434--->" + s);
            final String path = Global.STORAGE_FILE + s;
            File fileD = new File(path);
            LoggerUtil.logI(TAG + talker_id, "path  426--->" + fileD.exists());
//            if (fileD.exists()) {
//                fileD.delete();
//            }
            if (!fileD.exists()) {
//                HookActivity.baseActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        DownloadUtil.downloadFile(media_url, Global.STORAGE_FILE, s, 0);
//                    }
//                });
                DownloadUtil.downloadFileByOkGo(media_url, Global.STORAGE_FILE, s, 0);
                long length0 = 0;
                for (int i = 0; i < 30; i++) {
                    File file0 = new File(path);
                    LoggerUtil.logI(TAG + talker_id, "file0.exists()  665--->" + file0.exists() + "=====" + i);
                    if (!file0.exists()) {
                        SystemClock.sleep(1000);
                    } else {
                        File file = new File(path);
                        long length = file.length();
                        LoggerUtil.logI(TAG + talker_id, "length 671--->" + length + "---->" + length0 + "---->" + file.exists() + "----->" + i + "---->" + path);
                        if (length > length0) {
                            if (length0 != 0) {
                                SystemClock.sleep(1000);
                            }
                            length0 = length;
                        } else {
                            break;
                        }
                    }
                }
            }
            SystemClock.sleep(3000);
//            for (int i = 0; i < delay; i++) {
//                SystemClock.sleep(1000);
//            }

            File file = new File(path);
            if (!file.exists()) {
                LoggerUtil.logI(TAG + talker_id, "文件没有下载成功  168!!" + path);
                return;
            }
            LoggerUtil.logI(TAG + talker_id, "要回复语音了  497--->" + msgObj + "------------------->" + talker_id);
            SendMessage.sendVoice(talker_id, path);
//            SendMessageAction2.sendMessageForQueue("3", toTalker, path);
//            Intent intent = new Intent();
//            intent.setAction(WAReceiver.ACTION_XWA_SEND_MESSAGE);
//            intent.putExtra("content", path);
//            intent.putExtra("type", 2);
//            intent.putExtra("talker_id", talker_id);
//            MyApp.context.sendBroadcast(intent);
//            SystemClock.sleep(3000);
        } catch (Exception e) {
            LoggerUtil.logI(TAG + talker_id, "eee  227--->" + CrashHandler.getInstance().printCrash(e));
        }
    }

    public static void handleVideo(final JSONObject msgObj, final int delay, final long talker_id, final String TAG) throws JSONException {
        try {
            final String media_url = msgObj.getString("media_url");

            final String s = MatchUtil.splitFileName(media_url);
            LoggerUtil.logI(TAG + talker_id, "s  344--->" + s);
            final String path = Global.STORAGE_FILE + s;
            File fileD = new File(path);
            LoggerUtil.logI(TAG + talker_id, "path  349--->" + fileD.exists());
            if (!fileD.exists()) {
                DownloadUtil.downloadFileByOkGo(media_url, Global.STORAGE_FILE, s, 0);
                long length0 = 0;
                for (int i = 0; i < 30; i++) {
                    File file0 = new File(path);
                    LoggerUtil.logI(TAG + talker_id, "file0.exists()  584--->" + file0.exists() + "=====" + i);
                    if (!file0.exists()) {
                        SystemClock.sleep(1000);
                    } else {
                        File file = new File(path);
                        long length = file.length();
                        LoggerUtil.logI(TAG + talker_id, "length 591--->" + length + "---->" + length0 + "---->" + file.exists() + "----->" + i + "---->" + path);
                        if (length > length0) {
                            if (length0 != 0) {
                                SystemClock.sleep(1000);
                            }
                            length0 = length;
                        } else {
                            break;
                        }
                    }
                }
            }
//            for (int i = 0; i < delay; i++) {
//                SystemClock.sleep(1000);
//            }
            SystemClock.sleep(3000);
            File file = new File(path);
            if (!file.exists()) {
                LoggerUtil.logI(TAG + talker_id, "文件没有下载成功  107!!" + path);
                return;
            }
            long curTime = System.currentTimeMillis();
            LoggerUtil.logI(TAG + talker_id, "curTime  112--->" + curTime);
            LoggerUtil.logI(TAG + talker_id, "要回复视频了  397--->" + msgObj + "------------------->" + talker_id);
            SendMessage.sendVideo(false, talker_id, path);
//            SendMessageAction2.sendMessageForQueue("4", toTalker, path);
//            SendMessageAction.sendVideo(talker_id, path);
//            Intent intent = new Intent();
//            intent.setAction(WAReceiver.ACTION_XWA_SEND_MESSAGE);
//            intent.putExtra("content", path);
//            intent.putExtra("type", 3);
//            intent.putExtra("talker_id", talker_id);
//            MyApp.context.sendBroadcast(intent);
//            SystemClock.sleep(5000);
//                    }
        } catch (Exception e) {
            LoggerUtil.logI(TAG + talker_id, "eee 186: " + CrashHandler.getInstance().printCrash(e));
        }
    }


    public static void handleFile(final JSONObject msgObj, final int delay, final String talker_id, final String TAG) throws JSONException {
        try {
            final String media_url = msgObj.getString("media_url");

            final String s = MatchUtil.splitFileName(media_url);
            LoggerUtil.logI(TAG + talker_id, "s  709--->" + s);
            final String path = Global.STORAGE_FILE + s;
            File fileD = new File(path);
            LoggerUtil.logI(TAG + talker_id, "path  712--->" + fileD.exists());
            if (!fileD.exists()) {
                DownloadUtil.downloadFileByOkGo(media_url, Global.STORAGE_FILE, s, 0);
                long length0 = 0;
                for (int i = 0; i < 30; i++) {
                    File file0 = new File(path);
                    LoggerUtil.logI(TAG + talker_id, "file0.exists()  718--->" + file0.exists() + "=====" + i);
                    if (!file0.exists()) {
                        SystemClock.sleep(1000);
                    } else {
                        File file = new File(path);
                        long length = file.length();
                        LoggerUtil.logI(TAG + talker_id, "length 723--->" + length + "---->" + length0 + "---->" + file.exists() + "----->" + i + "---->" + path);
                        if (length > length0) {
                            if (length0 != 0) {
                                SystemClock.sleep(1000);
                            }
                            length0 = length;
                        } else {
                            break;
                        }
                    }
                }
            }
//            for (int i = 0; i < delay; i++) {
//                SystemClock.sleep(1000);
//            }
            File file = new File(path);
            if (!file.exists()) {
                LoggerUtil.logI(TAG + talker_id, "文件没有下载成功  741!!" + path);
                return;
            }
            long curTime = System.currentTimeMillis();
            LoggerUtil.logI(TAG + talker_id, "curTime  744--->" + curTime);
            LoggerUtil.logI(TAG + talker_id, "要回复文件了  745--->" + msgObj + "------------------->" + talker_id);
//            SendMessageAction2.sendMessageForQueue("4", toTalker, path);
//            SendMessageAction.sendFile(talker_id, path);
//            Intent intent = new Intent();
//            intent.setAction(WAReceiver.ACTION_XWA_SEND_MESSAGE);
//            intent.putExtra("content", path);
//            intent.putExtra("type", 3);
//            intent.putExtra("talker_id", talker_id);
//            MyApp.context.sendBroadcast(intent);
            SystemClock.sleep(5000);
//                    }
        } catch (Exception e) {
            LoggerUtil.logI(TAG + talker_id, "eee 186: " + CrashHandler.getInstance().printCrash(e));
        }
    }

}
