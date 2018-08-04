package com.shuzhengit.zhixin.question.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.view.CircleImageView;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/4 09:49
 * E-mail:yuancongbin@gmail.com
 */

class ItemQuestionDetailTitleHolder extends RecyclerView.ViewHolder {
    TextView tvQuestion, tvQuestionDescription,tvAnswerCount,tvFollowed,tvReleaseUserName;
    LinearLayout llImage,llReleaseUser;
    ImageView[] images = new ImageView[3];
    CircleImageView mUserAvatar;
    public ItemQuestionDetailTitleHolder(View itemView) {
        super(itemView);
        tvQuestion = (TextView) itemView.findViewById(R.id.tvQuestionTitle);
        tvQuestionDescription = (TextView) itemView.findViewById(R.id.tvDescription);
        llImage = (LinearLayout) itemView.findViewById(R.id.llImage);
        images[0] = (ImageView) itemView.findViewById(R.id.ivBanner1);
        images[1] = (ImageView)  itemView.findViewById(R.id.ivBanner2);
        images[2] = (ImageView)  itemView.findViewById(R.id.ivBanner3);
        tvAnswerCount = (TextView) itemView.findViewById(R.id.tvAnswerCount);
        tvFollowed = (TextView) itemView.findViewById(R.id.tvFollowed);
        mUserAvatar = (CircleImageView) itemView.findViewById(R.id.civUserAvatar);
        tvReleaseUserName = (TextView) itemView.findViewById(R.id.tvReleaseUserName);
        llReleaseUser = (LinearLayout) itemView.findViewById(R.id.llReleaseUser);
    }
}
