package com.shuzhengit.zhixin.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/6/22 15:16
 * E-mail:yuancongbin@gmail.com
 */

public class ResourceUtils {
    private static final ResourceUtils OUR_INSTANCE = new ResourceUtils();

    static ResourceUtils getInstance() {
        return OUR_INSTANCE;
    }

    private ResourceUtils() {
    }
    public static Drawable getResourceDrawable(Context context,int resId){
        return ContextCompat.getDrawable(context, resId);
    }
    /**
     * 获取xml文件中的颜色
     *
     * @param resId 颜色的id
     */
    public static int getResourceColor(Context context,int resId) {
        return ContextCompat.getColor(context.getApplicationContext(), resId);
    }

    /**
     * 获取xml文件中的字符串
     * @param resId id
     * @return
     */
    public static String getResourceString(Context context,int resId){
        return context.getApplicationContext().getString(resId);
    }

    /**
     * 获取xml文件中的字符串数组
     * @param resId
     * @return
     */
    public static String[] getResourceStringArray(Context context,int resId) {
        return context.getApplicationContext().getResources().getStringArray(resId);
    }
}
