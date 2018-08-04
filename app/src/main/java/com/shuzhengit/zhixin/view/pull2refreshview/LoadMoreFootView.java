package com.shuzhengit.zhixin.view.pull2refreshview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shuzhengit.zhixin.R;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/24 14:53
 * E-mail:yuancongbin@gmail.com
 */

public class LoadMoreFootView extends LinearLayout {
    private View mLoadMoreView;
    private TextView mTvLoadMoreStatus;
    private ProgressBar mPbLoading;
    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETED = 1;
    public final static int STATE_NO_MORE = 2;
    public final static int STATE_FAIL = 3;
    private   int mCurrentStatus;
//    private TextView mTvReset;

    public LoadMoreFootView(Context context) {
        this(context, null);
    }

    public LoadMoreFootView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreFootView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private OnLoadFail mOnLoadFail;

    public interface OnLoadFail {
        void resetLoad();
    }

    public void setOnLoadFail(OnLoadFail onLoadFail) {
        mOnLoadFail = onLoadFail;
    }

    private void init(Context context) {
        mLoadMoreView = LayoutInflater.from(context).inflate(R.layout.item_load_more, this, false);
        setLayoutParams(new LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        addView(mLoadMoreView);
        mTvLoadMoreStatus = (TextView) findViewById(R.id.tvLoadStatus);
        mPbLoading = (ProgressBar) findViewById(R.id.pbLoading);
//        mTvReset = (TextView) mLoadMoreView.findViewById(R.id.tvReset);

    }

    public int getCurrentStatus() {
        return mCurrentStatus;
    }

    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                mCurrentStatus = STATE_LOADING;
                mTvLoadMoreStatus.setText(getContext().getString(R.string.pullToRefreshLoading));
                mTvLoadMoreStatus.setVisibility(VISIBLE);
                mPbLoading.setVisibility(VISIBLE);
                this.setVisibility(VISIBLE);
                break;
            case STATE_COMPLETED:
                mCurrentStatus = STATE_COMPLETED;
                this.setVisibility(View.GONE);
                break;
            case STATE_NO_MORE:
                mCurrentStatus =STATE_NO_MORE;
                mTvLoadMoreStatus.setVisibility(VISIBLE);
                mTvLoadMoreStatus.setText(getContext().getString(R.string.pullToRefreshLoadNoMore));
                mPbLoading.setVisibility(GONE);
                setVisibility(VISIBLE);
                break;
            case STATE_FAIL:
                mCurrentStatus=STATE_FAIL;
                mPbLoading.setVisibility(GONE);
                mTvLoadMoreStatus.setText(getContext().getString(R.string.pullToRefreshLoadFail));
                setVisibility(VISIBLE);
                setOnClickListener(v -> {
//                    setState(STATE_LOADING);
                    if (mOnLoadFail!=null){
                        mOnLoadFail.resetLoad();
                    }
                });
                break;
            default:break;
        }
    }
}
