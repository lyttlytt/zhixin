package com.shuzhengit.zhixin.column.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuzhengit.zhixin.R;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/10/17 16:34
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class ColumnForMineViewHolder extends RecyclerView.ViewHolder {
    TextView mTvColumnName;
    ImageView mIvColumnDelete;
    public ColumnForMineViewHolder(View itemView) {
        super(itemView);
        mTvColumnName = (TextView) itemView.findViewById(R.id.tvColumnName);
        mIvColumnDelete = (ImageView) itemView.findViewById(R.id.ivColumnDelete);
    }
}
