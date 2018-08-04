package com.shuzhengit.zhixin.sign_in.money;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.view.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MoneyActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.civAvatar)
    CircleImageView mCivAvatar;
    @BindView(R.id.tvUserName)
    TextView mTvUserName;
    private Unbinder mUnbinder;

    @Override
    protected int layoutId() {
        return R.layout.activity_money;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        mTvTitle.setText("红包提现");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void createPresenter() {

    }

    @Override
    protected void onDestroy() {
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
