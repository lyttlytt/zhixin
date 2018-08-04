package com.shuzhengit.zhixin.wenba.mine;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.library.bean.BaseCallModel;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.HomeViewPagerAdapter;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.ApplyAdmin;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.shuzhengit.zhixin.util.CheckUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MineWenBaActivity extends BaseActivity {


    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;

    private String[] tabAdminNames = new String[]{"我关注的", "我发布的", "待回答的"};
    private String[] tabNames = new String[]{"我关注的", "我发布的"};
    private Unbinder mUnbinder;

    @Override
    protected int layoutId() {
        return R.layout.activity_mine_wen_ba;
    }

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
        mTvTitle.setText("我的问吧");
//        ArrayList<Fragment> fragments = new ArrayList<>();
//        fragments.add(MineFollowFragment.getInstance());
//        fragments.add(MineReleaseAskFragment.getInstance());
//        fragments.add(UnReplyFragment.getInstance());
//        mViewpager.setAdapter(new HomeViewPagerAdapter(getSupportFragmentManager(), fragments) {
//            @Override
//            public CharSequence getPageTitle(int position) {
//                return tabNames[position];
//            }
//        });
//        mTabLayout.setupWithViewPager(mViewpager);
    }

    @Override
    protected void createPresenter() {
        User user = CheckUser.checkUserIsExists();
//        status 0 审核中 1通过 2 未通过
        HttpProtocol.getApi()
                .checkIsAdmin(user.getId())
                .compose(RxSchedulersHelper.io_main())
                .subscribeWith(new RxSubscriber<BaseCallModel<ApplyAdmin>>() {
                    @Override
                    protected void _onNext(BaseCallModel<ApplyAdmin> baseCallModel) {
                        ApplyAdmin applyAdmin = baseCallModel.getData();
                        if (applyAdmin != null && applyAdmin.getStatus()==1) {
                            hasAdmin(true);
                        } else {
                            hasAdmin(false);
                        }
                    }
                });
    }

    private void hasAdmin(Boolean isAdmin) {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(MineFollowFragment.getInstance());
        fragments.add(MineReleaseAskFragment.getInstance());
        if (isAdmin) {
            fragments.add(UnReplyFragment.getInstance());
        }
        mViewpager.setAdapter(new HomeViewPagerAdapter(getSupportFragmentManager(), fragments) {
            @Override
            public CharSequence getPageTitle(int position) {
                if (isAdmin) {
                    return tabAdminNames[position];
                } else {
                    return tabNames[position];
                }
            }
        });
        mTabLayout.setupWithViewPager(mViewpager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }
}
