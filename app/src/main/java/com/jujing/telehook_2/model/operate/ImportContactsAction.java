package com.jujing.telehook_2.model.operate;

import static com.jujing.telehook_2.HookMain.classLoader;

import com.jujing.telehook_2.model.SendMessage;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.WriteFileUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XposedHelpers;

public class ImportContactsAction {

    private static final String TAG = "ImportContactsAction";

    public static void handle(String path) {
        try {
            List<String> stringList = WriteFileUtil.readFile(path);
            LoggerUtil.logI(TAG, "stringList  25---->" + stringList.size());
            LoggerUtil.sendLog6("开始导入，共" + stringList.size() + "个联系人");
            importContact(stringList, 0);
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee  26---->" + CrashHandler.getInstance().printCrash(e));
        }


    }

    public static void importContact(List<String> stringList, int index) {
        try {
            String phone0 = stringList.get(index);
            String phone = phone0.replace(" ", "");
            LoggerUtil.logI(TAG, "importContact  22---->" + phone0 + "---->" + index);
            LoggerUtil.sendLog6("开始导入第" + (index + 1) + "个联系人：" + phone);
            Class<?> TL_contacts_importContacts = classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_contacts_importContacts");
            Object req = XposedHelpers.newInstance(TL_contacts_importContacts);
            Class<?> TL_inputPhoneContact = classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_inputPhoneContact");
            Object inputPhoneContact = XposedHelpers.newInstance(TL_inputPhoneContact);
            XposedHelpers.setObjectField(inputPhoneContact, "first_name", phone + " ");
            XposedHelpers.setObjectField(inputPhoneContact, "last_name", " ");
            XposedHelpers.setObjectField(inputPhoneContact, "phone", phone + " ");
            Object contacts = XposedHelpers.getObjectField(req, "contacts");
            XposedHelpers.callMethod(contacts, "add", inputPhoneContact);

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

                    importResult(response, error, phone, index);

                    int indexNew = index + 1;
                    importContact(stringList, indexNew);
                }
            });
            XposedHelpers.callMethod(ConnectionsManagerIns, "sendRequest", req, cb);


        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee  74---->" + CrashHandler.getInstance().printCrash(e));
        }
    }

    private static void importResult(Object response, Object error, String phone, int index) {
        try {

            if (error != null) {
                String text = (String) XposedHelpers.getObjectField(error, "text");
                int code = XposedHelpers.getIntField(error, "code");
                LoggerUtil.logI(TAG, "导入出错  54:" + text + ":" + code + "----->" + phone);
                LoggerUtil.sendLog6("导入出错，第" + (index + 1) + "个联系人：" + phone);
                return;
            }
            if (response == null) {
                LoggerUtil.logI(TAG, "导入请求出错:response==null----》" + phone);
                LoggerUtil.sendLog6("导入请求出错，第" + (index + 1) + "个联系人：" + phone);
                return;
            }

            ArrayList users = (ArrayList) XposedHelpers.getObjectField(response, "users");
            if (users == null || users.isEmpty()) {
                LoggerUtil.logI(TAG, "导入为空:users==null||users.isEmpty---->" + phone);
                LoggerUtil.sendLog6("导入为空，第" + (index + 1) + "个联系人：" + phone);
                return;
            }
            LoggerUtil.sendLog6("导入成功，第" + (index + 1) + "个联系人：" + phone);
            LoggerUtil.logI(TAG, "users.size  21---->" + users.size() + "---->" + phone);

            for (int i = 0; i < users.size(); i++) {
                Object user = users.get(i);
//                            HookUtil.printAllFieldForSuperclass(user);
                final long id = XposedHelpers.getLongField(user, "id");
                LoggerUtil.logI(TAG, "user id:" + id);
                String username = (String) XposedHelpers.getObjectField(user, "username");
                LoggerUtil.logI(TAG, "user username:" + username);
                String first_name = (String) XposedHelpers.getObjectField(user, "first_name");
                LoggerUtil.logI(TAG, "user first_name:" + first_name);
            }


        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee  63---->" + CrashHandler.getInstance().printCrash(e));
        }
    }
}
