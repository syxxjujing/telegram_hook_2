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

import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.WriteFileUtil;
import com.leon.lfilepickerlibrary.LFilePicker;

import java.util.List;

public class SayHiActivity extends AppCompatActivity {
    private static final String TAG = "SayHiActivity";
    TextView console;
    MyReceiver myReceiver;
    EditText et_content;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_say_hi);
        et_content = findViewById(R.id.et_content);
        String read2 = WriteFileUtil.read(Global.INTERVAL_FRIENDS);
        et_content.setText(read2);
        
        
        Button btn_first = findViewById(R.id.btn_first);
        btn_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SayHiActivity.this, SayHiFirstActivity.class));
            }
        });
        Button btn_second = findViewById(R.id.btn_second);
        btn_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SayHiActivity.this, SayHiSecondActivity.class));
            }
        });

        Button btn_settings = findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SayHiActivity.this, SayHiSettingsActivity.class));
            }
        });
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Global.ACTION_APP_LOG_7);
        registerReceiver(myReceiver, intentFilter);
        console = findViewById(R.id.console);
        String str = LoggerUtil.read100Line(Global.STORAGE_APP_LOG_7);
        console.setText(str);


        Button btn_local_start = findViewById(R.id.btn_local_start);
        btn_local_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withActivity(SayHiActivity.this)
                        .withRequestCode(1024)
                        .withStartPath(Environment.getExternalStorageDirectory().getPath())//????????????????????????
                        .withMutilyMode(false)
                        .start();
            }
        });

        Button btn_stop = findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SayHiActivity.this)
                        .setTitle("??????????????????")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoggerUtil.logI(TAG, "???????????????   94--->");
                                Intent intent = new Intent();
                                intent.putExtra("content", "stop");
                                intent.setAction(HookMain.ACTION_XTELE_CONTACTS_BOOK_TRAN);
                                sendBroadcast(intent);
                            }
                        })
                        .setNegativeButton("??????", null);
                builder.show();
            }
        });

        EditText et_switch_num = findViewById(R.id.et_switch_num);
        et_switch_num.setText(WriteFileUtil.read(Global.SWITCH_NUM));
        et_switch_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = s.toString().trim();
                LoggerUtil.logI(TAG, "SWITCH_NUM  179--->" + trim);
                WriteFileUtil.write(trim, Global.SWITCH_NUM);
            }
        });
      
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {

                if (requestCode == 1024) {
                    List<String> list = data.getStringArrayListExtra("paths");
                    final String path = list.get(0);
                    LoggerUtil.logI(TAG, "path  330---->" + path);
                    List<String> stringList = WriteFileUtil.readFile(path);
                    LoggerUtil.logI(TAG, "stringList  331---->" + stringList.size());
                    if (stringList.size() == 0) {
                        Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(SayHiActivity.this)
                            .setTitle("??????????????????" + stringList.size() + "????????????????????????????????????")
                            .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    LoggerUtil.logI(TAG, "?????????????????????   362--->");
                                    String content = et_content.getText().toString().trim();
                                    if (TextUtils.isEmpty(content)) {
                                        Toast.makeText(SayHiActivity.this, "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    WriteFileUtil.write(content, Global.INTERVAL_FRIENDS);

                                    Intent intent = new Intent();
//                                    intent.putExtra("content",content);
//                                    intent.putExtra("interval",interval);
                                    intent.putExtra("path", path);
                                    intent.setAction(HookMain.ACTION_XTELE_LOCAL_TRAN);
                                    sendBroadcast(intent);
                                }
                            })
                            .setNegativeButton("??????", null);
                    builder.show();


                }
            }
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "e  343---->" + CrashHandler.getInstance().printCrash(e));
            Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
        }



    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
//                LoggerUtil.logAll(TAG, "sss   243---->");
                if (intent.getAction().equals(Global.ACTION_APP_LOG_7)) {

                    String str = LoggerUtil.read100Line(Global.STORAGE_APP_LOG_7);
//                    LoggerUtil.logAll(TAG, "sss   245---->" + str);
                    console.setText(str);
                }

            } catch (Exception e) {
            }
        }
    }
}
