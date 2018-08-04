package com.shuzhengit.zhixin.column.holder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.Column;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/10/17 16:28
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class ColumnAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //我的栏目头部
    public static final int ITEM_COLUMN_HEADER = 0;
    //我的栏目
    public static final int ITEM_MY_FOLLOW_COLUMN = 1;
    //推荐栏目头部
    public static final int ITEM_RECOMMEND_COLUMN_HEADER = 2;
    //推荐栏目
    public static final int ITEM_RECOMMEND_COLUMN = 3;

    private List<Column> mMyColumns = new ArrayList<>();
    private List<Column> mRecommendColumns = new ArrayList<>();
    public static int ITEM_SPACE = 5;

    private Context mContext;

    private boolean mOpenEditMode = false;
    private boolean isModifyColumn = false;

    public List<Column> getMyColumns() {
        return mMyColumns;
    }

    public ColumnAdapter(Context context) {
        mContext = context;
    }

    private OnMyColumnClickListener mOnMyColumnClickListener = null;
    public interface OnMyColumnClickListener{
        void onMyColumnClick(Column column);
    }

    public void setOnMyColumnClickListener(OnMyColumnClickListener onMyColumnClickListener) {
        mOnMyColumnClickListener = onMyColumnClickListener;
    }

    public void setMyColumns(List<Column> myColumns) {
        mMyColumns = myColumns;
        notifyDataSetChanged();
    }

    public void setRecommendColumns(List<Column> recommendColumns) {
        mRecommendColumns = recommendColumns;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_COLUMN_HEADER;
        } else if (position >= getItemColumnHeaderCount() && position < mMyColumns.size() + getItemColumnHeaderCount
                ()) {
            return ITEM_MY_FOLLOW_COLUMN;
        } else if (position >= mMyColumns.size() + getItemColumnHeaderCount() && position < mMyColumns.size()
                + getItemColumnHeaderCount() + getItemRecommendHeaderCount()) {
            return ITEM_RECOMMEND_COLUMN_HEADER;
        } else {
            return ITEM_RECOMMEND_COLUMN;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_COLUMN_HEADER:
                return onCreateColumnHeaderViewHolder(parent, viewType);
            case ITEM_MY_FOLLOW_COLUMN:
                return onCreateMyColumnViewHolder(parent, viewType);
            case ITEM_RECOMMEND_COLUMN_HEADER:
                return onCreateRecommendColumnHeaderViewHolder(parent, viewType);
            case ITEM_RECOMMEND_COLUMN:
            default:
                return onCreateRecommendColumnViewHolder(parent, viewType);
        }
    }
    private RecyclerView.ViewHolder onCreateRecommendColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_channel_recommend_header, parent, false);
        return new ColumnRecommendHeaderViewHolder(view);
    }

    private ColumnRecommendViewHolder onCreateRecommendColumnViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_channel_recommend, parent, false);
        return new ColumnRecommendViewHolder(view);
    }

    private ColumnForMineViewHolder onCreateMyColumnViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_channel_my, parent, false);
        return new ColumnForMineViewHolder(view);
    }

    private ColumnHeaderViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_channel_my_header, parent, false);
        return new ColumnHeaderViewHolder(view);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == ITEM_MY_FOLLOW_COLUMN) {
            onBindMyFollowColumnViewHolder((ColumnForMineViewHolder) holder, position - getItemColumnHeaderCount());
            return;
        }
        if (itemViewType == ITEM_RECOMMEND_COLUMN) {
            onBindRecommendColumnViewHolder((ColumnRecommendViewHolder) holder, position - getItemColumnHeaderCount() -
                    getItemRecommendHeaderCount()
                    - getMyColumnCount());
            return;
        }
        if (itemViewType == ITEM_COLUMN_HEADER) {
            onBindColumnHeaderViewHolder((ColumnHeaderViewHolder) holder, position);
            return;
        }
        if (itemViewType == ITEM_RECOMMEND_COLUMN_HEADER) {
            onBindRecommendColumnHeader(holder, position);
            return;
        }
    }
    private void onBindMyFollowColumnViewHolder(ColumnForMineViewHolder holder, int position) {
        Column column = mMyColumns.get(position);
        holder.mTvColumnName.setText(column.getColumnTitle());
        //设置字体大小,通过判断tab中的文字长度,如果有4个或4个以上的字,就14sp 4个以下16sp
        int textSize = column.getColumnTitle().length() >= 4 ? 14 : 16;
        holder.mTvColumnName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        //通过column的type设置文字背景贪色
        int type = column.getModifiable();
        //设置文字背景颜色
        holder.mTvColumnName.setBackgroundResource(type == 0 ? R.drawable.shape_column_fixed_bg : R.drawable
                .shape_column_bg);
        //根据tab状态设置文字颜色
        holder.mTvColumnName.setTextColor(type == 0 ? Color.parseColor("#666666") : Color.parseColor("#333333"));
        //设置右上角的删除按钮是否可见
        holder.mIvColumnDelete.setVisibility(mOpenEditMode ? View.VISIBLE : View.INVISIBLE);
        if (type==0 && mOpenEditMode){
            holder.mIvColumnDelete.setVisibility(View.GONE);
        }
        holder.mTvColumnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type != 0) {
                    if (mOpenEditMode){
                        removeColumn(column);
                    }else {
                        if (mOnMyColumnClickListener!=null) {
                            mOnMyColumnClickListener.onMyColumnClick(column);
                        }
                    }
                } else {
                    if (mOnMyColumnClickListener!=null) {
                        mOnMyColumnClickListener.onMyColumnClick(column);
                    }
                }
            }
        });

    }

    private void onBindRecommendColumnHeader(RecyclerView.ViewHolder holder, int position) {

    }

    private void onBindColumnHeaderViewHolder(ColumnHeaderViewHolder holder, int position) {
        holder.mTvColumnEditMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mOpenEditMode) {
                    holder.mTvColumnEditMode.setText("完成");
                } else {
                    holder.mTvColumnEditMode.setText("编辑");
                }
                mOpenEditMode = !mOpenEditMode;
                notifyDataSetChanged();
            }
        });
    }

    private void onBindRecommendColumnViewHolder(ColumnRecommendViewHolder holder, int position) {
        Column column = mRecommendColumns.get(position);
        holder.mTvColumnName.setText(column.getColumnTitle());
        int textSize = column.getColumnTitle().length() >= 4 ? 14 : 16;
        holder.mTvColumnName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        holder.mTvColumnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addColumn(column);
//                mColumnEditModelListener.clickRecommendColumn(mRecyclerView, holder);
            }
        });
    }
    @Override
    public int getItemCount() {
        return getItemColumnHeaderCount() + getMyColumnCount() + getItemRecommendHeaderCount() +
                getRecommendColumnCount();
    }

    public int getMyColumnCount() {
        return mMyColumns.size();
    }

    public int getRecommendColumnCount() {
        return mRecommendColumns.size();
    }

    public int getItemColumnHeaderCount() {
        return 1;
    }

    public int getItemRecommendHeaderCount() {
        return 1;
    }

    public void addColumn(Column column){
        isModifyColumn = true;
        mMyColumns.add(column);
        mRecommendColumns.remove(column);
        notifyDataSetChanged();
    }
    public void removeColumn(Column column){
        isModifyColumn = true;
        mMyColumns.remove(column);
        mRecommendColumns.add(0,column);
        notifyDataSetChanged();
    }

    public boolean isModifyColumn() {
        return isModifyColumn;
    }
}
