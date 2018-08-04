package com.shuzhengit.zhixin.util;

import android.content.Context;

import com.library.util.LogUtils;

import java.io.File;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/5 15:49
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class CacheUtils {
    private static CacheManager sCacheManager;
    private  CacheUtils(Context context) {
        //缓存
        File file = new File(context.getCacheDir().getAbsoluteFile() + "/zhixin");
        if (!file.exists()){
            file.mkdirs();
        }
        LogUtils.i(" cache path : " +file.getAbsolutePath());
        sCacheManager = CacheManager.get(file);
    }

    public static CacheManager getCacheManager(Context context) {
        if (sCacheManager==null) {
            new CacheUtils(context.getApplicationContext());
        }
        return sCacheManager;
    }
}
