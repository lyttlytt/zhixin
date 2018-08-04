package com.shuzhengit.zhixin.column.holder.base;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

import com.shuzhengit.zhixin.column.holder.ColumnAdapter2;

/**
 * Created by 钟光新 on 2016/10/15 0015.
 */

public abstract class EditModeHandler {
    public void startEditMode(RecyclerView mRecyclerView) {
    }

    public void cancelEditMode(RecyclerView mRecyclerView) {
    }

    public void clickMyChannel(RecyclerView mRecyclerView, ColumnAdapter2.ChannelViewHolder holder,boolean isEdit) {
    }

    public void clickLongMyChannel(RecyclerView mRecyclerView, ColumnAdapter2.ChannelViewHolder holder) {
    }

    public void touchMyChannel(MotionEvent motionEvent, ColumnAdapter2.ChannelViewHolder holder) {
    }

    public void clickRecChannel(RecyclerView mRecyclerView, ColumnAdapter2.ChannelViewHolder holder) {
    }
}
