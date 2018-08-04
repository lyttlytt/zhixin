package com.shuzhengit.zhixin.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.shuzhengit.zhixin.R;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/16 19:06
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class PromptLoginDialog extends Dialog {
    public PromptLoginDialog(@NonNull Context context) {
        this(context,0);
    }

    public PromptLoginDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.baseDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.WHITE);
        drawable.setCornerRadius(30);
        Window window = getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.height=WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.width = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(attributes);
        setContentView(R.layout.dialog_prompt_login);
        findViewById(R.id.llRoot).setBackground(drawable);
        findViewById(R.id.tvGoLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mDialogSingleCallBack!=null){
                    mDialogSingleCallBack.callBackPositive(PromptLoginDialog.this);
                }
            }
        });
    }

    public void setDialogSingleCallBack(DialogSingleCallBack dialogSingleCallBack) {
        mDialogSingleCallBack = dialogSingleCallBack;
    }

    private DialogSingleCallBack mDialogSingleCallBack=null;
    public interface DialogSingleCallBack {
        void callBackPositive(DialogInterface dialog);
    }
}
