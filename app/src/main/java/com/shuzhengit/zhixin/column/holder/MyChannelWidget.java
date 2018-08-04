//package com.shuzhengit.zhixin.column.holder;
//
//import android.graphics.Color;
//import android.support.v7.widget.RecyclerView;
//import android.util.TypedValue;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.shuzhengit.zhixin.R;
//import com.shuzhengit.zhixin.bean.Column;
//import com.shuzhengit.zhixin.column.holder.base.APPConst;
//import com.shuzhengit.zhixin.column.holder.base.EditModeHandler;
//import com.shuzhengit.zhixin.column.holder.base.IChannelType;
//
//
///**
// * Created by goach on 2016/9/28.
// * 我的频道控件
// */
//
//public class MyChannelWidget implements IChannelType {
//    private RecyclerView mRecyclerView;
//    private EditModeHandler editModeHandler;
//
//    public MyChannelWidget(EditModeHandler editModeHandler) {
//        this.editModeHandler = editModeHandler;
//    }
//
//    @Override
//    public ColumnAdapter2.ChannelViewHolder createViewHolder(LayoutInflater mInflater, ViewGroup parent) {
//        mRecyclerView = (RecyclerView) parent;
//        return new MyChannelHeaderViewHolder(mInflater.inflate(R.layout.activity_channel_my, parent, false));
//    }
//
//    @Override
//    public void bindViewHolder(final ColumnAdapter2.ChannelViewHolder holder, final int position, final Column data) {
//        final MyChannelHeaderViewHolder myHolder = (MyChannelHeaderViewHolder) holder;
//        myHolder.mChannelTitleTv.setText(data.getColumnTitle());
//        // 设置文字大小，通过判断tab中的文字长度，如果有4或者4个字以上则为16sp大小
//        int textSize = data.getColumnTitle().length() >= 4 ? 14 : 16;
//        myHolder.mChannelTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
//        // 通过channelbean的type值设置其文字背景颜色
//        int type = data.getModifiable();
//        // 设置文字背景颜色
////        myHolder.mChannelTitleTv.setBackgroundResource(type == Constant.ITEM_DEFAULT || type == APPConst.ITEM_UNEDIT ?
////                R.drawable.channel_fixed_bg_shape : R.drawable.channel_my_bg_shape);
//        //设置文字背景颜色
//        myHolder.mChannelTitleTv.setBackgroundResource(type == 0 ? R.drawable.shape_column_fixed_bg : R.drawable
//                .shape_column_bg);
//
//
//        // 根据tab状态设置文字颜色
//        myHolder.mChannelTitleTv.setTextColor(type == APPConst.ITEM_DEFAULT ? Color.RED :
//                type == APPConst.ITEM_UNEDIT ? Color.parseColor("#666666") : Color.parseColor("#333333"));
//        // 设置右上角删除按钮是否可见
//        // TODO: 2017/10/27 栏目的状态
////        myHolder.mDeleteIv.setVisibility(data.getEditStatus() == 1 ? View.VISIBLE : View.INVISIBLE);
//        myHolder.mChannelTitleTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (editModeHandler != null && data.getModifiable() == APPConst.ITEM_EDIT) {
//                    editModeHandler.clickMyChannel(mRecyclerView, holder);
//                }
//            }
//        });
//        myHolder.mChannelTitleTv.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (editModeHandler != null && data.getModifiable() == APPConst.ITEM_EDIT) {
//                    editModeHandler.touchMyChannel(motionEvent, holder);
//                }
//                return false;
//            }
//        });
//        myHolder.mChannelTitleTv.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                if (editModeHandler != null && data.getModifiable() == APPConst.ITEM_EDIT) {
//                    editModeHandler.clickLongMyChannel(mRecyclerView, holder);
//                }
//                return true;
//            }
//        });
//    }
//
//    public class MyChannelHeaderViewHolder extends ColumnAdapter2.ChannelViewHolder {
//        private TextView mChannelTitleTv;
//        private ImageView mDeleteIv;
//
//        private MyChannelHeaderViewHolder(View itemView) {
//            super(itemView);
//            mChannelTitleTv = (TextView) itemView.findViewById(R.id.id_channel_title);
//            mDeleteIv = (ImageView) itemView.findViewById(R.id.id_delete_icon);
//        }
//    }
//}
