package com.jujing.telehook_2.hook;

import android.widget.Toast;

import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.util.HookUtil;
import com.jujing.telehook_2.util.LoggerUtil;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class HookDialog {

    private static final String TAG = "HookDialog";

    public static void ban() {
        Class<?> aClass = XposedHelpers.findClass("org.telegram.ui.ActionBar.AlertDialog.Builder", HookMain.classLoader);
        XposedBridge.hookAllMethods(aClass, "show", new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
//                CharSequence arg = (CharSequence) param.args[1];
//                String s = arg.toString();
//                if (s.contains("现在可以开始聊天了")) {
//                    return null;
//                } else {
//                    return XposedBridge.invokeOriginalMethod(param.method, param.thisObject, param.args);
//                }
//                HookUtil.frames();
                Object alertDialog = XposedHelpers.getObjectField(param.thisObject, "alertDialog");
//                HookUtil.printAllField(alertDialog);
                Object message = XposedHelpers.getObjectField(alertDialog, "message");
                Object title = XposedHelpers.getObjectField(alertDialog, "title");
                LoggerUtil.logI(TAG,"message  31 --->"+message+"---->"+title);

                HookActivity.showToast("已禁止弹窗:"+title+"\n"+message);

                return null;

            }
        });

//        Class<?> VoIPFragment = XposedHelpers.findClass("org.telegram.ui.VoIPFragment", HookMain.classLoader);
//        XposedBridge.hookAllMethods(VoIPFragment, "showErrorDialog", new XC_MethodReplacement() {
//            @Override
//            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
//                return null;
//            }
//        });

    }
}
