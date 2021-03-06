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
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jujing.telehook_2.adapter.LocalReplyAdapter;
import com.jujing.telehook_2.bean.LocalReplyBean;
import com.jujing.telehook_2.hook.HookVPN;
import com.jujing.telehook_2.model.operate.Mp3ToOggAction;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.ExecutorUtil;
import com.jujing.telehook_2.util.FileUtils;
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
    //???????????????.
    int statusBarHeight = -1;


    ListView lv_reply_1;
    LocalReplyAdapter adapter_1;
    List<LocalReplyBean> dataList_1 = new ArrayList<>();


    ListView lv_reply_2;
    LocalReplyAdapter adapter_2;
    List<LocalReplyBean> dataList_2 = new ArrayList<>();

    TextView console;
    MyReceiver myReceiver;

    LinearLayout ll_list;
    ScrollView scrollview;
    Button btn_check_situation;

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
//            tv.setText("????????????:" + localSize);
//        }
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_notice = findViewById(R.id.tv_notice);
        et_content = findViewById(R.id.et_content);
        lv_reply = findViewById(R.id.lv_reply);
        ll_list = findViewById(R.id.ll_list);
        scrollview = findViewById(R.id.scrollview);
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
//        String title = "??????/skjdk";
//        title = title.replace("/", "");
//        LoggerUtil.logI(TAG, "title   94--->"+title);

//        boolean hasChinese = MatchUtil.hasChinese("SKHip");
//        LoggerUtil.logI(TAG, "hasChinese   259--->"+hasChinese);


        String read2 = WriteFileUtil.read(Global.INTERVAL_FRIENDS);
        et_content.setText(read2);
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

        Button btn_import_book = findViewById(R.id.btn_import_book);
        btn_import_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ImportBookActivity.class));

            }
        });

        btn_check_situation = findViewById(R.id.btn_check_situation);
        btn_check_situation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_check_situation.getText().toString().equals("???????????????????????????")){
                    ll_list.setVisibility(View.GONE);
                    scrollview.setVisibility(View.VISIBLE);
                    btn_check_situation.setText("?????????????????????");
                }else{
                    ll_list.setVisibility(View.VISIBLE);
                    scrollview.setVisibility(View.GONE);
                    btn_check_situation.setText("???????????????????????????");
                }

            }
        });

        EditText et_interval = findViewById(R.id.et_interval);
        et_interval.setText(WriteFileUtil.read(Global.INTERVAL_MESSAGES));
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
                LoggerUtil.logI(TAG, "INTERVAL_MESSAGES  219--->" + trim);
                WriteFileUtil.write(trim, Global.INTERVAL_MESSAGES);
            }
        });

        Button btn_dazhaohu = findViewById(R.id.btn_dazhaohu);
        btn_dazhaohu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));

            }
        });


        Button btn_local_start = findViewById(R.id.btn_local_start);
        btn_local_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withActivity(MainActivity.this)
                        .withRequestCode(1024)
                        .withStartPath(Environment.getExternalStorageDirectory().getPath())//????????????????????????
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
                        .withStartPath(Environment.getExternalStorageDirectory().getPath())//????????????????????????
                        .withMutilyMode(false)
                        .start();
            }
        });

        initSayHiSettings();
        myReceiver = new MyReceiver();
        IntentFilter intentFilter0 = new IntentFilter();
        intentFilter0.addAction(Global.ACTION_APP_LOG_7);
        registerReceiver(myReceiver, intentFilter0);
        console = findViewById(R.id.console);
        String str = LoggerUtil.read100Line(Global.STORAGE_APP_LOG_7);
        console.setText(str);
        Button btn_black = findViewById(R.id.btn_black);
        btn_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withActivity(MainActivity.this)
                        .withRequestCode(1026)
                        .withStartPath(Environment.getExternalStorageDirectory().getPath())//????????????????????????
                        .withMutilyMode(false)
                        .start();
            }
        });

        Button btn_clear = findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("????????????????????????")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoggerUtil.logI(TAG, "?????????????????????   94--->");
                                WriteFileUtil.write("", Global.SENT_MESSAGES_USER);
                                FileUtils.deleteDir(Global.SENT_MESSAGE);

                                Toast.makeText(MainActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("??????", null);
                builder.show();
            }
        });
        Button btn_stop = findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
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
        Button btn_other = findViewById(R.id.btn_other);
        btn_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });

        Button btn_clear_read = findViewById(R.id.btn_clear_read);
        btn_clear_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteFileUtil.write("", Global.MESSAGE_SUCCESS_NUM);
                WriteFileUtil.write("", Global.MESSAGE_READ_NUM);


                LoggerUtil.logI(TAG, "btn_clear_read   372--->");
                boolean b = FileUtils.deleteDir(Global.SENT_UID);
//                boolean b = FileUtils.deleteDir("/sdcard/1combine_reply/file3/");
                LoggerUtil.logI(TAG, "bbb   374--->" + b);
                Intent intent = new Intent();
                intent.putExtra("path", "check_result");
                intent.setAction(HookMain.ACTION_XTELE_COLLECT);
                sendBroadcast(intent);

            }
        });
        TextView tv_logout = findViewById(R.id.tv_logout);
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("???????????????????????????")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                WriteFileUtil.write("", Global.TOKEN);
                                WriteFileUtil.write("", Global.USER_ID);
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("??????", null);
                builder.show();
            }
        });

        Button btn_clear_dazhaohu = findViewById(R.id.btn_clear_dazhaohu);
        btn_clear_dazhaohu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("?????????????????????????????????")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoggerUtil.logI(TAG, "??????????????????????????????   235--->");
                                dataList.clear();
                                for (int i = 0; i < 20; i++) {
                                    LocalReplyBean localReplyBean = new LocalReplyBean("", "");
                                    dataList.add(localReplyBean);
                                }
                                adapter.setDatas(dataList);
                                saveReply();
                                Toast.makeText(MainActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("??????", null);
                builder.show();
            }
        });

        //??????????????????
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
                        new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},
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
                builder.setTitle("????????????" + (position + 1) + "????????????");
                String[] types = {"??????", "??????", "??????", "gif", "?????????", "??????", "??????"};
                builder.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("?????????" + (position + 1) + "???????????????")
                                    .setView(et)
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList.set(position, new LocalReplyBean(input, dataList.get(position).getTime()));
                                            adapter.setDatas(dataList);
                                            saveReply();
                                        }
                                    }).setNegativeButton("??????", null)
                                    .show();
                        } else if (which == 1) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("?????????" + (position + 1) + "????????????????????????")
                                    .setView(et)
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList.set(position, new LocalReplyBean("??????:" + input, dataList.get(position).getTime()));
                                            adapter.setDatas(dataList);
                                            saveReply();
                                        }
                                    }).setNegativeButton("??????", null)
                                    .show();
                        } else if (which == 2) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("?????????" + (position + 1) + "????????????????????????")
                                    .setView(et)
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList.set(position, new LocalReplyBean("??????:" + input, dataList.get(position).getTime()));
                                            adapter.setDatas(dataList);
                                            saveReply();
                                        }
                                    }).setNegativeButton("??????", null)
                                    .show();
                        } else if (which == 3) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("?????????" + (position + 1) + "???gif???????????????")
                                    .setView(et)
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList.set(position, new LocalReplyBean("gif:" + input, dataList.get(position).getTime()));
                                            adapter.setDatas(dataList);
                                            saveReply();
                                        }
                                    }).setNegativeButton("??????", null)
                                    .show();
                        } else if (which == 4) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("?????????" + (position + 1) + "???????????????????????????(???)")
                                    .setView(et)
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList.set(position, new LocalReplyBean("?????????:" + input, dataList.get(position).getTime()));
                                            adapter.setDatas(dataList);
                                            saveReply();
                                        }
                                    }).setNegativeButton("??????", null)
                                    .show();
                        } else if (which == 5) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("???????????????????????????????????????")
                                    .setView(et)
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList.set(position, new LocalReplyBean("??????:" + input, dataList.get(position).getTime()));
                                            adapter.setDatas(dataList);
                                            saveReply();
                                        }
                                    }).setNegativeButton("??????", null)
                                    .show();
                        } else if (which == 6) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("?????????" + (position + 1) + "????????????????????????")
                                    .setView(et)
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList.set(position, new LocalReplyBean("??????:" + input, dataList.get(position).getTime()));
                                            adapter.setDatas(dataList);
                                            saveReply();
                                        }
                                    }).setNegativeButton("??????", null)
                                    .show();
                        }
                    }
                });
                builder.show();

            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Intent intent = new Intent();
                        intent.putExtra("path", "check_result");
                        intent.setAction(HookMain.ACTION_XTELE_COLLECT);
                        sendBroadcast(intent);

                    } catch (Exception e) {

                    }
                    SystemClock.sleep(1000 * 30);
                }
            }
        }).start();


    }

    private void initSayHiSettings() {

        lv_reply_1 = findViewById(R.id.lv_reply_1);
        Button btn_local_set_1 = findViewById(R.id.btn_local_set_1);
        btn_local_set_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withActivity(MainActivity.this)
                        .withRequestCode(1025)
                        .withStartPath(Environment.getExternalStorageDirectory().getPath())//????????????????????????
                        .withMutilyMode(false)
                        .start();
            }
        });

        adapter_1 = new LocalReplyAdapter(this, dataList_1, R.layout.item_local_reply);
        lv_reply_1.setAdapter(adapter_1);
        try {
            String replyJson = WriteFileUtil.read(Global.STORAGE_LOCAL_REPLY_JSON);
            JSONArray jsonArray = new JSONArray(replyJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                String content = jsonObject.getString("content");
                String delay = jsonObject.getString("time");
                LocalReplyBean localReplyBean = new LocalReplyBean(content, delay);
                dataList_1.add(localReplyBean);
            }
            adapter_1.setDatas(dataList_1);
        } catch (Exception e) {
            for (int i = 0; i < 20; i++) {
                LocalReplyBean localReplyBean = new LocalReplyBean("", "");
                dataList_1.add(localReplyBean);
            }
            adapter_1.setDatas(dataList_1);
        }
        lv_reply_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("????????????" + (position + 1) + "????????????");
                String[] types = {"??????", "??????", "??????", "gif", "?????????", "??????", "??????"};
                builder.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("?????????" + (position + 1) + "???????????????")
                                    .setView(et)
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_1.set(position, new LocalReplyBean(input, dataList_1.get(position).getTime()));
                                            adapter_1.setDatas(dataList_1);
                                            saveReply_1();
                                        }
                                    }).setNegativeButton("??????", null)
                                    .show();
                        } else if (which == 1) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("?????????" + (position + 1) + "????????????????????????")
                                    .setView(et)
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_1.set(position, new LocalReplyBean("??????:" + input, dataList_1.get(position).getTime()));
                                            adapter_1.setDatas(dataList_1);
                                            saveReply_1();
                                        }
                                    }).setNegativeButton("??????", null)
                                    .show();
                        } else if (which == 2) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("?????????" + (position + 1) + "????????????????????????")
                                    .setView(et)
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_1.set(position, new LocalReplyBean("??????:" + input, dataList_1.get(position).getTime()));
                                            adapter_1.setDatas(dataList_1);
                                            saveReply_1();
                                        }
                                    }).setNegativeButton("??????", null)
                                    .show();
                        } else if (which == 3) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("?????????" + (position + 1) + "???gif???????????????")
                                    .setView(et)
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_1.set(position, new LocalReplyBean("gif:" + input, dataList_1.get(position).getTime()));
                                            adapter_1.setDatas(dataList_1);
                                            saveReply_1();
                                        }
                                    }).setNegativeButton("??????", null)
                                    .show();
                        } else if (which == 4) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("?????????" + (position + 1) + "???????????????????????????(???)")
                                    .setView(et)
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_1.set(position, new LocalReplyBean("?????????:" + input, dataList_1.get(position).getTime()));
                                            adapter_1.setDatas(dataList_1);
                                            saveReply_1();
                                        }
                                    }).setNegativeButton("??????", null)
                                    .show();
                        } else if (which == 5) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("???????????????????????????????????????")
                                    .setView(et)
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_1.set(position, new LocalReplyBean("??????:" + input, dataList_1.get(position).getTime()));
                                            adapter_1.setDatas(dataList_1);
                                            saveReply_1();
                                        }
                                    }).setNegativeButton("??????", null)
                                    .show();
                        } else if (which == 6) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("?????????" + (position + 1) + "????????????????????????")
                                    .setView(et)
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_1.set(position, new LocalReplyBean("??????:" + input, dataList_1.get(position).getTime()));
                                            adapter_1.setDatas(dataList_1);
                                            saveReply_1();
                                        }
                                    }).setNegativeButton("??????", null)
                                    .show();
                        }
                    }
                });
                builder.show();

            }
        });
        Button btn_clear_dazhaohu_1 = findViewById(R.id.btn_clear_dazhaohu_1);
        btn_clear_dazhaohu_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("?????????????????????????????????")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoggerUtil.logI(TAG, "??????????????????????????????   235--->");
                                dataList_1.clear();
                                for (int i = 0; i < 20; i++) {
                                    LocalReplyBean localReplyBean = new LocalReplyBean("", "");
                                    dataList_1.add(localReplyBean);
                                }
                                adapter_1.setDatas(dataList_1);
                                saveReply_1();
                                Toast.makeText(MainActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("??????", null);
                builder.show();
            }
        });


        CheckBox cb_only_unread = findViewById(R.id.cb_only_unread);
        String is_only_unread = WriteFileUtil.read(Global.IS_ONLY_UNREAD);
        LoggerUtil.logI(TAG, "is_only_unread   230--->" + is_only_unread);
        if (TextUtils.isEmpty(is_only_unread)) {
            cb_only_unread.setChecked(true);
        }
        if (is_only_unread.equals("true")) {
            cb_only_unread.setChecked(true);
        } else {
            cb_only_unread.setChecked(false);
            WriteFileUtil.write("false", Global.IS_ONLY_UNREAD);
        }
        cb_only_unread.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LoggerUtil.logI(TAG, "IS_ONLY_UNREAD   243--->" + b);
                WriteFileUtil.write(b + "", Global.IS_ONLY_UNREAD);
            }
        });
        lv_reply_2 = findViewById(R.id.lv_reply_2);
        Button btn_local_set_2 = findViewById(R.id.btn_local_set_2);
        btn_local_set_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withActivity(MainActivity.this)
                        .withRequestCode(1026)
                        .withStartPath(Environment.getExternalStorageDirectory().getPath())//????????????????????????
                        .withMutilyMode(false)
                        .start();
            }
        });

        adapter_2 = new LocalReplyAdapter(this, dataList_2, R.layout.item_local_reply);
        lv_reply_2.setAdapter(adapter_2);
        try {
            String replyJson = WriteFileUtil.read(Global.STORAGE_LOCAL_REPLY_JSON_2);
            JSONArray jsonArray = new JSONArray(replyJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                String content = jsonObject.getString("content");
                String delay = jsonObject.getString("time");
                LocalReplyBean localReplyBean = new LocalReplyBean(content, delay);
                dataList_2.add(localReplyBean);
            }
            adapter_2.setDatas(dataList_2);
        } catch (Exception e) {
            for (int i = 0; i < 20; i++) {
                LocalReplyBean localReplyBean = new LocalReplyBean("", "");
                dataList_2.add(localReplyBean);
            }
            adapter_2.setDatas(dataList_2);
        }
        lv_reply_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("????????????" + (position + 1) + "????????????");
                String[] types = {"??????", "??????", "??????", "gif", "?????????", "??????", "??????"};
                builder.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("?????????" + (position + 1) + "???????????????")
                                    .setView(et)
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_2.set(position, new LocalReplyBean(input, dataList_2.get(position).getTime()));
                                            adapter_2.setDatas(dataList_2);
                                            saveReply_2();
                                        }
                                    }).setNegativeButton("??????", null)
                                    .show();
                        } else if (which == 1) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("?????????" + (position + 1) + "????????????????????????")
                                    .setView(et)
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_2.set(position, new LocalReplyBean("??????:" + input, dataList_2.get(position).getTime()));
                                            adapter_2.setDatas(dataList_2);
                                            saveReply_2();
                                        }
                                    }).setNegativeButton("??????", null)
                                    .show();
                        } else if (which == 2) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("?????????" + (position + 1) + "????????????????????????")
                                    .setView(et)
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_2.set(position, new LocalReplyBean("??????:" + input, dataList_2.get(position).getTime()));
                                            adapter_2.setDatas(dataList_2);
                                            saveReply_2();
                                        }
                                    }).setNegativeButton("??????", null)
                                    .show();
                        } else if (which == 3) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("?????????" + (position + 1) + "???gif???????????????")
                                    .setView(et)
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_2.set(position, new LocalReplyBean("gif:" + input, dataList_2.get(position).getTime()));
                                            adapter_2.setDatas(dataList_2);
                                            saveReply_2();
                                        }
                                    }).setNegativeButton("??????", null)
                                    .show();
                        } else if (which == 4) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("?????????" + (position + 1) + "???????????????????????????(???)")
                                    .setView(et)
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_2.set(position, new LocalReplyBean("?????????:" + input, dataList_2.get(position).getTime()));
                                            adapter_2.setDatas(dataList_2);
                                            saveReply_2();
                                        }
                                    }).setNegativeButton("??????", null)
                                    .show();
                        } else if (which == 5) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("???????????????????????????????????????")
                                    .setView(et)
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_2.set(position, new LocalReplyBean("??????:" + input, dataList_2.get(position).getTime()));
                                            adapter_2.setDatas(dataList_2);
                                            saveReply_2();
                                        }
                                    }).setNegativeButton("??????", null)
                                    .show();
                        } else if (which == 6) {
                            final EditText et = new EditText(MainActivity.this);
                            new AlertDialog.Builder(MainActivity.this).setTitle("?????????" + (position + 1) + "????????????????????????")
                                    .setView(et)
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_2.set(position, new LocalReplyBean("??????:" + input, dataList_2.get(position).getTime()));
                                            adapter_2.setDatas(dataList_2);
                                            saveReply_2();
                                        }
                                    }).setNegativeButton("??????", null)
                                    .show();
                        }
                    }
                });
                builder.show();

            }
        });
        Button btn_clear_dazhaohu_2 = findViewById(R.id.btn_clear_dazhaohu_2);
        btn_clear_dazhaohu_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("?????????????????????????????????")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoggerUtil.logI(TAG, "??????????????????????????????   424--->");
                                dataList_2.clear();
                                for (int i = 0; i < 20; i++) {
                                    LocalReplyBean localReplyBean = new LocalReplyBean("", "");
                                    dataList_2.add(localReplyBean);
                                }
                                adapter_2.setDatas(dataList_2);
                                saveReply_2();
                                Toast.makeText(MainActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("??????", null);
                builder.show();
            }
        });

        EditText et_round = findViewById(R.id.et_round);
        et_round.setText(WriteFileUtil.read(Global.SAY_HI_ROUND_INTERVAL));
        et_round.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = s.toString().trim();
                LoggerUtil.logI(TAG, "SAY_HI_ROUND_INTERVAL  1022--->" + trim);
                WriteFileUtil.write(trim, Global.SAY_HI_ROUND_INTERVAL);
            }
        });
    }

    private void saveReply_1() {
        String s = JsonTool.paraseJson(dataList_1);
        LoggerUtil.logI(TAG, "sss  216---->" + s);
        WriteFileUtil.write(s, Global.STORAGE_LOCAL_REPLY_JSON);
    }

    private void saveReply_2() {
        String s = JsonTool.paraseJson(dataList_2);
        LoggerUtil.logI(TAG, "sss  450---->" + s);
        WriteFileUtil.write(s, Global.STORAGE_LOCAL_REPLY_JSON_2);
    }

    private void setSpinner() {
        list.add("??????");
        list.add("??????");
        //????????????????????????????????????????????????
        spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        //??????????????????????????????????????????????????????
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //????????????????????????????????????????????????
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

        //??????????????????????????????????????????????????????????????????
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
            tv_notice.setText("??????????????????");
            tv_notice.setTextColor(Color.BLACK);
        } else {
            tv_notice.setText("?????????????????????,?????????xposed??????");
            tv_notice.setTextColor(Color.RED);
        }

        Intent intent = new Intent();
        intent.putExtra("path", "check_result");
        intent.setAction(HookMain.ACTION_XTELE_COLLECT);
        sendBroadcast(intent);

//        boolean b = HookVPN.checkVPN();
//
//        LoggerUtil.logI(TAG, "bbb  698---->" + b);

    }

    private boolean isModuleLoaded() {
        return false;
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("??????????????????" + stringList.size() + "????????????????????????????????????")
                            .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    LoggerUtil.logI(TAG, "?????????????????????   362--->");
                                    String content = et_content.getText().toString().trim();
                                    if (TextUtils.isEmpty(content)) {
                                        Toast.makeText(MainActivity.this, "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
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

                if (requestCode == 1025) {
                    List<String> list = data.getStringArrayListExtra("paths");
                    final String path = list.get(0);
                    LoggerUtil.logI(TAG, "path  393---->" + path);
                    List<String> stringList = WriteFileUtil.readFile(path);
                    LoggerUtil.logI(TAG, "stringList  395---->" + stringList.size());
                    if (stringList.size() == 0) {
                        Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
                    }
                    for (int i = 0; i < stringList.size(); i++) {
                        try {
                            String s = stringList.get(i);
                            LoggerUtil.logI(TAG, "sss  687---->" + s);
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
                                dataList_1.set(i, new LocalReplyBean(txt, dataList_1.get(i).getTime()));
                                adapter_1.setDatas(dataList_1);
                            } else if (s1.equals("yuyin")) {
                                dataList_1.set(i, new LocalReplyBean("??????:" + split[2], dataList_1.get(i).getTime()));
                                adapter_1.setDatas(dataList_1);
                            } else if (s1.equals("tupian")) {
                                dataList_1.set(i, new LocalReplyBean("??????:" + split[2], dataList_1.get(i).getTime()));
                                adapter_1.setDatas(dataList_1);
                            } else if (s1.equals("gif")) {
                                dataList_1.set(i, new LocalReplyBean("gif:" + split[2], dataList_1.get(i).getTime()));
                                adapter_1.setDatas(dataList_1);
                            } else if (s1.equals("tanyuyin")) {
                                dataList_1.set(i, new LocalReplyBean("?????????:" + split[2], dataList_1.get(i).getTime()));
                                adapter_1.setDatas(dataList_1);
                            } else if (s1.equals("shoucang")) {
                                dataList_1.set(i, new LocalReplyBean("??????:" + split[2], dataList_1.get(i).getTime()));
                                adapter_1.setDatas(dataList_1);
                            } else if (s1.equals("shipin")) {
                                dataList_1.set(i, new LocalReplyBean("??????:" + split[2], dataList_1.get(i).getTime()));
                                adapter_1.setDatas(dataList_1);
                            }
                        } catch (Exception e) {

                        }

                    }
                    saveReply_1();
                }
                if (requestCode == 1026) {
                    List<String> list = data.getStringArrayListExtra("paths");
                    final String path = list.get(0);
                    LoggerUtil.logI(TAG, "path  516---->" + path);
                    List<String> stringList = WriteFileUtil.readFile(path);
                    LoggerUtil.logI(TAG, "stringList  518---->" + stringList.size());
                    if (stringList.size() == 0) {
                        Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
                    }
                    for (int i = 0; i < stringList.size(); i++) {
                        try {
                            String s = stringList.get(i);
                            LoggerUtil.logI(TAG, "sss  525---->" + s);
                            String[] split = s.split("-");
                            String s1 = split[1];

                            if (s1.equals("wenzi")) {
                                String txt = "";
                                if (split[2].contains("enter")) {
                                    try {
                                        String[] split1 = split[2].split("enter");
                                        txt = split1[0] + "\n" + split1[1];
                                        LoggerUtil.logI(TAG, "txt   535--->" + txt);
                                    } catch (Exception e) {
                                        txt = split[2];
                                    }
                                } else {
                                    txt = split[2];
                                }
                                dataList_2.set(i, new LocalReplyBean(txt, dataList_2.get(i).getTime()));
                                adapter_2.setDatas(dataList_2);
                            } else if (s1.equals("yuyin")) {
                                dataList_2.set(i, new LocalReplyBean("??????:" + split[2], dataList_2.get(i).getTime()));
                                adapter_2.setDatas(dataList_2);
                            } else if (s1.equals("tupian")) {
                                dataList_2.set(i, new LocalReplyBean("??????:" + split[2], dataList_2.get(i).getTime()));
                                adapter_2.setDatas(dataList_2);
                            } else if (s1.equals("gif")) {
                                dataList_2.set(i, new LocalReplyBean("gif:" + split[2], dataList_2.get(i).getTime()));
                                adapter_2.setDatas(dataList_2);
                            } else if (s1.equals("tanyuyin")) {
                                dataList_2.set(i, new LocalReplyBean("?????????:" + split[2], dataList_2.get(i).getTime()));
                                adapter_2.setDatas(dataList_2);
                            } else if (s1.equals("shoucang")) {
                                dataList_2.set(i, new LocalReplyBean("??????:" + split[2], dataList_2.get(i).getTime()));
                                adapter_2.setDatas(dataList_2);
                            } else if (s1.equals("shipin")) {
                                dataList_2.set(i, new LocalReplyBean("??????:" + split[2], dataList_2.get(i).getTime()));
                                adapter_2.setDatas(dataList_2);
                            }
                        } catch (Exception e) {

                        }

                    }
                    saveReply_2();


                }
            }
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "e  343---->" + CrashHandler.getInstance().printCrash(e));
            Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
        }

    }


    private void createToucher() {
        try {
            //??????WindowManager&LayoutParam.
            params = new WindowManager.LayoutParams();
            windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
            //??????type.????????????????????????????????????????????????????????????.
            //Android8.0??????????????????8.0????????????https://developer.android.google.cn/about/versions/oreo/android-8.0-changes#o-apps
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
//            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//        }
            //???????????????????????????.
            params.format = PixelFormat.RGBA_8888;
            //??????flags.?????????????????????????????????????????????????????????.
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

            //??????????????????????????????.
            params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;//?????????,???????????????
            params.x = 0;
            params.y = 0;

            //??????????????????????????????.
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = 300;

            LayoutInflater inflater = LayoutInflater.from(getApplication());
            //????????????????????????????????????.
            toucherLayout = (LinearLayout) inflater.inflate(R.layout.toucherlayout, null);
            //??????toucherlayout
            windowManager.addView(toucherLayout, params);

            Log.i(TAG, "toucherlayout-->left:" + toucherLayout.getLeft());
            Log.i(TAG, "toucherlayout-->right:" + toucherLayout.getRight());
            Log.i(TAG, "toucherlayout-->top:" + toucherLayout.getTop());
            Log.i(TAG, "toucherlayout-->bottom:" + toucherLayout.getBottom());

            //?????????????????????View???????????????.
            toucherLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            //???????????????????????????.
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }
            Log.i(TAG, "??????????????????:" + statusBarHeight);

            //??????????????????.
            tv = (TextView) toucherLayout.findViewById(R.id.tv);


//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    //                while (true) {
//                    //                    tv.setText("????????????:"+AddText.INSTANCE.getVirtualCount());
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
                    Log.i(TAG, "?????????");
                    //                System.arraycopy(hints, 1, hints, 0, hints.length - 1);
                    //                hints[hints.length - 1] = SystemClock.uptimeMillis();
                    //                if (SystemClock.uptimeMillis() - hints[0] >= 700) {
                    //                    Log.i(TAG, "?????????");
                    //                    Toast.makeText(KeepService.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
                    //                } else {
                    //                    Log.i(TAG, "????????????");
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

//                    LoggerUtil.logI(TAG, "success  897--->" + success + "---->" + read);

                    if (TextUtils.isEmpty(success)) {
                        success = "0";
                    }
                    if (TextUtils.isEmpty(read)) {
                        read = "0";
                    }


                    tv.setText("??????:" + success + "\n??????:" + read);

                }
            } catch (Exception e) {
                LoggerUtil.logI(TAG, "eee  890--->" + CrashHandler.getInstance().printCrash(e));
            }


        }


    }
}
