package com.shuzhengit.zhixin.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.library.util.KeyBorderUtil;

import java.util.ArrayList;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/11 11:18
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class KeyBorderLinearLayout extends LinearLayout{
    private static final String TAG = "KeyBorderLinearLayout";
    public KeyBorderLinearLayout(Context context) {
        super(context);
    }

    public KeyBorderLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyBorderLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getTouchTarget(this, (int) ev.getRawX(), (int) ev.getRawY());
        if (ev.getAction()!=MotionEvent.ACTION_DOWN) {
            if (!(view instanceof EditText)) {
                if (view != null) {
                    KeyBorderUtil.hideSoftInput(getContext(), view);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private View getTouchTarget(View view, int x, int y) {
        View target = null;
        ArrayList<View> TouchableViews = view.getTouchables();
        for (View child : TouchableViews) {
            if (isTouchPointInView(child, x, y)) {
                target = child;
                break;
            }
        }
        return target;
    }

    private boolean isTouchPointInView(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        if (view.isClickable() && y >= top && y <= bottom
                && x >= left && x <= right) {
            return true;
        }
        return false;
    }
}
