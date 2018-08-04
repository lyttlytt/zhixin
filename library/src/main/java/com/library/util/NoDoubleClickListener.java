package com.library.util;

import android.view.View;

/**
 * Author：袁从斌 on 27/03/2017 15:11
 * 防止快速点击
 */

public abstract class NoDoubleClickListener implements View.OnClickListener {
    private long lastClickTime = 0;
    //两次点击事件的事件间隔
    public static final int MIN_CLICK_DELAY_TIME = 1000;

    @Override
    public void onClick(View v) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME){
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }

    }

    protected abstract void onNoDoubleClick(View v);
}
