package com.shuzhengit.zhixin.index.document.detail.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shuzhengit.zhixin.R;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/27 15:36
 * E-mail:yuancongbin@gmail.com
 */

class ItemDocumentTitleHolder extends RecyclerView.ViewHolder {
    TextView mTvDocumentTitle, mTvAuthor, mTvReleaseTime;

    ItemDocumentTitleHolder(View itemView) {
        super(itemView);
        mTvDocumentTitle = (TextView) itemView.findViewById(R.id.tvDocumentTitle);
        mTvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
        mTvReleaseTime = (TextView) itemView.findViewById(R.id.tvReleaseTime);
    }
}
