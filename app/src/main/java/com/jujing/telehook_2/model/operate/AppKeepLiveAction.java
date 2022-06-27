package com.jujing.telehook_2.model.operate;

import android.os.SystemClock;
import android.text.TextUtils;

import com.jujing.telehook_2.Global;
import com.jujing.telehook_2.HttpApi;
import com.jujing.telehook_2.model.UsersAndChats;
import com.jujing.telehook_2.util.Aes;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.WriteFileUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class AppKeepLiveAction {
    private static final String TAG = "AppKeepLiveAction";
    private static boolean isLoop = false;

    public static void loopHttp() {
        if (isLoop) {
            return;
        }
        isLoop = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {

                        String myId = UsersAndChats.getUserInfoId();
                        LoggerUtil.logI(TAG, "myId   37---->" + myId);
                        if (!TextUtils.isEmpty(myId)) {

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("wx_id", myId);
                            String json = Aes.buildReqStr(Aes.Jia_Mi(jsonObject.toString()));
                            String token = WriteFileUtil.read(Global.TOKEN);
                            OkGo.post(HttpApi.AppKeepLive)
                                    .headers("Authorization", token)
                                    .upJson(json)
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(String s, Call call, Response response) {
                                            try {
                                                LoggerUtil.logAll(TAG, "sss 52: " + s);
                                                JSONObject jsonObject = new JSONObject(s);
                                                String ret = jsonObject.getString("ret");
                                                if (ret.equals("success")) {
                                                    String data = jsonObject.getString("data");
                                                    String s1 = Aes.Jie_Mi(data);
                                                    LoggerUtil.logI(TAG, "s1 412: " + s1);
                                                    JSONObject jsonObject1 = new JSONObject(s1);
                                                    String lang = jsonObject1.getString("lang");
                                                    LoggerUtil.logI(TAG, "lang 61: " + lang);
                                                    WriteFileUtil.write(lang, Global.TRAN_LANG);

//                                                    try {
//                                                        String whatsapp_key_word_list = jsonObject1.getString("whatsapp_key_word_list");
//                                                        LoggerUtil.logI(TAG, "whatsapp_key_word_list 60: " + whatsapp_key_word_list);
//                                                        JSONArray jsonArray = new JSONArray(whatsapp_key_word_list);
//                                                        for (int i = 0; i < jsonArray.length(); i++) {
//                                                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
//                                                            String backup = jsonObject2.getString("backup");
//                                                            if (backup.equals("时间")) {
//                                                                WriteFileUtil.write(jsonObject2.toString(), Global.WHATSAPP_KEY_WORD_LIST_TIME_JSON);
//                                                            } else if (backup.equals("国家")) {
//                                                                WriteFileUtil.write(jsonObject2.toString(), Global.WHATSAPP_KEY_WORD_LIST_COUNTRY_JSON);
//                                                            } else if (backup.equals("封锁")) {
//                                                                WriteFileUtil.write(jsonObject2.toString(), Global.WHATSAPP_KEY_WORD_LIST_LOCK_JSON);
//                                                            }
//                                                        }
//                                                    } catch (Exception e) {
//
//                                                    }
//
//                                                    try {
//                                                        String first_normal_rule_msgs = jsonObject1.getString("active_first_resp_msg");
//                                                        WriteFileUtil.write(first_normal_rule_msgs, Global.ACTIVE_FIRST_RESP_MSG);
//                                                    } catch (Exception e) {
//
//                                                    }


//                                                    try {
//                                                        JSONObject add_list = jsonObject1.getJSONObject("add_list");
//                                                        String content = add_list.getString("content");
//                                                        String delay = add_list.getString("delay");
//                                                        LoggerUtil.logI(TAG, "content & delay  95---->" + content + "---->" + delay);
//                                                        WriteFileUtil.write(content, Global.ADD_FRIENDS_CONTENT);
//                                                        WriteFileUtil.write(delay, Global.ADD_FRIENDS_DELAY);
//                                                        if (TextUtils.isEmpty(content) || delay.equals("0")) {
//                                                        } else {
//                                                            Intent intent = new Intent();
//                                                            intent.setAction(WAReceiver.ACTION_ADD_WS_FRIENDS);
//                                                            HookMain.context.sendBroadcast(intent);
//                                                        }
//                                                    } catch (Exception e) {
//                                                        LoggerUtil.logI(TAG, "eee 109---->" + CrashHandler.getInstance().printCrash(e));
//                                                    }


                                                }
                                            } catch (Exception e) {
                                                LoggerUtil.logI(TAG, "eee 115---->" + CrashHandler.getInstance().printCrash(e));
                                            }
                                        }

                                        @Override
                                        public void onError(Call call, Response response, Exception e) {
                                            super.onError(call, response, e);
                                            LoggerUtil.logI(TAG, "eee 79: " + e);
                                        }
                                    });
                        }
                    } catch (Exception e) {
                    }
                    SystemClock.sleep(1 * 60 * 1000);


                }

            }
        }).start();


    }
}
