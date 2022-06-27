package com.jujing.telehook_2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.jujing.telehook_2.util.LoggerUtil;


public class ProgressDialogActivity extends Activity {
    public static ProgressDialogActivity instance = null;
    public static final String ACTION_XREPLY_PROGRESS = "ACTION_XREPLY_PROGRESS";
    TextView tv_content;
    MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_dialog);
        instance = this;
        tv_content = findViewById(R.id.tv_content);
        String content = getIntent().getStringExtra("content");
        tv_content.setText(content);

        tv_content.setMovementMethod(new ScrollingMovementMethod());

        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_XREPLY_PROGRESS);
        registerReceiver(myBroadcastReceiver, intentFilter);
//        if (content.equals("finish")){
//            finish();
//        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }
    void refreshLogView(){
//        tv_content.append(msg);
        int offset=tv_content.getLineCount()*tv_content.getLineHeight();
        if(offset>tv_content.getHeight()){
            tv_content.scrollTo(0,offset-tv_content.getHeight());
        }
    }
    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_XREPLY_PROGRESS)) {
                String content = intent.getStringExtra("content");
                LoggerUtil.logAll("messagea","content  61 ---->"+content);

                if (content.equals("finish")) {
                    finish();
                } else {
                    tv_content.setText(content);
                    refreshLogView();
                }
            }
        }
    }
}
