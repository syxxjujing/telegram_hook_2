package com.jujing.telehook_2;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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

public class GroupUploadActivity extends AppCompatActivity {
    private static final String TAG = "GroupUploadActivity";
    Button btn_get;
    Button btn_copy;
    TextView tv_content;
    Dialog mDialog = null;
    public static boolean isSend = false;
    private ClipboardManager cm;
    private ClipData mClipData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_upload);
        btn_get = findViewById(R.id.btn_get);
        btn_copy = findViewById(R.id.btn_copy);
        tv_content = findViewById(R.id.tv_content);
        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSend) {
//            SendMessageAction2.sendText(talker,"正在压缩，请稍候...");
                    Toast.makeText(GroupUploadActivity.this, "正在压缩！", Toast.LENGTH_SHORT).show();
                    return;
                }
                isSend = true;
                showProgress();
                ExecutorUtil.doExecute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //  先把AllMessage删除  防止文件过大
//                            LoggerUtil.deleteBefore("/sdcard/superhook_tele_2/log/");


                            final String dest = "/sdcard/Download/群用户信息.zip";
                            String src = "/sdcard/Download/群用户信息";//要压缩的文件夹

                            CompressUtil.zip(src, dest);
                            final String url = UploadFileUtil.uploadFile(dest);


                            LoggerUtil.logI(TAG, "url  60---->" + url);


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
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//获取剪贴板管理器：
                cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
// 创建普通字符型ClipData
                mClipData = ClipData.newPlainText("Label", tv_content.getText().toString());
// 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                Toast.makeText(GroupUploadActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
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
