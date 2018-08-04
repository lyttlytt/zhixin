package com.shuzhengit.zhixin.sign_in.score;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.util.ResourceUtils;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2018/1/30 11:14
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class ScoreRankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int ITEM_HEADER = 1;
    private static final int ITEM_DEFAULT=2;
    private OnItemClickListener mOnItemClickListener = null;
    private Context mContext;


    public ScoreRankAdapter(Context context) {
        mContext = context;
    }

    public interface OnItemClickListener{
        void onItemClick();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return ITEM_HEADER;
        }else {
            return ITEM_DEFAULT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_HEADER){
            return onCreateHeaderViewHolder(parent,viewType);
        }else {
            return onCreateDefaultViewHolder(parent,viewType);
        }
    }

    private RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(mContext).inflate(R.layout.holder_score_rank_default,parent,false);
        return new DefaultViewHolder(view);
    }

    private HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.holder_socre_rank_header,parent,false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==ITEM_HEADER){
            onBindHeaderViewHolder(holder,position);
        }else {
            onBindDefaultViewHolder((DefaultViewHolder) holder,position-1);
        }
    }

    private void onBindDefaultViewHolder(DefaultViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick();
                }
            }
        });
        holder.mTvRank.setText(String.valueOf(position));
        if (position<3){
            Drawable drawable = ResourceUtils.getResourceDrawable(mContext, R.drawable
                    .ic_score_rank_red);
            drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
            holder.mTvScore.setCompoundDrawables(drawable,null,null,null);
            holder.mTvScore.setTextColor(ResourceUtils.getResourceColor(mContext,R.color.red500));
        }else {
            Drawable drawable = ResourceUtils.getResourceDrawable(mContext, R.drawable
                    .ic_score_rank_grey);
            drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
            holder.mTvScore.setCompoundDrawables(drawable,null,null,null);
            holder.mTvScore.setTextColor(ResourceUtils.getResourceColor(mContext,R.color.colorBlack));
        }
    }

    private void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder{

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    class DefaultViewHolder extends RecyclerView.ViewHolder{
        TextView mTvScore,mTvRank;
        public DefaultViewHolder(View itemView) {
            super(itemView);
            mTvScore = (TextView) itemView.findViewById(R.id.tvScore);
            mTvRank = (TextView) itemView.findViewById(R.id.tvRank);
        }
    }
}
