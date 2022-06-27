package com.jujing.telehook_2.adapter.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 创建人: Fu
 * <p/>
 * 创建时间: 2016/11/10  19:13
 * <p/>
 * 类说明:
 */
public class ListViewForScrollView extends ListView {

    public ListViewForScrollView(Context context) {
        super(context);
    }
    public ListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ListViewForScrollView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
