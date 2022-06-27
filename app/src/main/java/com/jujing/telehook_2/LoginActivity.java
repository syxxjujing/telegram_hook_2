package com.jujing.telehook_2;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jujing.telehook_2.model.operate.BanLoginAction;
import com.jujing.telehook_2.util.Aes;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.DownloadUtil;
import com.jujing.telehook_2.util.ExecutorUtil;
import com.jujing.telehook_2.util.JumpUtil;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.MatchUtil;
import com.jujing.telehook_2.util.WriteFileUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    Dialog mDialog = null;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSIONS = 1;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 2;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readInfo();
            } else {
                // Permission Denied
                Toast.makeText(LoginActivity.this, "不要拒绝存储权限！", Toast.LENGTH_SHORT).show();
                finish();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    EditText et_username;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final EditText et_pw = (EditText) findViewById(R.id.et_pw);
         et_username = (EditText) findViewById(R.id.et_username);
        Button btn_login = (Button) findViewById(R.id.btn_login);
        //动态请求权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSIONS);
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
            }
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        3);
            }
        }
        readInfo();
        LoggerUtil.logAll(TAG, "isMod 110---->" + isModuleLoaded());
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = et_username.getText().toString().trim();
                String psw = et_pw.getText().toString().trim();
                if (psw.equals("log")){
                    startActivity(new Intent(LoginActivity.this, ShowLogActivity.class));
                }
                BanLoginAction.handle();

                if (Global.IS_DEBUG_LOGIN) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("account", "22");
                    startActivity(intent);
                    finish();
                }

                if (!isModuleLoaded()) {
                    Toast.makeText(LoginActivity.this, "Xposed模块未启动，请检测Xposed!", Toast.LENGTH_SHORT).show();
//                    return;
                }

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(LoginActivity.this, "请输入账号!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(LoginActivity.this, "请输入密码!", Toast.LENGTH_SHORT).show();
                    return;
                }
                WriteFileUtil.write(username, Global.ACCOUNT);

                String user_info_id = WriteFileUtil.read(Global.USER_INFO_ID);
                LoggerUtil.logI(TAG, "user_info_id  133--->" + user_info_id);
                if (TextUtils.isEmpty(user_info_id)) {
                    Toast.makeText(LoginActivity.this, "tg信息未获取到!!请检查模块,然后打开tg,再登录!", Toast.LENGTH_SHORT).show();
//                    return;
                }
                String push_name = WriteFileUtil.read(Global.USER_INFO_NICKNAME);
                Exception eLog = LoggerUtil.logD(TAG, "push_name 143--->" + push_name);
                if (eLog != null) {
                    Toast.makeText(LoginActivity.this, "写入日志异常！" + eLog.toString(), Toast.LENGTH_LONG).show();
                    return;
                }

                showProgress();
                String json = "";
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("password", psw);//123aaa   123456
                    jsonObject.put("account", username);//admin  168002
                    jsonObject.put("wx_id", user_info_id);
                    jsonObject.put("wx_name", push_name);
                    LoggerUtil.logI(TAG, "JSON 73: " + jsonObject.toString());
                    json = buildReqStr(Aes.Jia_Mi(jsonObject.toString()));

//                    Log.e("jujing", "json: "+json );
//                    OkGo.post("http://123.57.244.105:8888/W3Login")
                    OkGo.post(HttpApi.LOGIN)
                            .tag(this)
                            .upJson(json)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
//                                    LoggerUtil.logI(TAG, "ttt: " + s);
                                    hideProgress();
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        String ret = jsonObject.getString("ret");
                                        if (ret.equals("success")) {

                                            String data = jsonObject.getString("data");
                                            String s1 = Aes.Jie_Mi(data);
//                                            LoggerUtil.logI(TAG, "s1 144: " + s1);
                                            WriteFileUtil.write(s1, Global.LOGIN_JSON);

                                            final JSONObject jsonObject1 = new JSONObject(s1);
                                            String token = jsonObject1.getString("token");
                                            LoggerUtil.logI(TAG, "token 170: " + token);
                                            WriteFileUtil.write(token, Global.TOKEN);

                                            JSONObject dataObj = jsonObject1.getJSONObject("data");
                                            String user_id = dataObj.getString("user_id");
                                            LoggerUtil.logI(TAG, "user_id  119: " + user_id);
                                            WriteFileUtil.write(user_id, Global.USER_ID);
//                                            String level = dataObj.getString("level");
//                                            WriteFileUtil.write(level, Global.LEVEL);
                                            Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_LONG).show();
//                                            JSONArray url = array.getJSONArray("url");
                                            ExecutorUtil.doExecute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        JSONArray array = jsonObject1.getJSONArray("files");

                                                        JumpUtil.jumpToProgressDialog("正在加载文件....", LoginActivity.this);

                                                        for (int i = 0; i < array.length(); i++) {
                                                            JumpUtil.sendMessageToProgressDialog("正在加载第" + (i + 1) + "个文件,共" + array.length() + "个,请稍等...", LoginActivity.this);
                                                            JSONObject jsonObject2 = new JSONObject(array.getString(i));
                                                            final String url = jsonObject2.getString("url");
                                                            if (url.endsWith("amr")) {
                                                                continue;
                                                            }
//                                                            LoggerUtil.logI(TAG, "url  224: " + url);
                                                            if (url.contains("www.51") || url.contains("xreply.51")) {
                                                                continue;
                                                            }

                                                            final String fileName = MatchUtil.splitFileName(url);
                                                            final String path = Global.STORAGE_FILE + fileName;
                                                            File file0 = new File(path);
//                                                            LoggerUtil.logI(TAG, "path  143--->" + file0.exists());
                                                            if (!file0.exists()) {//
//                                                                runOnUiThread(new Runnable() {
//                                                                    @Override
//                                                                    public void run() {
//                                                                        DownloadUtil.downloadFile(url, Global.STORAGE_FILE, fileName, 1);
////                                                                        DownloadUtil.downloadFile("http://xreply.51zhaoduiyou.com/msg_0300090830199935beb1018104.amr", Global.STORAGE_FILE,"msg_0917280910192739a7a93d6101.amr"+RandomUtil.randomNumber(0,10000),0);
//                                                                    }
//                                                                });

                                                                DownloadUtil.downloadFileByOkGo(url, Global.STORAGE_FILE, fileName, 1);

                                                                for (int j = 0; j < 600; j++) {
                                                                    if (!url.equals(DownloadUtil.judgeUrl)) {
//                                                                        LoggerUtil.logI(TAG, "DownloadUtil.judgeUrl  181--->" + DownloadUtil.judgeUrl + "---->" + j + "--->" + i + "---->" + url);
                                                                        SystemClock.sleep(100);
                                                                    } else {
//                                                                        LoggerUtil.logI(TAG, "DownloadUtil.judgeUrl  185--->" + DownloadUtil.judgeUrl + "---->" + j + "----->" + i + "----->" + url);
                                                                        break;
                                                                    }
                                                                }
                                                                if (!url.equals(DownloadUtil.judgeUrl)) {
                                                                    File file = new File(path);
//                                                                    LoggerUtil.logI(TAG, "file.exists  191--->" + file.exists() + "----->" + url);
                                                                    if (file.exists()) {
                                                                        file.delete();
                                                                    }
                                                                    JumpUtil.sendMessageToProgressDialog("加载第" + (i + 1) + "个文件,共" + array.length() + "个, 失败...", LoginActivity.this);
                                                                    return;
                                                                } else {
                                                                    DownloadUtil.judgeUrl = "";
                                                                }
                                                            }
                                                        }
//                                                        LoggerUtil.logI(TAG, "文件下载完毕 " + array.length());
                                                        JumpUtil.sendMessageToProgressDialog("finish", LoginActivity.this);

                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(LoginActivity.this, "文件下载完毕", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                                intent.putExtra("account", username);
                                                                startActivity(intent);

                                                                finish();
                                                            }
                                                        });
                                                    } catch (Exception e) {
                                                        LoggerUtil.logI(TAG, "eee  239--->" + CrashHandler.getInstance().printCrash(e));
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(LoginActivity.this, "文件下载完毕", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                                intent.putExtra("account", username);
                                                                startActivity(intent);

                                                                finish();
                                                            }
                                                        });
                                                    }
                                                }
                                            });


                                        } else if (ret.equals("failed")) {
                                            Toast.makeText(LoginActivity.this, "登录失败！错误码：" + jsonObject.getString("code"), Toast.LENGTH_LONG).show();

                                        }


                                    } catch (Exception e) {
                                        LoggerUtil.logAll(TAG, "eee 78: " + e);
                                        Toast.makeText(LoginActivity.this, "解析json失败", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    LoggerUtil.logAll(TAG, "eee 85: " + e);
                                    hideProgress();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                                            .setTitle("登录失败,请检查网络!")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            })
                                            .setNegativeButton("取消", null);
                                    builder.show();

                                }
                            });
                } catch (Exception e) {
                }

            }
        });
    }

    private void readInfo() {
        String account = WriteFileUtil.read(Global.ACCOUNT);
        et_username.setText(account);
        String token = WriteFileUtil.read(Global.TOKEN);

        LoggerUtil.logI(TAG, "token  313: " + token);
        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(account)) {

            String user_id = WriteFileUtil.read(Global.USER_ID);
            LoggerUtil.logI(TAG, "user_id  317: " + user_id);
            if (TextUtils.isEmpty(user_id)){
                return;
            }

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("account", account);
            startActivity(intent);
            finish();
        }
    }


    private String buildReqStr(String data) {
        String dataNew = data.replace("\\n", "");
        String origin = "{\"data\":\"" + dataNew + "\"}";
        return origin;
    }

    /**
     * 显示进度条对话框
     */
    private void showProgress() {
        if (mDialog == null) {
            mDialog = onCreateProgressDialog();
            if (mDialog == null) {
                return;
            }
        }
        try {
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public final Dialog onCreateProgressDialog() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("请稍等......");
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public final void hideProgress() {
        if (isProgressing()) {
            Dialog dialog = this.mDialog;
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    public final boolean isProgressing() {
        if (this.mDialog != null) {
            Dialog dialog = this.mDialog;
            if (dialog.isShowing()) {
                return true;
            }
        }
        return false;
    }


    public boolean isModuleLoaded() {
        return false;
    }


}
