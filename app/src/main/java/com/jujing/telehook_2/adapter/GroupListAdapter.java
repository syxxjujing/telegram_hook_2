package com.jujing.telehook_2.adapter;

import android.content.Context;

import com.jujing.telehook_2.R;
import com.jujing.telehook_2.adapter.common.CommonAdapter;
import com.jujing.telehook_2.adapter.common.ViewHolder;
import com.jujing.telehook_2.bean.ChatBean;

import org.telegram.tgnet.TLRPC;

import java.util.List;


public class GroupListAdapter extends CommonAdapter<ChatBean> {
    public GroupListAdapter(Context context, List<ChatBean> mList, int itemLayoutId) {
        super(context, mList, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder holder, ChatBean item, int positon) {
        holder.setTextViewText(R.id.tv_item, item.title);
    }
}
