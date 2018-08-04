package com.shuzhengit.zhixin.index.document.detail.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.shuzhengit.zhixin.R;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/27 15:36
 * E-mail:yuancongbin@gmail.com
 */

 class ItemDocumentRecommendHolder extends RecyclerView.ViewHolder {
     RelativeLayout mRlRecommendDocument;
     ItemDocumentRecommendHolder(View itemView) {
        super(itemView);
         mRlRecommendDocument = (RelativeLayout) itemView.findViewById(R.id.rlRecommendDocument);
    }
}
