package com.jujing.telehook_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
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

import com.jujing.telehook_2.util.ExecutorUtil;
import com.jujing.telehook_2.util.FileUtils;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.WriteFileUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupActivity extends AppCompatActivity {
    private static final String TAG = "GroupActivity";
    MyReceiver myReceiver;
    TextView console;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        CheckBox cb_about_chinese = findViewById(R.id.cb_about_chinese);
        String is_group_about_chinese = WriteFileUtil.read(Global.IS_GROUP_ABOUT_CHINESE);
        LoggerUtil.logI(TAG, "is_group_about_chinese   33--->" + is_group_about_chinese);
        if (TextUtils.isEmpty(is_group_about_chinese)) {
            cb_about_chinese.setChecked(true);
        }
        if (is_group_about_chinese.equals("true")) {
            cb_about_chinese.setChecked(true);
        } else {
            cb_about_chinese.setChecked(false);
            WriteFileUtil.write("false", Global.IS_GROUP_ABOUT_CHINESE);
        }
        cb_about_chinese.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LoggerUtil.logI(TAG, "IS_GROUP_ABOUT_CHINESE   46--->" + b);
                WriteFileUtil.write(b + "", Global.IS_GROUP_ABOUT_CHINESE);
            }
        });




        CheckBox cb_online = findViewById(R.id.cb_online);
        String is_group_online = WriteFileUtil.read(Global.IS_GROUP_ONLINE);
        LoggerUtil.logI(TAG, "is_group_online   24--->" + is_group_online);
        if (TextUtils.isEmpty(is_group_online)) {
            cb_online.setChecked(true);
        }
        if (is_group_online.equals("true")) {
            cb_online.setChecked(true);
        } else {
            cb_online.setChecked(false);
            WriteFileUtil.write("false", Global.IS_GROUP_ONLINE);
        }
        cb_online.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LoggerUtil.logI(TAG, "IS_GROUP_ONLINE   37--->" + b);
                WriteFileUtil.write(b + "", Global.IS_GROUP_ONLINE);
            }
        });


        CheckBox cb_nick_chinese = findViewById(R.id.cb_nick_chinese);
        String is_group_nick_chinese = WriteFileUtil.read(Global.IS_GROUP_NICK_CHINESE);
        LoggerUtil.logI(TAG, "is_group_nick_chinese   45--->" + is_group_nick_chinese);
        if (TextUtils.isEmpty(is_group_nick_chinese)) {
            cb_nick_chinese.setChecked(true);
        }
        if (is_group_nick_chinese.equals("true")) {
            cb_nick_chinese.setChecked(true);
        } else {
            cb_nick_chinese.setChecked(false);
            WriteFileUtil.write("false", Global.IS_GROUP_NICK_CHINESE);
        }
        cb_nick_chinese.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LoggerUtil.logI(TAG, "IS_GROUP_NICK_CHINESE   58--->" + b);
                WriteFileUtil.write(b + "", Global.IS_GROUP_NICK_CHINESE);
            }
        });

        final EditText et_online_day = findViewById(R.id.et_online_day);
        et_online_day.setText(WriteFileUtil.read(Global.ONLINE_DAY));
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
                LoggerUtil.logI(TAG, "ONLINE_DAY  109--->" + trim);
                WriteFileUtil.write(trim, Global.ONLINE_DAY);
            }
        });

        final EditText et_chatid = findViewById(R.id.et_chatid);
        et_chatid.setText(WriteFileUtil.read(Global.GROUP_CHAT_ID));
        et_chatid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = s.toString().trim();
                LoggerUtil.logI(TAG, "GROUP_CHAT_ID  88--->" + trim);
                WriteFileUtil.write(trim, Global.GROUP_CHAT_ID);
            }
        });

        final EditText et_day = findViewById(R.id.et_day);
        et_day.setText(WriteFileUtil.read(Global.IS_GROUP_DAY));
        et_day.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = s.toString().trim();
                LoggerUtil.logI(TAG, "IS_GROUP_DAY  84--->" + trim);
                WriteFileUtil.write(trim, Global.IS_GROUP_DAY);
            }
        });


        Button btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoggerUtil.logI(TAG, "btn_start  174--->");
                String online_day = et_online_day.getText().toString().trim();
                if (TextUtils.isEmpty(online_day)) {
                    Toast.makeText(GroupActivity.this, "请输入筛选几天内在线用户！", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("isRe", false);
                intent.setAction(HookMain.ACTION_XTELE_GROUP);
                sendBroadcast(intent);
            }
        });

        Button btn_clear_got = findViewById(R.id.btn_clear_got);
        btn_clear_got.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoggerUtil.logI(TAG, "btn_clear_got  189--->");
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupActivity.this)
                        .setTitle("确定清除缓存吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoggerUtil.logI(TAG, "清空采集缓存   199--->");
                                boolean b = FileUtils.deleteDir(Global.GOT_USERNAME);
                                LoggerUtil.logI(TAG, "bb   201--->"+b);
                                if (b){
                                    Toast.makeText(GroupActivity.this, "清除成功！", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(GroupActivity.this, "清除失败！", Toast.LENGTH_LONG).show();
                                }

                            }
                        })
                        .setNegativeButton("取消", null);
                builder.show();
            }
        });
        Button btn_re_start = findViewById(R.id.btn_re_start);
        btn_re_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoggerUtil.logI(TAG, "btn_re_start  185--->");
                String online_day = et_online_day.getText().toString().trim();
                if (TextUtils.isEmpty(online_day)) {
                    Toast.makeText(GroupActivity.this, "请输入筛选几天内在线用户！", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("isRe", true);
                intent.setAction(HookMain.ACTION_XTELE_GROUP);
                sendBroadcast(intent);
            }
        });


        Button btn_check_num = findViewById(R.id.btn_check_num);
        btn_check_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoggerUtil.logI(TAG, "btn_check_num  204--->");

                Intent intent = new Intent();
                intent.setAction(HookMain.ACTION_XTELE_GROUP);
                intent.putExtra("isStop", 2);
                sendBroadcast(intent);
            }
        });
        Button btn_stop = findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoggerUtil.logI(TAG, "btn_stop  192--->");

                Intent intent = new Intent();
                intent.setAction(HookMain.ACTION_XTELE_GROUP);
                intent.putExtra("isStop", 1);
                sendBroadcast(intent);
            }
        });

        Button btn_check = findViewById(R.id.btn_check);
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroupActivity.this, GroupCheckLogActivity.class));

            }
        });

        Button btn_upload = findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroupActivity.this, GroupUploadActivity.class));

            }
        });
        Button btn_join = findViewById(R.id.btn_join);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroupActivity.this, GroupJoinActivity.class));

            }
        });
        Button btn_add_invite = findViewById(R.id.btn_add_invite);
        btn_add_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroupActivity.this, GroupAddAndInviteActivity.class));

//                Intent intent = new Intent();
//                intent.setAction("ACTION_XTELE_GROUP_ADD_INVITE");
//                sendBroadcast(intent);

            }
        });

        Button btn_add_member = findViewById(R.id.btn_add_member);
        btn_add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroupActivity.this, GroupAddMemberActivity.class));

            }
        });


        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Global.ACTION_APP_LOG);
        registerReceiver(myReceiver, intentFilter);
        console = findViewById(R.id.console);
        String str = LoggerUtil.read100Line(Global.STORAGE_APP_LOG);
        console.setText(str);

    }


    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                LoggerUtil.logAll(TAG, "sss   243---->");
                if (intent.getAction().equals(Global.ACTION_APP_LOG)) {

                    String str = LoggerUtil.read100Line(Global.STORAGE_APP_LOG);
//                    LoggerUtil.logAll(TAG, "sss   245---->" + str);
                    console.setText(str);
                }

            } catch (Exception e) {
            }
        }
    }
}
