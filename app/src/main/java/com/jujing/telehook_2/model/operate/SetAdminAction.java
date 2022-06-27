package com.jujing.telehook_2.model.operate;

import static com.jujing.telehook_2.HookMain.classLoader;

import android.os.SystemClock;
import android.text.TextUtils;

import com.jujing.telehook_2.Global;
import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.model.UsersAndChats;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.ExecutorUtil;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.WriteFileUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;

import de.robv.android.xposed.XposedHelpers;

public class SetAdminAction {

    private static final String TAG = "SetAdminAction";

    public static void setUserAdminRole2(long chatId, long user_id) {
        try {
            Object chat = UsersAndChats.getChat2(chatId);
            Object user = UsersAndChats.getUser(user_id);
            Class TL_chatAdminRights = HookMain.classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_chatAdminRights");
            Object right = XposedHelpers.newInstance(TL_chatAdminRights);
            XposedHelpers.setBooleanField(right, "change_info", true);
            XposedHelpers.setBooleanField(right, "post_messages", true);
            XposedHelpers.setBooleanField(right, "edit_messages", true);
            XposedHelpers.setBooleanField(right, "delete_messages", true);
            XposedHelpers.setBooleanField(right, "ban_users", true);
            XposedHelpers.setBooleanField(right, "invite_users", true);
            XposedHelpers.setBooleanField(right, "add_admins", true);
            XposedHelpers.setBooleanField(right, "anonymous", true);
            XposedHelpers.setBooleanField(right, "manage_call", true);
            XposedHelpers.setBooleanField(right, "other", true);


//            Class TL_messages_editChatAdmin = HookMain.classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_messages_editChatAdmin");
//            Object req = XposedHelpers.newInstance(TL_messages_editChatAdmin);
//            XposedHelpers.setLongField(req,"chat_id",chatId);


            Class TL_channels_editAdmin = HookMain.classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_channels_editAdmin");
            Object req = XposedHelpers.newInstance(TL_channels_editAdmin);
            XposedHelpers.setObjectField(req, "admin_rights", right);
            Object inputChannel = UsersAndChats.getInputChannel(chat);
            XposedHelpers.setObjectField(req, "channel", inputChannel);
            Object inputUser = UsersAndChats.getInputUser(user);
            XposedHelpers.setObjectField(req, "user_id", inputUser);
            XposedHelpers.setObjectField(req, "rank", "");
//            XposedHelpers.setBooleanField(req,"is_admin",true);

            Class RequestDelegate = HookMain.classLoader.loadClass("org.telegram.tgnet.RequestDelegate");

            Object callback = Proxy.newProxyInstance(HookMain.classLoader, new Class[]{RequestDelegate}, new InvocationHandler() {
                @Override
                public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                    String mName = method.getName();
                    LoggerUtil.logI(TAG, " setUserAdminRole mName  65:" + mName);

                    switch (mName) {
                        case "run":

                            try {
                                Object response = objects[0];
                                Object error = objects[1];

                                if (error != null) {

                                    int code = XposedHelpers.getIntField(error, "code");
                                    String text = (String) XposedHelpers.getObjectField(error, "text");
                                    LoggerUtil.logI(TAG, "code  80:" + code + " text:" + text);
                                    LoggerUtil.sendLog4("设置管理员错误code:" + code + " text:" + text);
                                    return null;
                                }
                                LoggerUtil.sendLog4("设置管理员成功:" + " id:" + chatId);

                            } catch (Exception e) {
                                LoggerUtil.logI(TAG, "e  87---> " + CrashHandler.getInstance().printCrash(e));
                            }


                            break;
                    }


                    return null;
                }
            });
            Object AccountInstanceIns = Tools.getAccountInstance(HookMain.classLoader);
            Object connectionsManager = XposedHelpers.callMethod(AccountInstanceIns, "getConnectionsManager");
            XposedHelpers.callMethod(connectionsManager, "sendRequest", req, callback);
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eeee  102---->" + CrashHandler.getInstance().printCrash(e));
        }

    }

    public static void setUserAdminRole(long chatId, long user_id) {
        try {
            Object chat = UsersAndChats.getChat2(chatId);
            boolean isChannel = (boolean) XposedHelpers.callStaticMethod(XposedHelpers.findClass("org.telegram.messenger.ChatObject", classLoader), "isChannel", chat);
            LoggerUtil.logI(TAG, "isChannel  111---->" + isChannel);
            if (isChannel) {
                setUserAdminRole2(chatId, user_id);
                return;
            }
            Object user = UsersAndChats.getUser(user_id);
//            Class TL_chatAdminRights = HookMain.classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_chatAdminRights");
//            Object right = XposedHelpers.newInstance(TL_chatAdminRights);
//            XposedHelpers.setBooleanField(right,"change_info",true);
//            XposedHelpers.setBooleanField(right,"post_messages",true);
//            XposedHelpers.setBooleanField(right,"edit_messages",true);
//            XposedHelpers.setBooleanField(right,"delete_messages",true);
//            XposedHelpers.setBooleanField(right,"ban_users",true);
//            XposedHelpers.setBooleanField(right,"invite_users",true);
//            XposedHelpers.setBooleanField(right,"add_admins",true);
//            XposedHelpers.setBooleanField(right,"anonymous",true);
//            XposedHelpers.setBooleanField(right,"manage_call",true);
//            XposedHelpers.setBooleanField(right,"other",true);


            Class TL_messages_editChatAdmin = HookMain.classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_messages_editChatAdmin");
            Object req = XposedHelpers.newInstance(TL_messages_editChatAdmin);
            XposedHelpers.setLongField(req, "chat_id", chatId);


//            Class TL_channels_editAdmin = HookMain.classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_channels_editAdmin");
//            Object req = XposedHelpers.newInstance(TL_channels_editAdmin);
//            XposedHelpers.setObjectField(req, "admin_rights", right);
//            Object inputChannel = UsersAndChats.getInputChannel(chat);
//            XposedHelpers.setObjectField(req, "channel", inputChannel);
            Object inputUser = UsersAndChats.getInputUser(user);
            XposedHelpers.setObjectField(req, "user_id", inputUser);
//            XposedHelpers.setObjectField(req, "rank", "");
            XposedHelpers.setBooleanField(req, "is_admin", true);

            Class RequestDelegate = HookMain.classLoader.loadClass("org.telegram.tgnet.RequestDelegate");

            Object callback = Proxy.newProxyInstance(HookMain.classLoader, new Class[]{RequestDelegate}, new InvocationHandler() {
                @Override
                public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                    String mName = method.getName();
                    LoggerUtil.logI(TAG, " setUserAdminRole mName  65:" + mName);

                    switch (mName) {
                        case "run":

                            try {
                                Object response = objects[0];
                                Object error = objects[1];

                                if (error != null) {

                                    int code = XposedHelpers.getIntField(error, "code");
                                    String text = (String) XposedHelpers.getObjectField(error, "text");
                                    LoggerUtil.logI(TAG, "code  165:" + code + " text:" + text);
                                    LoggerUtil.sendLog4("设置管理员错误code:" + code + " text:" + text);
                                    return null;
                                }
                                LoggerUtil.sendLog4("设置管理员成功:" + " id:" + chatId);

                            } catch (Exception e) {
                                LoggerUtil.logI(TAG, "e  172---> " + CrashHandler.getInstance().printCrash(e));
                            }


                            break;
                    }


                    return null;
                }
            });
            Object AccountInstanceIns = Tools.getAccountInstance(HookMain.classLoader);
            Object connectionsManager = XposedHelpers.callMethod(AccountInstanceIns, "getConnectionsManager");
            XposedHelpers.callMethod(connectionsManager, "sendRequest", req, callback);
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eeee  187---->" + CrashHandler.getInstance().printCrash(e));
        }


    }
}
