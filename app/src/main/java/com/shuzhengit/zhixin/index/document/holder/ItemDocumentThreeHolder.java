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

class ItemDocumentThreeHolder extends RecyclerView.ViewHolder {
    TextView tvDocumentTitle, tvAdvert, tvAuthor, tvTopFlag;
    ImageView ivBanner, ivRemove;

    ItemDocumentThreeHolder(View itemView) {
        super(itemView);
        tvDocumentTitle = (TextView) itemView.findViewById(R.id.tvDocumentTitle);
        tvAdvert = (TextView) itemView.findViewById(R.id.tvAdvert);
        tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
        ivBanner = (ImageView) itemView.findViewById(R.id.ivBanner);
        ivRemove = (ImageView) itemView.findViewById(R.id.ivRemove);
        tvTopFlag = (TextView) itemView.findViewById(R.id.tvTopFlag);
    }
}
