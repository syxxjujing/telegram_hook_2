package com.jujing.telehook_2.model.operate;

import static com.jujing.telehook_2.HookMain.classLoader;

import android.os.SystemClock;

import com.jujing.telehook_2.Global;
import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.hook.HookActivity;
import com.jujing.telehook_2.model.UsersAndChats;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.ExecutorUtil;
import com.jujing.telehook_2.util.HookUtil;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.WriteFileUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XposedHelpers;

public class JoinToGroupAction {


    private static final String TAG = "JoinToGroupAction";
    public static boolean isStart = false;
    public static void handle(String path) {
        if (isStart) {
            HookActivity.showToast("正在加群，请稍后。。。");
            return;
        }
        isStart = true;

        ExecutorUtil.doExecute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> stringList = WriteFileUtil.readFile(path);
                    LoggerUtil.logI(TAG, "stringList  40---->" + stringList.size());
                    LoggerUtil.sendLog3("开始加群，群数量："+stringList.size());
                    for (int i = 0; i < stringList.size(); i++) {
                        String chat_link = stringList.get(i);
                        LoggerUtil.logI(TAG, "chat_link  46---->" + chat_link + "---->" + i);

                        LoggerUtil.sendLog3("开始加第"+(i+1)+"个群："+chat_link);
                        JoinToGroupAction.seachUsers(chat_link);

                        int i1 = Integer.parseInt(WriteFileUtil.read(Global.GROUP_JOIN_INTERVAL));
                        SystemClock.sleep(i1 * 1000L);

                    }

                } catch (Exception e) {
                    LoggerUtil.logI(TAG , "eee  54---> " + CrashHandler.getInstance().printCrash(e));
                }

                isStart = false;
            }
        });


    }
    public static void addUserToChat(long chatId){

        try {
            LoggerUtil.logI(TAG,"addUserToChat  15---->"+chatId);
            Object AccountInstanceIns = Tools.getAccountInstance(HookMain.classLoader);
            LoggerUtil.logAll(TAG,"AccountInstanceIns  15---->"+AccountInstanceIns);
            Object messagesController = XposedHelpers.callMethod(AccountInstanceIns, "getMessagesController");
            LoggerUtil.logAll(TAG,"messagesController  15---->"+messagesController);
            Object currentUser = UsersAndChats.getCurrentUser();
            LoggerUtil.logAll(TAG,"currentUser  15---->"+currentUser);
//        Class<?> BaseFragment = XposedHelpers.findClass("org.telegram.ui.ActionBar.BaseFragment", HookMain.classLoader);
//        Object chatActivity = XposedHelpers.newInstance(BaseFragment);
//        LoggerUtil.logAll(TAG,"chatActivity  15---->"+chatActivity);
            XposedHelpers.callMethod(messagesController,"addUserToChat",chatId,currentUser,0,null, null,null);
            LoggerUtil.logI(TAG,"addUserToChat  24---->"+chatId);

        } catch (Exception e) {
            LoggerUtil.logI(TAG,"eee  32---->"+ CrashHandler.getInstance().printCrash(e));
        }
    }

    public static void addUserToChat(long chatId,long user_id){

        try {
//            LoggerUtil.logI(TAG,"addUserToChat  15---->"+chatId);
            Object AccountInstanceIns = Tools.getAccountInstance(HookMain.classLoader);
//            LoggerUtil.logAll(TAG,"AccountInstanceIns  15---->"+AccountInstanceIns);
            Object messagesController = XposedHelpers.callMethod(AccountInstanceIns, "getMessagesController");
//            LoggerUtil.logAll(TAG,"messagesController  15---->"+messagesController);
//            Object currentUser = UsersAndChats.getCurrentUser();
//            LoggerUtil.logAll(TAG,"currentUser  15---->"+currentUser);

            Object user = UsersAndChats.getUser(user_id);
//        Class<?> BaseFragment = XposedHelpers.findClass("org.telegram.ui.ActionBar.BaseFragment", HookMain.classLoader);
//        Object chatActivity = XposedHelpers.newInstance(BaseFragment);
//        LoggerUtil.logAll(TAG,"chatActivity  15---->"+chatActivity);
            XposedHelpers.callMethod(messagesController,"addUserToChat",chatId,user,0,null, null,null);
            LoggerUtil.logI(TAG,"addUserToChat  103---->"+chatId);


        } catch (Exception e) {
            LoggerUtil.logI(TAG,"eee  32---->"+ CrashHandler.getInstance().printCrash(e));
        }
    }


    public  static void seachUsers(final String txt) {
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
                            LoggerUtil.logI(TAG, "搜索出错  153:" + text + ":" + code+"----->"+txt);
                            return;
                        }
                        if (response == null) {
                            LoggerUtil.logI(TAG, "搜索出错:response==null----》"+txt);
                            return;
                        }

                        ArrayList users = (ArrayList) XposedHelpers.getObjectField(response, "users");


                        ArrayList chats = (ArrayList) XposedHelpers.getObjectField(response, "chats");
                        LoggerUtil.logI(TAG, "chats.size  90----->"+chats.size());
                        if (chats == null || chats.isEmpty()) {
                            LoggerUtil.logI(TAG, "搜索出错:chats==null||chats.isEmpty----》"+txt);
                            return;
                        }

                        saveSearchRespondUsers(chats, users);
                        for (int a = 0; a < chats.size(); a++) {
                            Object chat = chats.get(a);
//

                            String title = (String) XposedHelpers.getObjectField(chat, "title");
                            long id = XposedHelpers.getLongField(chat, "id");
                            HookUtil.printAllFieldForSuperclass(chat);
                            String username = (String) XposedHelpers.getObjectField(chat, "username");
                            String[] split = txt.split("/");
                            String s = split[split.length - 1];
                            if (s.equals(username)){
                                LoggerUtil.logI(TAG, "chat  149--->"+title+"---->"+id+"---->"+username);


                                addUserToChat(id);
                                return;
                            }




//
//
//
//                            return;

//                            HookUtil.printAllFieldForSuperclass(user);

                        }
                    } catch (Exception e) {
                        LoggerUtil.logI(TAG, "eee  102---->" + CrashHandler.getInstance().printCrash(e));
                    }


                }
            });
            XposedHelpers.callMethod(ConnectionsManagerIns, "sendRequest", req, cb);

        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee  101---->" + CrashHandler.getInstance().printCrash(e));
        }
    }


    public  static void saveSearchRespondUsers(List chats, List users) {
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

}
