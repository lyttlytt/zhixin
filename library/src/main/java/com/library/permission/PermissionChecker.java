package com.library.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/6/23 17:45
 * E-mail:yuancongbin@gmail.com
 * 检测是权限工具类
 */

public class PermissionChecker {
    /**
     * 是否缺少权限
     *
     * @return true 缺少 false 已经有权限了
     */
    public static boolean lacksPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context.getApplicationContext(), permission) == PackageManager.PERMISSION_DENIED;
    }
}
