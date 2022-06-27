package com.jujing.telehook_2.model;

import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.hook.HookActivity;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.HookUtil;
import com.jujing.telehook_2.util.LogTool;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static com.jujing.telehook_2.HookMain.classLoader;

public class ContactsHandle {
    public static String joinFlag = "";

    public static void hook() {
        Class<?> MessagesController$$ExternalSyntheticLambda317 = XposedHelpers.findClass("org.telegram.messenger.MessagesController$$ExternalSyntheticLambda317", HookMain.classLoader);

        XposedBridge.hookAllConstructors(MessagesController$$ExternalSyntheticLambda317, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String s = HookUtil.printParams(param);
                LogTool.e("MessagesController$$ExternalSyntheticLambda317  27---->" + s);
            }
        });
//        XposedBridge.hookAllMethods(DialogsSearchAdapter, "lambda$searchDialogs$13", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                String s = HookUtil.printParams(param);
//                LogTool.e("lambda$searchDialogs$13  35---->" + s);
//            }
//        });


        Class<?> SearchAdapterHelper = XposedHelpers.findClass("org.telegram.ui.Adapters.SearchAdapterHelper", HookMain.classLoader);
        XposedBridge.hookAllMethods(SearchAdapterHelper, "queryServerSearch", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String s = HookUtil.printParams(param);
//                LogTool.e("queryServerSearch  27---->" + s);
//                joinFlag = "searchContactsOnly";
            }
        });


        Class<?> ConnectionsManager = XposedHelpers.findClass("org.telegram.tgnet.ConnectionsManager", HookMain.classLoader);
        XposedBridge.hookAllMethods(ConnectionsManager, "sendRequest", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String s = HookUtil.printParams(param);
                LogTool.e("sss  22---->" + s);
//                if (s.contains("SearchAdapterHelper$$ExternalSyntheticLambda8")) {
//                    HookUtil.frames();
//                }
//                org.telegram.tgnet.TLRPC$TL_contacts_search@321a4f7,org.telegram.ui.Adapters.SearchAdapterHelper$$ExternalSyntheticLambda7@3a0af64,2,
            }
        });
    }

    public static void search(final String query) {

        Class<?> DialogsSearchAdapter = XposedHelpers.findClass("org.telegram.ui.Adapters.DialogsSearchAdapter", HookMain.classLoader);
        Object o = XposedHelpers.newInstance(DialogsSearchAdapter, HookActivity.baseActivity, 1, 0);
//        XposedHelpers.callMethod(o, "lambda$searchDialogs$13", 1, query, query);

        Object searchAdapterHelper = XposedHelpers.getObjectField(o, "searchAdapterHelper");
        XposedHelpers.callMethod(searchAdapterHelper,"queryServerSearch",query,true,true,true,true,false,0,true,0,1);
        joinFlag = "searchChatAndJoin";
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                Class<?> SearchAdapterHelper = XposedHelpers.findClass("org.telegram.ui.Adapters.SearchAdapterHelper", HookMain.classLoader);
//                Object arg1 = XposedHelpers.newInstance(SearchAdapterHelper, true);//???
//                Object delegate = XposedHelpers.getObjectField(arg1, "delegate");
//                LogTool.e("delegate  55---->" + delegate);
//                XposedHelpers.callMethod(delegate,"canApplySearchResults",1);
//                LogTool.e("delegate  57---->" + delegate);
//                XposedHelpers.callMethod(arg1,"queryServerSearch",query,true,true,true,true,false,0,true,0,1);
//                LogTool.e("delegate  59---->" + delegate);
//
//                joinFlag = "searchContactsOnly";
//            }
//        };
//
//
//        Class<?> aClass = XposedHelpers.findClass("org.telegram.messenger.AndroidUtilities", classLoader);
//        XposedHelpers.callStaticMethod(aClass, "runOnUIThread", runnable);
    }

    //搜索好友
    public static void searchContactsOnly(final String query) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {

                    LogTool.e("query  18---->" + query);
                    Class<?> TLRPC$TL_contacts_search = XposedHelpers.findClass("org.telegram.tgnet.TLRPC$TL_contacts_search", HookMain.classLoader);
                    Object obj = XposedHelpers.newInstance(TLRPC$TL_contacts_search);

                    XposedHelpers.setObjectField(obj, "q", query);
                    XposedHelpers.setIntField(obj, "limit", 50);
                    LogTool.e("obj  24---->" + obj);
            Class<?> SearchAdapterHelper$$Lambda$1 = XposedHelpers.findClass("org.telegram.ui.Adapters.SearchAdapterHelper$$ExternalSyntheticLambda8", HookMain.classLoader);
//                    Class<?> SearchAdapterHelper$$Lambda$1 = XposedHelpers.findClass("org.telegram.ui.Adapters.SearchAdapterHelper$$ExternalSyntheticLambda7", HookMain.classLoader);
                    LogTool.e("SearchAdapterHelper$$Lambda$1  27---->" + SearchAdapterHelper$$Lambda$1);
                    //        Class<?> SearchAdapterHelper$$Lambda$1 = XposedHelpers.findClass("org.telegram.ui.Adapters.-$$Lambda$SearchAdapterHelper$dscYso9YL4gEzQpoIdpN_Bu9BdA", HookMain.classLoader);
                    Class<?> SearchAdapterHelper = XposedHelpers.findClass("org.telegram.ui.Adapters.SearchAdapterHelper", HookMain.classLoader);
                    Object arg1 = XposedHelpers.newInstance(SearchAdapterHelper, true);//???
                    LogTool.e("arg1  42---->" + arg1);
//        final Object obj1 = XposedHelpers.newInstance(SearchAdapterHelper$$Lambda$1, arg1, 1, true, true, true, query);
//                    final Object obj1 = XposedHelpers.newInstance(SearchAdapterHelper$$Lambda$1, arg1, query, true);
                    List<Object> list= new ArrayList<>();
                    List<Object> list2= new ArrayList<>();

                    final Object obj1 = XposedHelpers.newInstance(SearchAdapterHelper$$Lambda$1, arg1, list, 0,null,null,list2,0,null);
//            final Object obj1 = XposedHelpers.newInstance(SearchAdapterHelper$$Lambda$1, arg1);
                    LogTool.e("obj1  32---->" + obj1);

                    Class UserConfig = classLoader.loadClass("org.telegram.messenger.UserConfig");

                    int currentAccount = XposedHelpers.getStaticIntField(UserConfig, "selectedAccount");

                    LogTool.e("currentAccount  61---->" + currentAccount);
                    Class<?> ConnectionsManager = XposedHelpers.findClass("org.telegram.tgnet.ConnectionsManager", HookMain.classLoader);
//                sendRequest  23---->org.telegram.tgnet.TLRPC$TL_contacts_search@bd03b52,org.telegram.ui.Adapters.SearchAdapterHelper$$Lambda$1@d286e23,2,

                    Object getInstance = XposedHelpers.callStaticMethod(ConnectionsManager, "getInstance", currentAccount);
                    LogTool.e("getInstance  66---->" + getInstance);
                    XposedHelpers.callMethod(getInstance, "sendRequest", obj, obj1, 2);


                    joinFlag = "searchContactsOnly";
                    LogTool.e("query  39---->" + query);
                } catch (Exception e) {
                    LogTool.e("e  55---->" + CrashHandler.getInstance().printCrash(e));
                }
            }
        };


        Class<?> aClass = XposedHelpers.findClass("org.telegram.messenger.AndroidUtilities", classLoader);
        XposedHelpers.callStaticMethod(aClass, "runOnUIThread", runnable);
//        XposedBridge.hookAllMethods(SearchAdapterHelper$$Lambda$1, "run", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//
//                String s = HookUtil.printParams(param);
//
//                LogTool.e("run  41---->" + s);//
////
//                Object arg = param.args[0];//进行判断!!!
//                HookUtil.printAllField(arg);
//
//
//                List chats = (List) XposedHelpers.getObjectField(arg, "chats");
//                if (chats.size()!=0){
//                    for (int i = 0; i < chats.size(); i++) {
//                        HookUtil.printAllFieldForSuperclass(chats.get(i));//TODO 搜索出的信息在这里
//                    }
//                }
//
//                List users = (List) XposedHelpers.getObjectField(arg, "users");
//                if (users.size()!=0){
//                    for (int i = 0; i < users.size(); i++) {
//                        HookUtil.printAllFieldForSuperclass(users.get(i));//TODO 搜索出的信息在这里
//                    }
//                }
//
//
//                XposedBridge.unhookMethod(param.method, this);
//            }
//        });

    }

    /**
     * 加群
     */
    public static void searchChatAndJoin(final String query) {
        Class<?> TLRPC$TL_contacts_search = XposedHelpers.findClass("org.telegram.tgnet.TLRPC$TL_contacts_search", HookMain.classLoader);
        Object obj = XposedHelpers.newInstance(TLRPC$TL_contacts_search);

        XposedHelpers.setObjectField(obj, "q", query);
        XposedHelpers.setIntField(obj, "limit", 50);

//        Class<?> SearchAdapterHelper$$Lambda$1 = XposedHelpers.findClass("org.telegram.ui.Adapters.SearchAdapterHelper$$Lambda$1", HookMain.classLoader);
        Class<?> SearchAdapterHelper$$Lambda$1 = XposedHelpers.findClass("org.telegram.ui.Adapters.-$$Lambda$SearchAdapterHelper$dscYso9YL4gEzQpoIdpN_Bu9BdA", HookMain.classLoader);
        Class<?> SearchAdapterHelper = XposedHelpers.findClass("org.telegram.ui.Adapters.SearchAdapterHelper", HookMain.classLoader);
        Object arg1 = XposedHelpers.newInstance(SearchAdapterHelper, true);//???
        final Object obj1 = XposedHelpers.newInstance(SearchAdapterHelper$$Lambda$1, arg1, 1, true, true, true, query);

        Class<?> ConnectionsManager = XposedHelpers.findClass("org.telegram.tgnet.ConnectionsManager", HookMain.classLoader);
//                sendRequest  23---->org.telegram.tgnet.TLRPC$TL_contacts_search@bd03b52,org.telegram.ui.Adapters.SearchAdapterHelper$$Lambda$1@d286e23,2,

        Object getInstance = XposedHelpers.callStaticMethod(ConnectionsManager, "getInstance", 0);
        XposedHelpers.callMethod(getInstance, "sendRequest", obj, obj1, 2);

        joinFlag = "searchChatAndJoin";
//        XposedBridge.hookAllMethods(SearchAdapterHelper$$Lambda$1, "run", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                String s = HookUtil.printParams(param);
//                LogTool.e("run  41---->" + s);//
//                Object arg = param.args[0];// 进行判断!!!
//                List chats = (List) XposedHelpers.getObjectField(arg, "chats");
//                if (chats.size()!=0){
//                    for (int i = 0; i < chats.size(); i++) {
////                        HookUtil.printAllFieldForSuperclass(chats.get(i));
//                        String username = (String) XposedHelpers.getObjectField(chats.get(i), "username");
//                        LogTool.e("username  103---->" + username);
////                        String substring = query.substring(1, query.length());
//                        if (i==0){
//                            int id = XposedHelpers.getIntField(chats.get(i), "id");
//                            LogTool.e("id  109---->" + id);
//                            Class<?> MessagesController = XposedHelpers.findClass("org.telegram.messenger.MessagesController", HookMain.classLoader);
//                            Object MessagesControllerInstance = XposedHelpers.callStaticMethod(MessagesController, "getInstance", 0);
//                            XposedHelpers.callMethod(MessagesControllerInstance, "putChats", chats, false);
//
//                            loadFullChat(id);
//
//                            break;
//                        }
//
//                    }
//                }
//                XposedBridge.unhookMethod(param.method, this);
//            }
//        });

    }

    public static void loadFullChat(final long id) {//TLRPC$TL_channels_getFullChannel    1216938411
//        Class<?> MessagesStorage = XposedHelpers.findClass("org.telegram.messenger.MessagesStorage", HookMain.classLoader);
        Class<?> MessagesController = XposedHelpers.findClass("org.telegram.messenger.MessagesController", HookMain.classLoader);
        Object getInstance = XposedHelpers.callStaticMethod(MessagesController, "getInstance", 0);
        Object getChat = XposedHelpers.callMethod(getInstance, "getChat", id);
        LogTool.e("getChat 151--->" + getChat);
//        Class<?> MessagesController = XposedHelpers.findClass("org.telegram.messenger.MessagesController", HookMain.classLoader);
        Object MessagesControllerInstance = XposedHelpers.callStaticMethod(MessagesController, "getInstance", 0);
//        XposedHelpers.callMethod(MessagesControllerInstance, "putChat", getChat, true);

        Object getInputChannel = XposedHelpers.callMethod(MessagesControllerInstance, "getInputChannel", id);

        Class<?> TLRPC$TL_channels_getFullChannel = XposedHelpers.findClass("org.telegram.tgnet.TLRPC$TL_channels_getFullChannel", HookMain.classLoader);
        Object req = XposedHelpers.newInstance(TLRPC$TL_channels_getFullChannel);
        XposedHelpers.setObjectField(req, "channel", getInputChannel);

//        Class<?> MessagesController$$Lambda$14 = XposedHelpers.findClass("org.telegram.messenger.MessagesController$$Lambda$14", HookMain.classLoader);
        Class<?> MessagesController$$Lambda$14 = XposedHelpers.findClass("org.telegram.messenger.-$$Lambda$MessagesController$fTyU3cBjthj5hiBMDDqV22Po6Ko", HookMain.classLoader);

        Object o = XposedHelpers.newInstance(MessagesController$$Lambda$14, MessagesControllerInstance, getChat, -id, id, 0);

        Class<?> ConnectionsManager = XposedHelpers.findClass("org.telegram.tgnet.ConnectionsManager", HookMain.classLoader);
        Object getInstance2 = XposedHelpers.callStaticMethod(ConnectionsManager, "getInstance", 0);
        XposedHelpers.callMethod(getInstance2, "sendRequest", req, o);
        joinFlag = id + "";


    }


    public static void joinToChat(int chat_id) {
        Class<?> MessagesController = XposedHelpers.findClass("org.telegram.messenger.MessagesController", HookMain.classLoader);
        Object getInstance = XposedHelpers.callStaticMethod(MessagesController, "getInstance", 0);
        Object getInputChannel = XposedHelpers.callMethod(getInstance, "getInputChannel", chat_id);

        Class<?> TLRPC$TL_channels_joinChannel = XposedHelpers.findClass("org.telegram.tgnet.TLRPC$TL_channels_joinChannel", HookMain.classLoader);
        Object req = XposedHelpers.newInstance(TLRPC$TL_channels_joinChannel);
        XposedHelpers.setObjectField(req, "channel", getInputChannel);

        Class<?> UserConfig = XposedHelpers.findClass("org.telegram.messenger.UserConfig", HookMain.classLoader);
        Object getInstance1 = XposedHelpers.callStaticMethod(UserConfig, "getInstance", 0);
        Object user = XposedHelpers.callMethod(getInstance1, "getCurrentUser");

        Object inputUser = XposedHelpers.callMethod(getInstance, "getInputUser", user);
//                ArrayList<Object> users = new ArrayList<>();
//                users.add(inputUser);
//                XposedHelpers.setObjectField(req,"users",users);


//        Class<?> MessagesController$$Lambda$110 = XposedHelpers.findClass("org.telegram.messenger.MessagesController$$Lambda$110", HookMain.classLoader);
        Class<?> MessagesController$$Lambda$110 = XposedHelpers.findClass("org.telegram.messenger.-$$Lambda$MessagesController$h5a2RdtziFUaSy5oo3g6ZX4SUu4", HookMain.classLoader);
        Class<?> BaseFragment = XposedHelpers.findClass("org.telegram.ui.ActionBar.BaseFragment", HookMain.classLoader);
        Object chatActivity = XposedHelpers.newInstance(BaseFragment);

        Object o = XposedHelpers.newInstance(MessagesController$$Lambda$110, getInstance, true, inputUser, chat_id, chatActivity, req, true, null);

        Class<?> ConnectionsManager = XposedHelpers.findClass("org.telegram.tgnet.ConnectionsManager", HookMain.classLoader);
        Object getInstance2 = XposedHelpers.callStaticMethod(ConnectionsManager, "getInstance", 0);
        XposedHelpers.callMethod(getInstance2, "sendRequest", req, o);

//        XposedBridge.hookAllMethods(MessagesController$$Lambda$110, "run", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                String s = HookUtil.printParams(param);
//                LogTool.e("sss 173--->" + s);
//                HookUtil.printAllField(param.args[1]);
//                XposedBridge.unhookMethod(param.method, this);
//            }
//        });
    }


    /**
     * 拉人进群
     */
    public static void addChatUser(int userId, int chatId) {
        Object user = UsersAndChats.getUser(userId);

        Class<?> MessagesController = XposedHelpers.findClass("org.telegram.messenger.MessagesController", HookMain.classLoader);
        Object getInstance = XposedHelpers.callStaticMethod(MessagesController, "getInstance", 0);
        Object inputUser = XposedHelpers.callMethod(getInstance, "getInputUser", user);

        Class<?> TLRPC$TL_messages_addChatUser = XposedHelpers.findClass("org.telegram.tgnet.TLRPC$TL_messages_addChatUser", HookMain.classLoader);
        Object req = XposedHelpers.newInstance(TLRPC$TL_messages_addChatUser);
        XposedHelpers.setIntField(req, "chat_id", chatId);
        XposedHelpers.setIntField(req, "fwd_limit", 50);
        XposedHelpers.setObjectField(req, "user_id", inputUser);
        Class<?> BaseFragment = XposedHelpers.findClass("org.telegram.ui.ActionBar.BaseFragment", HookMain.classLoader);
        Object ProfileActivity = XposedHelpers.newInstance(BaseFragment);

//        Class<?> MessagesController$$Lambda$110 = XposedHelpers.findClass("org.telegram.messenger.MessagesController$$Lambda$110", HookMain.classLoader);
        Class<?> MessagesController$$Lambda$110 = XposedHelpers.findClass("org.telegram.messenger.-$$Lambda$MessagesController$h5a2RdtziFUaSy5oo3g6ZX4SUu4", HookMain.classLoader);
        Object o = XposedHelpers.newInstance(MessagesController$$Lambda$110, getInstance, false, inputUser, chatId, ProfileActivity, req, false, null);


        Class<?> ConnectionsManager = XposedHelpers.findClass("org.telegram.tgnet.ConnectionsManager", HookMain.classLoader);
        Object getInstance2 = XposedHelpers.callStaticMethod(ConnectionsManager, "getInstance", 0);
        XposedHelpers.callMethod(getInstance2, "sendRequest", req, o);

        XposedBridge.hookAllMethods(MessagesController$$Lambda$110, "run", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String s = HookUtil.printParams(param);
                LogTool.e("sss 176--->" + s);
                HookUtil.printAllField(param.args[1]);
                XposedBridge.unhookMethod(param.method, this);
            }
        });
    }
}
