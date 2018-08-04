//package com.shuzhengit.zhixin.column.holder;
//
//import android.support.v7.widget.RecyclerView;
//import android.util.TypedValue;
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
// * 更多频道控件
// */
//
//public class RecChannelWidget implements IChannelType {
//    private EditModeHandler editModeHandler;
//    private RecyclerView mRecyclerView;
//
//    public RecChannelWidget(EditModeHandler editModeHandler) {
//        this.editModeHandler = editModeHandler;
//    }
//
//    @Override
//    public ColumnAdapter2.ChannelViewHolder createViewHolder(LayoutInflater mInflater, ViewGroup parent) {
//        this.mRecyclerView = (RecyclerView) parent;
//        return new RecChannelHeaderViewHolder(mInflater.inflate(R.layout.activity_channel_rec, parent, false));
//    }
//
//    @Override
//    public void bindViewHolder(final ColumnAdapter2.ChannelViewHolder holder, int position, Column data) {
//        RecChannelHeaderViewHolder recHolder = (RecChannelHeaderViewHolder) holder;
//        recHolder.mChannelTitleTv.setText(data.getColumnTitle());
//        int textSize = data.getColumnTitle().length() >= 4 ? 14 : 16;
//        recHolder.mChannelTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
//        recHolder.mChannelTitleTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (editModeHandler != null) {
//                    editModeHandler.clickRecChannel(mRecyclerView, holder);
//                }
//            }
//        });
//    }
//
//    private class RecChannelHeaderViewHolder extends ColumnAdapter2.ChannelViewHolder {
//        private TextView mChannelTitleTv;
//
//        private RecChannelHeaderViewHolder(View itemView) {
//            super(itemView);
//            mChannelTitleTv = (TextView) itemView.findViewById(R.id.id_channel_title);
//        }
//    }
//}
