package com.shuzhengit.zhixin.sign_in.score;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuzhengit.zhixin.R;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2018/1/30 16:24
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class ScoreSourceAdapter extends RecyclerView.Adapter<ScoreSourceAdapter.ViewHolder> {
    private Context mContext;

    public ScoreSourceAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ScoreSourceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.holder_score_source_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScoreSourceAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
