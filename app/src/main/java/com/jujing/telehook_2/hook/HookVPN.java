package com.jujing.telehook_2.hook;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.MyApp;
import com.jujing.telehook_2.util.HookUtil;
import com.jujing.telehook_2.util.LoggerUtil;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class HookVPN {
    private static final String TAG = "HookVPN";

    public static void hook(){
        LoggerUtil.logI(TAG,"HookVPN  19---->");
        XposedHelpers.findAndHookMethod("org.webrtc.NetworkMonitorAutoDetect", HookMain.classLoader, "getConnectionType"
                , boolean.class, int.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                String s = HookUtil.printParams(param);
                LoggerUtil.logI(TAG,"sss  23---->"+s);
                int networkType = (int) param.args[1];
                if (networkType==17){
                    param.args[1] = 1;
                    param.args[2] = 0;
                }


            }
        });
        LoggerUtil.logI(TAG,"HookVPN end 36---->");
    }

    public  static boolean checkVPN() {
        ConnectivityManager  connMgr = (ConnectivityManager) MyApp.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //don't know why always returns null:
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_VPN);

        boolean isVpnConn = networkInfo == null ? false : networkInfo.isConnected();
        return isVpnConn;
    }

}
