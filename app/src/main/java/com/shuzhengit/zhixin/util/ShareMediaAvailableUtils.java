package com.shuzhengit.zhixin.util;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.library.util.DeviceUtil;
import com.library.util.LogUtils;

import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/2 09:34
 * E-mail:yuancongbin@gmail.com
 */


public class ShareMediaAvailableUtils {
    private static final String TAG = "ShareMediaAvailableUtil";
    /**
     * 判断用户手机中是否安装了微信
     *
     * @param context
     * @return true已安装／false未安装
     */
    public static boolean isWeChatAvailable(Context context) {
        List<PackageInfo> deviceInstallPackages = DeviceUtil.getDeviceInstallPackages(context.getApplicationContext());
        for (int i = 0; i < deviceInstallPackages.size(); i++) {
            PackageInfo packageInfo = deviceInstallPackages.get(i);
            String packageName = packageInfo.packageName;
            if ("com.tencent.mm".equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断用户手机中是否安装qq
     *
     * @param context
     * @return true已安装/false 未安装
     */
    public static boolean isQQAvailable(Context context) {
        List<PackageInfo> deviceInstallPackages = DeviceUtil.getDeviceInstallPackages(context.getApplicationContext());
        for (int i = 0; i < deviceInstallPackages.size(); i++) {
            PackageInfo packageInfo = deviceInstallPackages.get(i);
            String packageName = packageInfo.packageName;
            if ("com.tencent.mobileqq".equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断用户手机中是否安装新浪微博
     *
     * @param context
     * @return true已安装/false未安装
     */
    public static boolean isSinaWeiBo(Context context) {
        List<PackageInfo> deviceInstallPackages = DeviceUtil.getDeviceInstallPackages(context.getApplicationContext());
            for (int i = 0; i < deviceInstallPackages.size(); i++) {
                PackageInfo packageInfo = deviceInstallPackages.get(i);
                String packageName = packageInfo.packageName;
                LogUtils.i(TAG,packageName);
                if ("com.sina.weibo".equals(packageName)) {
                    return true;
                }
            }
        return false;
    }
}
