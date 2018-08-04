//package com.shuzhengit.zhixin.util;
//
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.StaggeredGridLayoutManager;
//import android.support.v7.widget.helper.ItemTouchHelper;
//
//
///**
// * Created by 江苏镇江树正科技 .
// * Author：袁从斌 on 2017/7/31 13:55
// * E-mail:yuancongbin@gmail.com
// */
//
//public class ChannelDragHelper extends ItemTouchHelper.Callback{
//    private ColumnDragListener mDragListener;
//
//    public ChannelDragHelper(ColumnDragListener dragListener) {
//        mDragListener = dragListener;
//    }
//
//    @Override
//    public boolean isLongPressDragEnabled() {
//        return false;
//    }
//
//    @Override
//    public boolean isItemViewSwipeEnabled() {
//        return false;
//    }
//
//    @Override
//    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//        int dragFlags;
//        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//        if (layoutManager instanceof GridLayoutManager || layoutManager instanceof StaggeredGridLayoutManager){
//            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN|ItemTouchHelper.START|ItemTouchHelper.END;
//        }else {
//            dragFlags = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
//        }
//        int swipeFlags = 0;
//        return makeMovementFlags(dragFlags,swipeFlags);
//    }
//
//    @Override
//    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
//            target) {
//        mDragListener.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
//        return true;
//    }
//
//    @Override
//    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//        mDragListener.onItemSwiped(viewHolder.getAdapterPosition());
//    }
//
//    @Override
//    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
//        if (actionState!=ItemTouchHelper.ACTION_STATE_IDLE){
//            ItemDragViewHolderListener itemViewHolder = (ItemDragViewHolderListener) viewHolder;
//            itemViewHolder.onItemSelected();
//        }
//        super.onSelectedChanged(viewHolder, actionState);
//    }
//
//    @Override
//    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//        super.clearView(recyclerView, viewHolder);
//        ItemDragViewHolderListener itemViewHolder = (ItemDragViewHolderListener) viewHolder;
//        itemViewHolder.onItemFinished();
//    }
//    interface ColumnDragListener{
//        void onItemMove(int fromPosition,int toPosition);
//        void onItemSwiped(int position);
//    }
//}
