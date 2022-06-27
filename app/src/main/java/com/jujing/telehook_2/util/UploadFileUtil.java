package com.jujing.telehook_2.util;

import android.os.SystemClock;

import com.jujing.telehook_2.HttpApi;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

public class UploadFileUtil {

    private static final String TAG = "UploadFileUtil_messagea";
    public static String uploadFile(String path){
        File file = new File(path);
        LoggerUtil.logI(TAG,"file  32----->"+file.exists()+"----->"+path);
        final String[] url = {""};
        OkGo.<String>post(HttpApi.FileUpload)
                .params("file", file)
                .isMultipart(true)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            LoggerUtil.logI(TAG, "sss 40-----> " + s);
                            JSONObject jsonObject = new JSONObject(s);
                            int code = jsonObject.getInt("code");
                            if (code==0){
                                String data = jsonObject.getString("data");
                                LoggerUtil.logI(TAG, "data 45----->" + data);
                                url[0] = data;
                            }else{
                                url[0] = "-1";
                            }
                        } catch (Exception e) {
                            LoggerUtil.logI(TAG, "eee 48----->" + CrashHandler.getInstance().printCrash(e));
                            url[0] = "-1";
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        try {
                            LoggerUtil.logI(TAG, "eee 55: " + e+"----->"+response.code());
                            url[0] = "-1";
                        } catch (Exception e1) {

                        }
                    }
                });

        for (int i = 0; i < 8000; i++) {
            LoggerUtil.logI(TAG,"url  69---->"+url[0]+"---->"+i);
            if (url[0].equals("")){
                SystemClock.sleep(1000);
            }else{
                break;
            }
        }
        return url[0];

    }
//    public static String getUploadToken(String token) throws Exception {
//        final String[] qiniu_token = {""};
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("tag","lgh");
//        String json = Aes.buildReqStr(Aes.Jia_Mi(jsonObject.toString()));
//        OkGo.post(HttpApi.getUploadToken)
//                .headers("Authorization", token)
//                .upJson(json)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(String s, Call call, Response response) {
//                        try {
//                            LoggerUtil.logI(TAG, "ttt 37: " + s);
//                            JSONObject jsonObject = new JSONObject(s);
//                            String ret = jsonObject.getString("ret");
//
//                            if (ret.equals("success")) {
//                                String data = jsonObject.getString("data");
//                                String s1 = Aes.Jie_Mi(data);
//
//                                qiniu_token[0] = s1;
//
////                                WriteFileUtil.write(s1,Global.QINIU_TOKEN);
////                                LoggerUtil.logAll(TAG,"ss  45--->"+s1);
//                            }else{
//                                qiniu_token[0] = "-1";
//                            }
//                        } catch (Exception e) {
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Call call, Response response, Exception e) {
//                        super.onError(call, response, e);
//                        LoggerUtil.logI(TAG, "eee 43: " + e);
//
//
//                    }
//                });
//
//
//        for (int i = 0; i < 20; i++) {
//            LoggerUtil.logI(TAG,"qiniu_token  72---->"+qiniu_token[0]);
//            if (qiniu_token[0].equals("")){
//                SystemClock.sleep(1000);
//            }else{
//                break;
//            }
//        }
//        if (qiniu_token[0].equals("-1")){
//            return "";
//        }
//
//        return qiniu_token[0];
//    }

}
