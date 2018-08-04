package com.library.permission;

import java.util.List;

/**
 * =========================================================
 * <p>
 * 描述:  监听器
 * =========================================================
 */
public interface AcpListener {
    /**
     * 同意
     */
    void onGranted();

    /**
     * 拒绝
     */
    void onDenied(List<String> permissions);
}
