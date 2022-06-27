package com.jujing.telehook_2;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jujing.telehook_2.util.CompressUtil;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.ExecutorUtil;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.UploadFileUtil;


public class ShowLogActivity extends AppCompatActivity {
    private static final String TAG = "ShowLogActivity";
    Button btn_get;
    TextView tv_content;
    Dialog mDialog = null;
    public static boolean isSend = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_log);
        btn_get = findViewById(R.id.btn_get);
        tv_content = findViewById(R.id.tv_content);
        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSend) {
//            SendMessageAction2.sendText(talker,"正在压缩，请稍候...");
                    Toast.makeText(ShowLogActivity.this, "正在压缩！", Toast.LENGTH_SHORT).show();
                    return;
                }
                isSend = true;
                showProgress();
                ExecutorUtil.doExecute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //  先把AllMessage删除  防止文件过大
                            LoggerUtil.deleteBefore("/sdcard/superhook_tele_2/log/");


                            final String dest = "/sdcard/superhook_tele_2/log.zip";
                            String src = "/sdcard/superhook_tele_2/log";//要压缩的文件夹

                            CompressUtil.zip(src, dest);
                            final String url = UploadFileUtil.uploadFile(dest);


//                    SendMessageAction.sendFile(Global.XPOSED_CODE+"log.zip",Global.XPOSED_CODE+"log.zip",dest,talker);
                            LoggerUtil.logI(TAG, "url  140---->"+url);


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_content.setText(url);
                                    hideProgress();
                                }
                            });

//                    com.jujing.xreply.xmodel.wxutil.FileUtils.deleteDir(src);
                        } catch (Exception e) {
                            LoggerUtil.logI(TAG, "eee  23----->" + CrashHandler.getInstance().printCrash(e));
                        }
                        isSend = false;

                    }
                });
            }
        });


        //动态请求权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        2);
            }
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        3);
            }
        }
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
}
