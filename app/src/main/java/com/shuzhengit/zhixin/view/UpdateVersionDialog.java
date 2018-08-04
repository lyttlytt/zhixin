package com.shuzhengit.zhixin.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import com.shuzhengit.zhixin.R;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/10/9 16:22
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class UpdateVersionDialog extends Dialog {
    public UpdateVersionDialog(@NonNull Context context) {
        this(context, R.style.baseDialog);
    }

    public UpdateVersionDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.baseDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
