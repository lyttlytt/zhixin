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

 class ItemDocumentOneHolder extends RecyclerView.ViewHolder {
     ImageView ivBanner,ivRemove;
     TextView tvDocumentTitle,mTvAuthor,tvReadCount,tvDate,tvTopFlag;
     ItemDocumentOneHolder(View itemView) {
        super(itemView);
        ivBanner = (ImageView) itemView.findViewById(R.id.ivBanner);
        tvDocumentTitle = (TextView) itemView.findViewById(R.id.tvDocumentTitle);
        mTvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
        tvReadCount = (TextView) itemView.findViewById(R.id.tvReadCount);
        tvDate  = (TextView) itemView.findViewById(R.id.tvDate);
        ivRemove = (ImageView) itemView.findViewById(R.id.ivRemove);
         tvTopFlag = (TextView) itemView.findViewById(R.id.tvTopFlag);
    }
}
