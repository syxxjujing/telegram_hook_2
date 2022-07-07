package com.jujing.telehook_2.model.operate;

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

import com.jujing.telehook_2.Global;
import com.jujing.telehook_2.R;
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

public class SayHiSettingsActivity extends AppCompatActivity {
    private static final String TAG = "SayHiSettingsActivity";
    ListView lv_reply_1;
    LocalReplyAdapter adapter_1;
    List<LocalReplyBean> dataList_1 = new ArrayList<>();



    ListView lv_reply_2;
    LocalReplyAdapter adapter_2;
    List<LocalReplyBean> dataList_2 = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_say_hi_settings);
        lv_reply_1 = findViewById(R.id.lv_reply_1);
        Button btn_local_set_1 = findViewById(R.id.btn_local_set_1);
        btn_local_set_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withActivity(SayHiSettingsActivity.this)
                        .withRequestCode(1025)
                        .withStartPath(Environment.getExternalStorageDirectory().getPath())//指定初始显示路径
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
                AlertDialog.Builder builder = new AlertDialog.Builder(SayHiSettingsActivity.this);
                builder.setTitle("请选择第" + (position + 1) + "条的类型");
                String[] types = {"文字", "语音", "图片", "gif", "弹语音", "收藏", "视频"};
                builder.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            final EditText et = new EditText(SayHiSettingsActivity.this);
                            new AlertDialog.Builder(SayHiSettingsActivity.this).setTitle("输入第" + (position + 1) + "条文字内容")
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_1.set(position, new LocalReplyBean(input, dataList_1.get(position).getTime()));
                                            adapter_1.setDatas(dataList_1);
                                            saveReply_1();
                                        }
                                    }).setNegativeButton("取消", null)
                                    .show();
                        } else if (which == 1) {
                            final EditText et = new EditText(SayHiSettingsActivity.this);
                            new AlertDialog.Builder(SayHiSettingsActivity.this).setTitle("输入第" + (position + 1) + "条语音的本地路径")
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_1.set(position, new LocalReplyBean("语音:" + input, dataList_1.get(position).getTime()));
                                            adapter_1.setDatas(dataList_1);
                                            saveReply_1();
                                        }
                                    }).setNegativeButton("取消", null)
                                    .show();
                        } else if (which == 2) {
                            final EditText et = new EditText(SayHiSettingsActivity.this);
                            new AlertDialog.Builder(SayHiSettingsActivity.this).setTitle("输入第" + (position + 1) + "条图片的本地路径")
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_1.set(position, new LocalReplyBean("图片:" + input, dataList_1.get(position).getTime()));
                                            adapter_1.setDatas(dataList_1);
                                            saveReply_1();
                                        }
                                    }).setNegativeButton("取消", null)
                                    .show();
                        } else if (which == 3) {
                            final EditText et = new EditText(SayHiSettingsActivity.this);
                            new AlertDialog.Builder(SayHiSettingsActivity.this).setTitle("输入第" + (position + 1) + "条gif的本地路径")
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_1.set(position, new LocalReplyBean("gif:" + input, dataList_1.get(position).getTime()));
                                            adapter_1.setDatas(dataList_1);
                                            saveReply_1();
                                        }
                                    }).setNegativeButton("取消", null)
                                    .show();
                        } else if (which == 4) {
                            final EditText et = new EditText(SayHiSettingsActivity.this);
                            new AlertDialog.Builder(SayHiSettingsActivity.this).setTitle("输入第" + (position + 1) + "条弹语音的延迟时间(秒)")
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_1.set(position, new LocalReplyBean("弹语音:" + input, dataList_1.get(position).getTime()));
                                            adapter_1.setDatas(dataList_1);
                                            saveReply_1();
                                        }
                                    }).setNegativeButton("取消", null)
                                    .show();
                        } else if (which == 5) {
                            final EditText et = new EditText(SayHiSettingsActivity.this);
                            new AlertDialog.Builder(SayHiSettingsActivity.this).setTitle("输入收藏夹从下往上数第几条")
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_1.set(position, new LocalReplyBean("收藏:" + input, dataList_1.get(position).getTime()));
                                            adapter_1.setDatas(dataList_1);
                                            saveReply_1();
                                        }
                                    }).setNegativeButton("取消", null)
                                    .show();
                        } else if (which == 6) {
                            final EditText et = new EditText(SayHiSettingsActivity.this);
                            new AlertDialog.Builder(SayHiSettingsActivity.this).setTitle("输入第" + (position + 1) + "条视频的本地路径")
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_1.set(position, new LocalReplyBean("视频:" + input, dataList_1.get(position).getTime()));
                                            adapter_1.setDatas(dataList_1);
                                            saveReply_1();
                                        }
                                    }).setNegativeButton("取消", null)
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
                AlertDialog.Builder builder = new AlertDialog.Builder(SayHiSettingsActivity.this)
                        .setTitle("确定清除打招呼设置吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoggerUtil.logI(TAG, "确定清除打招呼设置吗   235--->");
                                dataList_1.clear();
                                for (int i = 0; i < 20; i++) {
                                    LocalReplyBean localReplyBean = new LocalReplyBean("", "");
                                    dataList_1.add(localReplyBean);
                                }
                                adapter_1.setDatas(dataList_1);
                                saveReply_1();
                                Toast.makeText(SayHiSettingsActivity.this, "清除成功！", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", null);
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
                LoggerUtil.logI(TAG, "SAY_HI_ROUND_INTERVAL  263--->" + trim);
                WriteFileUtil.write(trim, Global.SAY_HI_ROUND_INTERVAL);
            }
        });
        lv_reply_2 = findViewById(R.id.lv_reply_2);
        Button btn_local_set_2 = findViewById(R.id.btn_local_set_2);
        btn_local_set_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withActivity(SayHiSettingsActivity.this)
                        .withRequestCode(1026)
                        .withStartPath(Environment.getExternalStorageDirectory().getPath())//指定初始显示路径
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
                AlertDialog.Builder builder = new AlertDialog.Builder(SayHiSettingsActivity.this);
                builder.setTitle("请选择第" + (position + 1) + "条的类型");
                String[] types = {"文字", "语音", "图片", "gif", "弹语音", "收藏", "视频"};
                builder.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            final EditText et = new EditText(SayHiSettingsActivity.this);
                            new AlertDialog.Builder(SayHiSettingsActivity.this).setTitle("输入第" + (position + 1) + "条文字内容")
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_2.set(position, new LocalReplyBean(input, dataList_2.get(position).getTime()));
                                            adapter_2.setDatas(dataList_2);
                                            saveReply_2();
                                        }
                                    }).setNegativeButton("取消", null)
                                    .show();
                        } else if (which == 1) {
                            final EditText et = new EditText(SayHiSettingsActivity.this);
                            new AlertDialog.Builder(SayHiSettingsActivity.this).setTitle("输入第" + (position + 1) + "条语音的本地路径")
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_2.set(position, new LocalReplyBean("语音:" + input, dataList_2.get(position).getTime()));
                                            adapter_2.setDatas(dataList_2);
                                            saveReply_2();
                                        }
                                    }).setNegativeButton("取消", null)
                                    .show();
                        } else if (which == 2) {
                            final EditText et = new EditText(SayHiSettingsActivity.this);
                            new AlertDialog.Builder(SayHiSettingsActivity.this).setTitle("输入第" + (position + 1) + "条图片的本地路径")
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_2.set(position, new LocalReplyBean("图片:" + input, dataList_2.get(position).getTime()));
                                            adapter_2.setDatas(dataList_2);
                                            saveReply_2();
                                        }
                                    }).setNegativeButton("取消", null)
                                    .show();
                        } else if (which == 3) {
                            final EditText et = new EditText(SayHiSettingsActivity.this);
                            new AlertDialog.Builder(SayHiSettingsActivity.this).setTitle("输入第" + (position + 1) + "条gif的本地路径")
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_2.set(position, new LocalReplyBean("gif:" + input, dataList_2.get(position).getTime()));
                                            adapter_2.setDatas(dataList_2);
                                            saveReply_2();
                                        }
                                    }).setNegativeButton("取消", null)
                                    .show();
                        } else if (which == 4) {
                            final EditText et = new EditText(SayHiSettingsActivity.this);
                            new AlertDialog.Builder(SayHiSettingsActivity.this).setTitle("输入第" + (position + 1) + "条弹语音的延迟时间(秒)")
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_2.set(position, new LocalReplyBean("弹语音:" + input, dataList_2.get(position).getTime()));
                                            adapter_2.setDatas(dataList_2);
                                            saveReply_2();
                                        }
                                    }).setNegativeButton("取消", null)
                                    .show();
                        } else if (which == 5) {
                            final EditText et = new EditText(SayHiSettingsActivity.this);
                            new AlertDialog.Builder(SayHiSettingsActivity.this).setTitle("输入收藏夹从下往上数第几条")
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_2.set(position, new LocalReplyBean("收藏:" + input, dataList_2.get(position).getTime()));
                                            adapter_2.setDatas(dataList_2);
                                            saveReply_2();
                                        }
                                    }).setNegativeButton("取消", null)
                                    .show();
                        } else if (which == 6) {
                            final EditText et = new EditText(SayHiSettingsActivity.this);
                            new AlertDialog.Builder(SayHiSettingsActivity.this).setTitle("输入第" + (position + 1) + "条视频的本地路径")
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            dataList_2.set(position, new LocalReplyBean("视频:" + input, dataList_2.get(position).getTime()));
                                            adapter_2.setDatas(dataList_2);
                                            saveReply_2();
                                        }
                                    }).setNegativeButton("取消", null)
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
                AlertDialog.Builder builder = new AlertDialog.Builder(SayHiSettingsActivity.this)
                        .setTitle("确定清除打招呼设置吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoggerUtil.logI(TAG, "确定清除打招呼设置吗   424--->");
                                dataList_2.clear();
                                for (int i = 0; i < 20; i++) {
                                    LocalReplyBean localReplyBean = new LocalReplyBean("", "");
                                    dataList_2.add(localReplyBean);
                                }
                                adapter_2.setDatas(dataList_2);
                                saveReply_2();
                                Toast.makeText(SayHiSettingsActivity.this, "清除成功！", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.show();
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
                        Toast.makeText(this, "文件为空！", Toast.LENGTH_SHORT).show();
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
                                dataList_1.set(i, new LocalReplyBean("语音:" + split[2], dataList_1.get(i).getTime()));
                                adapter_1.setDatas(dataList_1);
                            } else if (s1.equals("tupian")) {
                                dataList_1.set(i, new LocalReplyBean("图片:" + split[2], dataList_1.get(i).getTime()));
                                adapter_1.setDatas(dataList_1);
                            } else if (s1.equals("gif")) {
                                dataList_1.set(i, new LocalReplyBean("gif:" + split[2], dataList_1.get(i).getTime()));
                                adapter_1.setDatas(dataList_1);
                            } else if (s1.equals("tanyuyin")) {
                                dataList_1.set(i, new LocalReplyBean("弹语音:" + split[2], dataList_1.get(i).getTime()));
                                adapter_1.setDatas(dataList_1);
                            } else if (s1.equals("shoucang")) {
                                dataList_1.set(i, new LocalReplyBean("收藏:" + split[2], dataList_1.get(i).getTime()));
                                adapter_1.setDatas(dataList_1);
                            } else if (s1.equals("shipin")) {
                                dataList_1.set(i, new LocalReplyBean("视频:" + split[2], dataList_1.get(i).getTime()));
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
                        Toast.makeText(this, "文件为空！", Toast.LENGTH_SHORT).show();
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
                                dataList_2.set(i, new LocalReplyBean("语音:" + split[2], dataList_2.get(i).getTime()));
                                adapter_2.setDatas(dataList_2);
                            } else if (s1.equals("tupian")) {
                                dataList_2.set(i, new LocalReplyBean("图片:" + split[2], dataList_2.get(i).getTime()));
                                adapter_2.setDatas(dataList_2);
                            } else if (s1.equals("gif")) {
                                dataList_2.set(i, new LocalReplyBean("gif:" + split[2], dataList_2.get(i).getTime()));
                                adapter_2.setDatas(dataList_2);
                            } else if (s1.equals("tanyuyin")) {
                                dataList_2.set(i, new LocalReplyBean("弹语音:" + split[2], dataList_2.get(i).getTime()));
                                adapter_2.setDatas(dataList_2);
                            } else if (s1.equals("shoucang")) {
                                dataList_2.set(i, new LocalReplyBean("收藏:" + split[2], dataList_2.get(i).getTime()));
                                adapter_2.setDatas(dataList_2);
                            } else if (s1.equals("shipin")) {
                                dataList_2.set(i, new LocalReplyBean("视频:" + split[2], dataList_2.get(i).getTime()));
                                adapter_2.setDatas(dataList_2);
                            }
                        } catch (Exception e) {

                        }

                    }
                    saveReply_2();


                }

            }
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "e  575---->" + CrashHandler.getInstance().printCrash(e));
            Toast.makeText(this, "有异常！", Toast.LENGTH_SHORT).show();
        }

    }
}
