package com.shuzhengit.zhixin.sign_in.calendar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CalendarActivity extends BaseActivity {
    private static final String TAG = "CalendarActivity";

    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.calendarView)
    CalendarView mCalendarView;
    private Unbinder mUnbinder;

    @Override
    protected int layoutId() {
        return R.layout.activity_calendar;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        mTvTitle.setText("打卡日历");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mCalendarView.setSelected(true);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        LogUtils.d(TAG,year+"年" + (month+1) + "月 " + dayOfMonth + "日");

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
