package com.jujing.telehook_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.WriteFileUtil;
import com.leon.lfilepickerlibrary.LFilePicker;

import java.util.List;

public class GroupJoinActivity extends AppCompatActivity {
    private static final String TAG = "GroupJoinActivity";
    MyReceiver myReceiver;
    TextView console;
    EditText et_interval;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_join);

         et_interval = findViewById(R.id.et_interval);
        et_interval.setText(WriteFileUtil.read(Global.GROUP_JOIN_INTERVAL));
        et_interval.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = s.toString().trim();
                LoggerUtil.logI(TAG, "GROUP_JOIN_INTERVAL  35--->" + trim);
                WriteFileUtil.write(trim, Global.GROUP_JOIN_INTERVAL);
            }
        });


        Button btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withActivity(GroupJoinActivity.this)
                        .withRequestCode(1024)
                        .withStartPath(Environment.getExternalStorageDirectory().getPath())//指定初始显示路径
                        .withMutilyMode(false)
                        .start();
            }
        });

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Global.ACTION_APP_LOG_3);
        registerReceiver(myReceiver, intentFilter);
        console = findViewById(R.id.console);
        String str = LoggerUtil.read100Line(Global.STORAGE_APP_LOG_3);
        console.setText(str);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                List<String> list = data.getStringArrayListExtra("paths");
                final String path = list.get(0);
                LoggerUtil.logI(TAG, "path  80---->" + path);
                List<String> stringList = WriteFileUtil.readFile(path);
                LoggerUtil.logI(TAG, "stringList  82---->" + stringList.size());
                if (stringList.size() == 0) {
                    Toast.makeText(this, "文件为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(GroupJoinActivity.this)
                        .setTitle("导入成功，共" + stringList.size() + "个群，确定开始转发吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoggerUtil.logI(TAG, "确定开始转发吗   101--->");
                                String interval = et_interval.getText().toString().trim();
                                if (TextUtils.isEmpty(interval)) {
                                    Toast.makeText(GroupJoinActivity.this, "请输入时间间隔！", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                Intent intent = new Intent();
                                intent.putExtra("path", path);
                                intent.setAction(HookMain.ACTION_XTELE_GROUP_JOIN);
                                sendBroadcast(intent);
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.show();





            }
        } catch (Exception e) {

        }
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
//                LoggerUtil.logAll(TAG, "sss   243---->");
                if (intent.getAction().equals(Global.ACTION_APP_LOG_3)) {

                    String str = LoggerUtil.read100Line(Global.STORAGE_APP_LOG_3);
//                    LoggerUtil.logAll(TAG, "sss   245---->" + str);
                    console.setText(str);
                }

            } catch (Exception e) {
            }
        }
    }
}
