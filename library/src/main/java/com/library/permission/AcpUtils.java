package com.library.permission;

import android.content.Context;


public class AcpUtils {
    private static AcpUtils mInstance;
    private AcpManager mAcpManager;

    public static AcpUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (AcpUtils.class) {
                if (mInstance == null) {
                    mInstance = new AcpUtils(context);
                }
            }
        }
        return mInstance;
    }

    private AcpUtils(Context context) {
        mAcpManager = new AcpManager(context.getApplicationContext());
    }

    /**
     * 开始请求
     *
     * @param options
     * @param acpListener
     */
    public void request(AcpOptions options, AcpListener acpListener) {
        if (options == null) {
            new NullPointerException("AcpOptions is null...");
        }
        if (acpListener == null) {
            new NullPointerException("AcpListener is null...");
        }
        mAcpManager.request(options, acpListener);
    }

    AcpManager getAcpManager() {
        return mAcpManager;
    }
}
