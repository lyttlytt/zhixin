//package com.shuzhengit.zhixin.mine.question;
//
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.ActionBar;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.library.base.BaseActivity;
//import com.shuzhengit.zhixin.HomeViewPagerAdapter;
//import com.shuzhengit.zhixin.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.Unbinder;
//
//public class MineQuestionActivity extends BaseActivity {
//    @BindView(R.id.tvTitle)
//    TextView mTvTitle;
//    @BindView(R.id.tvRight)
//    TextView mTvRight;
//    @BindView(R.id.ivRightShare)
//    ImageView mIvRightShare;
//    @BindView(R.id.toolBar)
//    Toolbar mToolBar;
//    @BindView(R.id.tabLayout)
//    TabLayout mTabLayout;
//    @BindView(R.id.viewpager)
//    ViewPager mViewpager;
//    private Unbinder mUnbinder;
//    private String[] mTabNames = new String[]{"提问", "回答", "关注"};
//
//    @Override
//    protected int layoutId() {
//        return R.layout.activity_fans_with_follow;
//    }
//
//    @Override
//    protected void initView() {
//        mUnbinder = ButterKnife.bind(this);
//        setSupportActionBar(mToolBar);
//        ActionBar actionBar = getSupportActionBar();
//        assert actionBar!=null;
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        mTvTitle.setText("我的问答");
//        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//    }
//
//    @Override
//    protected void createPresenter() {
//        List<Fragment> fragments = new ArrayList<Fragment>();
//        for (int i = 0; i < mTabNames.length; i++) {
//            fragments.add(MineQuestionFragment.getInstance(i));
//        }
//        mViewpager.setAdapter(new HomeViewPagerAdapter(getSupportFragmentManager(),fragments){
//            @Override
//            public CharSequence getPageTitle(int position) {
//                return mTabNames[position];
//            }
//        });
//        mTabLayout.setupWithViewPager(mViewpager);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mUnbinder.unbind();
//    }
//
//}
