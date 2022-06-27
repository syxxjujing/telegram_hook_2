package com.jujing.telehook_2.hook;

import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.util.HookUtil;
import com.jujing.telehook_2.util.LogTool;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class HookUserInfos {

    public static void hook(){
        Class<?> MessagesStorage = XposedHelpers.findClass("org.telegram.messenger.MessagesStorage", HookMain.classLoader);
        XposedBridge.hookAllMethods(MessagesStorage, "loadChatInfo", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String s = HookUtil.printParams(param);
                LogTool.e("loadChatInfo  22---->"+s);//打开群的时候 加载的
            }
        });


        Class<?> aClass = XposedHelpers.findClass("org.telegram.messenger.ContactsController", HookMain.classLoader);
        XposedBridge.hookAllMethods(aClass, "getContactsCopy", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String s = HookUtil.printParams(param);
                LogTool.e("getContactsCopy  20---->"+s);

            }
        });


        XposedBridge.hookAllMethods(aClass, "processLoadedContacts", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String s = HookUtil.printParams(param);
                LogTool.e("processLoadedContacts  31---->"+s);

                List list = (List) param.args[0];
                LogTool.e("list 36--->"+list.size());

                Object o = list.get(0);
                HookUtil.printAllField(o);


                List list1 = (List) param.args[1];
                LogTool.e("list 43--->"+list1.size());
                Object o1 = list1.get(0);
                HookUtil.printAllField(o1);

//                HookUtil.frames();

            }
        });


    }


}
