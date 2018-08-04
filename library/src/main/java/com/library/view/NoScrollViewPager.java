package com.library.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/18 15:23
 * E-mail:yuancongbin@gmail.com
 * 不能左右滑动的viewpager
 */

public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //不拦截
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //return false  不消费
        return false;
    }

    @Override
    public void setCurrentItem(int item) {
        //取消切换时的动画
        super.setCurrentItem(item,false);
    }


}
