package com.shuzhengit.zhixin.view.pull2refreshview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shuzhengit.zhixin.R;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/24 11:46
 * E-mail:yuancongbin@gmail.com
 */

public class RefreshHeaderView extends LinearLayout implements RefreshHeaderState {
    private LinearLayout mRefreshHeaderView;
    private TextView mTvRefreshStatus;
    private ProgressBar mProgressRefreshing;
    private ImageView mIvRefreshArrow;
    private static final int ROTATE_ANIMATION_DURATION = 300;
    private RotateAnimation mRotateUpAnimation, mRotateDownAnimation;
    private int mMeasuredHeight;
    private int mCurrentState = STATE_NORMAL;

    public RefreshHeaderView(Context context) {
        super(context);
        init(context);
    }

    public RefreshHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mRefreshHeaderView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_refresh, this, false);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        this.setLayoutParams(lp);
        this.setPadding(0, 0, 0, 0);
        addView(mRefreshHeaderView, new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
        setGravity(Gravity.BOTTOM);
        mTvRefreshStatus = (TextView) findViewById(R.id.tvRefreshStatus);
        mProgressRefreshing = (ProgressBar) findViewById(R.id.pbRefreshing);
        mIvRefreshArrow = (ImageView) findViewById(R.id.ivRefreshArrow);

        mRotateUpAnimation = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnimation.setDuration(ROTATE_ANIMATION_DURATION);
        //保持动画移动的位置,否则动画结束后会回到初始位置
        mRotateUpAnimation.setFillAfter(true);


        mRotateDownAnimation = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnimation.setDuration(ROTATE_ANIMATION_DURATION);
        //保持动画移动的位置,否则动画结束后会回到初始位置
        mRotateDownAnimation.setFillAfter(true);
//        measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        measure(-2,-2);
        mMeasuredHeight = getMeasuredHeight();
    }

    public void setState(int state) {
        if (state == mCurrentState) {
            return;
        }
        if (state == STATE_REFRESHING) {
            mIvRefreshArrow.clearAnimation();
            mIvRefreshArrow.setVisibility(View.INVISIBLE);
            mProgressRefreshing.setVisibility(View.VISIBLE);
            smoothScrollTo(mMeasuredHeight);
        } else if (state == STATE_COMPLETED) {
            mIvRefreshArrow.setVisibility(INVISIBLE);
            mProgressRefreshing.setVisibility(INVISIBLE);
        } else {
            mIvRefreshArrow.setVisibility(View.VISIBLE);
            mProgressRefreshing.setVisibility(View.INVISIBLE);
        }

        switch (state) {
            case STATE_NORMAL:
                if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
                    mIvRefreshArrow.startAnimation(mRotateDownAnimation);
                }
                if (mCurrentState == STATE_REFRESHING) {
                    mIvRefreshArrow.clearAnimation();
                }
                mTvRefreshStatus.setText(getContext().getString(R.string.pullToRefreshNormal));
                break;
            case STATE_RELEASE_TO_REFRESH:
                if (mCurrentState != STATE_RELEASE_TO_REFRESH) {
                    mIvRefreshArrow.clearAnimation();
                    mIvRefreshArrow.startAnimation(mRotateUpAnimation);
                    mTvRefreshStatus.setText(getContext().getString(R.string.pullToRefreshRelease));
                }
                break;
            case STATE_REFRESHING:
                mTvRefreshStatus.setText(getContext().getString(R.string.pullToRefreshing));
                break;
            case STATE_COMPLETED:
                mTvRefreshStatus.setText(getContext().getString(R.string.pullToRefreshCompleted));
                break;
            default:
        }
        mCurrentState = state;
    }

    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibilityHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(animation -> {
            setVisibilityHeight((int) animation.getAnimatedValue());
        });
        animator.start();
    }

    public int getState() {
        return mCurrentState;
    }

    private void setVisibilityHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        LayoutParams layoutParams = (LayoutParams) mRefreshHeaderView.getLayoutParams();
        layoutParams.height = height;
        mRefreshHeaderView.setLayoutParams(layoutParams);
    }

    public int getVisibilityHeight() {
        LayoutParams layoutParams = (LayoutParams) mRefreshHeaderView.getLayoutParams();
        return layoutParams.height;
    }

    @Override
    public void onMove(float delta) {
        if (getVisibilityHeight() > 0 || delta > 0) {
            setVisibilityHeight((int) delta + getVisibilityHeight());
            if (mCurrentState <= STATE_RELEASE_TO_REFRESH) {
                if (getVisibilityHeight() > mMeasuredHeight) {
                    setState(STATE_RELEASE_TO_REFRESH);
                } else {
                    setState(STATE_NORMAL);
                }
            }
        }
    }

    @Override
    public boolean releaseAction() {
        boolean isOpenRefresh = false;
        int height = getVisibilityHeight();
        if (height == 0) {
            isOpenRefresh = false;
        }
        if (getVisibilityHeight() > mMeasuredHeight && mCurrentState < STATE_REFRESHING) {
            setState(STATE_REFRESHING);
            isOpenRefresh = true;
        }
        //  刷新中或者refreshView没完全显示,什么都不做
        if (mCurrentState == STATE_REFRESHING && height <= mMeasuredHeight) {
        }
        if (mCurrentState != STATE_REFRESHING) {
            smoothScrollTo(0);
        }
        if (mCurrentState == STATE_REFRESHING) {
            int destHeight = mMeasuredHeight;
            smoothScrollTo(destHeight);
        }
        return isOpenRefresh;
    }

    @Override
    public void refreshCompleted() {
        mTvRefreshStatus.setText(getContext().getString(R.string.pullToRefreshCompleted));
        setState(STATE_COMPLETED);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                reset();
            }
        }, 300);
    }

    private void reset() {
        smoothScrollTo(0);
        new Handler().postDelayed(() -> setState(STATE_NORMAL), 500);
    }
}
