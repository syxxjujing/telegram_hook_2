package com.jujing.telehook_2;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jujing.telehook_2.adapter.LocalReplyAdapter;
import com.jujing.telehook_2.bean.LocalReplyBean;
import com.jujing.telehook_2.model.operate.Mp3ToOggAction;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.ExecutorUtil;
import com.jujing.telehook_2.util.JsonTool;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.MatchUtil;
import com.jujing.telehook_2.util.ShellUtils;
import com.jujing.telehook_2.util.WriteFileUtil;
import com.leon.lfilepickerlibrary.LFilePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    TextView tv_notice;
    EditText et_content;
    EditText et_interval;
    private Spinner spinnertext;
    ListView lv_reply;
    LocalReplyAdapter adapter;
    List<LocalReplyBean> dataList = new ArrayList<>();
    private ArrayAdapter<String> spAdapter;
    private List<String> list = new ArrayList<String>();
    public static boolean isLoop = false;
    private KeyeReceiver keyeReceiver;
    WindowManager.LayoutParams params;
    WindowManager windowManager;
    LinearLayout toucherLayout;
    TextView tv;
    //状态栏高度.
    int statusBarHeight = -1;

    public static final String ACTION_XTELE_COLLECT_RESULT = "ACTION_XTELE_COLLECT_RESULT";

    //    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
////            int virtualCount = AddText.INSTANCE.getVirtualCount();
////            WriteFileUtil.INSTANCE.wrieFileByBufferedWriter(virtualCount+"",Global.INSTANCE.getSTORAGE_VIRTUAL_NUM());
//            int localSize = 0;
////            try {
////                localSize = Integer.parseInt(WriteFileUtil.read(Global.STORAGE_COUNTER_ALL_NUM));
////            } catch (Exception e) {
////
////            }
//
//            tv.setText("添加人数:" + localSize);
//        }
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_notice = findViewById(R.id.tv_notice);
        et_content = findViewById(R.id.et_content);
        et_interval = findViewById(R.id.et_interval);
        lv_reply = findViewById(R.id.lv_reply);
        spinnertext = (Spinner) findViewById(R.id.spinner1);
        TextView tv_title = findViewById(R.id.tv_title);
        String account = WriteFileUtil.read(Global.ACCOUNT);
        tv_title.setText(account);
        setSpinner();
        createToucher();
        int online_day = 1;
        int expires = 1646845296;
        int iii = expires + online_day * 24 * 60 * 60;
        LoggerUtil.logAll(TAG, "iiii 76--->" + iii + "---->" + System.currentTimeMillis() / 1000);
        keyeReceiver = new KeyeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_XTELE_COLLECT_RESULT);
        registerReceiver(keyeReceiver, intentFilter);
//        String title = "黑客/skjdk";
//        title = title.replace("/", "");
//        LoggerUtil.logI(TAG, "title   94--->"+title);

//        boolean hasChinese = MatchUtil.hasChinese("SKHip");
//        LoggerUtil.logI(TAG, "hasChinese   259--->"+hasChinese);


        String read = WriteFileUtil.read(Global.INTERVAL_MESSAGES);
        et_interval.setText(read);
        String read2 = WriteFileUtil.read(Global.INTERVAL_FRIENDS);
        et_content.setText(read2);

        Button btn_import_book = findViewById(R.id.btn_import_book);
        btn_import_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ImportBookActivity.class));

            }
        });

//        ExecutorUtil.doExecute(new Runnable() {
//            @Override
//            public void run() {
//                if (isLoop) {
//                    return;
//                }
//                isLoop = true;
//
//                while (true) {
//                    try {
////                        SystemClock.sleep(10000);
//                        long stop_caiji = 0;
//                        try {
//                            stop_caiji = Long.parseLong(WriteFileUtil.read(Global.IS_STOP_CAIJI));
//                        } catch (Exception e) {
//
//                        }
//                        long l = System.currentTimeMillis() - stop_caiji;
//                        String is_start_caiji = WriteFileUtil.read(Global.IS_START_CAIJI);
//                        if (is_start_caiji.equals("1")) {
//                            if (l > 1000 * 60 * 5) {//可改成五分钟
//                                LoggerUtil.logI(TAG, "过五分钟了   107--->" + stop_caiji + "---->" + l + "----->" + is_start_caiji);
//                                Intent intent = new Intent();
//                                intent.setAction(HookMain.ACTION_XTELE_GROUP);
//                                intent.putExtra("isStop", 3);
//                                sendBroadcast(intent);
////                                SystemClock.sleep(8000);
////                                Intent redIntent = new Intent();
////                                redIntent.setClassName("org.telegram.messenger", "org.telegram.ui.LaunchActivity");
////                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                                startActivity(redIntent);
//                            }
//
//                        }
//
//
//                        LoggerUtil.logI(TAG, "stop_caiji   89--->" + stop_caiji + "---->" + l + "----->" + is_start_caiji);
//                    } catch (Exception e) {
//                        LoggerUtil.logI(TAG, "eee   126--->" + CrashHandler.getInstance().printCrash(e));
//                    }
//
//                    SystemClock.sleep(1000 * 60);//可改成一分钟
//                }
//
//
//            }
//        });

        Button btnText = findViewById(R.id.btn_text);
        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String s = Mp3ToOggAction.mp3ToOgg("/sdcard/1aweme/test.mp3");
//
//                LoggerUtil.logI(TAG, "sss   94--->"+s);

                String interval = et_interval.getText().toString().trim();
                if (TextUtils.isEmpty(interval)) {
                    Toast.makeText(MainActivity.this, "请输入每批消息之间的时间间隔！", Toast.LENGTH_SHORT).show();
                    return;
                }
                String content = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(MainActivity.this, "请输入转发时好友之间的时间间隔", Toast.LENGTH_SHORT).show();
                    return;
                }
                WriteFileUtil.write(interval, Global.INTERVAL_MESSAGES);
                WriteFileUtil.write(content, Global.INTERVAL_FRIENDS);

                Intent intent = new Intent();
                intent.putExtra("content", content);
                intent.putExtra("interval", interval);
                intent.setAction(HookMain.ACTION_XTELE_CONTACTS_BOOK_TRAN);
                sendBroadcast(intent);
            }
        });

        Button btn_local_start = findViewById(R.id.btn_local_start);
        btn_local_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withActivity(MainActivity.this)
                        .withRequestCode(1024)
                        .withStartPath(Environment.getExternalStorageDirectory().getPath())//指定初始显示路径
                        .withMutilyMode(false)
                        .start();
            }
        });

        Button btn_local_set = findViewById(R.id.btn_local_set);
        btn_local_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withActivity(MainActivity.this)
                        .withRequestCode(1025)
                        .withStartPath(Environment.getExternalStorageDirectory().getPath())//指定初始显示路径
                        .withMutilyMode(false)
                        .start();
            }
        });

        Button btn_black = findViewById(R.id.btn_black);
        btn_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withActivity(MainActivity.this)
                        .withRequestCode(1026)
                        .withStartPath(Environment.getExternalStorageDirectory().getPath())//指定初始显示路径
                        .withMutilyMode(false)
                        .start();
            }
        });

        Button btn_clear = findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("确定清除缓存吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoggerUtil.logI(TAG, "确定清除缓存吗   94--->");
                                WriteFileUtil.write("", Global.SENT_MESSAGES_USER);

                                Toast.makeText(MainActivity.this, "清除成功！", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.show();
            }
        });
        Button btn_stop = findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("确定停止吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoggerUtil.logI(TAG, "确定停止吗   94--->");
                                Intent intent = new Intent();
                                intent.putExtra("content", "stop");
                                intent.setAction(HookMain.ACTION_XTELE_CONTACTS_BOOK_TRAN);
                                sendBroadcast(intent);
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.show();
            }
        });

        Button btn_collect = findViewById(R.id.btn_collect);
        btn_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoggerUtil.logI(TAG, "btn_collect   177--->");
                Intent intent = new Intent();
                intent.setAction(HookMain.ACTION_XTELE_COLLECT);
                sendBroadcast(intent);

            }
        });

        Button btn_check_read = findViewById(R.id.btn_check_read);
        btn_check_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoggerUtil.logI(TAG, "btn_check_read  266--->");
                Intent intent = new Intent();
                intent.putExtra("path", "check");
                intent.setAction(HookMain.ACTION_XTELE_COLLECT);
                sendBroadcast(intent);

            }
        });

        Button btn_group = findViewById(R.id.btn_group);
        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GroupActivity.class));
            }
        });

        Button btn_rebot = findViewById(R.id.btn_rebot);
        btn_rebot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ShellUtils.forceAdb("am force-stop org.telegram.messenger");
                        SystemClock.sleep(2000);
                        ShellUtils.forceAdb("am start -n org.telegram.messenger/org.telegram.ui.LaunchActivity");
                    }
                }).start();

            }
        });


//        try {
//            Context context =null;
//            JSONObject jSONObject = new JSONObject("222");
//
//            setLocalKV(context, "user_unlimit", "1");
//            setLocalKV(context, "user_unlimit", jSONObject.get("unlimit").toString());
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        Button btn_log = findViewById(R.id.btn_log);
        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShowLogActivity.class));
            }
        });
        TextView tv_logout = findViewById(R.id.tv_logout);
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("确定要退出登录吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                WriteFileUtil.write("", Global.TOKEN);
                                WriteFileUtil.write("", Global.USER_ID);
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.show();
            }
        });

        Button btn_clear_dazhaohu = findViewById(R.id.btn_clear_dazhaohu);
        btn_clear_dazhaohu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("确定清除打招呼设置吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoggerUtil.logI(TAG, "确定清除打招呼设置吗   235--->");
                                dataList.clear();
                                for (int i = 0; i < 20; i++) {
                                    LocalReplyBean localReplyBean = new LocalReplyBean("", "");
                                    dataList.add(localReplyBean);
                                }
                                adapter.setDatas(dataList);
                                saveReply();
                                Toast.makeText(MainActivity.this, "清除成功！", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.show();
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

            if (checkSelfPermission(Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        3);
            }
        }


        adapter = new LocalReplyAdapter(this, dataList, R.layout.item_local_reply);
        lv_reply.setAdapter(adapter);
        try {
            String replyJson = WriteFileUtil.read(Global.STORAGE_LOCAL_REPLY_JSON);
            JSONArray jsonArray = new JSONArray(replyJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                String content = jsonObject.getString("content");
                String delay = jsonObject.getString("time");
                LocalReplyBean localReplyBean = new LocalReplyBean(content, delay);
                dataList.add(localReplyBean);
            }
            adapter.setDatas(dataList);
        } catch (Exception e) {
            for (int i = 0; i < 20; i++) {
                LocalReplyBean localReplyBean = new LocalReplyBean("", "");
                dataList.add(localReplyBean);
            }
            adapter.setDatas(dataList);
        }

        lv_reply.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("请选择第" + (position + 1) + "条的类型");
                String[] types = {"文字", "语音", "图片", "gif", "弹语音", "收藏", "视频"};
                builder.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("输入第" + (position + 1) + "条文字内容")
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList.set(position, new LocalReplyBean(input, dataList.get(position).getTime()));
                                            adapter.setDatas(dataList);
                                            saveReply();
                                        }
                                    }).setNegativeButton("取消", null)
                                    .show();
                        } else if (which == 1) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("输入第" + (position + 1) + "条语音的本地路径")
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList.set(position, new LocalReplyBean("语音:" + input, dataList.get(position).getTime()));
                                            adapter.setDatas(dataList);
                                            saveReply();
                                        }
                                    }).setNegativeButton("取消", null)
                                    .show();
                        } else if (which == 2) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("输入第" + (position + 1) + "条图片的本地路径")
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList.set(position, new LocalReplyBean("图片:" + input, dataList.get(position).getTime()));
                                            adapter.setDatas(dataList);
                                            saveReply();
                                        }
                                    }).setNegativeButton("取消", null)
                                    .show();
                        } else if (which == 3) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("输入第" + (position + 1) + "条gif的本地路径")
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList.set(position, new LocalReplyBean("gif:" + input, dataList.get(position).getTime()));
                                            adapter.setDatas(dataList);
                                            saveReply();
                                        }
                                    }).setNegativeButton("取消", null)
                                    .show();
                        } else if (which == 4) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("输入第" + (position + 1) + "条弹语音的延迟时间(秒)")
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList.set(position, new LocalReplyBean("弹语音:" + input, dataList.get(position).getTime()));
                                            adapter.setDatas(dataList);
                                            saveReply();
                                        }
                                    }).setNegativeButton("取消", null)
                                    .show();
                        } else if (which == 5) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("输入收藏夹从下往上数第几条")
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList.set(position, new LocalReplyBean("收藏:" + input, dataList.get(position).getTime()));
                                            adapter.setDatas(dataList);
                                            saveReply();
                                        }
                                    }).setNegativeButton("取消", null)
                                    .show();
                        } else if (which == 6) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("输入第" + (position + 1) + "条视频的本地路径")
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList.set(position, new LocalReplyBean("视频:" + input, dataList.get(position).getTime()));
                                            adapter.setDatas(dataList);
                                            saveReply();
                                        }
                                    }).setNegativeButton("取消", null)
                                    .show();
                        }
                    }
                });
                builder.show();

            }
        });


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        Intent intent = new Intent();
//                        intent.putExtra("path", "check_result");
//                        intent.setAction(HookMain.ACTION_XTELE_COLLECT);
//                        sendBroadcast(intent);
//
//                    } catch (Exception e) {
//
//                    }
//                    SystemClock.sleep(1000 * 30);
//                }
//            }
//        }).start();


    }

    private void setSpinner() {
        list.add("图片");
        list.add("语音");
        //第二步：为下拉列表定义一个适配器
        spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        //第三步：设置下拉列表下拉时的菜单样式
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        spinnertext.setAdapter(spAdapter);
        String dazhaohu_type = WriteFileUtil.read(Global.DAZHAOHU_TYPE);
        if (!TextUtils.isEmpty(dazhaohu_type)) {
            for (int i = 0; i < list.size(); i++) {
                String s = list.get(i);
                if (s.equals(dazhaohu_type)) {
                    spinnertext.setSelection(i);
                    break;
                }
            }
        }

        //第五步：添加监听器，为下拉列表设置事件的响应
        spinnertext.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoggerUtil.logI(TAG, "position  91 ---->" + position);
                parent.setVisibility(View.VISIBLE);
                String s = list.get(position);
                WriteFileUtil.write(s, Global.DAZHAOHU_TYPE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setVisibility(View.VISIBLE);
            }
        });
    }

    private void saveReply() {
        String s = JsonTool.paraseJson(dataList);
        LoggerUtil.logI(TAG, "sss  230---->" + s);
        WriteFileUtil.write(s, Global.STORAGE_LOCAL_REPLY_JSON);
    }

    private void setLocalKV(Context context, String code, String code1) {


    }

    private boolean areEqual(String code, String flavor) {
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isModuleLoaded()) {
            tv_notice.setText("插件正常运行");
            tv_notice.setTextColor(Color.BLACK);
        } else {
            tv_notice.setText("插件未正常运行,请检查xposed设置");
            tv_notice.setTextColor(Color.RED);
        }

//        Intent intent = new Intent();
//        intent.putExtra("path", "check_result");
//        intent.setAction(HookMain.ACTION_XTELE_COLLECT);
//        sendBroadcast(intent);


    }

    private boolean isModuleLoaded() {
        return false;
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
                if (requestCode == 1025) {
                    List<String> list = data.getStringArrayListExtra("paths");
                    final String path = list.get(0);
                    LoggerUtil.logI(TAG, "path  393---->" + path);
                    List<String> stringList = WriteFileUtil.readFile(path);
                    LoggerUtil.logI(TAG, "stringList  395---->" + stringList.size());
                    if (stringList.size() == 0) {
                        Toast.makeText(this, "文件为空！", Toast.LENGTH_SHORT).show();
                    }
                    for (int i = 0; i < stringList.size(); i++) {
                        try {
                            String s = stringList.get(i);
                            LoggerUtil.logI(TAG,"sss  687---->"+s);
                            String[] split = s.split("-");
                            String s1 = split[1];

                            if (s1.equals("wenzi")) {
                                String txt = "";
                                if (split[2].contains("enter")) {
                                    try {
                                        String[] split1 = split[2].split("enter");
                                        txt = split1[0] + "\n" + split1[1];
                                        LoggerUtil.logI(TAG, "txt   94--->" + txt);
                                    } catch (Exception e) {
                                        txt = split[2];
                                    }
                                } else {
                                    txt = split[2];
                                }
                                dataList.set(i, new LocalReplyBean(txt, dataList.get(i).getTime()));
                                adapter.setDatas(dataList);
                            } else if (s1.equals("yuyin")) {
                                dataList.set(i, new LocalReplyBean("语音:" + split[2], dataList.get(i).getTime()));
                                adapter.setDatas(dataList);
                            } else if (s1.equals("tupian")) {
                                dataList.set(i, new LocalReplyBean("图片:" + split[2], dataList.get(i).getTime()));
                                adapter.setDatas(dataList);
                            } else if (s1.equals("gif")) {
                                dataList.set(i, new LocalReplyBean("gif:" + split[2], dataList.get(i).getTime()));
                                adapter.setDatas(dataList);
                            } else if (s1.equals("tanyuyin")) {
                                dataList.set(i, new LocalReplyBean("弹语音:" + split[2], dataList.get(i).getTime()));
                                adapter.setDatas(dataList);
                            } else if (s1.equals("shoucang")) {
                                dataList.set(i, new LocalReplyBean("收藏:" + split[2], dataList.get(i).getTime()));
                                adapter.setDatas(dataList);
                            } else if (s1.equals("shipin")) {
                                dataList.set(i, new LocalReplyBean("视频:" + split[2], dataList.get(i).getTime()));
                                adapter.setDatas(dataList);
                            }
                        } catch (Exception e) {

                        }

                    }
                    saveReply();


                }

                if (requestCode == 1024) {
                    List<String> list = data.getStringArrayListExtra("paths");
                    final String path = list.get(0);
                    LoggerUtil.logI(TAG, "path  330---->" + path);
                    List<String> stringList = WriteFileUtil.readFile(path);
                    LoggerUtil.logI(TAG, "stringList  331---->" + stringList.size());
                    if (stringList.size() == 0) {
                        Toast.makeText(this, "文件为空！", Toast.LENGTH_SHORT).show();
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("导入成功，共" + stringList.size() + "个好友，确定开始转发吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    LoggerUtil.logI(TAG, "确定开始转发吗   362--->");
                                    String interval = et_interval.getText().toString().trim();
                                    if (TextUtils.isEmpty(interval)) {
                                        Toast.makeText(MainActivity.this, "请输入每批消息之间的时间间隔！", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    String content = et_content.getText().toString().trim();
                                    if (TextUtils.isEmpty(content)) {
                                        Toast.makeText(MainActivity.this, "请输入转发时好友之间的时间间隔", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    WriteFileUtil.write(interval, Global.INTERVAL_MESSAGES);
                                    WriteFileUtil.write(content, Global.INTERVAL_FRIENDS);

                                    Intent intent = new Intent();
//                                    intent.putExtra("content",content);
//                                    intent.putExtra("interval",interval);
                                    intent.putExtra("path", path);
                                    intent.setAction(HookMain.ACTION_XTELE_LOCAL_TRAN);
                                    sendBroadcast(intent);
                                }
                            })
                            .setNegativeButton("取消", null);
                    builder.show();


                }
            }
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "e  343---->" + CrashHandler.getInstance().printCrash(e));
            Toast.makeText(this, "有异常！", Toast.LENGTH_SHORT).show();
        }

    }


    private void createToucher() {
        try {
            //赋值WindowManager&LayoutParam.
            params = new WindowManager.LayoutParams();
            windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
            //设置type.系统提示型窗口，一般都在应用程序窗口之上.
            //Android8.0行为变更，对8.0进行适配https://developer.android.google.cn/about/versions/oreo/android-8.0-changes#o-apps
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
//            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//        }
            //设置效果为背景透明.
            params.format = PixelFormat.RGBA_8888;
            //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

            //设置窗口初始停靠位置.
            params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;//若弗设,恐盖父件也
            params.x = 0;
            params.y = 0;

            //设置悬浮窗口长宽数据.
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = 300;

            LayoutInflater inflater = LayoutInflater.from(getApplication());
            //获取浮动窗口视图所在布局.
            toucherLayout = (LinearLayout) inflater.inflate(R.layout.toucherlayout, null);
            //添加toucherlayout
            windowManager.addView(toucherLayout, params);

            Log.i(TAG, "toucherlayout-->left:" + toucherLayout.getLeft());
            Log.i(TAG, "toucherlayout-->right:" + toucherLayout.getRight());
            Log.i(TAG, "toucherlayout-->top:" + toucherLayout.getTop());
            Log.i(TAG, "toucherlayout-->bottom:" + toucherLayout.getBottom());

            //主动计算出当前View的宽高信息.
            toucherLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            //用于检测状态栏高度.
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }
            Log.i(TAG, "状态栏高度为:" + statusBarHeight);

            //浮动窗口按钮.
            tv = (TextView) toucherLayout.findViewById(R.id.tv);


//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    //                while (true) {
//                    //                    tv.setText("添加人数:"+AddText.INSTANCE.getVirtualCount());
//                    //                handler.sendEmptyMessage(1);
//                    //                    SystemClock.sleep(3000);
//                    //                }
//                    //
//                }
//            }).start();


            tv.setOnClickListener(new View.OnClickListener() {
                long[] hints = new long[2];

                @Override
                public void onClick(View v) {
                    Log.i(TAG, "点击了");
                    //                System.arraycopy(hints, 1, hints, 0, hints.length - 1);
                    //                hints[hints.length - 1] = SystemClock.uptimeMillis();
                    //                if (SystemClock.uptimeMillis() - hints[0] >= 700) {
                    //                    Log.i(TAG, "要执行");
                    //                    Toast.makeText(KeepService.this, "连续点击两次以退出", Toast.LENGTH_SHORT).show();
                    //                } else {
                    //                    Log.i(TAG, "即将关闭");
                    //                    stopSelf();
                    //                }
                }
            });

            tv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    params.x = (int) event.getRawX() - 150;
                    params.y = (int) event.getRawY() - 150 - statusBarHeight;
                    windowManager.updateViewLayout(toucherLayout, params);
                    return false;
                }
            });
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee  877--->" + CrashHandler.getInstance().printCrash(e));
        }
    }

    public class KeyeReceiver extends BroadcastReceiver {
        public final static String TAG = "KeyeReceiver";

        private String username = "";

        @Override
        public void onReceive(final Context context, final Intent intent) {

            try {
                if (ACTION_XTELE_COLLECT_RESULT.equals(intent.getAction())) {
//                    int success = intent.getIntExtra("success", -1);
//                    int read = intent.getIntExtra("read", -1);

                    String success = WriteFileUtil.read(Global.MESSAGE_SUCCESS_NUM);
                    String read = WriteFileUtil.read(Global.MESSAGE_READ_NUM);

                    LoggerUtil.logI(TAG, "success  897--->" + success + "---->" + read);

                    if (TextUtils.isEmpty(success)){
                        success = "0";
                    }
                    if (TextUtils.isEmpty(read)){
                        read = "0";
                    }


                    tv.setText("成功:" + success + "\n已读:" + read);

                }
            } catch (Exception e) {
                LoggerUtil.logI(TAG, "eee  890--->" + CrashHandler.getInstance().printCrash(e));
            }


        }


    }
}
