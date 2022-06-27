package com.jujing.telehook_2.model.operate;

import static com.jujing.telehook_2.model.operate.Tools.getDatabase;
import static com.jujing.telehook_2.model.operate.Tools.getUser;

import android.content.Intent;
import android.text.TextUtils;

import com.jujing.telehook_2.Global;
import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.MainActivity;
import com.jujing.telehook_2.bean.ChatBean;
import com.jujing.telehook_2.bean.UserBean;
import com.jujing.telehook_2.hook.HookActivity;
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
import org.json.JSONObject;
import org.telegram.tgnet.TLRPC;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import de.robv.android.xposed.XposedHelpers;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserReadAction {

    private static final String TAG = "UserReadAction";

    public static boolean isStart = false;

    public static void handle() {
        if (isStart) {
            HookActivity.showToast("正在收集，请稍后。。。");
            return;
        }
        isStart = true;
        HookActivity.showToast("开始收集");
        ExecutorUtil.doExecute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<UserBean> msgReadUsers = getMsgReadUsers();
                    LoggerUtil.logI(TAG, "msgReadUsers  36----》" + msgReadUsers.size());
                    int count = 0;
                    for (int i = 0; i < msgReadUsers.size(); i++) {
                        UserBean userBean = msgReadUsers.get(i);

                        ArrayList list = getUserSomeMsgs(userBean);
                        count = count + list.size();
//                        LoggerUtil.logI(TAG, "list  46----》" + list.size());
                    }
                    LoggerUtil.logI(TAG, "count  49----》" + count);
                    List<UserBean> list = checkNickname();
                    LoggerUtil.logI(TAG, "list  51----》" + list.size());

                    try {
                        JSONArray jsonArray = new JSONArray();


                        Object currentUser = UsersAndChats.getCurrentUser();
                        String username = (String) XposedHelpers.getObjectField(currentUser, "username");
                        String push_name = WriteFileUtil.read(Global.USER_INFO_NICKNAME);
                        JSONObject jsonObject0 = new JSONObject();
                        jsonObject0.put("userName", username);
                        jsonObject0.put("nickName", push_name);
                        jsonObject0.put("readUserNum", msgReadUsers.size());
                        jsonObject0.put("readAutoReplyNum", count);
                        jsonObject0.put("chineseNum", list.size());
                        jsonObject0.put("collectTime", DateUtils.formatDate(System.currentTimeMillis()));
                        jsonArray.put(0, jsonObject0);

                        final JSONObject jsonObject = new JSONObject();
                        jsonObject.put("appId", "tg");
                        jsonObject.put("appSecret", "PQoTESG6rQkA2wwU");
                        jsonObject.put("data", jsonArray);
                        LoggerUtil.logI(TAG, "jsonObject  87---->" + jsonObject);
                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject));
                        OkGo.post("https://tgcollect.crazy-customer.com/api/TgInfo/Add")
                                .requestBody(body)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        LoggerUtil.logI(TAG, "ttt 95: " + s + "---->" + jsonObject);
                                        HookActivity.showToast("上传成功");
                                    }

                                    @Override
                                    public void onError(Call call, Response response, Exception e) {
                                        super.onError(call, response, e);
                                        LoggerUtil.logI(TAG, "eee 101: " + e);
                                        HookActivity.showToast("上传失败" + e);

                                    }
                                });

                    } catch (Exception e) {
                        LoggerUtil.logI(TAG, "eee  108---->" + CrashHandler.getInstance().printCrash(e));
                    }


                } catch (Exception e) {
                    LoggerUtil.logI(TAG, "eee  39" + CrashHandler.getInstance().printCrash(e));
                }
                isStart = false;
            }
        });


    }


    public static List<ChatBean> getGroupIdList() {
        List<ChatBean> list = new ArrayList<>();
        try {

//            Thread.sleep(5000);
            //out 1发出去的，0发来的，
            //read_state ，1未发送 ，2已发送但是对方未读取,3对方已读

            String sql = "SELECT distinct uid FROM messages_v2 where uid<0";
//            String sql = "SELECT distinct uid FROM messages_v2";

//            LoggerUtil.logI(TAG, "sql  133--->" + sql);
            Object database = getDatabase(HookMain.classLoader);

            Object[] aaaa = new Object[]{sql, new Object[]{}};
            Object cursor = XposedHelpers.callMethod(database, "queryFinalized", aaaa);


            LoggerUtil.logAll(TAG, "cursor  144---->" + cursor);
            while ((boolean) XposedHelpers.callMethod(cursor, "next")) {
                try {
                    long userid = (long) XposedHelpers.callMethod(cursor, "longValue", 0) * -1;
                    LoggerUtil.logI(TAG, "userid  146---->" + userid);


                    Object chat = UsersAndChats.getChat2(userid);
//                    title  108---> Btok官方交流15群---->1162772946
                    boolean megagroup = false;
                    try {
                        megagroup = XposedHelpers.getBooleanField(chat, "megagroup");
                    } catch (Exception e) {

                    }
                    String title = (String) XposedHelpers.getObjectField(chat, "title");

                    Class<?> aClass = XposedHelpers.findClass("org.telegram.messenger.ChatObject", HookMain.classLoader);
                    boolean isNotInChat = (boolean) XposedHelpers.callStaticMethod(aClass, "isNotInChat", chat);
//                    boolean chat3 = UsersAndChats.getChat3(userid);
//                    int participants_count = XposedHelpers.getIntField(chat, "participants_count");
                    if (megagroup && !isNotInChat) {
//                    if (megagroup){
                        LoggerUtil.logI(TAG, "title  163---> " + title + "---->" + userid + "---->" + megagroup + "---->" + isNotInChat);
                        ChatBean bean = new ChatBean();
                        bean.setChatId(userid);
                        bean.setTitle(title);

                        list.add(bean);
                    }


                } catch (Exception e) {
                    LoggerUtil.logI(TAG, "eee  154----》" + CrashHandler.getInstance().printCrash(e));
                }
            }

            XposedHelpers.callMethod(cursor, "dispose");

        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee  165----》" + CrashHandler.getInstance().printCrash(e));
        }
        return list;
    }


    public static boolean judgeAd(long uid) {
        boolean isAd = false;
        try {

            String sql = "SELECT data FROM messages_v2 where out=0  and uid=" + uid;
            Object database = getDatabase(HookMain.classLoader);

            Object[] aaaa = new Object[]{sql, new Object[]{}};
            Object cursor = XposedHelpers.callMethod(database, "queryFinalized", aaaa);
            while ((boolean) XposedHelpers.callMethod(cursor, "next")) {
                try {
//                    LoggerUtil.logI(TAG, "next  146----》");
                    Object data = XposedHelpers.callMethod(cursor, "byteBufferValue", 0);
                    if (data != null) {
                        int i = (int) XposedHelpers.callMethod(data, "readInt32", false);
                        Class Message = HookMain.classLoader.loadClass("org.telegram.tgnet.TLRPC$Message");
                        Object message = XposedHelpers.callStaticMethod(Message, "TLdeserialize", data, i, false);
                        String messageText = (String) XposedHelpers.getObjectField(message, "message");
                        if (TextUtils.isEmpty(messageText)) {
                            continue;
                        }
//                        LoggerUtil.logI(TAG, "messageText  156----》"+messageText);
                        if (messageText.contains("@")) {
                            isAd = true;
                            break;
                        }

                    }


                } catch (Exception e) {
                    LoggerUtil.logI(TAG, "eee  67----》" + CrashHandler.getInstance().printCrash(e));
                }
            }

            XposedHelpers.callMethod(cursor, "dispose");
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee  242----》" + CrashHandler.getInstance().printCrash(e));
        }
        return isAd;

    }

    public static int checkReadNum() {
        String sql = "SELECT distinct uid FROM messages_v2 where out=1 and uid>0 and read_state =3";
        Object database = getDatabase(HookMain.classLoader);
        Object[] aaaa = new Object[]{sql, new Object[]{}};
        Object cursor = XposedHelpers.callMethod(database, "queryFinalized", aaaa);
        List<Long> readList = new ArrayList<>();


        while ((boolean) XposedHelpers.callMethod(cursor, "next")) {
            try {
                long uid = (long) XposedHelpers.callMethod(cursor, "longValue", 0);
                int read_state = (int) XposedHelpers.callMethod(cursor, "intValue", 1);
                int send_state = (int) XposedHelpers.callMethod(cursor, "intValue", 2);
                if (uid == 777000) {//Telegram
                    continue;
                }

                boolean exists = new File(Global.SENT_UID + uid).exists();
                if (!exists) {
                    continue;
                }

                Object user = UsersAndChats.getUser(uid);
                Object username = XposedHelpers.getObjectField(user, "username");
                Object first_name = XposedHelpers.getObjectField(user, "first_name");

                LoggerUtil.logI(TAG, "checkReadNum  262---->" + uid + "---->" + read_state + "---->" + send_state + "---->" + username + "---->" + first_name);

                boolean b = judgeAd(uid);
                LoggerUtil.logI(TAG, "judgeAd   265---->" + uid + "---->" + b);
                if (!b) {
                    readList.add(uid);
                }

            } catch (Exception e) {
                LoggerUtil.logI(TAG, "eee  271----》" + CrashHandler.getInstance().printCrash(e));
            }
        }

        XposedHelpers.callMethod(cursor, "dispose");

        return readList.size();
    }

    public static int checkReadState(long mid) {
        String sql = "SELECT read_state FROM messages_v2 where uid="+mid;
        Object database = getDatabase(HookMain.classLoader);
        Object[] aaaa = new Object[]{sql, new Object[]{}};
        Object cursor = XposedHelpers.callMethod(database, "queryFinalized", aaaa);

        int read_state = -1;
        while ((boolean) XposedHelpers.callMethod(cursor, "next")) {
            try {
                 read_state = (int) XposedHelpers.callMethod(cursor, "intValue", 0);

            } catch (Exception e) {
                LoggerUtil.logI(TAG, "eee  271----》" + CrashHandler.getInstance().printCrash(e));
            }
        }

        XposedHelpers.callMethod(cursor, "dispose");

        return read_state;
    }
    public static void checkSendSucceedNum(boolean isShowToast) {
        try {
            //read_state ，1未发送 ，2已发送但是对方未读取,3对方已读
//            String sql = "SELECT read_state,send_state FROM messages_v2 where out=1 and uid="+939531867;//已读的 read_state=3  send_state=0
//            String sql = "SELECT read_state,send_state FROM messages_v2 where out=1 and uid="+5345910614L;//发送失败的 read_state=2 send_state=2
//            String sql = "SELECT read_state,send_state FROM messages_v2 where out=1 and uid="+5004187384L;//发送失败的 read_state=2 send_state=2
//            String sql = "SELECT read_state,send_state FROM messages_v2 where out=1 and uid="+5385708386L;//发送成功的，未读的，read_state=2  send_state=0

            String sql = "SELECT distinct uid,read_state,send_state FROM messages_v2 where out=1 and uid>0";
            Object database = getDatabase(HookMain.classLoader);
            Object[] aaaa = new Object[]{sql, new Object[]{}};
            Object cursor = XposedHelpers.callMethod(database, "queryFinalized", aaaa);
            List<Long> sentSuccessList = new ArrayList<>();
            List<Long> sentFailList = new ArrayList<>();
            List<Long> readList = new ArrayList<>();

            List<Long> list = new ArrayList<>();

            while ((boolean) XposedHelpers.callMethod(cursor, "next")) {
                try {
                    long uid = (long) XposedHelpers.callMethod(cursor, "longValue", 0);
                    int read_state = (int) XposedHelpers.callMethod(cursor, "intValue", 1);
                    int send_state = (int) XposedHelpers.callMethod(cursor, "intValue", 2);
                    if (uid == 777000) {//Telegram
                        continue;
                    }

                    boolean exists = new File(Global.SENT_UID + uid).exists();
                    if (!exists) {
                        continue;
                    }
                    if (list.contains(uid)) {
                        continue;
                    }
                    list.add(uid);

                    Object user = UsersAndChats.getUser(uid);
                    Object username = XposedHelpers.getObjectField(user, "username");
                    Object first_name = XposedHelpers.getObjectField(user, "first_name");

                    LoggerUtil.logI(TAG, "read_state & send_state  257---->" + uid + "---->" + read_state + "---->" + send_state + "---->" + username + "---->" + first_name);


                    if (send_state == 0) {
                        sentSuccessList.add(uid);

                    } else if (send_state == 2) {
//                        LoggerUtil.logI(TAG, "read_state & send_state  271---->" + uid + "---->" + read_state + "---->" + send_state + "---->" + username + "---->" + first_name);
                        sentFailList.add(uid);
                    }
//                    if (read_state == 3) {
//                        boolean b = judgeAd(uid);
//                        LoggerUtil.logI(TAG, "judgeAd   268---->" + uid + "---->" + b);
//                        if (!b) {
//                            readList.add(uid);
//                        }
//                    }

                } catch (Exception e) {
                    LoggerUtil.logI(TAG, "eee  340----》" + CrashHandler.getInstance().printCrash(e));
                }
            }

            XposedHelpers.callMethod(cursor, "dispose");


            int readNum = checkReadNum();
            LoggerUtil.logI(TAG, "readNum  348----》" + readNum);


            if (sentSuccessList.size() != 0) {
                WriteFileUtil.write(sentSuccessList.size() + "", Global.MESSAGE_SUCCESS_NUM);
                WriteFileUtil.write(readNum + "", Global.MESSAGE_READ_NUM);
            }

            Intent intent = new Intent();
            intent.putExtra("success", sentSuccessList.size());
            intent.putExtra("read", readNum);
            intent.setAction(MainActivity.ACTION_XTELE_COLLECT_RESULT);
            HookActivity.baseActivity.sendBroadcast(intent);
            if (isShowToast) {

                HookActivity.showLongToast("发送成功人数:" + sentSuccessList.size() + "\n已读人数:" + readList.size() + "\n发送失败人数：" + sentFailList.size());
            } else {


            }


        } catch (Exception e) {

        }
    }

    public static List<UserBean> checkNickname() {
        try {

//            Thread.sleep(5000);
            //out 1发出去的，0发来的，
            //read_state ，1未发送 ，2已发送但是对方未读取,3对方已读

            String sql = "SELECT distinct uid FROM messages_v2 where uid>0";


            Object database = getDatabase(HookMain.classLoader);

            Object[] aaaa = new Object[]{sql, new Object[]{}};
            Object cursor = XposedHelpers.callMethod(database, "queryFinalized", aaaa);


//            ArrayList users = new ArrayList();

//            List<Integer> useridList = new ArrayList<>();
            List<UserBean> list = new ArrayList<>();


            while ((boolean) XposedHelpers.callMethod(cursor, "next")) {
                try {
                    int userid = (int) XposedHelpers.callMethod(cursor, "intValue", 0);
                    Object user = getUser(HookMain.classLoader, userid);
                    String username = "";
                    String nickname = "";
                    try {
                        username = (String) XposedHelpers.getObjectField(user, "username");


                        String last_name = (String) XposedHelpers.getObjectField(user, "last_name");
                        String first_name = (String) XposedHelpers.getObjectField(user, "first_name");

                        if (TextUtils.isEmpty(last_name)) {
                            nickname = first_name;
                        } else {
                            nickname = first_name + " " + last_name;
                        }
                    } catch (Exception e) {

                    }
                    if (!MatchUtil.hasChinese(nickname)) {
                        if (TextUtils.isEmpty(nickname)) {
                            continue;
                        }
                        UserBean userBean = new UserBean();
                        userBean.setUserid(userid);
                        userBean.setUsername(username);
                        userBean.setNickname(nickname);
                        list.add(userBean);

                        LoggerUtil.logI(TAG, "userBean  111---->" + userBean);
                    }


                } catch (Exception e) {
                    LoggerUtil.logI(TAG, "eee  117----》" + CrashHandler.getInstance().printCrash(e));
                }
            }

            XposedHelpers.callMethod(cursor, "dispose");

            return list;
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee  125----》" + CrashHandler.getInstance().printCrash(e));
        }


        return null;

    }

    public static List<UserBean> getMsgReadUsers() {
        try {

//            Thread.sleep(5000);
            //out 1发出去的，0发来的，
            //read_state ，1未发送 ，2已发送但是对方未读取,3对方已读


            String sql = "SELECT distinct uid FROM messages_v2 where out=1 and read_state=3 and uid>0";


            Object database = getDatabase(HookMain.classLoader);

            Object[] aaaa = new Object[]{sql, new Object[]{}};
            Object cursor = XposedHelpers.callMethod(database, "queryFinalized", aaaa);


//            ArrayList users = new ArrayList();

//            List<Integer> useridList = new ArrayList<>();
            List<UserBean> list = new ArrayList<>();


            while ((boolean) XposedHelpers.callMethod(cursor, "next")) {
                try {
                    int userid = (int) XposedHelpers.callMethod(cursor, "intValue", 0);
                    Object user = getUser(HookMain.classLoader, userid);
                    String username = "";
                    String nickname = "";
                    try {
                        username = (String) XposedHelpers.getObjectField(user, "username");


                        String last_name = (String) XposedHelpers.getObjectField(user, "last_name");
                        String first_name = (String) XposedHelpers.getObjectField(user, "first_name");

                        if (TextUtils.isEmpty(last_name)) {
                            nickname = first_name;
                        } else {
                            nickname = first_name + " " + last_name;
                        }
                    } catch (Exception e) {

                    }
                    UserBean userBean = new UserBean();
                    userBean.setUserid(userid);
                    userBean.setUsername(username);
                    userBean.setNickname(nickname);
                    list.add(userBean);

                    LoggerUtil.logI(TAG, "userBean  108---->" + userBean);
//                    LoggerUtil.logI(TAG, "\n");

//                    users.add(user);
//                    useridList.add(userid);


                } catch (Exception e) {
                    LoggerUtil.logI(TAG, "eee  93----》" + CrashHandler.getInstance().printCrash(e));
                }
            }

            XposedHelpers.callMethod(cursor, "dispose");

            return list;
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee  101----》" + CrashHandler.getInstance().printCrash(e));
        }


        return null;

    }


    //    public static ArrayList getUserSomeMsgs(long uid, String containsMsg) {
    public static ArrayList getUserSomeMsgs(UserBean userBean) {
        try {

            //out 1发出去的，0发来的，
            //read_state ，1未发送 ，2已发送但是对方未读取,3对方已读
            String sql = "SELECT data FROM messages_v2 where out=0  and uid=" + userBean.getUserid();

            Object database = getDatabase(HookMain.classLoader);

            Object[] aaaa = new Object[]{sql, new Object[]{}};
            Object cursor = XposedHelpers.callMethod(database, "queryFinalized", aaaa);

//            ArrayList allMasg = new ArrayList();
            ArrayList cMsgs = new ArrayList();

            while ((boolean) XposedHelpers.callMethod(cursor, "next")) {
                try {


                    Object data = XposedHelpers.callMethod(cursor, "byteBufferValue", 0);


                    if (data != null) {


                        int i = (int) XposedHelpers.callMethod(data, "readInt32", false);
                        Class Message = HookMain.classLoader.loadClass("org.telegram.tgnet.TLRPC$Message");
                        Object message = XposedHelpers.callStaticMethod(Message, "TLdeserialize", data, i, false);

                        String messageText = (String) XposedHelpers.getObjectField(message, "message");


                        if (TextUtils.isEmpty(messageText)) {

                            continue;
                        }
//                        allMasg.add(message);
                        if (messageText.contains("@") || messageText.contains("广告") || messageText.contains("合作")) {
                            LoggerUtil.logI(TAG, "msg  164:" + messageText + "---->" + userBean);
                            cMsgs.add(message);
                        }

                    }


                } catch (Exception e) {
                    LoggerUtil.logI(TAG, "eee  129----》" + CrashHandler.getInstance().printCrash(e));
//                    Tool.printException(e);
                }
            }

//            LoggerUtil.logI(TAG, "总共收到消息:" + allMasg.size() + "条");
//            LoggerUtil.logI(TAG, "包含\"" + containsMsg + "\"的消息共" + cMsgs.size() + "条");

            return cMsgs;
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee  139----》" + CrashHandler.getInstance().printCrash(e));
//            Tool.printException(e);
        }


        return null;

    }
}
