package com.jujing.telehook_2.util;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogTool {

    private static final String TAG = "my_log";

    public static void ei(String info){
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String date = "[" + df.format(new Date()) + "]:  ";
        wrieFileByBufferedWriter(date + info + "\n", "operate_ei/", getTodayDate());
        Log.e(TAG+"_ei", info);
    }


    public static void user(String info) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String date = "[" + df.format(new Date()) + "]:  ";
        wrieFileByBufferedWriter(date + info + "\n", "operate_user/", getTodayDate());
        Log.e(TAG+"_user", info);
    }
    public static void cr(String info) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String date = "[" + df.format(new Date()) + "]:  ";
        wrieFileByBufferedWriter(date + info + "\n", "operate_chatroom/", getTodayDate());
        Log.e(TAG+"_cr", info);
    }

    public static void h(String info) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String date = "[" + df.format(new Date()) + "]:  ";
        wrieFileByBufferedWriter(date + info + "\n", "operate_hook/", getTodayDate());
        Log.e(TAG+"_cr", info);
    }

    public static void location(String info) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String date = "[" + df.format(new Date()) + "]:  ";
        wrieFileByBufferedWriter(date + info + "\n", "operate_location/", getTodayDate());
        Log.e(TAG+"_cr", info);
    }



    public static void c(String info) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String date = "[" + df.format(new Date()) + "]:  ";
        wrieFileByBufferedWriter(date + info + "\n", "operate_cache/", getTodayDate());
        Log.e(TAG+"_c", info);
    }

    public static void s(String info) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String date = "[" + df.format(new Date()) + "]:  ";
        wrieFileByBufferedWriter(date + info + "\n", "operate_step/", getTodayDate());
        Log.e(TAG+"_d", info);
    }


    public static void f(String info) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String date = "[" + df.format(new Date()) + "]:  ";
        wrieFileByBufferedWriter(date + info + "\n", "operate_forbid/", getTodayDate());
        Log.e(TAG+"_d", info);
    }

    public static void d(String info) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String date = "[" + df.format(new Date()) + "]:  ";
        wrieFileByBufferedWriter(date + info + "\n", "operate_developer/", getTodayDate());
        all(TAG+"_d", info);
    }

    public static void traceAty(String info) {
//        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
//        String date = "[" + df.format(new Date()) + "]:  ";
//        wrieFileByBufferedWriter(date + info + "\n", "operate_traceAty/", getTodayDate());
//        Log.e(TAG+"_traceAty", info);
    }

    public static void sns(String info) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String date = "[" + df.format(new Date()) + "]:  ";
        wrieFileByBufferedWriter(date + info + "\n", "operate_sns/", getTodayDate());
        Log.e(TAG+"_sns", info);
    }


    public static void contact(String info) {



        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String date = "[" + df.format(new Date()) + "]:  ";
        wrieFileByBufferedWriter(date + info + "\n", "operate_contact/", getTodayDate());
        Log.e(TAG+"_contact", info);
    }

    public static void statistics(String info) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String date = "[" + df.format(new Date()) + "]:  ";
        wrieFileByBufferedWriter(date + info + "\n", "operate_statistics/", getTodayDate());
        Log.e(TAG+"_statistics", info);
    }

    public static void queue(String info) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String date = "[" + df.format(new Date()) + "]:  ";
        wrieFileByBufferedWriter(date + info + "\n", "operate_queue/", getTodayDate());
        Log.e(TAG+"_queue", info);
    }



    public static void error(String info) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String date = "[" + df.format(new Date()) + "]:  ";
        wrieFileByBufferedWriter(date + info + "\n", "operate_error/", getTodayDate());
        Log.e(TAG+"_error", info);
    }

//    public static void autoReply(String info) {
//        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
//        String date = "[" + df.format(new Date()) + "]:  ";
//        wrieFileByBufferedWriter(date + info + "\n", "operate_autoReply/", getTodayDate());
//        Log.e(TAG, info);
//    }



    public static void conversation(String info) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String date = "[" + df.format(new Date()) + "]:  ";
        wrieFileByBufferedWriter(date + info + "\n", "operate_conversation/", getTodayDate());
        Log.e(TAG+"_conversation", info);
    }


    public static void e(String info) {
//        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
//        String date = "[" + df.format(new Date()) + "]:  ";
//        wrieFileByBufferedWriter(date + info + "\n", "tntdata_pointer_log/", getTodayDate());
        all(TAG+"_e", info);
    }

    public static void ed(String info) {
//        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
//        String date = "[" + df.format(new Date()) + "]:  ";
//        wrieFileByBufferedWriter(date + info + "\n", "tntdata_pointer_log/", getTodayDate());
        Log.e("wxdb_", info);
    }

    public static void all(String tag, String msg) {
        if (tag == null || tag.length() == 0
                || msg == null || msg.length() == 0)
            return;

        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize ) {// 长度小于等于限制直接打印
            Log.e(tag, msg);
        }else {
            while (msg.length() > segmentSize ) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize );
                msg = msg.replace(logContent, "");
                Log.e(tag, logContent);
            }
            Log.e(tag, msg);// 打印剩余日志
        }
    }







    public static void wrieFileByBufferedWriter(String info, String dirName, String fileName) {
        try {
//            File file = new File("storage/emulated/0/Download/auto/" + fileName + ".txt");
//            File file = new File("storage/emulated/0/Download/auto_log/"+dirName);
//            if (!file.exists()){
//                file.mkdirs();
//            }


            File fullFile = new File("storage/emulated/0/Download/auto_log_api/" +dirName+ fileName + ".txt");
            fullFile.getParentFile().mkdirs();
            //第二个参数意义是说是否以append方式添加内容
            BufferedWriter bw = new BufferedWriter(new FileWriter(fullFile, true));
            bw.write(info);
            bw.flush();
//            Log.d(TAG, "写入成功");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "e:" + e);
        }
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


}