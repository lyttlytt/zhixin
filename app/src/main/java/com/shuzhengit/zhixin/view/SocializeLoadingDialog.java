package com.shuzhengit.zhixin.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.shuzhengit.zhixin.R;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/1 16:09
 * E-mail:yuancongbin@gmail.com
 */

public class SocializeLoadingDialog extends Dialog {
    public SocializeLoadingDialog(@NonNull Context context) {
        this(context,0);
    }

    public SocializeLoadingDialog(@NonNull Context context, @StyleRes int themeResId) {
//        super(context, com.library.R.style.baseDialog);
        super(context, R.style.baseDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width= WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height=WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.CENTER);
        ProgressBar progressBar = new ProgressBar(getContext());
        setContentView(progressBar);

    }
}
