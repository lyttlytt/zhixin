package com.shuzhengit.zhixin.index.document.detail.holder;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.library.util.LogUtils;
import com.shuzhengit.zhixin.index.document.PhotoViewActivity;
import com.shuzhengit.zhixin.util.EventCodeUtils;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/12/8 09:36
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */
public class JavascriptInterfaceImpl {
    private static final String TAG = "JavascriptInterfaceImpl";
    private Context context;
    private String mContent;

    public JavascriptInterfaceImpl(Context context,String content) {
        this.context = context;
        mContent = content;
    }
    @JavascriptInterface
    public void openImage(String img) {
        //
        Intent intent = new Intent();
        intent.putExtra(EventCodeUtils.DOCUMENT_PICTURE, img);
        intent.setClass(context, PhotoViewActivity.class);
        context.startActivity(intent);
    }

    @JavascriptInterface
    public String modifyBody(){
        LogUtils.d(TAG,"change body content");
        return mContent;
    }
}
