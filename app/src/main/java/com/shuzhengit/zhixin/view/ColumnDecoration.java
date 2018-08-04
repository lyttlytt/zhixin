package com.shuzhengit.zhixin.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.library.util.DeviceUtil;
import com.shuzhengit.zhixin.APP;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/31 17:49
 * E-mail:yuancongbin@gmail.com
 */

public class ColumnDecoration extends RecyclerView.ItemDecoration{
    private int spacing;

    public ColumnDecoration(int spacing) {
//        this.spacing = spacing;
        this.spacing = DeviceUtil.dp2px(APP.getInstance(), spacing);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position >= 0) {
            outRect.left = spacing;
            outRect.right = spacing;
            outRect.top = spacing;
        } else {
            outRect.left = 0;
            outRect.right = 0;
            outRect.top = 0;
            outRect.bottom = 0;
        }
    }
}
