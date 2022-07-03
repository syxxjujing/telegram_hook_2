package com.jujing.telehook_2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

public class SayHiFirstActivity extends AppCompatActivity {
    private static final String TAG = "SayHiFirstActivity";
    ListView lv_reply;
    LocalReplyAdapter adapter;
    List<LocalReplyBean> dataList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_say_hi_first);
        lv_reply = findViewById(R.id.lv_reply);
        Button btn_local_set = findViewById(R.id.btn_local_set);
        btn_local_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withActivity(SayHiFirstActivity.this)
                        .withRequestCode(1025)
                        .withStartPath(Environment.getExternalStorageDirectory().getPath())//指定初始显示路径
                        .withMutilyMode(false)
                        .start();
            }
        });

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
                AlertDialog.Builder builder = new AlertDialog.Builder(SayHiFirstActivity.this);
                builder.setTitle("请选择第" + (position + 1) + "条的类型");
                String[] types = {"文字", "语音", "图片", "gif", "弹语音", "收藏", "视频"};
                builder.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            final EditText et = new EditText(SayHiFirstActivity.this);
                            new AlertDialog.Builder(SayHiFirstActivity.this).setTitle("输入第" + (position + 1) + "条文字内容")
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
                            final EditText et = new EditText(SayHiFirstActivity.this);
                            new AlertDialog.Builder(SayHiFirstActivity.this).setTitle("输入第" + (position + 1) + "条语音的本地路径")
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
                            final EditText et = new EditText(SayHiFirstActivity.this);
                            new AlertDialog.Builder(SayHiFirstActivity.this).setTitle("输入第" + (position + 1) + "条图片的本地路径")
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
                            final EditText et = new EditText(SayHiFirstActivity.this);
                            new AlertDialog.Builder(SayHiFirstActivity.this).setTitle("输入第" + (position + 1) + "条gif的本地路径")
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
                            final EditText et = new EditText(SayHiFirstActivity.this);
                            new AlertDialog.Builder(SayHiFirstActivity.this).setTitle("输入第" + (position + 1) + "条弹语音的延迟时间(秒)")
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
                            final EditText et = new EditText(SayHiFirstActivity.this);
                            new AlertDialog.Builder(SayHiFirstActivity.this).setTitle("输入收藏夹从下往上数第几条")
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
                            final EditText et = new EditText(SayHiFirstActivity.this);
                            new AlertDialog.Builder(SayHiFirstActivity.this).setTitle("输入第" + (position + 1) + "条视频的本地路径")
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
        Button btn_clear_dazhaohu = findViewById(R.id.btn_clear_dazhaohu);
        btn_clear_dazhaohu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SayHiFirstActivity.this)
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
                                Toast.makeText(SayHiFirstActivity.this, "清除成功！", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.show();
            }
        });
    }

    private void saveReply() {
        String s = JsonTool.paraseJson(dataList);
        LoggerUtil.logI(TAG, "sss  193---->" + s);
        WriteFileUtil.write(s, Global.STORAGE_LOCAL_REPLY_JSON);
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

            }
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "e  343---->" + CrashHandler.getInstance().printCrash(e));
            Toast.makeText(this, "有异常！", Toast.LENGTH_SHORT).show();
        }

    }
}
