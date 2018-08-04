package com.library.util;

/**
 * FILE:NoDoubleClickUtils
 * AUTHOR: yangzhe
 * DATE:2016/5/13
 * TYPE:  防止二次点击的工具类
 */

public class NoDoubleClickUtils {
    private static long lastClickTime;
    private final static int SPACE_TIME = 1000;

    public static void initLastClickTime() {
        lastClickTime = 0;
    }

    /**
     * 判断是否可以二次点击的方法
     *
     * @return
     */
    public synchronized static boolean isDoubleClick() {

        long currentTime = System.currentTimeMillis();
        boolean isClickAble;
        if (currentTime - lastClickTime >
                SPACE_TIME) {
            isClickAble = false;
        } else {
            isClickAble = true;
        }
        lastClickTime = currentTime;
        return isClickAble;
    }

}
