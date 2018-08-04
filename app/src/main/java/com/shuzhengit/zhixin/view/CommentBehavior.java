package com.shuzhengit.zhixin.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;


public class CommentBehavior extends CoordinatorLayout.Behavior {
    private boolean visible = true;

    public CommentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View
            target, int nestedScrollAxes) {
        return  nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL||super.onStartNestedScroll(coordinatorLayout,child,
                directTargetChild,target,nestedScrollAxes);
//        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int
            dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyConsumed>0&& visible){
            visible = false;
            onHide(child);
        }else if (dyConsumed<0){
            visible=true;
            onShow(child);
        }
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    private void onHide(View child) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        child.animate().translationY(child.getHeight()+layoutParams.bottomMargin).setInterpolator(new
                AccelerateInterpolator(3));
        ViewCompat.animate(child).start();

    }

    private void onShow(View child) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        child.animate().translationY(0).setInterpolator(new
                AccelerateInterpolator(3));
        ViewCompat.animate(child).start();
    }
}
