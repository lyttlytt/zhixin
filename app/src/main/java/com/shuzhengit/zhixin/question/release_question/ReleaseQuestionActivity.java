package com.shuzhengit.zhixin.question.release_question;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.library.rx.RxBus2;
import com.library.util.LogUtils;
import com.library.view.NoScrollViewPager;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.HomeViewPagerAdapter;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.EventType;
import com.shuzhengit.zhixin.bean.Question;
import com.shuzhengit.zhixin.bean.QuestionTag;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.util.KeyBorderLeakUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ReleaseQuestionActivity extends BaseActivity<ReleaseQuestionContract.Presenter> implements
        ReleaseQuestionContract.View {

    private static final String TAG = "ReleaseQuestionActivity";
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.viewpager)
    NoScrollViewPager mViewPager;

    private User mUser;
    private ReleaseQuestionFragment mReleaseQuestionFragment;
    private Question mReleaseSuccessQuestion;
    private ReleaseQuestionAddTagFragment mReleaseQuestionAddTagFragment;
    private List<Fragment> mFragments = new ArrayList<>();
    private Unbinder mUnbinder;

    public Question getReleaseSuccessQuestion() {
        return mReleaseSuccessQuestion;
    }

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
        return R.layout.activity_release_question;
    }


    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        mUser = CheckUser.checkUserIsExists();
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(v -> {
//            RxBus2.getDefault().post(new EventType<String>(EventCodeUtils.RELEASE_REFRESH_QUESTION,""));
            onBackPressed();
        });

        mReleaseQuestionFragment = ReleaseQuestionFragment.getInstance(mUser.getId());
        mReleaseQuestionAddTagFragment = ReleaseQuestionAddTagFragment.getInstance(mUser.getId());
        mFragments.add(mReleaseQuestionFragment);
        mFragments.add(mReleaseQuestionAddTagFragment);

        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText("下一步");
        mTvTitle.setText("发布一个问题");
        mViewPager.setAdapter(new HomeViewPagerAdapter(getSupportFragmentManager(), mFragments));
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 1) {
                    mTvRight.setText("发布");
                    mTvTitle.setText("");
                } else {
                    mTvRight.setVisibility(View.VISIBLE);
                    mTvRight.setText("下一步");
                    mTvTitle.setText("发布一个问题");
                }
            }
        });

        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = mViewPager.getCurrentItem();
                if (currentItem != 1) {
                    addTag();
                } else {
//
                    List<String> tags = mReleaseQuestionAddTagFragment.getTags();
                    LogUtils.d(TAG,tags.toString());
                    mBasePresenter
                            .releaseQuestion(mUser.getId(),
                                    mReleaseQuestionFragment.getTitle(),
                            mReleaseQuestionFragment.getDescription(),
                                    mReleaseQuestionFragment.getUpLoadImage(),1,
                                    tags);
                }
            }
        });
    }

    private void addTag() {
        if (TextUtils.isEmpty(mReleaseQuestionFragment.getTitle())) {
            failure("问题标题不能为空");
            return;
        }
        mBasePresenter.findQuestionTag(mReleaseQuestionFragment.getTitle());
        mViewPager.setCurrentItem(1);
    }

    @Override
    protected void createPresenter() {
        mBasePresenter = new ReleaseQuestionPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            KeyBorderLeakUtil.fixFocusedViewLeak(APP.getInstance());
        }
    }

    @Override
    public void goReleaseResult() {
        failure("发布成功");
        RxBus2.getDefault().post(new EventType<Integer>(EventCodeUtils.RELEASE_REFRESH_QUESTION,1001));
//        RxBus2.getDefault().post(new EventType<Integer>(EventCodeUtils.REFRESH_QUESTIONS, category));
        onBackPressed();
    }



    @Override
    public void setQuestionKeyWords(List<QuestionTag> tags) {
        mReleaseQuestionAddTagFragment.setQuestionTags(tags);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode==KeyEvent.KEYCODE_BACK){
////            RxBus2.getDefault().post(new EventType<String>(EventCodeUtils.RELEASE_REFRESH_QUESTION,""));
//            onBackPressed();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
