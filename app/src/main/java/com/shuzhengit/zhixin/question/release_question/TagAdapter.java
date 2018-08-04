package com.shuzhengit.zhixin.question.release_question;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuzhengit.zhixin.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/10/30 15:54
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder>{
    private Context mContext;
    private List<String> mTags = new ArrayList<>();
    public TagAdapter(Context context) {
        mContext = context;
    }

    public List<String> getTags() {
        return mTags;
    }

    public void setTags(List<String> tags) {
        mTags = tags;
        notifyDataSetChanged();
    }

    public void addTag(String tag){
        mTags.add(tag);
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_channel_my, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mTvTag.setText(mTags.get(position));
        holder.mIvDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTags.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTags.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvDeleteIcon;
        TextView mTvTag;
        public ViewHolder(View itemView) {
            super(itemView);
            mTvTag = (TextView) itemView.findViewById(R.id.id_channel_title);
            mTvTag.setMaxEms(4);
            mTvTag.setEllipsize(TextUtils.TruncateAt.END);
            mTvTag.setSingleLine(true);
            mIvDeleteIcon = (ImageView) itemView.findViewById(R.id.id_delete_icon);
        }
    }
}
