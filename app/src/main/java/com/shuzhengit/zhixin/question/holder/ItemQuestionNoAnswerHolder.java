package com.shuzhengit.zhixin.question.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shuzhengit.zhixin.R;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/27 20:24
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class ItemQuestionNoAnswerHolder extends RecyclerView.ViewHolder {
    TextView tvNoContent;
    public ItemQuestionNoAnswerHolder(View itemView) {
        super(itemView);
        tvNoContent = (TextView) itemView.findViewById(R.id.tvNOContent);
    }
}
