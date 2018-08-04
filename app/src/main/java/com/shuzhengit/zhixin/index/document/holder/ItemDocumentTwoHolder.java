package com.shuzhengit.zhixin.index.document.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuzhengit.zhixin.R;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/19 11:15
 * E-mail:yuancongbin@gmail.com
 */

 class ItemDocumentTwoHolder extends RecyclerView.ViewHolder {
     TextView tvDocumentTitle, mTvAuthor, tvReadCount, tvDate,tvTopFlag;
     ImageView ivBanner1, ivBanner2, ivBanner3;
    ImageView ivRemove;
     ImageView[] images = new ImageView[3];
     ItemDocumentTwoHolder(View itemView) {
        super(itemView);
        tvDocumentTitle = (TextView) itemView.findViewById(R.id.tvDocumentTitle);
        mTvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
        tvReadCount = (TextView) itemView.findViewById(R.id.tvReadCount);
        tvDate = (TextView) itemView.findViewById(R.id.tvDate);
        images[0] = (ImageView) itemView.findViewById(R.id.ivBanner1);
        images[1] = (ImageView) itemView.findViewById(R.id.ivBanner2);
        images[2] = (ImageView) itemView.findViewById(R.id.ivBanner3);
         tvTopFlag = (TextView) itemView.findViewById(R.id.tvTopFlag);
    }
}
