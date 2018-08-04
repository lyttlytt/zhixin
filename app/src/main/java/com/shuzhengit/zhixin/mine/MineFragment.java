package com.shuzhengit.zhixin.mine;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.library.base.BaseLazyLoadFragment;
import com.library.rx.RxBus2;
import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.EventType;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.login.LoginActivity;
import com.shuzhengit.zhixin.mine.collect.CollectActivity;
import com.shuzhengit.zhixin.mine.history.HistoryActivity;
import com.shuzhengit.zhixin.mine.user.UserInfoActivity;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.shuzhengit.zhixin.sign_in.SignInActivity;
import com.shuzhengit.zhixin.sign_in.money.MoneyActivity;
import com.shuzhengit.zhixin.sign_in.score.ScoreRankActivity;
import com.shuzhengit.zhixin.util.CacheKeyManager;
import com.shuzhengit.zhixin.util.CacheUtils;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.util.GlideLoadImageUtils;
import com.shuzhengit.zhixin.view.CircleImageView;
import com.shuzhengit.zhixin.view.PromptLoginDialog;
import com.shuzhengit.zhixin.wenba.mine.MineWenBaActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Predicate;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/18 17:46
 * E-mail:yuancongbin@gmail.com
 */

public class MineFragment extends BaseLazyLoadFragment {
    private static final String TAG = "MineFragment";
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.civUserAvatar)
    CircleImageView mCivUserAvatar;
    @BindView(R.id.tvUserName)
    TextView mTvUserName;
    @BindView(R.id.rlUserInfo)
    RelativeLayout mRlUserInfo;
    @BindView(R.id.ivLeftIcon)
    ImageView mIvLeftIcon;
    @BindView(R.id.tvItemName)
    TextView mTvItemName;
    private RxSubscriber<EventType> mRxSubscriber;
    @BindView(R.id.tvSchool)
    TextView mTvSchool;
    @BindView(R.id.tvFansCount)
    TextView mTvFansCount;
    @BindView(R.id.tvFollowCount)
    TextView mTvFollowCount;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    private User mUser;
    private Unbinder mUnbinder;
    @Override
    public void onPause() {
        super.onPause();
        //统计页面
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        mUser = CheckUser.checkUserIsExists();
        if (mUser != null) {
            HttpProtocol.getApi()
                    .findUserInfoById(mUser.getId(), mUser.getId() + "")
                    .compose(RxSchedulersHelper.io_main())
                    .compose(RxResultHelper.handleResult())
                    .subscribeWith(new RxSubscriber<User>() {
                        @Override
                        protected void _onNext(User user) {
                            mUser = user;
                            CacheUtils.getCacheManager(APP.getInstance()).put(CacheKeyManager.USER, mUser);
                            initData();
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            dispose();
                        }

                        @Override
                        protected void _onCompleted() {
                            super._onCompleted();
                            dispose();
                        }
                    });
        }
        //登录成功后,回到这个页面 重新调用LazyLoadCreatePresenter,进行数据的更新
        mRxSubscriber = new RxSubscriber<EventType>() {
            @Override
            protected void _onNext(EventType eventType) {
                if (isAdded()) {
                    initData();
                }
            }

            @Override
            protected void _onError(String message) {

            }

            @Override
            protected void _onCompleted() {

            }
        };
        RxBus2.getDefault().toFlowable(EventType.class)
                .filter(new Predicate<EventType>() {
                    @Override
                    public boolean test(EventType eventType) throws Exception {
                        return eventType.getEventType().equals(EventCodeUtils.REFRESH_USER_INT);
                    }
                })
                .subscribe(mRxSubscriber);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(this,view);
//        mTvTitle = (TextView) view.findViewById(R.id.tvTitle);
//        mToolBar = (Toolbar) view.findViewById(R.id.toolBar);
//        mCivUserAvatar = (CircleImageView) view.findViewById(R.id.civUserAvatar);
//        mTvUserName = (TextView) view.findViewById(R.id.tvUserName);
//        mRlUserInfo = (RelativeLayout) view.findViewById(R.id.rlUserInfo);
//        mIvLeftIcon = (ImageView) view.findViewById(R.id.ivLeftIcon);
//        mTvItemName = (TextView) view.findViewById(R.id.tvItemName);
//        mTvSchool = (TextView) view.findViewById(R.id.tvSchool);
//        mTvFansCount = (TextView) view.findViewById(R.id.tvFansCount);
//        mTvFollowCount = (TextView) view.findViewById(R.id.tvFollowCount);
        getHoldingActivity().setSupportActionBar(mToolBar);
        ActionBar actionBar = getHoldingActivity().getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mTvTitle.setText("我的");
        mTvRight.setText("红包提现");
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MoneyActivity.class);
            }
        });
        ((ImageView) view.findViewById(R.id.dynamic).findViewById(R.id.ivMineTabImg)).setImageResource(R.drawable
                .ic_mine_dynamic);
        ((TextView) view.findViewById(R.id.dynamic).findViewById(R.id.tvMineTabName)).setText("动态");
        view.findViewById(R.id.dynamic).setOnClickListener(v -> {
            User user = CheckUser.checkUserIsExists();
            if (user != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("memberId", user.getId());
                startActivity(UserInfoActivity.class, bundle);
            } else {
                PromptLoginDialog promptLoginDialog = new PromptLoginDialog(getContext());
                promptLoginDialog.show();
                promptLoginDialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                    @Override
                    public void callBackPositive(DialogInterface dialog) {
                        startActivity(LoginActivity.class);
                    }
                });
            }
        });

        view.findViewById(R.id.history).setOnClickListener(v -> {
            User user = CheckUser.checkUserIsExists();
            if (user == null) {
                PromptLoginDialog promptLoginDialog = new PromptLoginDialog(getContext());
                promptLoginDialog.show();
                promptLoginDialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                    @Override
                    public void callBackPositive(DialogInterface dialog) {
                        startActivity(LoginActivity.class);
                    }
                });
                return;
            }
            startActivity(HistoryActivity.class);
        });
        ((ImageView) view.findViewById(R.id.history).findViewById(R.id.ivMineTabImg)).setImageResource(R.drawable
                .ic_mine_history);
        ((TextView) view.findViewById(R.id.history).findViewById(R.id.tvMineTabName)).setText("历史");




        view.findViewById(R.id.collect).setOnClickListener(v -> {
            User user = CheckUser.checkUserIsExists();
            if (user == null) {
                PromptLoginDialog promptLoginDialog = new PromptLoginDialog(getContext());
                promptLoginDialog.show();
                promptLoginDialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                    @Override
                    public void callBackPositive(DialogInterface dialog) {
                        startActivity(LoginActivity.class);
                    }
                });
                return;
            }
            startActivity(CollectActivity.class);
        });
        ((ImageView) view.findViewById(R.id.collect).findViewById(R.id.ivMineTabImg)).setImageResource(R.drawable
                .ic_mine_collect);
        ((TextView) view.findViewById(R.id.collect).findViewById(R.id.tvMineTabName)).setText("收藏");




        //打卡
        view.findViewById(R.id.signIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SignInActivity.class);
//                failure("signIn");
            }
        });
        ((TextView) view.findViewById(R.id.signIn).findViewById(R.id.tvItemName)).setText("打卡");
        ((ImageView) view.findViewById(R.id.signIn).findViewById(R.id.ivLeftIcon)).setImageResource(R.drawable
                .ic_sign_in);

        //积分排行榜
        view.findViewById(R.id.scoreRank).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ScoreRankActivity.class);
            }
        });
        ((TextView) view.findViewById(R.id.scoreRank).findViewById(R.id.tvItemName)).setText("积分排行榜");
        ((ImageView) view.findViewById(R.id.scoreRank).findViewById(R.id.ivLeftIcon)).setImageResource(R.drawable.ic_rank);



        ((ImageView) view.findViewById(R.id.alumni).findViewById(R.id.ivLeftIcon)).setImageResource(R
                .drawable.ic_mine_alumni);
        ((TextView) view.findViewById(R.id.alumni).findViewById(R.id.tvItemName)).setText("我的校友");
        view.findViewById(R.id.alumni).setOnClickListener(v -> failure(R.string.unFinish));

        ((ImageView) view.findViewById(R.id.mineMajor).findViewById(R.id.ivLeftIcon)).setImageResource(R
                .drawable.ic_mine_major);
        ((TextView) view.findViewById(R.id.mineMajor).findViewById(R.id.tvItemName)).setText("我的专业");
        view.findViewById(R.id.mineMajor).setOnClickListener(v -> failure(R.string.unFinish));

        ((ImageView) view.findViewById(R.id.mineQuestion).findViewById(R.id.ivLeftIcon)).setImageResource(R
                .drawable.ic_question_unselect);
        ((TextView) view.findViewById(R.id.mineQuestion).findViewById(R.id.tvItemName)).setText("我的问吧");
        view.findViewById(R.id.mineQuestion).setOnClickListener(v -> {
            User user = CheckUser.checkUserIsExists();
            if (user!=null){
                startActivity(MineWenBaActivity.class);
            }else {
                PromptLoginDialog promptLoginDialog = new PromptLoginDialog(getContext());
                promptLoginDialog.show();
                promptLoginDialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                    @Override
                    public void callBackPositive(DialogInterface dialog) {
                        startActivity(LoginActivity.class);
                    }
                });
            }
        });

        view.findViewById(R.id.feedback).setOnClickListener(v ->
        {
            User user = CheckUser.checkUserIsExists();
            if (user != null) {
                startActivity(FeedBackActivity.class);
            } else {
                PromptLoginDialog promptLoginDialog = new PromptLoginDialog(getContext());
                promptLoginDialog.show();
                promptLoginDialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                    @Override
                    public void callBackPositive(DialogInterface dialog) {
                        startActivity(LoginActivity.class);
                    }
                });
            }
        });
        ((ImageView) view.findViewById(R.id.feedback).findViewById(R.id.ivLeftIcon)).setImageResource(R
                .drawable.ic_mine_feedback);
        ((TextView) view.findViewById(R.id.feedback).findViewById(R.id.tvItemName)).setText("用户反馈");

        view.findViewById(R.id.systemSetting).setOnClickListener(v -> {
            startActivity(SystemSettingActivity.class);

        });
        ((ImageView) view.findViewById(R.id.systemSetting).findViewById(R.id.ivLeftIcon)).setImageResource(R
                .drawable.ic_mine_system_setting);
        ((TextView) view.findViewById(R.id.systemSetting).findViewById(R.id.tvItemName)).setText("系统设置");

    }

    @Override
    protected void lazyLoadCreatePresenter() {
        MobclickAgent.onPageStart(TAG); //统计页面

        if (isAdded()) {
            initData();
        }
    }

    private void initData() {


        if (mUser != null) {
            LogUtils.i(mUser.toString());
            if (TextUtils.isEmpty(mUser.getAvatarUrl())) {
                mCivUserAvatar.setImageResource(R.drawable.ic_normal_icon);
            } else {
                GlideLoadImageUtils.loadImg(APP.getInstance(), mUser.getAvatarUrl(), mCivUserAvatar);
            }
            mTvUserName.setText(mUser.getNickname());
            mRlUserInfo.setOnClickListener(v -> {
                startActivity(EditUserInfoActivity.class);
            });
            mTvSchool.setText(mUser.getSchoolName());
            mTvFansCount.setText(String.format(getString(R.string.userFansCount), mUser.getFansCount()));
            mTvFollowCount.setText(String.format(getString(R.string.userFollow), mUser.getFollowingCount()));
            mTvFansCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("memberId", mUser.getId());
                    startActivity(FansWithFollowActivity.class, bundle);
                }
            });
            mTvFollowCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("memberId", mUser.getId());
                    startActivity(FansWithFollowActivity.class, bundle);

                }
            });
        } else {
            mTvUserName.setText("未登录");
//            GlideLoadImageUtils.loadCircleImg(getContext(), R.drawable.ic_normal_icon, mCivUserAvatar, R.drawable
//                    .ic_normal_icon);
            mCivUserAvatar.setImageResource(R.drawable.ic_normal_icon);
            mRlUserInfo.setOnClickListener(v -> {
                startActivity(LoginActivity.class);
            });
            mTvFansCount.setText(String.format(getString(R.string.userFansCount), 0));
            mTvFollowCount.setText(String.format(getString(R.string.userFollow), 0));
            mTvFansCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PromptLoginDialog dialog = new PromptLoginDialog(getContext());
                    dialog.show();
                    dialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                        @Override
                        public void callBackPositive(DialogInterface dialog) {
                            startActivity(LoginActivity.class);
                        }
                    });
                }
            });
            mTvFollowCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PromptLoginDialog dialog = new PromptLoginDialog(getContext());
                    dialog.show();
                    dialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                        @Override
                        public void callBackPositive(DialogInterface dialog) {
                            startActivity(LoginActivity.class);
                        }
                    });

                }
            });
        }
//        String iconUrl = (String) SPUtils.get(getContext(), "iconUrl", "");
//        if (!TextUtils.isEmpty(iconUrl)) {
//            LogUtils.e(iconUrl);
//            GlideLoadImageUtils.loadImg(getContext(), iconUrl, mCivUserAvatar);
//        }
//        String name = (String) SPUtils.get(getContext(), "name", "");
//        if (!TextUtils.isEmpty(name))
//            mTvUserName.setText(name);
    }

    @Override
    public void onStart() {
        super.onStart();
//        LogUtils.i(TAG,"onStart");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (mRxSubscriber != null && !mRxSubscriber.isDisposed()) {
            mRxSubscriber.dispose();
        }
    }

}
