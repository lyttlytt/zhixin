package com.shuzhengit.zhixin.question.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.view.CircleImageView;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/4 09:49
 * E-mail:yuancongbin@gmail.com
 */

class ItemQuestionDetailCommentHolder extends RecyclerView.ViewHolder {
    TextView mTvAnswer,mTvReplyUserName,mTvAgreeCount,mTvAddOne;
    CircleImageView mReplyUserAvatar;
    ImageView mIvImg;
    ItemQuestionDetailCommentHolder(View itemView) {
        super(itemView);
        mTvAnswer = (TextView) itemView.findViewById(R.id.tvAnswer);
        mTvAgreeCount = (TextView) itemView.findViewById(R.id.tvAgreeCount);
        mTvReplyUserName = (TextView) itemView.findViewById(R.id.tvReplyUserName);
        mReplyUserAvatar = (CircleImageView) itemView.findViewById(R.id.civUserAvatar);
        mTvAddOne = (TextView) itemView.findViewById(R.id.tvAddOne);
        mIvImg = (ImageView) itemView.findViewById(R.id.img);
    }
}
