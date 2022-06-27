package com.jujing.telehook_2.hook;

import android.os.SystemClock;
import android.provider.Settings;

import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.model.ContactsHandle;
import com.jujing.telehook_2.model.SendMessage;
import com.jujing.telehook_2.util.HookUtil;
import com.jujing.telehook_2.util.LogTool;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class HookRun {


    public static void hookConnectionsManager() {
//        Class<?> SearchAdapterHelper$$Lambda$1 = XposedHelpers.findClass("org.telegram.ui.Adapters.-$$Lambda$SearchAdapterHelper$dscYso9YL4gEzQpoIdpN_Bu9BdA", HookMain.classLoader);
        Class<?> SearchAdapterHelper$$Lambda$1 = XposedHelpers.findClass("org.telegram.ui.Adapters.SearchAdapterHelper$$ExternalSyntheticLambda8", HookMain.classLoader);

        XposedBridge.hookAllMethods(SearchAdapterHelper$$Lambda$1, "run", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String s = HookUtil.printParams(param);
                LogTool.e("run  41---->" + s);//
                Object arg = param.args[0];// 进行判断!!!

                if (ContactsHandle.joinFlag.equals("searchChatAndJoin")) {
                    ContactsHandle.joinFlag = "";
                    List chats = (List) XposedHelpers.getObjectField(arg, "chats");
                    if (chats.size() != 0) {
                        for (int i = 0; i < chats.size(); i++) {
//                        HookUtil.printAllFieldForSuperclass(chats.get(i));
                            String username = (String) XposedHelpers.getObjectField(chats.get(i), "username");
                            LogTool.e("username  35---->" + username);
//                        String substring = query.substring(1, query.length());
                            if (i == 0) {
                                int id = XposedHelpers.getIntField(chats.get(i), "id");
                                LogTool.e("id  44---->" + id);
//                                Class<?> MessagesController = XposedHelpers.findClass("org.telegram.messenger.MessagesController", HookMain.classLoader);
//                                Object MessagesControllerInstance = XposedHelpers.callStaticMethod(MessagesController, "getInstance", 0);
//                                XposedHelpers.callMethod(MessagesControllerInstance, "putChats", chats, false);
//                                ContactsHandle.loadFullChat(id);

                                break;
                            }

                        }
                    }

                    List users = (List) XposedHelpers.getObjectField(arg, "users");
                    LogTool.e("users  67---->" + users.size());
                    if (users.size() != 0) {

                        final long id = XposedHelpers.getLongField(users.get(0), "id");
                        LogTool.e("id  75---->" + id);
                        Class<?> MessagesController = XposedHelpers.findClass("org.telegram.messenger.MessagesController", HookMain.classLoader);
                        Object MessagesControllerInstance = XposedHelpers.callStaticMethod(MessagesController, "getInstance", 0);
                        XposedHelpers.callMethod(MessagesControllerInstance, "putChats", chats, false);
                        ContactsHandle.loadFullChat(id);

                    }


                }


                if (ContactsHandle.joinFlag.equals("searchContactsOnly")) {
                    ContactsHandle.joinFlag = "";

//                    List chats = (List) XposedHelpers.getObjectField(arg, "chats");
//                    LogTool.e("chats  39---->" + chats.size());
//
//                    if (chats.size()!=0){
//                        for (int i = 0; i < chats.size(); i++) {
//                            HookUtil.printAllFieldForSuperclass(chats.get(i));//TODO 搜索出的信息在这里
//                        }
//                    }

                    List users = (List) XposedHelpers.getObjectField(arg, "users");
                    LogTool.e("users  67---->" + users.size());
                    if (users.size() != 0) {

                        final long id = XposedHelpers.getLongField(users.get(0), "id");
                        LogTool.e("id  75---->" + id);
//                        SendMessage.sendMessage("hello", id);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SystemClock.sleep(1000);
//                                SendMessage.sendMessage("hello", id);
                                SendMessage.sendText(false, id, "hello");
                            }
                        }).start();


//                        for (int i = 0; i < users.size(); i++) {
//                            HookUtil.printAllFieldForSuperclass(users.get(i));//TODO 搜索出的信息在这里
//                        }
                    }

                }


            }
        });


//        Class<?> MessagesController$$Lambda$14 = XposedHelpers.findClass("org.telegram.messenger.-$$Lambda$MessagesController$fTyU3cBjthj5hiBMDDqV22Po6Ko", HookMain.classLoader);
//
//        XposedBridge.hookAllMethods(MessagesController$$Lambda$14, "run", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                String s = HookUtil.printParams(param);
//                LogTool.e("sss 83--->" + s);
//                if (!ContactsHandle.joinFlag.equals("")){
//                    LogTool.e("joinFlag  86--->"+ContactsHandle.joinFlag);
//                    HookUtil.printAllField(param.args[1]);
//                    int id = Integer.parseInt(ContactsHandle.joinFlag);
//                    ContactsHandle.joinToChat(id);
//
//                    ContactsHandle.joinFlag = "";
//                }
//
//
////                XposedBridge.unhookMethod(param.method, this);
//            }
//        });
//
//
//        Class<?> MessagesController$$Lambda$110 = XposedHelpers.findClass("org.telegram.messenger.-$$Lambda$MessagesController$h5a2RdtziFUaSy5oo3g6ZX4SUu4", HookMain.classLoader);
//
//        XposedBridge.hookAllMethods(MessagesController$$Lambda$110, "run", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                String s = HookUtil.printParams(param);
//                LogTool.e("sss 104--->" + s);
//                HookUtil.printAllField(param.args[1]);
////                XposedBridge.unhookMethod(param.method, this);
//            }
//        });
    }
}
