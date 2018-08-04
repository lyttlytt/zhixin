package com.shuzhengit.zhixin.index.document.detail.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.view.HtmlTextView;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/27 15:36
 * E-mail:yuancongbin@gmail.com
 */

class ItemDocumentContentHolder extends RecyclerView.ViewHolder {

    HtmlTextView mHtmlTextView;
    RelativeLayout mLlLike;
    ImageView mIvLikeIcon;
    TextView mTvLikeCount;
    TextView mTvAddOne;
    WebView mWebView;
    ItemDocumentContentHolder(View itemView) {
        super(itemView);
        mHtmlTextView = (HtmlTextView) itemView.findViewById(R.id.htmlTextView);
        mLlLike = (RelativeLayout) itemView.findViewById(R.id.llLike);
        mIvLikeIcon = (ImageView) itemView.findViewById(R.id.ivLikeIcon);
        mTvLikeCount = (TextView) itemView.findViewById(R.id.tvAgreeCount);
        mTvAddOne = (TextView) itemView.findViewById(R.id.tvAddOne);
        mWebView = (WebView) itemView.findViewById(R.id.webView);
    }
}
