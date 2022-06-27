package com.jujing.telehook_2;


import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.jujing.telehook_2.adapter.GroupListAdapter;
import com.jujing.telehook_2.bean.ChatBean;
import com.jujing.telehook_2.bean.LocalReplyBean;
import com.jujing.telehook_2.model.operate.AddAndInviteGroupAction;
import com.jujing.telehook_2.util.ExecutorUtil;
import com.jujing.telehook_2.util.WriteFileUtil;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class GroupListActivity extends AppCompatActivity {
    List<ChatBean> groupIdList = new ArrayList<>();
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        ListView lv = findViewById(R.id.lv);
        Toast.makeText(this, "正在获取群聊。。。", Toast.LENGTH_SHORT).show();
        ExecutorUtil.doExecute(new Runnable() {
            @Override
            public void run() {
                try {
                    SystemClock.sleep(2000);
                    String json = WriteFileUtil.read(Global.GROUP_LIST_JSON);
                    JSONArray jsonArray = new JSONArray(json);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                        long chatId = jsonObject.getLong("chatId");
                        String title = jsonObject.getString("title");
                        ChatBean bean = new ChatBean();
                        bean.setChatId(chatId);
                        bean.setTitle(title);
                        groupIdList.add(bean);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GroupListAdapter adapter = new GroupListAdapter(GroupListActivity.this, groupIdList, R.layout.item_local_reply);
                            lv.setAdapter(adapter);
                        }
                    });

                } catch (Exception e) {

                }
            }
        });




        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                TLRPC.Chat chat = groupIdList.get(i);
//                Toast.makeText(GroupListActivity.this, "开始添加好友，群："+chat.title, Toast.LENGTH_SHORT).show();

                long chatId = groupIdList.get(i).getChatId();
                Intent intent = new Intent();
                intent.putExtra("chatId",chatId);
                intent.setAction(HookMain.ACTION_XTELE_GROUP_START);
                sendBroadcast(intent);

                finish();

//                AddAndInviteGroupAction.handle(chat.id);

            }
        });
    }
}
