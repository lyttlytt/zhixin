package com.shuzhengit.zhixin.index.document.detail.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuzhengit.zhixin.R;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/27 15:36
 * E-mail:yuancongbin@gmail.com
 */

class ItemDocumentCommentHolder extends RecyclerView.ViewHolder {
    TextView mTvComment;
    TextView mTvCommentTime;
    TextView mTvCommentCount;
    TextView mTvUserName;
    TextView mTvAgree;
    ImageView mIvAvatar;
    TextView mTvAddOne;

    ItemDocumentCommentHolder(View itemView) {
        super(itemView);
        mTvComment = (TextView) itemView.findViewById(R.id.tvComment);
        mTvCommentTime = (TextView) itemView.findViewById(R.id.tvCommentTime);
        mTvCommentCount = (TextView) itemView.findViewById(R.id.tvCommentCount);
        mTvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
        mTvAgree = (TextView) itemView.findViewById(R.id.tvAgree);
        mIvAvatar = (ImageView) itemView.findViewById(R.id.ivAvatar);
        mTvAddOne = (TextView) itemView.findViewById(R.id.tvAddOne);
    }
}
