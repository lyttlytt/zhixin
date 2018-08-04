package com.library.util;

import android.util.Log;

/**
 * Author:yuanCongBin on 2017/5/25 09:22
 * e-mail:whereycb@163.com
 */

public class LogUtils {
    private static final boolean IS_DEBUG = true;
    private static final String TAG = "debug";

    public static void i(String info) {
        if (IS_DEBUG) {
            Log.i(TAG, info);
        }
    }

    public static void i(String tag, String info) {
        if (IS_DEBUG) {
            Log.i(tag, info);
        }
    }

    public static void w(String warning) {
        if (IS_DEBUG) {
            Log.w(TAG, warning);
        }
    }

    public static void w(String tag, String warning) {
        if (IS_DEBUG) {
            Log.w(tag, warning);
        }
    }

    public static void e(String error) {
        if (IS_DEBUG) {
            Log.e(TAG, error);
        }
    }

    public static void e(String tag, String error) {
        if (IS_DEBUG) {
            Log.e(tag, error);
        }
    }

    public static void d(String debug) {
        if (IS_DEBUG) {
            Log.d(TAG, debug);
        }
    }

    public static void d(String tag, String debug) {
        if (IS_DEBUG) {
            Log.d(tag, debug);
        }
    }

    public static void v(String verbose) {
        if (IS_DEBUG) {
            Log.v(TAG, verbose);
        }
    }

    public static void v(String tag, String verbose) {
        if (IS_DEBUG) {
            Log.v(tag, verbose);
        }
    }
}
