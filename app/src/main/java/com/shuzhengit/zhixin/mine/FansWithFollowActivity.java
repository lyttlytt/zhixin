package com.shuzhengit.zhixin.mine;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.shuzhengit.zhixin.HomeViewPagerAdapter;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.mine.fragment.fans.FansOrFollowFragment;
import com.shuzhengit.zhixin.util.CheckUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FansWithFollowActivity extends BaseActivity {

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
    ViewPager mViewpager;
    private int mMemberId;
    private Unbinder mUnbinder;
    private User mUser;

    @Override
    protected int layoutId() {
        return R.layout.activity_fans_with_follow;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        mMemberId = getIntent().getIntExtra("memberId", 0);
        mUser = CheckUser.checkUserIsExists();
        if (mMemberId == 0) {
            failure("操作错误,请稍后重试");
            onBackPressed();
            return;
        }
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar!=null;
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void createPresenter() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FansOrFollowFragment.getInstance(FansOrFollowFragment.TYPE_FANS, mMemberId));
        fragments.add(FansOrFollowFragment.getInstance(FansOrFollowFragment.TYPE_FOLLOW, mMemberId));
        mViewpager.setAdapter(new HomeViewPagerAdapter(getSupportFragmentManager(), fragments) {
            @Override
            public CharSequence getPageTitle(int position) {
                if (mUser != null && mUser.getId() == mMemberId) {
                    if (position == 1) {
                        return "我的关注";
                    } else {
                        return "我的粉丝";
                    }
                } else {
                    if (position == 1) {
                        return "TA的关注";
                    } else {
                        return "TA的粉丝";
                    }
                }
            }
        });
        mTabLayout.setupWithViewPager(mViewpager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
