package com.jujing.telehook_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.WriteFileUtil;

public class GroupAddMemberActivity extends AppCompatActivity {
    private static final String TAG = "GroupAddMemberActivity";
    TextView console;
    MyReceiver myReceiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add_member);

        EditText et_group_id = findViewById(R.id.et_group_id);
        et_group_id.setText(WriteFileUtil.read(Global.GROUP_ADD_MEMBER_ID));
        et_group_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = s.toString().trim();
                LoggerUtil.logI(TAG, "GROUP_ADD_MEMBER_ID  35--->" + trim);
                WriteFileUtil.write(trim, Global.GROUP_ADD_MEMBER_ID);
            }
        });

        EditText et_num = findViewById(R.id.et_num);
        et_num.setText(WriteFileUtil.read(Global.GROUP_ADD_MEMBER_NUM));
        et_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = s.toString().trim();
                LoggerUtil.logI(TAG, "GROUP_ADD_MEMBER_NUM  58--->" + trim);
                WriteFileUtil.write(trim, Global.GROUP_ADD_MEMBER_NUM);
            }
        });


        Button btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent();
                intent.setAction(HookMain.ACTION_XTELE_GROUP_ADD_MEMBER);
                sendBroadcast(intent);

            }
        });

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Global.ACTION_APP_LOG_5);
        registerReceiver(myReceiver, intentFilter);
        console = findViewById(R.id.console);
        String str = LoggerUtil.read100Line(Global.STORAGE_APP_LOG_5);
        console.setText(str);
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
//                LoggerUtil.logAll(TAG, "sss   243---->");
                if (intent.getAction().equals(Global.ACTION_APP_LOG_5)) {

                    String str = LoggerUtil.read100Line(Global.STORAGE_APP_LOG_5);
//                    LoggerUtil.logAll(TAG, "sss   245---->" + str);
                    console.setText(str);
                }

            } catch (Exception e) {
            }
        }
    }
}
