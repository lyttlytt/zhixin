package com.shuzhengit.zhixin.sign_in;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.sign_in.calendar.CalendarActivity;
import com.shuzhengit.zhixin.view.SignInView;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 打卡界面
 */
public class SignInActivity extends BaseActivity<SignInContract.Presenter> implements SignInContract.View {

    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.siMorning)
    SignInView mSiMorning;
    @BindView(R.id.siNight)
    SignInView mSiNight;
    private Unbinder mUnbinder;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private SimpleDateFormat mSdf;

    @Override
    protected int layoutId() {
        return R.layout.activity_sign_in;
    }
    private  Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mSiMorning.setText(mSdf.format(System.currentTimeMillis()));
            mSiNight.setText(mSdf.format(System.currentTimeMillis()));
            mHandler.postDelayed(mRunnable,1000);
        }
    };
    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTvTitle.setText("打卡");
        mSdf = new SimpleDateFormat("hh:mm:ss");
        mHandler.postDelayed(mRunnable,1000);
        mTvRight.setText("打卡日历");
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CalendarActivity.class);
            }
        });

    }

    @Override
    protected void createPresenter() {

    }

    @Override
    public void morningSignInSuccess() {

    }

    @Override
    public void nightSignInSuccess() {

    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        mUnbinder.unbind();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
