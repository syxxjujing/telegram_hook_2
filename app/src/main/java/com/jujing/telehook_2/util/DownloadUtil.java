package com.jujing.telehook_2.util;

import android.os.SystemClock;
import android.text.TextUtils;

import com.jujing.telehook_2.Global;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;

public class DownloadUtil {


    private static final String TAG = "DownloadUtil_messagea_TranSnsLocalTimer";
    public static String judgeUrl = "";

    public static void downloadFileByOkGo(final String url, String destFileDir, final String destFileName, final int type){
        LoggerUtil.logI(TAG, "downloadFile  25--->" + url + "----" + destFileName);
        String urlNew = url;
        if (urlNew.contains(" ")){
            urlNew = url.replaceAll(" ","%20");
        }
        LoggerUtil.logI(TAG, "downloadFile  29--->" + urlNew + "----" + destFileName);
        final long[] finalTotal = {0};
        final boolean[] isDownload = {false};
        OkGo.<File>get(url)
                .execute(new FileCallback(destFileDir, destFileName) {
                    @Override
                    public void onSuccess(File file, Call call, okhttp3.Response response2) {
                        LoggerUtil.logI(TAG, "file  42---->" + file + "----->" + file.exists() + "---->" + response2+"---->"+url);
                        try {
                            LoggerUtil.logI(TAG, " ========44================" + file);

                            long len = FileUtils.getFileLength(file);
                            LoggerUtil.logI(TAG, " ========len  46================" + len+"----->"+finalTotal[0]);
                            if (len!=finalTotal[0]){

                                LoggerUtil.logI(TAG,"文件下载不完整 50========");
                                boolean delete = file.delete();
                                LoggerUtil.logI(TAG,"delete  52========"+delete);
                            }else{
                                isDownload[0] = true;
                                if (type==1){//登录的时候
                                    judgeUrl = url;
                                }
                            }
                        } catch (Exception e) {
                            LoggerUtil.logI(TAG,"eee  59--->"+CrashHandler.getInstance().printCrash(e));
                        }
                    }

                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                        LoggerUtil.logI(TAG, "downloadProgress  66---->" + progress + "----->" + totalSize + "---->" + networkSpeed+"---->"+url);
                        finalTotal[0] = totalSize;
                    }

                    @Override
                    public void onError(Call call, okhttp3.Response response, Exception e) {
                        super.onError(call, response, e);
                        LoggerUtil.logI(TAG, "onError  72 ---->" + response + "----->" + e+"----->"+url);
                        isDownload[0] = true;

                        try {
                            String path = Global.STORAGE_FILE + destFileName;
                            new File(path).delete();
                        } catch (Exception e1) {

                        }

                    }
                });
        for (int i = 0; i < 500; i++) {
            LoggerUtil.logI(TAG,"iiii  81---->"+i+"---->"+isDownload[0]+"---->"+url);
            if (isDownload[0]){
                break;
            }else{
                SystemClock.sleep(1000);
            }
        }
    }

//    你登录 168002 ，密码 123456
//    public static void downloadFile(final String url, String destFileDir, String destFileName, final int type) {
//        LoggerUtil.logI(TAG, "downloadFile  25--->" + url + "----" + destFileName);
//        String urlNew = url;
//        if (urlNew.contains(" ")){
//            urlNew = url.replaceAll(" ","%20");
//        }
//        LoggerUtil.logI(TAG, "downloadFile  29--->" + urlNew + "----" + destFileName);
//        final long[] finalTotal = {0};
//        UrlHttpUtil.downloadFile(urlNew, new CallBackUtil.CallBackFile(destFileDir, destFileName) {
//            @Override
//            public void onFailure(int code, String errorMessage) {
//                LoggerUtil.logI(TAG, "onFailure  31--->" + code + "----" + errorMessage+"---->"+url);
//            }
//
//            @Override
//            public void onProgress(float progress, long total) {
//                //super.onProgress(progress, total);
//                LoggerUtil.logI(TAG, "progress  37--->" + progress + "----" + total+"---->"+url);//TODO 以这个total来判断!
//                finalTotal[0] = total;
//            }
//
//            @Override
//            public void onResponse(File response) {
//                try {
//                    LoggerUtil.logI(TAG, " ========37================" + response+"----->"+url);
//
//                    long len = FileUtils.getFileLength(response);
//                    LoggerUtil.logI(TAG, " ========len  48================" + len+"----->"+finalTotal[0]);
//                    if (len!=finalTotal[0]){
//
//                        LoggerUtil.logI(TAG,"文件下载不完整53========");
//                        boolean delete = response.delete();
//                        LoggerUtil.logI(TAG,"delete  55========"+delete);
//                    }else{
//                        if (type==1){//登录的时候
//                            judgeUrl = url;
//                        }
//                    }
//                } catch (Exception e) {
//                    LoggerUtil.logI(TAG,"eee  53--->"+CrashHandler.getInstance().printCrash(e));
//                }
//            }
//        });
//
//
////        try {
////            HttpDownloader httpDownloader=new HttpDownloader();
////            int i = httpDownloader.downFile(url, path);
////            LoggerUtil.logI(TAG,"downloadFile  22---->"+i);
////        } catch (Exception e) {
////            LoggerUtil.logI(TAG,"downloadFile  25---->"+CrashHandler.getInstance().printCrash(e));
////
////        }
//
//
////        try {
////            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
////                    .readTimeout(30, TimeUnit.SECONDS)
////                    .writeTimeout(30, TimeUnit.SECONDS).build();
////            Request request = new Request.Builder()
////                    .url(url).build();
////            Response response = client.newCall(request).execute();
////            byte[] bytes = response.body().bytes();
////            writeBytesToDisk(path, bytes);
////            File file = new File(path);
////            if (!file.exists()) {
////                LoggerUtil.logI(TAG, "downloadFile faild  28-->" + count);
////                if (count >= 4) {
////                    return;
////                }
////
////                downloadFile(url, path, count + 1);
////
////            }
////
////        } catch (Exception e) {
////            LoggerUtil.logI(TAG, "eee 24--->" + CrashHandler.getInstance().printCrash(e));
////            File file = new File(path);
////            if (!file.exists()) {
////                LoggerUtil.logI(TAG, "downloadFile faild  41-->" + count);
////                if (count >= 4) {
////                    return;
////                }
////
////                downloadFile(url, path, count + 1);
////
////            }
////        }
//    }

//    public static void downloadFile(String netUrl, String path, int count) {
//        try {
//            URL url = new URL(netUrl);
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setReadTimeout(50000);
//            con.setConnectTimeout(50000);
//            con.setRequestProperty("Charset", "UTF-8");
//            con.setRequestMethod("GET");
//            if (con.getResponseCode() == 200) {
//                InputStream is = con.getInputStream();//获取输入流
//                FileOutputStream fileOutputStream = null;//文件输出流
//                if (is != null) {
////                    FileUtils fileUtils = new FileUtils();
//                    File file = new File(path);
//                    fileOutputStream = new FileOutputStream(file);//指定文件保存路径，代码看下一步
//
//                    byte[] buf = new byte[1024];
//                    int ch;
//                    while ((ch = is.read(buf)) != -1) {
//                        fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
//                    }
//                }
//                if (fileOutputStream != null) {
//                    fileOutputStream.flush();
//                    fileOutputStream.close();
//                }
//            }
//        } catch (Exception e) {
//
//        }
//
//}


    public static void writeBytesToDisk(String path, byte[] bytes) {
        File file = new File(path);
        file.getParentFile().mkdirs();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(bytes);
        } catch (Exception e) {

        } finally {
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // readBytesFromDisk returns all the bytes of a binary file.
    public static byte[]  readBytesFromDisk( String path) {
        byte[] thumb = null;
        if (!TextUtils.isEmpty(path)) {
            File icon = new File(path);
            if (icon.exists()) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(icon);
                    thumb = new byte[fis.available()];
                    fis.read(thumb);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return thumb;
    }



    /**
     * 得到文件流
     * @param url
     * @return
     */
    public static byte[] getFileStream(String url){
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(60 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从输入流中获取数据
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

}
