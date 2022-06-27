package com.jujing.telehook_2;

import android.app.Application;
import android.content.Context;

import com.jujing.telehook_2.util.AbstractCrashReportHandler;


public class MyApp extends Application {

//    public static Context thisApp;
//    public final static String Sp_NoReplyKey = "use-no-reply";
//    private static SharedPreferences thisShare;

    public static Context context;
//    public static SpeechRecognizer mIat;

    /**
     * 初始化监听器。
     */
//    private InitListener mInitListener = new InitListener() {
//
//        @Override
//        public void onInit(int code) {
//            LogTool.d("SpeechRecognizer init() code = " + code);
//            if (code != ErrorCode.SUCCESS) {
////                showTip("初始化失败，错误码：" + code);
//                LogTool.d("初始化失败 初始化失败");
//            }else{
//                LogTool.d("初始化失败 初始化成功");
//            }
//        }
//    };

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
        new AbstractCrashReportHandler();

    }

//    public static SharedPreferences getShare(){
//        if(thisShare == null){
//            thisShare = MyApp.thisApp.getSharedPreferences("checkbox-date", Context.MODE_PRIVATE);
//        }
//        return thisShare;
//    }

}
