package com.shuzhengit.zhixin;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.library.util.SPUtils;
import com.shuzhengit.zhixin.util.CacheKeyManager;


/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/11 11:31
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class SplashFragment extends DialogFragment {

    private TextView mTvTimeCount;
    private CountDownTimer mCountDownTimer;
    private Activity mParentActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mParentActivity = activity;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        SPUtils.put(mParentActivity, CacheKeyManager.SHOW_SPLASH,false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        mTvTimeCount = (TextView) view.findViewById(R.id.tvTimeCount);
        GradientDrawable background = (GradientDrawable) mTvTimeCount.getBackground();
//        int resourceColor = ResourceUtils.getResourceColor(APP.getInstance(), Color.parseColor("#66666666"));
        int resourceColor =Color.parseColor("#88666666");
        background.setColor(resourceColor);
        mTvTimeCount.setBackground(background);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCountDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTvTimeCount.setText(String.valueOf((millisUntilFinished/1000) +"s后跳转"));
            }

            @Override
            public void onFinish() {
                SPUtils.put(APP.getInstance(), CacheKeyManager.SHOW_SPLASH, true);
                SplashFragment.super.dismiss();
            }
        }.start();
        mTvTimeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.put(APP.getInstance(), CacheKeyManager.SHOW_SPLASH, true);
                mCountDownTimer.cancel();
                SplashFragment.super.dismiss();
            }
        });
    }

   public void onHide(){
       mCountDownTimer.cancel();
       dismiss();
   }
}
