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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jujing.telehook_2.model.operate.AddAndInviteGroupAction;
import com.jujing.telehook_2.util.ExecutorUtil;
import com.jujing.telehook_2.util.FileUtils;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.WriteFileUtil;
import com.leon.lfilepickerlibrary.LFilePicker;

public class GroupAddAndInviteActivity extends AppCompatActivity {
    private static final String TAG = "GroupAddAndInviteActivity";
    EditText et_interval;
    EditText et_num;
    TextView console;
    MyReceiver myReceiver;
    EditText et_title;
    EditText et_online_day;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add_invite);
        CheckBox cb_group = findViewById(R.id.cb_group);
        String is_group_create = WriteFileUtil.read(Global.IS_GROUP_CREATE);
        LoggerUtil.logI(TAG, "is_group_create   46--->" + is_group_create);
        if (TextUtils.isEmpty(is_group_create)) {
            cb_group.setChecked(true);
        }
        if (is_group_create.equals("true")) {
            cb_group.setChecked(true);
        } else {
            cb_group.setChecked(false);
            WriteFileUtil.write("false", Global.IS_GROUP_CREATE);
        }
        cb_group.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LoggerUtil.logI(TAG, "IS_GROUP_CREATE   59--->" + b);
                WriteFileUtil.write(b + "", Global.IS_GROUP_CREATE);
            }
        });

        Button btn_check_num = findViewById(R.id.btn_check_num);
        btn_check_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String added_contact = WriteFileUtil.read(Global.ADDED_CONTACT);
//                int count = 0;
//                if (TextUtils.isEmpty(added_contact)) {
//                    count = 0;
//                } else {
//                    String[] split = added_contact.split(",");
//                    count = split.length;
//                }
//
//                LoggerUtil.logI(TAG, "count  77---> " + count);

                int index = 0;
                try {
                    index = Integer.parseInt(WriteFileUtil.read(Global.ADDED_CONTACTS_NUM));
                } catch (Exception e) {
                }
                LoggerUtil.logI(TAG, "index  84---> " + index);

                LoggerUtil.writeLog4("已经添加了" + index + "人", GroupAddAndInviteActivity.this);
            }
        });
        Button btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoggerUtil.logI(TAG, "btn_start  26---> ");
                String interval = et_interval.getText().toString().trim();
                if (TextUtils.isEmpty(interval)) {
                    Toast.makeText(GroupAddAndInviteActivity.this, "请输入时间间隔！", Toast.LENGTH_SHORT).show();
                    return;
                }
                String num = et_num.getText().toString().trim();
                if (TextUtils.isEmpty(num)) {
                    Toast.makeText(GroupAddAndInviteActivity.this, "请输入人数！", Toast.LENGTH_SHORT).show();
                    return;
                }

                String online_day = et_online_day.getText().toString().trim();
                if (TextUtils.isEmpty(online_day)) {
                    Toast.makeText(GroupAddAndInviteActivity.this, "请输入筛选几天内在线用户！", Toast.LENGTH_SHORT).show();
                    return;
                }

                String added_contact = WriteFileUtil.read(Global.ADDED_CONTACT);
                if (!TextUtils.isEmpty(added_contact)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GroupAddAndInviteActivity.this)
                            .setTitle("前一次加的人没有建群成功，确定需要重新加人吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    LoggerUtil.logI(TAG, "前一次加的人没有建群成功，确定需要重新加人吗？   71--->");
                                    WriteFileUtil.write("", Global.ADDED_CONTACT);
                                    Intent intent = new Intent();
                                    intent.setAction(HookMain.ACTION_XTELE_GROUP_CHECK_LIST);
                                    sendBroadcast(intent);
                                    startActivity(new Intent(GroupAddAndInviteActivity.this, GroupListActivity.class));

                                }
                            })
                            .setNegativeButton("取消", null);
                    builder.show();


                    return;
                }


                Intent intent = new Intent();
                intent.setAction(HookMain.ACTION_XTELE_GROUP_CHECK_LIST);
                sendBroadcast(intent);
                startActivity(new Intent(GroupAddAndInviteActivity.this, GroupListActivity.class));

            }
        });
        Button btn_stop = findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoggerUtil.logI(TAG, "btn_stop  64---> ");
                Intent intent = new Intent();
                intent.setAction(HookMain.ACTION_XTELE_GROUP_STOP);
                sendBroadcast(intent);
            }
        });
        EditText et_admin_id = findViewById(R.id.et_admin_id);
        et_admin_id.setText(WriteFileUtil.read(Global.GROUP_ADD_INVITE_ADMIN_ID));
        et_admin_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = s.toString().trim();
                LoggerUtil.logI(TAG, "GROUP_ADD_INVITE_ADMIN_ID  94--->" + trim);
                WriteFileUtil.write(trim, Global.GROUP_ADD_INVITE_ADMIN_ID);
            }
        });
        EditText et_num_2 = findViewById(R.id.et_num_2);
        et_num_2.setText(WriteFileUtil.read(Global.GROUP_ADD_INVITE_NUM_2));
        et_num_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = s.toString().trim();
                LoggerUtil.logI(TAG, "GROUP_ADD_INVITE_NUM_2  95--->" + trim);
                WriteFileUtil.write(trim, Global.GROUP_ADD_INVITE_NUM_2);
            }
        });
        EditText et_interval_2 = findViewById(R.id.et_interval_2);
        et_interval_2.setText(WriteFileUtil.read(Global.GROUP_ADD_INVITE_INTERVAL_2));
        et_interval_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = s.toString().trim();
                LoggerUtil.logI(TAG, "GROUP_ADD_INVITE_INTERVAL_2  94--->" + trim);
                WriteFileUtil.write(trim, Global.GROUP_ADD_INVITE_INTERVAL_2);
            }
        });
        et_interval = findViewById(R.id.et_interval);
        et_interval.setText(WriteFileUtil.read(Global.GROUP_ADD_INVITE_INTERVAL));
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
                LoggerUtil.logI(TAG, "GROUP_ADD_INVITE_INTERVAL  54--->" + trim);
                WriteFileUtil.write(trim, Global.GROUP_ADD_INVITE_INTERVAL);
            }
        });


        et_num = findViewById(R.id.et_num);
        et_num.setText(WriteFileUtil.read(Global.GROUP_ADD_INVITE_NUM));
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
                LoggerUtil.logI(TAG, "GROUP_ADD_INVITE_NUM  77--->" + trim);
                WriteFileUtil.write(trim, Global.GROUP_ADD_INVITE_NUM);
            }
        });


        et_title = findViewById(R.id.et_title);
        et_title.setText(WriteFileUtil.read(Global.GROUP_ADD_INVITE_TITLE));
        et_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = s.toString().trim();
                LoggerUtil.logI(TAG, "GROUP_ADD_INVITE_TITLE  129--->" + trim);
                WriteFileUtil.write(trim, Global.GROUP_ADD_INVITE_TITLE);
            }
        });
        et_online_day = findViewById(R.id.et_online_day);
        et_online_day.setText(WriteFileUtil.read(Global.ONLINE_DAY2));
        et_online_day.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = s.toString().trim();
                LoggerUtil.logI(TAG, "ONLINE_DAY2  151--->" + trim);
                WriteFileUtil.write(trim, Global.ONLINE_DAY2);
            }
        });

        Button btn_create = findViewById(R.id.btn_create);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String title = et_title.getText().toString().trim();
//                if (TextUtils.isEmpty(title)) {
//                    Toast.makeText(GroupAddAndInviteActivity.this, "请输入群标题！", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                String added_contact = WriteFileUtil.read(Global.ADDED_CONTACT);
                int count = 0;
                if (TextUtils.isEmpty(added_contact)) {
                    count = 0;
                } else {
                    String[] split = added_contact.split(",");
                    count = split.length;
                }

                LoggerUtil.logI(TAG, "count  156---> " + count);


                Intent intent = new Intent();
                intent.setAction(HookMain.ACTION_XTELE_GROUP_CREATE);
                sendBroadcast(intent);

            }
        });
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Global.ACTION_APP_LOG_4);
        registerReceiver(myReceiver, intentFilter);
        console = findViewById(R.id.console);
        String str = LoggerUtil.read100Line(Global.STORAGE_APP_LOG_4);
        console.setText(str);
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
//                LoggerUtil.logAll(TAG, "sss   243---->");
                if (intent.getAction().equals(Global.ACTION_APP_LOG_4)) {

                    String str = LoggerUtil.read100Line(Global.STORAGE_APP_LOG_4);
//                    LoggerUtil.logAll(TAG, "sss   245---->" + str);
                    console.setText(str);
                }

            } catch (Exception e) {
            }
        }
    }
}
