package com.jujing.telehook_2.util;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;


import com.jujing.telehook_2.Global;
import com.jujing.telehook_2.hook.HookActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2018/11/29.
 */

public class LoggerUtil {
    private static final String TAG = "LoggerUtil";
    static private boolean flag = true;

    static public void logE(final String key, final String value) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        if (flag) {
            Log.i(key, value);
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            String date = "[" + df.format(new Date()) + "]:  ";
            wrieFileByBufferedWriter(date + "[" + key + "]" + value + "\n", key, getTodayDate());
        }
//            }
//        }).start();

    }

    static public Exception logD(String key, String value) {
        if (flag) {
            Log.d(key, value);
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            String date = "[" + df.format(new Date()) + "]:  ";
            return wrieFileByBufferedWriter2(date + "[" + key + "]" + value + "\n", key, getTodayDate());
        }
        return null;
    }

    static public void logI(final String key, final String value) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//        ExecutorUtil.doExecute(new Runnable() {//取消线程   因为线程太多可能导致栈溢出
//            @Override
//            public void run() {
        try {
            if (flag) {
                //            if (key.equals(FileReaderHook.TAG)){
                //                frames(value);
                //            }
                logAllI(key + "[tele_jimyo]", value);
                //                    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                //                    String date = "[" + df.format(new Date()) + "]:  ";
                //                    wrieFileByBufferedWriter(date + "[" + key + "]" + value + "\n", key, getTodayDate());
                logP(key, value);
            }
        } catch (Exception e) {

        }
//            }
//        });


//            }
//        }).start();

    }

    static public void logQ(final String key, final String value) {
//            if (key.equals(FileReaderHook.TAG)){
//                frames(value);
//            }
//        Log.i(key, value);
//        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
//        String date = "[" + df.format(new Date()) + "]:  ";
        wrieFileByBufferedWriter(value, key, getTodayDate());

    }

    public static void frames(String value) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] st = Thread.currentThread().getStackTrace();

        for (int i = 0; i < st.length; i++) {
            sb.append(st[i].toString()).append("\n");
            st[i].isNativeMethod();
        }
        LoggerUtil.logAll("hook", "franmes=================>" + value + "\n" + sb.toString());
//        LoggerUtil.logI("XposedCheckFrames", sb.toString());
//        LoggerUtil.logI("XposedCheckFrames", "77==================================>"+value);
    }

    static public void logW(final String key, final String value) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        if (flag) {
            Log.w(key, value);
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            String date = "[" + df.format(new Date()) + "]:  ";
            wrieFileByBufferedWriter(date + "[" + key + "]" + value + "\n", key, getTodayDate());
        }
//            }
//        }).start();

    }

    static public void log(String key, String value) {
        if (flag) {
            Log.i(key, value);
        }
    }

    public static void logP(String tag, String msg) {
        if (tag == null || tag.length() == 0 || msg == null || msg.length() == 0) {
            return;
        }
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String date = "[" + df.format(new Date()) + "]:  ";
//        wrieFileByBufferedWriter(date + "[" + key + "]" + value + "\n", key, getTodayDate());


        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize) {//长度小于等于限制直接打印
            logQ(tag, date + "[" + tag + "]" + msg + "\n");
        } else {
            while (msg.length() > segmentSize) {//循环分段打印日志
                String logContent = msg.substring(0, segmentSize);
                msg = msg.replace(logContent, "");
                logQ(tag, date + "[" + tag + "]" + logContent + "\n");
//                logQ(tag, logContent);
            }
//            logQ(tag, msg);//打印剩余日志
            logQ(tag, date + "[" + tag + "]" + msg + "\n");
        }
    }

    public static void logAllI(String tag, String msg) {
        if (tag == null || tag.length() == 0 || msg == null || msg.length() == 0) {
            return;
        }
        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize) {//长度小于等于限制直接打印
            Log.i(tag, msg);
        } else {
            while (msg.length() > segmentSize) {//循环分段打印日志
                String logContent = msg.substring(0, segmentSize);
                msg = msg.replace(logContent, "");
                Log.i(tag, logContent);
            }
            Log.i(tag, msg);//打印剩余日志
        }
    }

    public static void logAll(String tag, String msg) {
        if (tag == null || tag.length() == 0 || msg == null || msg.length() == 0) {
            return;
        }
        String tag2 = tag + "[tele_jimyo]";
        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize) {//长度小于等于限制直接打印
            Log.i(tag2, msg);
        } else {
            while (msg.length() > segmentSize) {//循环分段打印日志
                String logContent = msg.substring(0, segmentSize);
                msg = msg.replace(logContent, "");
                Log.i(tag2, logContent);
            }
            Log.i(tag, msg);//打印剩余日志
        }
    }

    public static void sendLog(String str) {
        writeLog(str, HookActivity.baseActivity);
    }

    public static void writeLog(String info, Context context) {
        try {
            LoggerUtil.logI(TAG, "打印的日志 1---->" + info);
//        LogTool.d("打印的日志!!!--->$info")
            String localLog = read100Line(Global.STORAGE_APP_LOG);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = "[" + df.format(new Date()) + "]:  ";
            String log = date + info + "\n" + localLog;
//        wrieFileByBufferedWriter(log,Global.STORAGE_APP_LOG);
            WriteFileUtil.write(log, Global.STORAGE_APP_LOG);
//        LoggerUtil.logAll(TAG,"context 101---->"+context);
            Intent intent = new Intent(Global.ACTION_APP_LOG);
            context.sendBroadcast(intent);
        } catch (Exception e) {

        }
//        HookActivity.baseActivity.sendBroadcast(intent);
//        context.sendBroadcast(new Intent().setAction(Global.ACTION_APP_LOG));
    }

    public static void sendLog2(String str) {
        writeLog2(str, HookActivity.baseActivity);
    }

    public static void writeLog2(String info, Context context) {
        try {
            LoggerUtil.logI(TAG, "打印的日志 2---->" + info);
//        LogTool.d("打印的日志!!!--->$info")
            String localLog = read100Line(Global.STORAGE_APP_LOG_2);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = "[" + df.format(new Date()) + "]:  ";
            String log = date + info + "\n" + localLog;
//        wrieFileByBufferedWriter(log,Global.STORAGE_APP_LOG);
            WriteFileUtil.write(log, Global.STORAGE_APP_LOG_2);
//        LoggerUtil.logAll(TAG,"context 101---->"+context);
            Intent intent = new Intent(Global.ACTION_APP_LOG_2);
            context.sendBroadcast(intent);
        } catch (Exception e) {

        }
    }

    public static void sendLog3(String str) {
        writeLog3(str, HookActivity.baseActivity);
    }

    public static void writeLog3(String info, Context context) {
        try {
            LoggerUtil.logI(TAG, "打印的日志 3---->" + info);
//        LogTool.d("打印的日志!!!--->$info")
            String localLog = read100Line(Global.STORAGE_APP_LOG_3);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = "[" + df.format(new Date()) + "]:  ";
            String log = date + info + "\n" + localLog;
//        wrieFileByBufferedWriter(log,Global.STORAGE_APP_LOG);
            WriteFileUtil.write(log, Global.STORAGE_APP_LOG_3);
//        LoggerUtil.logAll(TAG,"context 101---->"+context);
            Intent intent = new Intent(Global.ACTION_APP_LOG_3);
            context.sendBroadcast(intent);
        } catch (Exception e) {

        }
    }

    public static void sendLog4(String str) {
        writeLog4(str, HookActivity.baseActivity);
    }

    public static void writeLog4(String info, Context context) {
        try {
            LoggerUtil.logI(TAG, "打印的日志 4---->" + info);
//        LogTool.d("打印的日志!!!--->$info")
            String localLog = read100Line(Global.STORAGE_APP_LOG_4);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = "[" + df.format(new Date()) + "]:  ";
            String log = date + info + "\n" + localLog;
//        wrieFileByBufferedWriter(log,Global.STORAGE_APP_LOG);
            WriteFileUtil.write(log, Global.STORAGE_APP_LOG_4);
//        LoggerUtil.logAll(TAG,"context 101---->"+context);
            Intent intent = new Intent(Global.ACTION_APP_LOG_4);
            context.sendBroadcast(intent);
        } catch (Exception e) {

        }
    }

    public static void sendLog5(String str) {
        writeLog5(str, HookActivity.baseActivity);
    }

    public static void writeLog5(String info, Context context) {
        try {
            LoggerUtil.logI(TAG, "打印的日志 5---->" + info);
//        LogTool.d("打印的日志!!!--->$info")
            String localLog = read100Line(Global.STORAGE_APP_LOG_5);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = "[" + df.format(new Date()) + "]:  ";
            String log = date + info + "\n" + localLog;
//        wrieFileByBufferedWriter(log,Global.STORAGE_APP_LOG);
            WriteFileUtil.write(log, Global.STORAGE_APP_LOG_5);
//        LoggerUtil.logAll(TAG,"context 101---->"+context);
            Intent intent = new Intent(Global.ACTION_APP_LOG_5);
            context.sendBroadcast(intent);
        } catch (Exception e) {
        }
    }

    //使用BufferReader读取文件
    public static String read100Line(String path) {
        try {

            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String readline = "";
//            StringBuffer sb = new StringBuffer();
            int line = 0;
            String result = "";
            while ((readline = br.readLine()) != null) {
                if (line == 100) {
//                    LogTool.e("超过100个了!!");
                    break;
                }

                result = result + readline + "\n";
                line++;
            }
            br.close();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Exception wrieFileByBufferedWriter(String info, String tagName, String fileName) {
        try {
//            File file;
//            if (Global.IS_DEBUG) {
//            file = Environment.getExternalStoragePublicDirectory("superhook" + File.separator + "log" + File.separator + tagName);
//            } else {
//                file = Environment.getExternalStoragePublicDirectory("superhook" + File.separator + "log" + File.separator + "AllMessage");
//            }

            File file = new File("/sdcard/superhook_tele_2" + File.separator + "log" + File.separator + tagName);
//
            if ((!file.exists()) || (!file.isDirectory())) {
                file.mkdirs();
            }
            String absolutePath = file.getAbsolutePath();

            File fullFile = new File(absolutePath + File.separator + fileName + ".txt");
            fullFile.getParentFile().mkdirs();
            //第二个参数意义是说是否以append方式添加内容
            BufferedWriter bw = new BufferedWriter(new FileWriter(fullFile, true));
            bw.write(info);
            bw.flush();
            bw.close();
//            Log.d(TAG, "写入成功");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
//            Log.d(TAG, "e:" + e);
            return e;
        }
    }

    public static Exception wrieFileByBufferedWriter2(String info, String tagName, String fileName) {
        try {
//            File file = new File("storage/emulated/0/Download/auto/" + fileName + ".txt");
//            File file = new File("storage/emulated/0/Download/auto_log/"+dirName);
//            if (!file.exists()){
//                file.mkdirs();
//            }

            File file;
//            if (Global.IS_DEBUG) {
            file = Environment.getExternalStoragePublicDirectory("superhook_wa_1" + File.separator + "log" + File.separator + tagName);
//            } else {
//                file = Environment.getExternalStoragePublicDirectory("superhook" + File.separator + "log" + File.separator + "AllMessage");
//            }
            if ((!file.exists()) || (!file.isDirectory())) {
                file.mkdirs();
            }
            String absolutePath = file.getAbsolutePath();
//            File[] allFiles = file.listFiles();//
//            for (File logFile : allFiles) {
//                String name = logFile.getName();
//                String todayDate = getTodayDate() + ".txt";
//                String yestoryDate = getYesterdayDate() + ".txt";
//                if (name.equals(todayDate) || name.equals(yestoryDate)) {
//                } else {
//
//                    boolean delete = logFile.delete();
//                    LoggerUtil.logI(TAG, "删除日志了 128--->" + delete + "----->" + logFile);
//
//                }
//            }
            File fullFile = new File(absolutePath + File.separator + fileName + ".txt");
            fullFile.getParentFile().mkdirs();
            //第二个参数意义是说是否以append方式添加内容
            BufferedWriter bw = new BufferedWriter(new FileWriter(fullFile, true));
            bw.write(info);
//            bw.flush();
            bw.close();
//            Log.d(TAG, "写入成功");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
//            Log.d(TAG, "e:" + e);
            return e;
        }
    }

    public static void deleteBef() {

    }

    /**
     * 得到今天的日期
     *
     * @return
     */
    public static String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        return date;
    }

    public static String getTodayDate2() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        String date = sdf.format(new Date());
        return date;
    }

    public static String getYesterdayDate() {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(instance.getTime());
        return date;
    }

    public static String formatDate(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(timeStamp);
        return date;
    }


    public static void deleteBefore(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] allFiles = file.listFiles();
            for (File logFile : allFiles) {
                if (logFile.isDirectory()) {
                    File[] files = logFile.listFiles();
                    for (File txtFile : files) {
//                        if (txtFile.toString().contains("HookMain")){
//                            long l = txtFile.lastModified();
//                            logAll(TAG,"llll 303---->"+l+"---->"+txtFile);
//                        }
                        long l = txtFile.lastModified();
                        if (System.currentTimeMillis() - l > 1000 * 60 * 60 * 24 * 2) {
                            LoggerUtil.logI(TAG, "删除日志了 --->" + txtFile);
                            txtFile.delete();
                        }

//                        String fileName = txtFile.getName();
//                        String todayDate = getTodayDate() + ".txt";
//                        String yestoryDate = getYesterdayDate() + ".txt";
//                        if (fileName.equals(todayDate) || fileName.equals(yestoryDate)) {
//
//                        } else {
//                            LoggerUtil.logI(TAG, "删除日志了 --->" + txtFile);
//                            txtFile.delete();
//
//                        }
                    }


                }


            }
        }
    }


}
