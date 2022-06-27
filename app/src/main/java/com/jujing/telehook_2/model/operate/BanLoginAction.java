package com.jujing.telehook_2.model.operate;

import com.jujing.telehook_2.Global;
import com.jujing.telehook_2.HttpApi;
import com.jujing.telehook_2.util.Aes;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.ExecutorUtil;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.WriteFileUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

import static com.jujing.telehook_2.util.Aes.buildReqStr;


public class BanLoginAction {

    private static final String TAG = "BanLoginAction";
//    http://ec2-54-169-240-122.ap-southeast-1.compute.amazonaws.com/admin/
//    lgh ， 123aaa  是商户 登录进去，然后设置wa_jujing的号 通讯录设置 自动接受好友请求
    public static void handle(){//
        String json = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("password", "19920211j");
            jsonObject.put("account", "tg_jujing");
            jsonObject.put("wx_id", "");
            jsonObject.put("wx_name", "");
            LoggerUtil.logI(TAG, "JSON 46: " + jsonObject.toString());
            json = buildReqStr(Aes.Jia_Mi(jsonObject.toString()));

            OkGo.post(HttpApi.BAN_LOGIN)
                    .upJson(json)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(final String s, Call call, Response response) {
                            LoggerUtil.logI(TAG, "ttt: " + s);
                            ExecutorUtil.doExecute(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        String ret = jsonObject.getString("ret");
                                        if (ret.equals("success")) {
                                            String data = jsonObject.getString("data");
                                            String s1 = Aes.Jie_Mi(data);
                                            LoggerUtil.logI(TAG, "s1 61: " + s1);
                                            JSONObject jsonObject1 = new JSONObject(s1);
                                            JSONObject data1 = jsonObject1.getJSONObject("data");
                                            boolean need_verify_accept = data1.getBoolean("need_verify_accept");
                                            LoggerUtil.logI(TAG, "need_verify_accept 64: " + need_verify_accept);
                                            WriteFileUtil.write(need_verify_accept+"",Global.BAN_LOGIN);
                                        } else if (ret.equals("failed")) {

                                        }


                                    } catch (Exception e) {
                                        LoggerUtil.logAll(TAG, "eee 78: " + CrashHandler.getInstance().printCrash(e));
                                    }
                                }
                            });

                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            LoggerUtil.logAll(TAG, "eee 85: " + e);

                        }
                    });


        } catch (Exception e) {
            LoggerUtil.logI(TAG,"eee  23---->"+CrashHandler.getInstance().printCrash(e));
        }
    }
}
