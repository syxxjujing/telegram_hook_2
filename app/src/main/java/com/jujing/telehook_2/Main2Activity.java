package com.jujing.telehook_2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jujing.telehook_2.bean.LocalReplyBean;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.FileUtils;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.WriteFileUtil;
import com.leon.lfilepickerlibrary.LFilePicker;

import java.util.List;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "Main2Activity";
    EditText et_content;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button btnText = findViewById(R.id.btn_text);
        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String s = Mp3ToOggAction.mp3ToOgg("/sdcard/1aweme/test.mp3");
//
//                LoggerUtil.logI(TAG, "sss   94--->"+s);

//                String interval = et_interval.getText().toString().trim();
//                if (TextUtils.isEmpty(interval)) {
//                    Toast.makeText(Main2Activity.this, "请输入每批消息之间的时间间隔！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                WriteFileUtil.write(interval, Global.INTERVAL_MESSAGES);
                String interval_friends0 = WriteFileUtil.read(Global.INTERVAL_FRIENDS);

                Intent intent = new Intent();
                intent.putExtra("content", interval_friends0);
//                intent.putExtra("interval", interval);
                intent.setAction(HookMain.ACTION_XTELE_CONTACTS_BOOK_TRAN);
                sendBroadcast(intent);
            }
        });

        Button btn_group = findViewById(R.id.btn_group);
        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this, GroupActivity.class));
            }
        });

        Button btn_black = findViewById(R.id.btn_black);
        btn_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withActivity(Main2Activity.this)
                        .withRequestCode(1026)
                        .withStartPath(Environment.getExternalStorageDirectory().getPath())//指定初始显示路径
                        .withMutilyMode(false)
                        .start();
            }
        });

        Button btn_check_read = findViewById(R.id.btn_check_read);
        btn_check_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoggerUtil.logI(TAG, "btn_check_read  80--->");
                Intent intent = new Intent();
                intent.putExtra("path", "check");
                intent.setAction(HookMain.ACTION_XTELE_COLLECT);
                sendBroadcast(intent);

            }
        });

        Button btn_import_book = findViewById(R.id.btn_import_book);
        btn_import_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this, ImportBookActivity.class));

            }
        });
        Button btn_clear_read = findViewById(R.id.btn_clear_read);
        btn_clear_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteFileUtil.write("", Global.MESSAGE_SUCCESS_NUM);
                WriteFileUtil.write("", Global.MESSAGE_READ_NUM);


                LoggerUtil.logI(TAG, "btn_clear_read   106--->");
                boolean b = FileUtils.deleteDir(Global.SENT_UID);
//                boolean b = FileUtils.deleteDir("/sdcard/1combine_reply/file3/");
                LoggerUtil.logI(TAG, "bbb   374--->" + b);
                Intent intent = new Intent();
                intent.putExtra("path", "check_result");
                intent.setAction(HookMain.ACTION_XTELE_COLLECT);
                sendBroadcast(intent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == 1026) {
                    List<String> list = data.getStringArrayListExtra("paths");
                    final String path = list.get(0);
                    LoggerUtil.logI(TAG, "path  525---->" + path);
                    WriteFileUtil.write(path, Global.BLACK_PATH);

                }

            }
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "e  343---->" + CrashHandler.getInstance().printCrash(e));
            Toast.makeText(this, "有异常！", Toast.LENGTH_SHORT).show();
        }

    }
}
