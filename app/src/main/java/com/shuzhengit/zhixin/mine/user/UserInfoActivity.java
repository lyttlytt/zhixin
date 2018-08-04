package com.shuzhengit.zhixin.mine.user;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.HomeViewPagerAdapter;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.mine.FansWithFollowActivity;
import com.shuzhengit.zhixin.mine.fragment.dynamic.DynamicFragment;
import com.shuzhengit.zhixin.mine.fragment.dynamic.DynamicQuestionFragment;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.util.GlideLoadImageUtils;
import com.shuzhengit.zhixin.util.ResourceUtils;
import com.shuzhengit.zhixin.view.CircleImageView;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UserInfoActivity extends BaseActivity<UserInfoContract.Presenter> implements UserInfoContract.View {
    @BindView(R.id.layoutToolbar)
    Toolbar mToolbar;

    @BindView(R.id.civUserAvatar)
    CircleImageView mUserAvatar;

    @BindView(R.id.btnFollow)
    Button mBtnFollow;

    @BindView(R.id.tvSendMessage)
    TextView mTvSendMessage;

    @BindView(R.id.userName)
    TextView mTvUserName;

    @BindView(R.id.tvFollowCount)
    TextView mTvFollowCount;

    @BindView(R.id.tvFansCount)
    TextView mTvFansCount;

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    int mQueryUserId;
    private User mCacheUser;
    private Unbinder mUnbinder;

    @Override
    protected int layoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar!=null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        mQueryUserId = getIntent().getIntExtra("memberId", 0);
    }

    @Override
    protected void createPresenter() {
        mTvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("memberId", mQueryUserId);
                startActivity(FansWithFollowActivity.class, bundle);
            }
        });
        findViewById(R.id.llFollows).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("memberId", mQueryUserId);
                startActivity(FansWithFollowActivity.class, bundle);
            }
        });

        if (mQueryUserId == 0) {
            failure("错误操作,请稍后重试");
            onBackPressed();
            return;
        } else {
            User user = CheckUser.checkUserIsExists();
            if (user != null) {
                mBasePresenter = new UserPresenter(this);
                mBasePresenter.findUser(user.getId(), mQueryUserId);
            } else {
                failure("请先登录");
                onBackPressed();
            }
        }
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(DynamicFragment.getInstance(mQueryUserId));
        fragments.add(DynamicQuestionFragment.getInstance(mQueryUserId));
        mViewpager.setAdapter(new HomeViewPagerAdapter(getSupportFragmentManager(), fragments) {
            @Override
            public CharSequence getPageTitle(int position) {

                if (position == 0) {
                    return "动态";
                } else {
                    return "问吧";
                }
            }
        });
        mTabLayout.setupWithViewPager(mViewpager);
    }

    @Override
    public void findUserSuccess(User user) {
        if (TextUtils.isEmpty(user.getAvatarUrl())) {
            mUserAvatar.setImageResource(R.drawable.ic_normal_icon);
        } else {
            GlideLoadImageUtils.loadImg(this, user.getAvatarUrl(), mUserAvatar);
        }

        mTvUserName.setText(user.getNickname());
        mTvFansCount.setText(String.valueOf(user.getFansCount()));
        mTvFollowCount.setText(String.valueOf(user.getFollowingCount()));
        mCacheUser = CheckUser.checkUserIsExists();
        if (user.getId() == mCacheUser.getId()) {
            mBtnFollow.setVisibility(View.GONE);
            mTvSendMessage.setVisibility(View.GONE);
            return;
        }
        GradientDrawable background = (GradientDrawable) mBtnFollow.getBackground();
        if (mCacheUser.getId() != user.getId() && user.getFollowed() == 1) {
            background.setColor(ResourceUtils.getResourceColor(APP.getInstance(),R.color.grey300));
            setFollowBackground("已关注", background);
        } else if (mCacheUser.getId() != user.getId() && user.getFollowed() == 0) {
            background.setColor(ResourceUtils.getResourceColor(APP.getInstance(),R.color.colorPrimary));
            setFollowBackground("关注", background);
        } else {
            mBtnFollow.setVisibility(View.GONE);
        }
        mBtnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradientDrawable background = (GradientDrawable) mBtnFollow.getBackground();
                if (user.getFollowed() == 0) {
                    background.setColor(ResourceUtils.getResourceColor(APP.getInstance(),R.color.grey300));
                    setFollowBackground("已关注", background);
                    mBtnFollow.setTextColor(Color.BLACK);
                    mBasePresenter.followUser(mCacheUser.getId(), user.getId());
                    user.setFollowed(1);
                } else {
                    user.setFollowed(0);
                    background.setColor(ResourceUtils.getResourceColor(APP.getInstance(),R.color.colorPrimary));
                    setFollowBackground("关注", background);
                    mBtnFollow.setTextColor(Color.WHITE);
                    mBasePresenter.unFollowUser(mCacheUser.getId(), user.getId());
                }
            }
        });
    }

    private void setFollowBackground(String follow, GradientDrawable background) {
        mBtnFollow.setText(follow);
        mBtnFollow.setBackground(background);
    }

    @Override
    public void followSuccess() {
    }

    @Override
    public void unFollowSuccess() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);//完成回调
    }
}
