package com.shuzhengit.zhixin.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.util.ResourceUtils;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/25 09:49
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class DynamicDividerItemDecoration extends RecyclerView.ItemDecoration {

    private final Drawable mDivider;
    private int mOrientation;
    private boolean mShowPullToRefresh = false;

    public DynamicDividerItemDecoration() {
        mDivider = ResourceUtils.getResourceDrawable(APP.getInstance(), R.drawable
                .recycler_view_fifteen_divider_bg);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        mOrientation = ((LinearLayoutManager) parent.getLayoutManager()).getOrientation();
        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            outRect.left = mDivider.getIntrinsicWidth();
        } else if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.top = mDivider.getIntrinsicHeight();
        }
        if (parent instanceof PullToRefreshRecyclerView){
            mShowPullToRefresh = ((PullToRefreshRecyclerView) parent).isShowPullToRefresh();
        }
//        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0,0,0,mDivider.getIntrinsicHeight());
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
//        super.onDraw(c, parent, state);
        int parentLeft = parent.getPaddingLeft();
        int parentRight = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        if (childCount == 0) {
            return;
        }
        for (int i = 0; i < childCount - 1; i++) {

            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int parentTop = child.getBottom() + params.bottomMargin;
            int parentBottom = parentTop + mDivider.getIntrinsicHeight();

            mDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom);
            mDivider.draw(canvas);
        }
    }
}
