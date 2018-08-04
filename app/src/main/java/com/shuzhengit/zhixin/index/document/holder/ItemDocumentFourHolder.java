package com.shuzhengit.zhixin.index.document.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuzhengit.zhixin.R;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/14 14:24
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:只有标题的条目holder
 */

 class ItemDocumentFourHolder extends RecyclerView.ViewHolder{
    TextView tvDocumentTitle, mTvAuthor, tvReadCount, tvDate,tvTopFlag;
    ImageView ivBanner1, ivBanner2, ivBanner3, ivRemove;
    ImageView[] images = new ImageView[3];
    LinearLayout mLlImages;
     ItemDocumentFourHolder(View itemView) {
        super(itemView);
         tvDocumentTitle = (TextView) itemView.findViewById(R.id.tvDocumentTitle);
         mTvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
         tvReadCount = (TextView) itemView.findViewById(R.id.tvReadCount);
         tvDate = (TextView) itemView.findViewById(R.id.tvDate);
         images[0] = (ImageView) itemView.findViewById(R.id.ivBanner1);
         images[1] = (ImageView) itemView.findViewById(R.id.ivBanner2);
         images[2] = (ImageView) itemView.findViewById(R.id.ivBanner3);
         mLlImages = (LinearLayout) itemView.findViewById(R.id.llImages);
         tvTopFlag = (TextView) itemView.findViewById(R.id.tvTopFlag);
    }
}
