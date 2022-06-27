package com.jujing.telehook_2.util;


import com.wanjian.cockroach.Cockroach;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    public static CrashHandler instance = null;

    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    public static  void handle(){
        Cockroach.install(new Cockroach.ExceptionHandler() {

            // handlerException内部建议手动try{  你的异常处理逻辑  }catch(Throwable e){ } ，以防handlerException内部再次抛出异常，导致循环调用handlerException

            @Override
            public void handlerException(final Thread thread, final Throwable throwable) {
                //开发时使用Cockroach可能不容易发现bug，所以建议开发阶段在handlerException中用Toast谈个提示框，
                //由于handlerException可能运行在非ui线程中，Toast又需要在主线程，所以new了一个new Handler(Looper.getMainLooper())，
                //所以千万不要在下面的run方法中执行耗时操作，因为run已经运行在了ui线程中。
                //new Handler(Looper.getMainLooper())只是为了能弹出个toast，并无其他用途
                try {
                    LoggerUtil.logI("CrashHandler_Cockroach", "Exception Happend 32 \n---->"+thread+"---\n"+ CrashHandler.getInstance().printCrash(throwable));
//                    LoggerUtil.logI(TAG, "Exception Happend 32 \n---->"+thread+"---\n"+throwable);
                } catch (Exception e) {

                }
//                    new Handler(Looper.getMainLooper()).post(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                //建议使用下面方式在控制台打印异常，这样就可以在Error级别看到红色log
//                                Log.e("AndroidRuntime","--->CockroachException:"+thread+"<---",throwable);
//                                Toast.makeText(App.this, "Exception Happend\n" + thread + "\n" + throwable.toString(), Toast.LENGTH_SHORT).show();
////                        throw new RuntimeException("..."+(i++));
//                            } catch (Throwable e) {
//
//                            }
//                        }
//                    });

            }
        });
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        StringBuffer sb = printCrash(ex);

        LoggerUtil.logI(TAG, "crash--->"+sb.toString());
//        LogUtil.show(TAG, "uncaughtException222_log_: " + sb.toString());
//        String txt = TConfigure.APP_DIR + "/" + code + ".txt";
//        LogTool.error(sb.toString());

//        FileUtil.copyFile(cacheFile.getAbsolutePath(), newPath);


//        BootUpReceiver.rebootTask(HookMain.context);
    }

    public StringBuffer printCrash(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.flush();
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        return sb;

    }

}
