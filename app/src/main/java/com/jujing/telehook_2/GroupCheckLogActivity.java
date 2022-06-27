package com.jujing.telehook_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jujing.telehook_2.util.LoggerUtil;

public class GroupCheckLogActivity extends AppCompatActivity {
    private static final String TAG = "GroupCheckLogActivity";
    TextView console;
  MyReceiver myReceiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_check_log);

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Global.ACTION_APP_LOG_2);
        registerReceiver(myReceiver, intentFilter);
        console = findViewById(R.id.console);
        String str = LoggerUtil.read100Line(Global.STORAGE_APP_LOG_2);
        console.setText(str);
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
//                LoggerUtil.logAll(TAG, "sss   243---->");
                if (intent.getAction().equals(Global.ACTION_APP_LOG_2)) {

                    String str = LoggerUtil.read100Line(Global.STORAGE_APP_LOG_2);
//                    LoggerUtil.logAll(TAG, "sss   245---->" + str);
                    console.setText(str);
                }

            } catch (Exception e) {
            }
        }
    }
}
