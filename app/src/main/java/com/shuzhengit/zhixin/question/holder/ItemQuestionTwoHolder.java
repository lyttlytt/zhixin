package com.shuzhengit.zhixin.question.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuzhengit.zhixin.R;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/3 16:47
 * E-mail:yuancongbin@gmail.com
 */

class ItemQuestionTwoHolder extends RecyclerView.ViewHolder {
    ImageView mIvQuestion;
    TextView mTvQuestionTitle;
    TextView mTvAnswerCount;

    ItemQuestionTwoHolder(View itemView) {
        super(itemView);
        mIvQuestion = (ImageView) itemView.findViewById(R.id.ivQuestion);
        mTvQuestionTitle = (TextView) itemView.findViewById(R.id.tvQuestionTitle);
        mTvAnswerCount = (TextView) itemView.findViewById(R.id.tvAnswerCount);
    }
}
