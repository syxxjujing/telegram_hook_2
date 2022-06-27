package com.jujing.telehook_2.adapter;

import android.content.Context;


import com.jujing.telehook_2.R;
import com.jujing.telehook_2.adapter.common.CommonAdapter;
import com.jujing.telehook_2.adapter.common.ViewHolder;
import com.jujing.telehook_2.bean.LocalReplyBean;

import java.util.List;

public class LocalReplyAdapter extends CommonAdapter<LocalReplyBean> {
    public LocalReplyAdapter(Context context, List<LocalReplyBean> mList, int itemLayoutId) {
        super(context, mList, itemLayoutId);

    }

    @Override
    public void convert(ViewHolder holder, LocalReplyBean item, int positon) {
        String content = "";
//        String time = "";
        if (item.getContent().equals("")){
            content = "第"+(positon+1)+"条未设置";
        }else{
            content = item.getContent();
        }
//        if (item.getTime().equals("")){
//            time = "延时未设置";
//        }else{
//            time = "延时:"+item.getTime()+"秒";
//        }

        holder.setTextViewText(R.id.tv_item, content);
//        holder.setTextViewText(R.id.tv_item, content);

    }
}
