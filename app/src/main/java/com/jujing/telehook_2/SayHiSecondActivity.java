package com.jujing.telehook_2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jujing.telehook_2.adapter.LocalReplyAdapter;
import com.jujing.telehook_2.bean.LocalReplyBean;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.JsonTool;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.WriteFileUtil;
import com.leon.lfilepickerlibrary.LFilePicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SayHiSecondActivity extends AppCompatActivity {
    private static final String TAG = "SayHiSecondActivity";
    ListView lv_reply;
    LocalReplyAdapter adapter;
    List<LocalReplyBean> dataList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_say_hi_second);

        CheckBox cb_only_unread = findViewById(R.id.cb_only_unread);
        String is_only_unread = WriteFileUtil.read(Global.IS_ONLY_UNREAD);
        LoggerUtil.logI(TAG, "is_only_unread   46--->" + is_only_unread);
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
                LoggerUtil.logI(TAG, "IS_ONLY_UNREAD   59--->" + b);
                WriteFileUtil.write(b + "", Global.IS_ONLY_UNREAD);
            }
        });
        EditText et_interval = findViewById(R.id.et_interval);
        et_interval.setText(WriteFileUtil.read(Global.SAY_HI_ROUND_INTERVAL));
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
                LoggerUtil.logI(TAG, "SAY_HI_ROUND_INTERVAL  81--->" + trim);
                WriteFileUtil.write(trim, Global.SAY_HI_ROUND_INTERVAL);
            }
        });


        lv_reply = findViewById(R.id.lv_reply);
        Button btn_local_set = findViewById(R.id.btn_local_set);
        btn_local_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withActivity(SayHiSecondActivity.this)
                        .withRequestCode(1025)
                        .withStartPath(Environment.getExternalStorageDirectory().getPath())//????????????????????????
                        .withMutilyMode(false)
                        .start();
            }
        });

        adapter = new LocalReplyAdapter(this, dataList, R.layout.item_local_reply);
        lv_reply.setAdapter(adapter);
        try {
            String replyJson = WriteFileUtil.read(Global.STORAGE_LOCAL_REPLY_JSON_2);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(SayHiSecondActivity.this);
                builder.setTitle("????????????" + (position + 1) + "????????????");
                String[] types = {"??????", "??????", "??????", "gif", "?????????", "??????", "??????"};
                builder.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            final EditText et = new EditText(SayHiSecondActivity.this);
                            new AlertDialog.Builder(SayHiSecondActivity.this).setTitle("?????????" + (position + 1) + "???????????????")
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
                            final EditText et = new EditText(SayHiSecondActivity.this);
                            new AlertDialog.Builder(SayHiSecondActivity.this).setTitle("?????????" + (position + 1) + "????????????????????????")
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
                            final EditText et = new EditText(SayHiSecondActivity.this);
                            new AlertDialog.Builder(SayHiSecondActivity.this).setTitle("?????????" + (position + 1) + "????????????????????????")
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
                            final EditText et = new EditText(SayHiSecondActivity.this);
                            new AlertDialog.Builder(SayHiSecondActivity.this).setTitle("?????????" + (position + 1) + "???gif???????????????")
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
                            final EditText et = new EditText(SayHiSecondActivity.this);
                            new AlertDialog.Builder(SayHiSecondActivity.this).setTitle("?????????" + (position + 1) + "???????????????????????????(???)")
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
                            final EditText et = new EditText(SayHiSecondActivity.this);
                            new AlertDialog.Builder(SayHiSecondActivity.this).setTitle("???????????????????????????????????????")
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
                            final EditText et = new EditText(SayHiSecondActivity.this);
                            new AlertDialog.Builder(SayHiSecondActivity.this).setTitle("?????????" + (position + 1) + "????????????????????????")
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
        Button btn_clear_dazhaohu = findViewById(R.id.btn_clear_dazhaohu);
        btn_clear_dazhaohu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SayHiSecondActivity.this)
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
                                Toast.makeText(SayHiSecondActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("??????", null);
                builder.show();
            }
        });
    }

    private void saveReply() {
        String s = JsonTool.paraseJson(dataList);
        LoggerUtil.logI(TAG, "sss  193---->" + s);
        WriteFileUtil.write(s, Global.STORAGE_LOCAL_REPLY_JSON_2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
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
                                dataList.set(i, new LocalReplyBean(txt, dataList.get(i).getTime()));
                                adapter.setDatas(dataList);
                            } else if (s1.equals("yuyin")) {
                                dataList.set(i, new LocalReplyBean("??????:" + split[2], dataList.get(i).getTime()));
                                adapter.setDatas(dataList);
                            } else if (s1.equals("tupian")) {
                                dataList.set(i, new LocalReplyBean("??????:" + split[2], dataList.get(i).getTime()));
                                adapter.setDatas(dataList);
                            } else if (s1.equals("gif")) {
                                dataList.set(i, new LocalReplyBean("gif:" + split[2], dataList.get(i).getTime()));
                                adapter.setDatas(dataList);
                            } else if (s1.equals("tanyuyin")) {
                                dataList.set(i, new LocalReplyBean("?????????:" + split[2], dataList.get(i).getTime()));
                                adapter.setDatas(dataList);
                            } else if (s1.equals("shoucang")) {
                                dataList.set(i, new LocalReplyBean("??????:" + split[2], dataList.get(i).getTime()));
                                adapter.setDatas(dataList);
                            } else if (s1.equals("shipin")) {
                                dataList.set(i, new LocalReplyBean("??????:" + split[2], dataList.get(i).getTime()));
                                adapter.setDatas(dataList);
                            }
                        } catch (Exception e) {

                        }

                    }
                    saveReply();


                }

            }
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "e  343---->" + CrashHandler.getInstance().printCrash(e));
            Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
        }

    }
}
