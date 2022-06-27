package com.jujing.telehook_2.util;/**
 * Created by tarena on 2016/12/5.
 */

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * created by Vampire
 * on: 2016/12/5 下午2:10
 */
public class WriteFileUtil {
    private static final String TAG = "WriteFileUtil-vampire";

    //判断SD卡是否存在
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


    //使用FileInputStream读取文件
    public static String read(String path) {
        try {
            File file = new File(path);
            FileInputStream is = new FileInputStream(file);
            byte[] b = new byte[is.available()];
            is.read(b);
            String result = new String(b);
            return result;
//            System.out.println("读取成功：" + result);
        } catch (Exception e) {
//            e.printStackTrace();
//            LoggerUtil.logI(TAG, "ee  45---->" + CrashHandler.getInstance().printCrash(e));
            return "";
        }
    }

    public static String read2(String path) {
        File file = new File(path);//riskcontrol.json
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LoggerUtil.logW("weixinlogHook", "创建login.json文件错误:" + e.toString());
            }
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[fileInputStream.available()];
            fileInputStream.read(buffer);

            return new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    public static void write(String info, String fileName) {
        try {
            File fullFile = new File(fileName);
            fullFile.getParentFile().mkdirs();

            //第二个参数意义是说是否以append方式添加内容
            BufferedWriter bw = new BufferedWriter(new FileWriter(fullFile, false));
            bw.write(info);
            bw.flush();
//            Log.d(TAG, "写入成功");
        } catch (Exception e) {
//            e.printStackTrace();
            Log.d(TAG, "e:" + e);
        }
    }

    public static void writeAppend(String info, String fileName) {
        try {
            File fullFile = new File(fileName);
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


    /**读取文件
     * @param strFilePath
     */
    public static List<String> readFile(String strFilePath){
        List<String> txtList = new ArrayList<>();
        File file = new File(strFilePath);
        if (file.isDirectory()){
            Log.d(TAG, "The File doesn't not exist.");
        }else{
            try{
                InputStream instream = new FileInputStream(file);
                if (instream != null){
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //逐行读取
                    while (( line = buffreader.readLine()) != null){
//                        LoggerUtil.logAll("messagea_", "line 82--->" + line);
                        txtList.add(line);
                    }
                    instream.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return txtList;
    }

}
