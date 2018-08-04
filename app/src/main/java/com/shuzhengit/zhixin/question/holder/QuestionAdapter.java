package com.shuzhengit.zhixin.question.holder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuzhengit.zhixin.R;

import java.util.Random;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/3 16:46
 * E-mail:yuancongbin@gmail.com
 */

public class QuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM_ONE = 0;
    private static final int TYPE_ITEM_TWO = 1;
    private OnItemClickListener mOnItemClickListener=null;
    public interface OnItemClickListener {
        void onItemClickListener(View view,int position);
        void onItemRemoveListener(View view,int position);
    }

    public void setOnItemListener(OnItemClickListener onItemListener) {
        mOnItemClickListener = onItemListener;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM_ONE:
                return onCreateTypeOneViewHolder(parent, viewType);
            case TYPE_ITEM_TWO:
            default:
                return onCreateTypeTwoViewHolder(parent, viewType);
        }
    }

    private ItemQuestionTwoHolder onCreateTypeTwoViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_two, parent, false);
        return new ItemQuestionTwoHolder(view);
    }

    private ItemQuestionOneHolder onCreateTypeOneViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_one, parent, false);
        return new ItemQuestionOneHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case TYPE_ITEM_ONE:
                onBindTypeOneViewHolder((ItemQuestionOneHolder) holder,position);
                break;
            case TYPE_ITEM_TWO:
                onBindTypeTwoViewHolder((ItemQuestionTwoHolder) holder,position);
                break;
            default:break;
        }
    }

    private void onBindTypeTwoViewHolder(ItemQuestionTwoHolder holder, int position) {
        String string = holder.itemView.getResources().getString(R.string.answer);
        Random random = new Random();
        int i = random.nextInt(10) - 1;
        String format = String.format(string, i);
        holder.mTvAnswerCount.setText(format);
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener!=null){
                mOnItemClickListener.onItemClickListener(v,position);
            }
        });
    }

    private void onBindTypeOneViewHolder(ItemQuestionOneHolder holder, int position) {
        String string = holder.itemView.getResources().getString(R.string.answer);
        Random random = new Random();
        int i = random.nextInt(10) - 1;
        String format = String.format(string, i);
        holder.mTvAnswerCount.setText(format);
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener!=null){
                mOnItemClickListener.onItemClickListener(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
