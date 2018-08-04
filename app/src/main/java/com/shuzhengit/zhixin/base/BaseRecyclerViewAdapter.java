package com.shuzhengit.zhixin.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> implements
        OnItemClickListener<T> {
    private int layoutRes;
    protected List<T> mData = new ArrayList<T>();
    private View createView;

    public List<T> getData() {
        return mData;
    }

    public BaseRecyclerViewAdapter(int layoutRes) {
        this.layoutRes = layoutRes;
    }

    public BaseRecyclerViewAdapter(int layoutRes, List<T> data) {
        this.layoutRes = layoutRes;
        mData = data;
    }



//    public BaseRecyclerViewAdapter(int layoutRes, List<T> data, OnItemClickListener<T> onItemClickListener) {
//        this.layoutRes = layoutRes;
//        mData = data;
//        mOnItemClickListener = onItemClickListener;
//    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void onRefreshData(List<T> data) {
        mData.clear();
        mData = data;
        notifyDataSetChanged();
    }

    public void addMoreData(List<T> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
//        BaseViewHolder holder = new BaseViewHolder(view);
//        BaseViewHolder holder =
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onItemClick(holder.itemView,holder.getLayoutPosition());
//            }
//        });
        BaseViewHolder holder = BaseViewHolder.getViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        convert(holder, holder.getLayoutPosition(), mData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(holder.itemView, position, mData.get(position));
            }
        });
    }


    public abstract void convert(BaseViewHolder holder, int position, T t);

    @Override
    public int getItemCount() {
        if (null != mData) {
            return mData.size();
        } else {
            return 0;
        }
    }

}
