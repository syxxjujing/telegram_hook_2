package com.jujing.telehook_2.hook;

import android.database.sqlite.SQLiteDatabase;

import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.util.HookUtil;
import com.jujing.telehook_2.util.LogTool;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class HookSqlite {
    public  static Object database = null;
    public static void hook(){
        Class<?> MessagesStorage = XposedHelpers.findClass("org.telegram.messenger.MessagesStorage", HookMain.classLoader);
        XposedBridge.hookAllMethods(MessagesStorage, "openDatabase", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                 database = XposedHelpers.getObjectField(param.thisObject, "database");
                 LogTool.e("database  22--->"+database);
            }
        });


        Class<?> aClass = XposedHelpers.findClass("org.telegram.SQLite.SQLiteDatabase", HookMain.classLoader);
//        XposedBridge.hookAllMethods(aClass, "executeFast", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                String s = HookUtil.printParams(param);
//                LogTool.e("executeFast  22---->"+s);
//
////                messages_holes  疑似群的
//
//
//
//            }
//        });
//
//        XposedBridge.hookAllMethods(aClass, "queryFinalized", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                String s = HookUtil.printParams(param);
//                LogTool.e("queryFinalized  36---->"+s);
//
////                messages_holes  疑似群的
//
//
//
//            }
//        });
//
//
//
//        XposedBridge.hookAllMethods(SQLiteDatabase.class, "insertWithOnConflict", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                String s = HookUtil.printParams(param);
//                LogTool.e("sss  19---->"+s);
//
//
//            }
//        });
    }
}
