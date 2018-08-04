package com.shuzhengit.zhixin.wenba.mine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/15 14:41
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class WenBaMineFollowAdapter extends RecyclerView.Adapter<WenBaMineFollowAdapter.ViewHolder> {
    private Context mContext;

    public WenBaMineFollowAdapter(Context context) {
        mContext = context;
    }

    @Override
    public WenBaMineFollowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(WenBaMineFollowAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
