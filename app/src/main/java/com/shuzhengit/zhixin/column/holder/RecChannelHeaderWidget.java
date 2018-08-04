//package com.shuzhengit.zhixin.column.holder;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.shuzhengit.zhixin.R;
//import com.shuzhengit.zhixin.bean.Column;
//import com.shuzhengit.zhixin.column.holder.base.IChannelType;
//
//
///**
// * Created by goach on 2016/9/28.
// * 推荐分类（文字界面）
// */
//
//public class RecChannelHeaderWidget implements IChannelType {
//    @Override
//    public ColumnAdapter2.ChannelViewHolder createViewHolder(LayoutInflater mInflater, ViewGroup parent) {
//        return new MyChannelHeaderViewHolder(mInflater.inflate(R.layout.activity_channel_rec_header, parent, false));
//    }
//
//    @Override
//    public void bindViewHolder(ColumnAdapter2.ChannelViewHolder holder, int position, Column data) {
//
//    }
//
//    public class MyChannelHeaderViewHolder extends ColumnAdapter2.ChannelViewHolder {
//
//        public MyChannelHeaderViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//}
