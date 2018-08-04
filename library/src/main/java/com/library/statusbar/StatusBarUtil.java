package com.library.statusbar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.library.R;
import com.library.util.DeviceUtil;


/**
 * Author：袁从斌 on 2017/5/22 08:49
 *
 */

public class StatusBarUtil {

    /**
     * 设置状态栏颜色
     * @param activity 当前的activity
     */
    public static void setStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            //设置透明状态栏,这样才能让 ContentView 向上
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.statusBarColor));
            ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统
                // View 预留空间.
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }
            //状态栏文字颜色设置黑色
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            }
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//            ViewGroup contentView = (ViewGroup) window.getDecorView().findViewById(android.R.id
//                    .content);
//            View childAt = contentView.getChildAt(0);
//            if (childAt != null) {
//                ViewCompat.setFitsSystemWindows(childAt, false);
//            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup contentView = (ViewGroup) window.getDecorView().findViewById(android.R.id
                    .content);

            View mTopView = contentView.getChildAt(0);
            if (mTopView != null && mTopView.getLayoutParams() != null &&
                    mTopView.getLayoutParams().height == DeviceUtil.getStatusBarHeight(activity)) {
                //避免重复添加 View
                mTopView.setBackgroundColor(ContextCompat.getColor(activity,R.color.statusBarColor));
                return;
            }
            //使 ChildView 预留空间
            if (mTopView != null) {
//                ViewCompat.setFitsSystemWindows(mTopView, true);
            }
            //添加假 View
            View view = new View(activity);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams
                    .MATCH_PARENT, DeviceUtil.getStatusBarHeight(activity));
            view.setBackgroundColor(ContextCompat.getColor(activity,R.color.statusBarColor));
//            mTopView = new View(activity);
//            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
//                    .MATCH_PARENT, DeviceUtil.getStatusBarHeight(activity));
//            mTopView.setBackgroundColor(ContextCompat.getColor(activity,R.color.statusBarColor));
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mTopView.getLayoutParams();
            layoutParams.setMargins(0,DeviceUtil.getStatusBarHeight(activity),0,0);
            mTopView.setLayoutParams(layoutParams);
            ViewCompat.setFitsSystemWindows(view, true);
            contentView.addView(view, 0, lp);
            //状态栏文字颜色设置黑色
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            }
        }
    }
    /**
     * 设置状态栏颜色
     * @param activity 当前的activity
     */
    public static void setStatusBarColor(Activity activity,int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            //设置透明状态栏,这样才能让 ContentView 向上
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
            ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统
                // View 预留空间.
//                ViewCompat.setFitsSystemWindows(mChildView, true);
            }
            //状态栏文字颜色设置黑色
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            }
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//            ViewGroup contentView = (ViewGroup) window.getDecorView().findViewById(android.R.id
//                    .content);
//            View childAt = contentView.getChildAt(0);
//            if (childAt != null) {
//                ViewCompat.setFitsSystemWindows(childAt, false);
//            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup contentView = (ViewGroup) window.getDecorView().findViewById(android.R.id
                    .content);

            View mTopView = contentView.getChildAt(0);
            if (mTopView != null && mTopView.getLayoutParams() != null &&
                    mTopView.getLayoutParams().height == DeviceUtil.getStatusBarHeight(activity)) {
                //避免重复添加 View
                mTopView.setBackgroundColor(color);
                return;
            }
            //使 ChildView 预留空间
            if (mTopView != null) {
//                ViewCompat.setFitsSystemWindows(mTopView, true);
            }
            //添加假 View
            View view = new View(activity);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams
                    .MATCH_PARENT, DeviceUtil.getStatusBarHeight(activity));
            view.setBackgroundColor(color);
//            mTopView = new View(activity);
//            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
//                    .MATCH_PARENT, DeviceUtil.getStatusBarHeight(activity));
//            mTopView.setBackgroundColor(ContextCompat.getColor(activity,R.color.statusBarColor));
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mTopView.getLayoutParams();
            layoutParams.setMargins(0,DeviceUtil.getStatusBarHeight(activity),0,0);
            mTopView.setLayoutParams(layoutParams);
            ViewCompat.setFitsSystemWindows(view, true);
            contentView.addView(view, 0, lp);
            //状态栏文字颜色设置黑色
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            }
        }
    }
    /**
     * 设置状态栏透明
     * @param activity 当前的activity
     */
    public static void setStatusTranslucent(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            //设置透明状态栏,这样才能让 ContentView 向上
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统
                // View 预留空间.
                ViewCompat.setFitsSystemWindows(mChildView, false);
            }
            //状态栏文字颜色设置黑色
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            }
        } else if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup content = (ViewGroup) window.findViewById(android.R.id.content);
            View topView = content.getChildAt(0);
            if (topView != null && topView.getLayoutParams() != null && topView.getLayoutParams()
                    .height == DeviceUtil.getStatusBarHeight(activity)) {
                content.removeView(topView);
            }
            if (topView != null) {
                ViewCompat.setFitsSystemWindows(content.getChildAt(0), false);
            }
            //状态栏文字颜色设置黑色
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            }
        }
    }
}
