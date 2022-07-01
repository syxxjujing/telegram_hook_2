package com.jujing.telehook_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Process;
import android.os.SystemClock;

import com.jujing.telehook_2.bean.ChatBean;
import com.jujing.telehook_2.hook.HookActivity;
import com.jujing.telehook_2.hook.HookConnection;
import com.jujing.telehook_2.hook.HookDialog;
import com.jujing.telehook_2.hook.HookHttp;
import com.jujing.telehook_2.hook.HookMessage;
import com.jujing.telehook_2.hook.HookRun;
import com.jujing.telehook_2.hook.HookSqlite;
import com.jujing.telehook_2.hook.HookUserInfos;
import com.jujing.telehook_2.hook.OnFragmentCreate;
import com.jujing.telehook_2.model.ContactsHandle;
import com.jujing.telehook_2.model.SendMessage;
import com.jujing.telehook_2.model.UsersAndChats;
import com.jujing.telehook_2.model.operate.AddAndInviteGroupAction;
import com.jujing.telehook_2.model.operate.AppKeepLiveAction;
import com.jujing.telehook_2.model.operate.BanLoginAction;
import com.jujing.telehook_2.model.operate.GetChannelParticipants;
import com.jujing.telehook_2.model.operate.GroupAddMemberAction;
import com.jujing.telehook_2.model.operate.GroupInfoAction;
import com.jujing.telehook_2.model.operate.ImportContactsAction;
import com.jujing.telehook_2.model.operate.JoinToGroupAction;
import com.jujing.telehook_2.model.operate.JudgeCountryAndLangAction;
import com.jujing.telehook_2.model.operate.LoadFullUser;
import com.jujing.telehook_2.model.operate.LoadFullUserAction;
import com.jujing.telehook_2.model.operate.SearchContactAction;
import com.jujing.telehook_2.model.operate.UserReadAction;
import com.jujing.telehook_2.util.AbstractCrashReportHandler;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.ExecutorUtil;
import com.jujing.telehook_2.util.HookUtil;
import com.jujing.telehook_2.util.JsonTool;
import com.jujing.telehook_2.util.LogTool;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.ReflexUtils;
import com.jujing.telehook_2.util.WriteFileUtil;
import com.tencent.bugly.crashreport.CrashReport;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.sql.DataSource;

import dalvik.system.PathClassLoader;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class HookMain implements IXposedHookLoadPackage {
    private static final String TAG = "HookMain";
    public static ClassLoader classLoader;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.jujing.telehook_2")) {
            handleLoadApp(lpparam);
        }


        if (lpparam.packageName.equals("org.telegram.messenger") || lpparam.packageName.equals("org.telegram.messenger.web")) {
//        if (lpparam.packageName.equals("org.telegram.messenger.web")) {
            XposedHelpers.findAndHookMethod("android.app.Application", lpparam.classLoader, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Context context = (Context) param.thisObject;
                    if (Global.IS_DEBUG_XIAOMI) {//TODO 测试
                        handleLoad(lpparam, context); //vxposed
                    } else {
                        handleLoadOnFly(context, lpparam); //xposed
                    }
                }
            });
        }


        if (lpparam.packageName.equals("com.network.xfjsq")) {
            Object currentActivityThread = callStaticMethod(findClass("android.app.ActivityThread", null), "currentActivityThread");
            final Context context = (Context) XposedHelpers.callMethod(currentActivityThread, "getSystemContext", new Object[0]);
            handleLoadVPN(lpparam, context); //vxposed
//            XposedHelpers.findAndHookMethod("android.app.Application", lpparam.classLoader, "attach", Context.class, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    super.afterHookedMethod(param);
//                    Context context = (Context) param.thisObject;
//                    if (false) {
//                        handleLoadVPN(lpparam, context); //vxposed
//                    } else {
//                        handleLoadVPNOnFly(context, lpparam); //xposed
//                    }
//
//                }
//            });
        }
    }


    private void handleLoad(XC_LoadPackage.LoadPackageParam lpparam, Context context) {
        LoggerUtil.logI(TAG, "lpparam  118--->" + lpparam.processName + "---->" + Global.XPOSED_CODE);
        classLoader = lpparam.classLoader;
        new AbstractCrashReportHandler();
        String ban_login = WriteFileUtil.read(Global.BAN_LOGIN);
        LoggerUtil.logI(TAG, "ban_login =====================>" + ban_login);
        if (ban_login.equals("false")) {
            BanLoginAction.handle();
            return;
        }


//        HookSqlite.hook();
        HookMessage.hook();
//        HookUserInfos.hook();
//        try {
//            HookHttp.initHooking(lpparam);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        HookConnection.hook();
//        ContactsHandle.hook();
//        HookRun.hookConnectionsManager();
        HookActivity.hook();
//        AppKeepLiveAction.loopHttp();
        LoadFullUserAction.hook();
        HookDialog.ban();
        OnFragmentCreate.hook();
        CrashHandler.handle();
        AddAndInviteGroupAction.hook();
        CrashReport.initCrashReport(context, "9e15f1224f", true);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(HookMain.ACTION_XTELE_CONTACTS_BOOK_TRAN);
        intentFilter.addAction(HookMain.ACTION_XTELE_LOCAL_TRAN);
        intentFilter.addAction(HookMain.ACTION_XTELE_GROUP);
        intentFilter.addAction(HookMain.ACTION_XTELE_COLLECT);
        intentFilter.addAction(HookMain.ACTION_XTELE_GROUP_JOIN);
        intentFilter.addAction(HookMain.ACTION_XTELE_GROUP_CHECK_LIST);
        intentFilter.addAction(HookMain.ACTION_XTELE_GROUP_START);
        intentFilter.addAction(HookMain.ACTION_XTELE_GROUP_STOP);
        intentFilter.addAction(HookMain.ACTION_XTELE_GROUP_CREATE);
        intentFilter.addAction(HookMain.ACTION_XTELE_GROUP_ADD_MEMBER);
        intentFilter.addAction(HookMain.ACTION_XTELE_IMPORT_BOOK);
        MyReceiver myReceiver = new MyReceiver();
        context.registerReceiver(myReceiver, intentFilter);
        ExecutorUtil.doExecute(new Runnable() {
            @Override
            public void run() {
                try {
                    JudgeCountryAndLangAction.initCountry();
                    for (int i = 0; i < 50; i++) {
                        Object currentUser = UsersAndChats.getCurrentUser();
                        LoggerUtil.logI(TAG, "currentUser   113=============>" + i + "-----------------" + currentUser);
                        if (currentUser == null) {
                            SystemClock.sleep(1000);
                        } else {
                            break;
                        }
                    }
                    Object currentUser = UsersAndChats.getCurrentUser();
//                HookUtil.printAllFieldForSuperclass(currentUser);
                    long id = XposedHelpers.getLongField(currentUser, "id");
                    LoggerUtil.logI(TAG, "id  123---->" + id);
                    WriteFileUtil.write(id + "", Global.USER_INFO_ID);
                    String first_name = (String) XposedHelpers.getObjectField(currentUser, "first_name");
                    String last_name = (String) XposedHelpers.getObjectField(currentUser, "last_name");
                    WriteFileUtil.write(first_name + last_name, Global.USER_INFO_NICKNAME);
                    LoggerUtil.logI(TAG, "first_name&last_name  126---->" + first_name + last_name);
                    String phone = (String) XposedHelpers.getObjectField(currentUser, "phone");
                    LoggerUtil.logI(TAG, "phone  158---->" + phone);
                    WriteFileUtil.write(phone, Global.USER_INFO_PHONE);
                    SystemClock.sleep(5000);
                    UsersAndChats.getContactsInfo();


                    String is_start_caiji = WriteFileUtil.read(Global.IS_START_CAIJI);
                    LoggerUtil.logI(TAG, "is_start_caiji  191---->" + is_start_caiji);
                    if (is_start_caiji.equals("1")) {
                        SystemClock.sleep(30000);
                        LoggerUtil.sendLog("重启飞机后，开始继续上次的采集。。。");
                        GetChannelParticipants.handleAll(false);
                    }
                } catch (Exception e) {
                    LoggerUtil.logI(TAG, "eee  188---->" + CrashHandler.getInstance().printCrash(e));
                }


            }
        });
        LoggerUtil.logI(TAG, "lpparam  59--->" + lpparam.processName);
    }


    private void handleLoadVPN(XC_LoadPackage.LoadPackageParam lpparam, Context context) {
        classLoader = lpparam.classLoader;
        LoggerUtil.logI(TAG, "handleLoadVPN  123--->" + lpparam.packageName);
        Class<?> CommUrlApi = XposedHelpers.findClass("com.network.xfjsq.ui.CommUrlApi", HookMain.classLoader);
//        XposedBridge.hookAllMethods(CommUrlApi, "updateInfo", new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
//
//                String s = HookUtil.printParams(param);
//
//                LoggerUtil.logI(TAG,"updateInfo  100--->" + s);
//
//                String str = "{\"id\":\"3295966\",\"unicode\":\"35e22bf237ca9cc2QC658T9SEUMN89F6\",\"code\":\"3MXUUAO\",\"recomm_code\":\"\",\"num\":\"0\",\"mz\":\"0\",\"xb\":\"0\",\"addtime\":\"1563502035\",\"time_all\":7200,\"unlimit\":1,\"time_cooldown\":480,\"time_remain\":360,\"api_share\":\"https:\\/\\/www.ck88.live\",\"api_share_text\":\"【亲测有效】撸油管、刷INS、访Tui特，高清1080P视频流畅播放，无任何流量限制，真正免费的加速软件\"}";
//                param.args[1] = str;
//            }
//        });
        XposedBridge.hookAllMethods(CommUrlApi, "setLocalKV", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                String s = HookUtil.printParams(param);

                LoggerUtil.logI(TAG, "setLocalKV  144--->" + s);
                if (s.contains("user_unlimit")) {
                    param.args[2] = "1";
                }
                if (s.contains("user_level")) {
                    param.args[2] = "2";
                }
                if (s.contains("level_name")) {
                    param.args[2] = "黄金会员";
                }
                if (s.contains("user_endtime")) {
                    param.args[2] = "1612792086";
                }

            }
        });

        Class<?> LineItem = XposedHelpers.findClass("com.network.xfjsq.ui.LineItem", HookMain.classLoader);
        XposedBridge.hookAllMethods(LineItem, "getEnable", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult(1);

            }
        });
    }

    public static final String ACTION_XTELE_CONTACTS_BOOK_TRAN = "ACTION_XTELE_CONTACTS_BOOK_TRAN";
    public static final String ACTION_XTELE_LOCAL_TRAN = "ACTION_XTELE_LOCAL_TRAN";
    public static final String ACTION_XTELE_GROUP = "ACTION_XTELE_GROUP";
    public static final String ACTION_XTELE_COLLECT = "ACTION_XTELE_COLLECT_2";
    public static final String ACTION_XTELE_GROUP_JOIN = "ACTION_XTELE_GROUP_JOIN";
    public static final String ACTION_XTELE_GROUP_CHECK_LIST = "ACTION_XTELE_GROUP_CHECK_LIST";
    public static final String ACTION_XTELE_GROUP_START = "ACTION_XTELE_GROUP_START";
    public static final String ACTION_XTELE_GROUP_STOP = "ACTION_XTELE_GROUP_STOP";
    public static final String ACTION_XTELE_GROUP_CREATE = "ACTION_XTELE_GROUP_CREATE";
    public static final String ACTION_XTELE_GROUP_ADD_MEMBER = "ACTION_XTELE_GROUP_ADD_MEMBER";
    public static final String ACTION_XTELE_IMPORT_BOOK = "ACTION_XTELE_IMPORT_BOOK";

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, final Intent intent) {
            try {
                if (!intent.getAction().equals(ACTION_XTELE_COLLECT)) {
                    LoggerUtil.logI(TAG, "intent  94--->" + intent.getAction());
                }
            } catch (Exception e) {

            }

            if (intent.getAction().equals(ACTION_XTELE_IMPORT_BOOK)) {
                String path = intent.getStringExtra("path");
                LoggerUtil.logI(TAG, "path  288----------" + path);
                ImportContactsAction.handle(path);
            }

            if (intent.getAction().equals(ACTION_XTELE_GROUP_ADD_MEMBER)) {
                GroupAddMemberAction.handle();

            }
            if (intent.getAction().equals(ACTION_XTELE_GROUP_CREATE)) {
//                String title = intent.getStringExtra("title");
                AddAndInviteGroupAction.createGroup();

            }
            if (intent.getAction().equals(ACTION_XTELE_GROUP_STOP)) {
                AddAndInviteGroupAction.isStart = false;
                LoggerUtil.sendLog4("已停止加人");
            }

            if (intent.getAction().equals(ACTION_XTELE_GROUP_START)) {
                long chatId = intent.getLongExtra("chatId", -1);

                AddAndInviteGroupAction.handle(chatId);
            }


            if (intent.getAction().equals(ACTION_XTELE_GROUP_CHECK_LIST)) {
                List<ChatBean> groupIdList = UserReadAction.getGroupIdList();
                String json = JsonTool.paraseJson(groupIdList);
                LoggerUtil.logI(TAG, "json  271--->" + json);
                WriteFileUtil.write(json, Global.GROUP_LIST_JSON);


            }

            if (intent.getAction().equals(ACTION_XTELE_GROUP_JOIN)) {
                String path = intent.getStringExtra("path");
                LoggerUtil.logI(TAG, "path  255--->" + path);
                JoinToGroupAction.handle(path);
            }
            if (intent.getAction().equals(ACTION_XTELE_COLLECT)) {
                String path = intent.getStringExtra("path");
                if (path.equals("check")) {
                    UserReadAction.checkSendSucceedNum(true);
                    return;
                }

                if (path.equals("check_result")) {
                    UserReadAction.checkSendSucceedNum(false);
                    return;
                }


                UserReadAction.handle();

            }
            if (intent.getAction().equals(ACTION_XTELE_GROUP)) {
                boolean isStop0 = false;
                try {
                    int isStop = intent.getIntExtra("isStop", -1);
                    if (isStop == 1) {
                        isStop0 = true;
                        LoggerUtil.sendLog("已停止采集");
                        GetChannelParticipants.Stop = true;
                        GetChannelParticipants.isStart = false;
                        WriteFileUtil.write("0", Global.IS_START_CAIJI);
                        GetChannelParticipants.chatidList.clear();
                        return;
                    }
                    if (isStop == 2) {
                        isStop0 = true;

                        List<ChatBean> groupIdList = UserReadAction.getGroupIdList();
                        LoggerUtil.logI(TAG, "groupIdList  262--------->" + groupIdList.size());
                        LoggerUtil.sendLog("群数量为:" + groupIdList.size());
                        HookActivity.showToast("群数量为:" + groupIdList.size());
                        return;
                    }
                    if (isStop == 3) {
//                        ExecutorUtil.doExecute(new Runnable() {
//                            @Override
//                            public void run() {
//                                LoggerUtil.sendLog("已经5分钟没有采集到数据了，现将要退出重开...");
//                                LoggerUtil.logI(TAG,"杀死  281---------");
//                                SystemClock.sleep(2000);
//
//                                System.exit(0);
//                            }
//                        });
                        isStop0 = true;
                        ExecutorUtil.doExecute(new Runnable() {
                            @Override
                            public void run() {
                                GetChannelParticipants.Stop = true;
                                GetChannelParticipants.isStart = false;
                                GetChannelParticipants.chatidList.clear();
                                LoggerUtil.sendLog("已经5分钟没有采集到数据了，现将要退出重开...");
                                WriteFileUtil.write(System.currentTimeMillis() + "", Global.IS_STOP_CAIJI);
                                SystemClock.sleep(60 * 1000);
                                WriteFileUtil.write(System.currentTimeMillis() + "", Global.IS_STOP_CAIJI);
                                GetChannelParticipants.handleAll(true);


                            }
                        });

                        return;
                    }


                } catch (Exception e) {

                }
                if (!isStop0) {
                    boolean isRe = intent.getBooleanExtra("isRe", false);
                    GetChannelParticipants.handleAll(isRe);
                }


            }
            if (intent.getAction().equals(ACTION_XTELE_CONTACTS_BOOK_TRAN)) {
                LoggerUtil.logI(TAG, "start  127----------");

                String inerval_friends = intent.getStringExtra("content");
                if (inerval_friends.equals("stop")) {
                    UsersAndChats.isStart = false;
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        UsersAndChats.getChatsInfos();
                        String inerval_friends = intent.getStringExtra("content");
                        String interval_messages = intent.getStringExtra("interval");

                        LoggerUtil.logI(TAG, "inerval_friends  230----------" + inerval_friends + "---->" + interval_messages);
                        UsersAndChats.contacts(inerval_friends, interval_messages);
                    }
                }).start();
                LoggerUtil.logI(TAG, "start  140----------");
            }

            if (intent.getAction().equals(ACTION_XTELE_LOCAL_TRAN)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        UsersAndChats.getChatsInfos();
                        String path = intent.getStringExtra("path");

                        LoggerUtil.logI(TAG, "path  258----------" + path);
                        SearchContactAction.handle(path);
                    }
                }).start();
            }

        }


    }


    private void handleLoadOnFly(Context context, XC_LoadPackage.LoadPackageParam loadPackageParam) throws Exception {
        String apkPath = findAPKPath(context, "com.jujing.telehook_2");
        if (!new File(apkPath).exists()) {
//            LogTool.d("Cannot load module on fly: APK not found");
            return;
        }

        PathClassLoader pathClassLoader = new PathClassLoader(apkPath, ClassLoader.getSystemClassLoader());
        Class<?> clazz = Class.forName("com.jujing.telehook_2.HookMain", true, pathClassLoader);
        Method method = clazz.getDeclaredMethod("handleLoad", loadPackageParam.getClass(), Context.class);
        method.setAccessible(true);
        method.invoke(clazz.newInstance(), loadPackageParam, context);
    }


    private void handleLoadVPNOnFly(Context context, XC_LoadPackage.LoadPackageParam loadPackageParam) throws Exception {
        String apkPath = findAPKPath(context, "com.jujing.telehook_2");
        if (!new File(apkPath).exists()) {
//            LogTool.d("Cannot load module on fly: APK not found");
            return;
        }

        PathClassLoader pathClassLoader = new PathClassLoader(apkPath, ClassLoader.getSystemClassLoader());
        Class<?> clazz = Class.forName("com.jujing.telehook_2.HookMain", true, pathClassLoader);
        Method method = clazz.getDeclaredMethod("handleLoadVPN", loadPackageParam.getClass(), Context.class);
        method.setAccessible(true);
        method.invoke(clazz.newInstance(), loadPackageParam, context);
    }


    private String findAPKPath(Context context, String packageName) throws PackageManager.NameNotFoundException {

        String publicSourceDir = context.getPackageManager().getApplicationInfo(packageName, 0).publicSourceDir;

        return publicSourceDir;

    }

    private void handleLoadApp(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        XposedHelpers.findAndHookMethod("android.app.Application", loadPackageParam.classLoader, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                XposedHelpers.findAndHookMethod("com.jujing.telehook_2.MainActivity", loadPackageParam.classLoader, "isModuleLoaded", new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                        return true;
                    }
                });
                XposedHelpers.findAndHookMethod("com.jujing.telehook_2.LoginActivity", loadPackageParam.classLoader, "isModuleLoaded", new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                        return true;
                    }
                });

            }
        });
    }
}
