package com.shuzhengit.zhixin.view.pull2refreshview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/24 09:03
 * E-mail:yuancongbin@gmail.com
 */

public class PullToRefreshRecyclerView extends RecyclerView implements LoadMoreFootView.OnLoadFail {

    public boolean isShowPullToRefresh = true;
    public boolean isShowLoadMore = true;
    private RefreshHeaderView mRefreshHeaderView;
    private WrapAdapter mWrapAdapter;
    private static final int TYPE_REFRESH_HEADER = 10086;
    private static final int TYPE_LOAD_MORE = 10087;
    private final RecyclerView.AdapterDataObserver mDataObserver = new DataObserver();
    private View mEmptyView;
    private LoadMoreListener mLoadMoreListener = null;
    private RefreshListener mRefreshListener = null;
    private boolean isLoadingData = false;
    private boolean isNoMore = false;
    private float mLastY = -1;
    private static final int DRAG_RATE = 3;
    private AppBarStateChangeListener.State appbarState = AppBarStateChangeListener.State.EXPANDED;
    private View mLoadingMoreView;
    public PullToRefreshRecyclerView(Context context) {
        this(context, null);
    }

    public PullToRefreshRecyclerView(Context context,  AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshRecyclerView(Context context,  AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public boolean isShowPullToRefresh() {
        return isShowPullToRefresh;
    }

    public void isShowPullToRefresh(boolean enable){
        this.isShowPullToRefresh = enable;
    }
    private void init(Context context) {
        if (isShowPullToRefresh) {
            mRefreshHeaderView = new RefreshHeaderView(context);
        }else {
            if (mRefreshHeaderView!=null){
                mRefreshHeaderView.setVisibility(GONE);
            }
        }
        LoadMoreFootView loadMoreFootView = new LoadMoreFootView(context);
        mLoadingMoreView = loadMoreFootView;
        mLoadingMoreView.setVisibility(GONE);
        loadMoreFootView.setOnLoadFail(this);
//        if (isShowLoadMore) {
//            mLoadMoreView = new LoadMoreView(context);
//            mLoadMoreView.setVisibility(GONE);
//            mMoreView = mLoadMoreView;
//            mMoreView.setVisibility(GONE);
//        }
    }

    //判断是否是refreshHeaderViewHolder/loadMoreViewHolder的Type
    private boolean isReservedItemViewType(int itemViewType) {
        if (itemViewType == TYPE_REFRESH_HEADER || itemViewType == TYPE_LOAD_MORE) {
            return true;
        } else {
            return false;
        }
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        mRefreshListener = refreshListener;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        mDataObserver.onChanged();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

    //防止用户自己调用getAdapter() 引起ClassCastException
    @Override
    public Adapter getAdapter() {
        if (mWrapAdapter != null) {
            return mWrapAdapter.getOriginalAdapter();
        } else {
            return null;
        }
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (mWrapAdapter != null) {
            if (layout instanceof GridLayoutManager) {
                final GridLayoutManager gridLayoutManager = ((GridLayoutManager) layout);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (mWrapAdapter.isRefreshHeader(position) || mWrapAdapter.isLoadMore(position)) ?
                                gridLayoutManager.getSpanCount() : 1;
//                        return (mWrapAdapter.isRefreshHeader(position)) ?
//                                gridLayoutManager
//                                        .getSpanCount() : 1;
                    }
                });
            }
        }
    }

    @Override
    public void resetLoad() {
        ((LoadMoreFootView)mLoadingMoreView).setState(LoadMoreFootView.STATE_LOADING);
        mLoadMoreListener.onLoadMore();
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }

    public interface RefreshListener {
        void onRefresh();
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadMoreListener != null && !isLoadingData && isShowLoadMore) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (layoutManager.getChildCount() > 0 && lastVisibleItemPosition >= layoutManager.getItemCount()
                    - 1 && layoutManager.getItemCount() > layoutManager.getChildCount() && !isNoMore &&
                    mRefreshHeaderView
                            .getState() < RefreshHeaderState.STATE_REFRESHING &&((LoadMoreFootView)mLoadingMoreView)
                    .getCurrentStatus()!=LoadMoreFootView.STATE_LOADING) {
                isLoadingData = true;

                if (mLoadingMoreView instanceof LoadMoreFootView) {
                    ((LoadMoreFootView) mLoadingMoreView).setState(LoadMoreFootView.STATE_LOADING);
                }
//                else {
//                    mLoadingMoreView.setVisibility(VISIBLE);
//                }
                mLoadMoreListener.onLoadMore();
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mLastY == -1) {
            mLastY = e.getRawY();
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = e.getRawY() - mLastY;
                mLastY = e.getRawY();
                if (isOnTop() && isShowPullToRefresh && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    mRefreshHeaderView.onMove(deltaY / DRAG_RATE);
                    if (mRefreshHeaderView.getVisibilityHeight() > 0 && mRefreshHeaderView.getState() <
                            RefreshHeaderView.STATE_REFRESHING) {
                        return false;
                    }
                }
                break;
            default:
                mLastY = -1;
                if (isOnTop() && isShowPullToRefresh && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    if (mRefreshHeaderView.releaseAction()) {
                        if (mRefreshListener != null) {
                            mRefreshListener.onRefresh();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(e);
    }

    private boolean isOnTop() {
        return mRefreshHeaderView.getParent() != null;
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private class WrapAdapter extends RecyclerView.Adapter<ViewHolder> {
        private RecyclerView.Adapter mAdapter;

        public WrapAdapter(RecyclerView.Adapter adapter) {
            mAdapter = adapter;
        }

        public Adapter getOriginalAdapter() {
            return mAdapter;
        }

        public boolean isRefreshHeader(int position) {
            return position == 0;
        }
//
        public boolean isLoadMore(int position) {
            return isShowLoadMore && position == getItemCount() - 1;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_REFRESH_HEADER) {
                return new RefreshViewHolder(mRefreshHeaderView);
            } else if (viewType == TYPE_LOAD_MORE) {
                return new LoadMoreViewHolder(mLoadingMoreView);
            }
            return mAdapter.onCreateViewHolder(parent, viewType);
        }

        public int getRefreshHeaderCount() {
            return 1;
        }

        public int getLoadMoreCount() {
            return 1;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (isRefreshHeader(position)) {
                return;
            }
            //调整默认adapter的position 从0开始
            int adjustPosition = position - getRefreshHeaderCount();
            int itemCount;
            if (mAdapter != null) {
                itemCount = mAdapter.getItemCount();
                if (adjustPosition < itemCount) {
                    mAdapter.onBindViewHolder(holder, adjustPosition);
                }
            }
        }

        //需要重写这个
        @Override
        public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
            if (isRefreshHeader(position)) {
                return;
            }
            //调整默认adapter的position 从0开始
            int adjustPosition = position - (getRefreshHeaderCount());
            int itemCount;
            if (mAdapter != null) {
                itemCount = mAdapter.getItemCount();
                if (adjustPosition < itemCount) {
                    if (payloads.isEmpty()) {
                        mAdapter.onBindViewHolder(holder, adjustPosition);
                    } else {
                        mAdapter.onBindViewHolder(holder, adjustPosition, payloads);
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            if (isShowLoadMore) {
                if (mAdapter != null) {
                    return mAdapter.getItemCount() + getRefreshHeaderCount() + getLoadMoreCount();
                } else {
                    return getRefreshHeaderCount() + getLoadMoreCount();
                }
            } else {
                if (mAdapter != null) {
                    return mAdapter.getItemCount() + getRefreshHeaderCount();
                } else {
                    return getRefreshHeaderCount();
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            int adjustPosition = position - (getRefreshHeaderCount());
            if (isRefreshHeader(position)) {
                return TYPE_REFRESH_HEADER;
            }
            if (isLoadMore(position)) {
                return TYPE_LOAD_MORE;
            }
            if (mAdapter != null) {
                int itemCount = mAdapter.getItemCount();
                if (adjustPosition < itemCount) {
                    int itemViewType = mAdapter.getItemViewType(adjustPosition);
                    if (isReservedItemViewType(itemViewType)) {
                        throw new IllegalStateException("PullToRefreshRecyclerView require itemViewType must can not " +
                                "different the refreshHeaderViewHolder/loadMoreViewHolder  . refreshHeaderViewHolder " +
                                "type is " +
                                TYPE_REFRESH_HEADER + "/loadMoreViewHolder type is : " + TYPE_LOAD_MORE +
                                "itemViewType的类型不能和refreshHeader/loadMoreViewHolder的类型相同,refreshHeader的类型是 : " +
                                TYPE_REFRESH_HEADER + " loadMoreViewHolder的类型是 : " + TYPE_LOAD_MORE);
//                        throw new IllegalStateException("XRecyclerView require itemViewType in adapter should be
// less" +
//                                " than 10086 ");
                    }
                    return itemViewType;
                }
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            if (mAdapter != null && position >= getRefreshHeaderCount()) {
                int adjustPosition = position - getRefreshHeaderCount();
                if (adjustPosition < mAdapter.getItemCount()) {
                    return mAdapter.getItemId(adjustPosition);
                }
            }
            return -1;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isRefreshHeader(position) || isLoadMore(position)) ? gridLayoutManager.getSpanCount()
                                : 1;
//                        return isRefreshHeader(position)  ? gridLayoutManager.getSpanCount()
//                                : 1;
                    }
                });
            }
            mAdapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            mAdapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams &&
                    (isRefreshHeader(holder.getLayoutPosition()) || isLoadMore(holder.getLayoutPosition()))) {
//                    (isRefreshHeader(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                params.setFullSpan(true);
            }
            mAdapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
            mAdapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            mAdapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
            return mAdapter.onFailedToRecycleView(holder);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            mAdapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            mAdapter.registerAdapterDataObserver(observer);
        }

        private class RefreshViewHolder extends RecyclerView.ViewHolder {
            public RefreshViewHolder(View itemView) {
                super(itemView);
            }
        }

        private class LoadMoreViewHolder extends RecyclerView.ViewHolder {
            public LoadMoreViewHolder(View itemView) {
                super(itemView);
            }
        }
    }


    public void loadMoreCompleted() {
        isLoadingData = false;
        if (mLoadingMoreView instanceof LoadMoreFootView){
            ((LoadMoreFootView)mLoadingMoreView).setState(LoadMoreFootView.STATE_COMPLETED);
        }
    }

    /**
     * 以设置了点击事件,点击重新执行loading方法
     */
    public void loadMoreFail(){
        isLoadingData = false;
        if (mLoadingMoreView instanceof LoadMoreFootView){
            ((LoadMoreFootView)mLoadingMoreView).setState(LoadMoreFootView.STATE_FAIL);
        }
    }
    public void setNoMore(boolean isNoMore) {
        isLoadingData = false;
        this.isNoMore = isNoMore;
        if (mLoadingMoreView instanceof LoadMoreFootView) {
            ((LoadMoreFootView)mLoadingMoreView).setState(isNoMore ? LoadMoreFootView.STATE_NO_MORE : LoadMoreFootView.STATE_COMPLETED);
        }
    }

    public void refresh() {
        if (isShowPullToRefresh) {
            mRefreshHeaderView.setState(RefreshHeaderView.STATE_REFRESHING);
            if (mRefreshListener != null) {
                mRefreshListener.onRefresh();
            }
        }
    }

    public void reset() {
        setNoMore(false);
        loadMoreCompleted();
        refreshCompleted();
    }

    public void refreshCompleted() {
        mRefreshHeaderView.refreshCompleted();
        setNoMore(false);
    }

    public void setRefreshHeader(RefreshHeaderView refreshHeader) {
        this.mRefreshHeaderView = refreshHeader;
    }

    public void setShowPullToRefresh(boolean showPullToRefresh) {
        isShowPullToRefresh = showPullToRefresh;
    }

    public void setShowLoadMore(boolean showLoadMore) {
        isShowLoadMore = showLoadMore;
        if (!showLoadMore) {
            if (mLoadingMoreView instanceof LoadMoreFootView) {
                ((LoadMoreFootView)mLoadingMoreView).setState(LoadMoreFootView.STATE_COMPLETED);
            }
        }
    }

    private class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
            if (mWrapAdapter != null && mEmptyView != null) {
                int emptyCount = mWrapAdapter.getRefreshHeaderCount();
                if (isShowLoadMore) {
                    emptyCount++;
                }
                if (mWrapAdapter.getItemCount() == emptyCount) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    PullToRefreshRecyclerView.this.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    PullToRefreshRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //解决和CollapsingToolbarLayout冲突的问题
        AppBarLayout appBarLayout = null;
        ViewParent p = getParent();
        while (p != null) {
            if (p instanceof CoordinatorLayout) {
                break;
            }
            p = p.getParent();
        }
        if (p instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) p;
            int childCount = coordinatorLayout.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                View child = coordinatorLayout.getChildAt(i);
                if (child instanceof AppBarLayout) {
                    appBarLayout = (AppBarLayout) child;
                    break;
                }
            }
            if (appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                    @Override
                    public void onStateChanged(AppBarLayout appBarLayout, State state) {
                        appbarState = state;
                    }
                });
            }
        }
    }

    public class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private Drawable mDivider;
        private int mOrientation;

        /**
         * Sole constructor. Takes in a {@link Drawable} to be used as the interior
         * divider.
         *
         * @param divider A divider {@code Drawable} to be drawn on the RecyclerView
         */
        public DividerItemDecoration(Drawable divider) {
            mDivider = divider;
        }

        /**
         * Draws horizontal or vertical dividers onto the parent RecyclerView.
         *
         * @param canvas The {@link Canvas} onto which dividers will be drawn
         * @param parent The RecyclerView onto which dividers are being added
         * @param state  The current RecyclerView.State of the RecyclerView
         */
        @Override
        public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            if (mOrientation == LinearLayoutManager.HORIZONTAL) {
                drawHorizontalDividers(canvas, parent);
            } else if (mOrientation == LinearLayoutManager.VERTICAL) {
                drawVerticalDividers(canvas, parent);
            }
        }

        /**
         * Determines the size and location of offsets between items in the parent
         * RecyclerView.
         *
         * @param outRect The {@link Rect} of offsets to be added around the child
         *                view
         * @param view    The child view to be decorated with an offset
         * @param parent  The RecyclerView onto which dividers are being added
         * @param state   The current RecyclerView.State of the RecyclerView
         */
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            if (parent.getChildAdapterPosition(view) <= mWrapAdapter.getRefreshHeaderCount()) {
                return;
            }
            mOrientation = ((LinearLayoutManager) parent.getLayoutManager()).getOrientation();
            if (mOrientation == LinearLayoutManager.HORIZONTAL) {
                outRect.left = mDivider.getIntrinsicWidth();
            } else if (mOrientation == LinearLayoutManager.VERTICAL) {
                outRect.top = mDivider.getIntrinsicHeight();
            }
        }

        /**
         * Adds dividers to a RecyclerView with a LinearLayoutManager or its
         * subclass oriented horizontally.
         *
         * @param canvas The {@link Canvas} onto which horizontal dividers will be
         *               drawn
         * @param parent The RecyclerView onto which horizontal dividers are being
         *               added
         */
        private void drawHorizontalDividers(Canvas canvas, RecyclerView parent) {
            int parentTop = parent.getPaddingTop();
            int parentBottom = parent.getHeight() - parent.getPaddingBottom();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int parentLeft = child.getRight() + params.rightMargin;
                int parentRight = parentLeft + mDivider.getIntrinsicWidth();

                mDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom);
                mDivider.draw(canvas);
            }
        }

        /**
         * Adds dividers to a RecyclerView with a LinearLayoutManager or its
         * subclass oriented vertically.
         *
         * @param canvas The {@link Canvas} onto which vertical dividers will be
         *               drawn
         * @param parent The RecyclerView onto which vertical dividers are being
         *               added
         */
        private void drawVerticalDividers(Canvas canvas, RecyclerView parent) {
            int parentLeft = parent.getPaddingLeft();
            int parentRight = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
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
}
