//package com.shuzhengit.zhixin.column.holder;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.shuzhengit.zhixin.R;
//import com.shuzhengit.zhixin.bean.Column;
//import com.shuzhengit.zhixin.column.holder.base.EditModeHandler;
//import com.shuzhengit.zhixin.column.holder.base.IChannelType;
//
//
///**
// * Created by goach on 2016/9/28.
// * 我的频道文字控件，右侧有编辑/完成按键
// */
//
//public class MyChannelHeaderWidget implements IChannelType {
//    private RecyclerView mRecyclerView;
//    private EditModeHandler editModeHandler;
//
//    public MyChannelHeaderWidget(EditModeHandler handler) {
//        this.editModeHandler = handler;
//    }
//
//    @Override
//    public ColumnAdapter2.ChannelViewHolder createViewHolder(LayoutInflater mInflater, ViewGroup parent) {
//        mRecyclerView = (RecyclerView) parent;
//        return new MyChannelHeaderViewHolder(mInflater.inflate(R.layout.activity_channel_my_header, parent, false));
//    }
//
//    @Override
//    public void bindViewHolder(final ColumnAdapter2.ChannelViewHolder holder, int position, Column data) {
//        final MyChannelHeaderViewHolder viewHolder = (MyChannelHeaderViewHolder) holder;
//        // 右侧按键点击时改变样式，如编辑-》完成
//        viewHolder.mEditModeTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!viewHolder.mEditModeTv.isSelected()) {
//
//                    if (editModeHandler != null) {
//                        editModeHandler.startEditMode(mRecyclerView);
//                    }
//                    viewHolder.mEditModeTv.setText("完成");
//                } else {
//                    if (editModeHandler != null) {
//                        editModeHandler.cancelEditMode(mRecyclerView);
//                    }
//                    viewHolder.mEditModeTv.setText("编辑");
//                }
//                viewHolder.mEditModeTv.setSelected(!viewHolder.mEditModeTv.isSelected());
//            }
//        });
//    }
//
//    public class MyChannelHeaderViewHolder extends ColumnAdapter2.ChannelViewHolder {
//        private TextView mEditModeTv;
//
//        public MyChannelHeaderViewHolder(View itemView) {
//            super(itemView);
//            mEditModeTv = (TextView) itemView.findViewById(R.id.id_edit_mode);
//        }
//    }
//}
