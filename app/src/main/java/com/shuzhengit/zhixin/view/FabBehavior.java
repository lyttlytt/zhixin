package com.shuzhengit.zhixin.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;


public class FabBehavior extends FloatingActionButton.Behavior {
    private boolean visible = true;

    public FabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //当观察的view(RecyclerView)发生滑动的开始时候回调
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View
            directTargetChild, View target, int nestedScrollAxes) {

        //nestedScrollAxes滑动关联轴,只监听垂直滑动
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout,
                child,
                directTargetChild, target, nestedScrollAxes);
//        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    //当观察的view滑动时候回调
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int
            dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        //根据情况执行动画
        if (dyConsumed>0&&visible){
            visible=false;
            onHide(child);
        }else if (dyConsumed<0){
            visible=true;
            onShow(child);
        }
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    /**
     * 隐藏动画 属性动画
     * @param child fab
     */
    private void onHide(FloatingActionButton child) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        child.animate().translationY(child.getHeight()+layoutParams.bottomMargin).setInterpolator(new
                AccelerateInterpolator(3));
        ViewCompat.animate(child).scaleY(0f).scaleX(0f).start();
    }

    /**
     * 显示动画
     * @param child fab
     */
    private void onShow(FloatingActionButton child) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        child.animate().translationY(0).setInterpolator(new DecelerateInterpolator(3));
        ViewCompat.animate(child).scaleX(1f).scaleY(1f).start();
    }
}
