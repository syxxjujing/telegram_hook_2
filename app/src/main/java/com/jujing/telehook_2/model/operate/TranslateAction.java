package com.jujing.telehook_2.model.operate;

import android.os.SystemClock;
import android.text.TextUtils;

import com.jujing.telehook_2.Global;
import com.jujing.telehook_2.HttpApi;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.WriteFileUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TranslateAction {


    private static final String TAG = "TranslateAction";

    //    public static String post0(final String talker_id,String content,boolean isZh){
//        final String[] result2 = {""};
//        try {
//            final JSONObject jsonObject = new JSONObject();
//            JSONArray jsonArray = new JSONArray();
//            jsonArray.put(content);
//            jsonObject.put("text",jsonArray);
//            if (isZh){
//                jsonObject.put("targetLang","zh");
//            }else{
//                String lang = WriteFileUtil.read(Global.TRAN_LANG);
//                jsonObject.put("targetLang",lang);
//            }
//            LoggerUtil.logI(TAG + talker_id, "JSON 108: " + jsonObject.toString());
//            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//            RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject));
//            OkGo.post(HttpApi.Translate)
//                    .requestBody(body)
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onSuccess(String s, Call call, Response response) {
//                            LoggerUtil.logI(TAG + talker_id , "ttt 62: " + s+"---->"+jsonObject.toString());
//                            try {
//                                JSONObject jsonObject1 = new JSONObject(s);
//                                int code = jsonObject1.getInt("code");
//                                if (code==0){
//                                    JSONObject data = jsonObject1.getJSONObject("data");
//                                    JSONArray text = data.getJSONArray("text");
//                                    String string = text.getString(0).replace("&#39;","'");
//                                    result2[0] = string;
//                                    LoggerUtil.logI(TAG + talker_id , "string 72: " + string);
//                                }
//                            } catch (Exception e) {
//                                LoggerUtil.logI(TAG + talker_id , "eee 67: " + CrashHandler.getInstance().printCrash(e));
//                            }
//                        }
//
//                        @Override
//                        public void onError(Call call, Response response, Exception e) {
//                            super.onError(call, response, e);
//                            LoggerUtil.logI(TAG + talker_id , "eee 74: " + e);
//                            result2[0] = "-1";
//
//                        }
//                    });
//        } catch (Exception e) {
//            LoggerUtil.logI(TAG + talker_id , "eee 73: " + CrashHandler.getInstance().printCrash(e));
//        }
//        for (int i = 0; i < 20; i++) {
//            String result = result2[0];
//            LoggerUtil.logI(TAG + talker_id,"result 91---->"+result+"---->"+i);
//            if (result.equals("")){
//                SystemClock.sleep(1000);
//            }else{
//                break;
//            }
//        }
//        if (result2[0].equals("")||result2[0].equals("-1")){
////            String result = TranslateAction.query(content);
//            return content;
//        }else{
//            return result2[0];
//        }
//
//    }
    public static String post(final String talker_id, String content, String lang) {
        final String[] result2 = {""};
        try {
            final JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(content);
            jsonObject.put("text", jsonArray);
            jsonObject.put("targetLang", lang);
            LoggerUtil.logI(TAG + talker_id, "JSON 108: " + jsonObject.toString());
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject));
            OkGo.post(HttpApi.Translate)
                    .requestBody(body)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            LoggerUtil.logI(TAG + talker_id, "ttt 62: " + s + "---->" + jsonObject.toString());
                            try {
                                JSONObject jsonObject1 = new JSONObject(s);
                                int code = jsonObject1.getInt("code");
                                if (code == 0) {
                                    JSONObject data = jsonObject1.getJSONObject("data");
                                    JSONArray text = data.getJSONArray("text");
                                    String string = text.getString(0).replace("&#39;", "'");
                                    result2[0] = string;
                                    LoggerUtil.logI(TAG + talker_id, "string 72: " + string);
                                }
                            } catch (Exception e) {
                                LoggerUtil.logI(TAG + talker_id, "eee 67: " + CrashHandler.getInstance().printCrash(e));
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            LoggerUtil.logI(TAG + talker_id, "eee 74: " + e);
                            result2[0] = "-1";

                        }
                    });
        } catch (Exception e) {
            LoggerUtil.logI(TAG + talker_id, "eee 73: " + CrashHandler.getInstance().printCrash(e));
        }
        for (int i = 0; i < 20; i++) {
            String result = result2[0];
            LoggerUtil.logI(TAG + talker_id, "result 91---->" + result + "---->" + i);
            if (result.equals("")) {
                SystemClock.sleep(1000);
            } else {
                break;
            }
        }
        if (result2[0].equals("") || result2[0].equals("-1")) {
//            String result = TranslateAction.query(content);
            return content;
        } else {
            return result2[0];
        }

    }


    public static String detect(final String talker_id, String text) {
        String lang = WriteFileUtil.read(Global.LANG_JUDGE + talker_id);
        if (lang.equals("und")) {
            WriteFileUtil.write("", Global.LANG_JUDGE + talker_id);
            lang = "";
        }
        if (!TextUtils.isEmpty(lang)) {
            if (!lang.equals("error")) {
                return lang;
            }
        }
        if (text.equalsIgnoreCase("hi")||text.equalsIgnoreCase("hello")) {
            WriteFileUtil.write("error", Global.LANG_JUDGE + talker_id);
            return "error";
        }

        String c = text.toLowerCase();
        if (c.contains("okk")){
            LoggerUtil.logI(TAG + talker_id, "c 172: " + c);
            String c1 = c.replace(".", "");
            if (c1.equals("okk")){
                WriteFileUtil.write("error", Global.LANG_JUDGE + talker_id);
                return "error";
            }
        }
        if (c.contains("hai")){
            LoggerUtil.logI(TAG + talker_id, "c 180: " + c);
            String c1 = c.replace(".", "");
            if (c1.equals("hai")){
                WriteFileUtil.write("error", Global.LANG_JUDGE + talker_id);
                return "error";
            }
        }

        final String[] result2 = {""};
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("text", text);
            LoggerUtil.logI(TAG + talker_id, "JSON 94: " + jsonObject.toString());
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject));
            OkGo.post(HttpApi.Detect)
                    .requestBody(body)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            LoggerUtil.logI(TAG + talker_id, "ttt 102: " + s + "---->" + jsonObject.toString());
                            try {
                                JSONObject jsonObject1 = new JSONObject(s);
                                int code = jsonObject1.getInt("code");
                                if (code == 0) {
                                    String string = jsonObject1.getString("data");
                                    result2[0] = string;
                                    LoggerUtil.logI(TAG + talker_id, "string 109: " + string);
                                    if (string.equals("und")) {
                                        result2[0] = "-1";
                                    }


                                }
                            } catch (Exception e) {
                                LoggerUtil.logI(TAG + talker_id, "eee 112: " + CrashHandler.getInstance().printCrash(e));
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            LoggerUtil.logI(TAG + talker_id, "eee 119: " + e);
                            result2[0] = "-1";

                        }
                    });
        } catch (Exception e) {
            LoggerUtil.logI(TAG + talker_id, "eee 125: " + CrashHandler.getInstance().printCrash(e));
        }
        for (int i = 0; i < 20; i++) {
            String result = result2[0];
            LoggerUtil.logI(TAG + talker_id, "result 129---->" + result + "---->" + i);
            if (result.equals("")) {
                SystemClock.sleep(1000);
            } else {
                break;
            }
        }
        if (result2[0].equals("") || result2[0].equals("-1")) {
//            String result = TranslateAction.query(content);
            WriteFileUtil.write("error", Global.LANG_JUDGE + talker_id);
            return "error";
        } else {
            WriteFileUtil.write(result2[0], Global.LANG_JUDGE + talker_id);
            return result2[0];
        }

    }


}
