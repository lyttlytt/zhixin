package com.library.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by 袁从斌-where on 2017/1/4.
 * Toast工具类
 */

public class ToastUtils {
    private ToastUtils() {
    }

    public static Toast mToast;

    /**
     * 单例短toast
     *
     * @param context
     * @param message
     */
    public static void showShortToast(Context context, String message) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
        }
        mToast.setText(message);
        mToast.show();
    }

    /**
     * 单例短toast 中间显示
     * @param context
     * @param message
     */
    public static void showToastShortCenter(Context context, String message) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setText(message);
        mToast.show();
    }

    /**
     * 单例长toast
     *
     * @param context
     * @param message
     */
    public static void showLongToast(Context context, String message) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_LONG);
        }
        mToast.setText(message);
        mToast.show();
    }
}
