package com.shuzhengit.zhixin.mine.history;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.shuzhengit.zhixin.HomeViewPagerAdapter;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.index.document.holder.MultipleAdapter;
import com.shuzhengit.zhixin.util.CheckUser;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HistoryActivity extends BaseActivity {
    private static final String TAG = "HistoryActivity";
    @BindView(R.id.tvTitle)
    TextView mTvTitle;

    @BindView(R.id.tvRight)
    TextView mTvRight;

    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;

    @BindView(R.id.toolBar)
    Toolbar mToolBar;

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private int mCurrentPage = 1;
    private User mUser;
    private Unbinder mUnbinder;
    private MultipleAdapter mMultipleAdapter;

    @Override
    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart(TAG);
        //统计时长
        MobclickAgent.onResume(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        //统计页面
        MobclickAgent.onPageEnd(TAG);
        //统计页面的时长
        MobclickAgent.onPause(this);

    }

    @Override
    protected int layoutId() {
        return R.layout.activity_history;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        mTvRight = (TextView) findViewById(R.id.tvRight);
        mIvRightShare = (ImageView) findViewById(R.id.ivRightShare);
        mToolBar = (Toolbar) findViewById(R.id.toolBar);

        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        mToolBar.setNavigationOnClickListener(v -> onBackPressed());
        mUser = CheckUser.checkUserIsExists();
        mTvTitle.setText("浏览历史");
    }

    @Override
    protected void createPresenter() {
        List<Fragment> list = new ArrayList<>();
        HistoryDocumentFragment historyDocumentFragment = HistoryDocumentFragment.getInstance(mUser.getId());
        list.add(historyDocumentFragment);
        HistoryQuestionFragment historyQuestionFragment = HistoryQuestionFragment.getInstance(mUser.getId());
        list.add(historyQuestionFragment);
        mViewPager.setAdapter(new HomeViewPagerAdapter(getSupportFragmentManager(), list) {
            @Override
            public CharSequence getPageTitle(int position) {

                if (position == 0) {
                    return "文章";
                } else {
                    return "问吧";
                }
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

}
