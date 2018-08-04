package com.shuzhengit.zhixin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.shuzhengit.zhixin.R;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/15 10:41
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class FollowTextView extends TextView {

    private Drawable mFollowedBackgroundDrawable;
    private boolean mIsFollow;
    private Drawable mUnFollowBackgroundDrawable;
    private String mFollowed;
    private String mUnFollowed;
    private int mFollowedColor;
    private int mUnFollowedColor;

    public FollowTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);

    }

    public FollowTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FollowTextView);
        mIsFollow = typedArray.getBoolean(R.styleable.FollowTextView_isFollow, false);
        mFollowedBackgroundDrawable = typedArray.getDrawable(R.styleable.FollowTextView_followBackground);
        mUnFollowBackgroundDrawable = typedArray.getDrawable(R.styleable.FollowTextView_unFollowBackground);
        mFollowed = typedArray.getString(R.styleable.FollowTextView_followed);
        mUnFollowed = typedArray.getString(R.styleable.FollowTextView_unFollowed);
        mFollowedColor = typedArray.getColor(R.styleable.FollowTextView_followedTextColor, ContextCompat.getColor
                (context, R.color.colorBlack));
        mUnFollowedColor = typedArray.getColor(R.styleable.FollowTextView_unFollowedTextColor, ContextCompat.getColor
                (context, R.color.colorBlack));
        typedArray.recycle();
        setUI();
    }

    private void setUI() {
        if (mIsFollow){
            this.setText(mFollowed);
            this.setTextColor(mFollowedColor);
            if (mFollowedBackgroundDrawable!=null) {
                this.setBackground(mFollowedBackgroundDrawable);
            }
        }else {
            this.setText(mUnFollowed);
            this.setTextColor(mUnFollowedColor);
            if (mUnFollowBackgroundDrawable!=null) {
                this.setBackground(mUnFollowBackgroundDrawable);
            }
        }
    }

    public void setFollowedBackgroundDrawable(Drawable followedBackgroundDrawable) {
        mFollowedBackgroundDrawable = followedBackgroundDrawable;
    }

    public void setFollow(boolean follow) {
        mIsFollow = follow;
        setUI();
    }

    public void setUnFollowBackgroundDrawable(Drawable unFollowBackgroundDrawable) {
        mUnFollowBackgroundDrawable = unFollowBackgroundDrawable;
    }

    public void setFollowed(String followed) {
        mFollowed = followed;
    }

    public void setUnFollowed(String unFollowed) {
        mUnFollowed = unFollowed;
    }
}
