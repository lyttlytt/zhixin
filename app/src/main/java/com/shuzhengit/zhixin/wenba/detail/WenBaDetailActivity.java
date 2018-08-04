package com.shuzhengit.zhixin.wenba.detail;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.library.bean.BaseCallModel;
import com.library.rx.RxSchedulersHelper;
import com.library.statusbar.StatusBarUtil;
import com.library.util.KeyBorderUtil;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.AskWithReply;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.bean.WenBa;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.login.LoginActivity;
import com.shuzhengit.zhixin.mine.user.UserInfoActivity;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.view.PromptLoginDialog;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;
import com.shuzhengit.zhixin.wenba.ApplyAdminActivity;
import com.shuzhengit.zhixin.wenba.detail.reply.ReplyDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class WenBaDetailActivity extends BaseActivity<WenBaDetailContract.Presenter> implements WenBaDetailContract
        .View {

    private static final String TAG = "WenBaDetailActivity";
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.pull2RecyclerView)
    PullToRefreshRecyclerView mPull2RecyclerView;
    @BindView(R.id.etComment)
    EditText mEtComment;
    @BindView(R.id.ibSendComment)
    ImageButton mIbSendComment;
    @BindView(R.id.rlAsk)
    RelativeLayout mRlAsk;
    private Unbinder mUnbinder;
    private int mDistance = 0;
    private int maxDistance = 255;//当距离在[0,255]变化时，透明度在[0,255之间变化]
    private WenBaDetailAdapter mWenBaDetailAdapter;
    private int mAdminId;
    private int mCurrentPage = 1;
    private DataContainer<AskWithReply> mDataContainer;
    private int mWenBaId;
    private AnimationSet mAnimationSet;

    @Override
    protected int layoutId() {
        return R.layout.activity_wen_ba_detail;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowTitleEnabled(false);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mToolBar.setBackgroundColor(Color.alpha(255));
        mPull2RecyclerView.setShowPullToRefresh(false);
        mWenBaDetailAdapter = new WenBaDetailAdapter(this);
        mPull2RecyclerView.setAdapter(mWenBaDetailAdapter);
        mPull2RecyclerView.setLoadMoreListener(new PullToRefreshRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mDataContainer!=null && mDataContainer.isHasNextPage()){
                    User user = CheckUser.checkUserIsExists();
                    if (mWenBaDetailAdapter.isNew()){
                        mBasePresenter.findAskWithReplysOnNew(++mCurrentPage,10,mWenBaId,user==null?0:user.getId());
                    }else {
                        mBasePresenter.findAskWithReplysOnHot(++mCurrentPage,10,mWenBaId,user==null?0:user.getId());
                    }
                }else {
                    loadNoMore();
                }
//                mDataContainer.isHasNextPage()
            }
        });
        mWenBaDetailAdapter.setOnItemClickListener(new WenBaDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AskWithReply askWithReply) {
                Bundle bundle = new Bundle();
                bundle.putInt("askId", askWithReply.getId());
                startActivity(ReplyDetailActivity.class, bundle);
            }
        });
        mWenBaDetailAdapter.setOnAskIsNewOrHotListener(new WenBaDetailAdapter.OnAskIsNewOrHotListener() {
            @Override
            public void onNewOrHot(boolean isNew) {
                mCurrentPage = 1;
                User user = CheckUser.checkUserIsExists();
                if (isNew) {
                    mBasePresenter.refreshAskWithReplyOnNew(mCurrentPage, 10, mWenBaId,user == null ? 0 : user.getId());
                }else {
                    mBasePresenter.refreshAskWithReplyOnHot(mCurrentPage, 10,mWenBaId, user == null ? 0 : user.getId());

                }
            }
        });
        mWenBaDetailAdapter.setOnItemAgreeAskListener(new WenBaDetailAdapter.OnItemAgreeAskListener() {
            @Override
            public void onItemAgreeAsk(AskWithReply askWithReply, TextView tvAddOne) {
                User user = CheckUser.checkUserIsExists();
                if (user != null) {
                    mBasePresenter.agreeAsk(askWithReply.getId(), user.getId());
                    tvAddOne.setVisibility(View.VISIBLE);
                    tvAddOne.startAnimation(mAnimationSet);
                    mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            tvAddOne.setVisibility(View.GONE);
                            askWithReply.setIsSupport(1);
                            int i = askWithReply.getLikeCount() + 1;
                            askWithReply.setLikeCount(i);
                            mWenBaDetailAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                } else {
                    PromptLoginDialog promptLoginDialog = new PromptLoginDialog(WenBaDetailActivity.this);
                    promptLoginDialog.show();
                    promptLoginDialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                        @Override
                        public void callBackPositive(DialogInterface dialog) {
                            startActivity(LoginActivity.class);
                        }
                    });
                }
            }
        });
        mPull2RecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mDistance += dy;
                float percent = mDistance * 1f / maxDistance;//百分比
                int alpha = (int) (percent * 255);
                setToolBarAlphaColor(alpha);
            }
        });
        mIbSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mEtComment.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    failure("内容不能为空");
                    return;
                }
                User user = CheckUser.checkUserIsExists();
                if (user == null) {
                    PromptLoginDialog dialog = new PromptLoginDialog(WenBaDetailActivity.this);
                    dialog.show();
                    dialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                        @Override
                        public void callBackPositive(DialogInterface dialog) {
                            startActivity(LoginActivity.class);
                        }
                    });
                    return;
                }
                if (mWenBaId != 0) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("answerUserId", mWenBaId);
                        jsonObject.put("description", s);
                        jsonObject.put("userId", user.getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                            jsonObject.toString());
                    HttpProtocol.getApi()
                            .postAsk(requestBody)
                            .compose(RxSchedulersHelper.io_main())
                            .subscribeWith(new RxSubscriber<BaseCallModel>() {
                                @Override
                                protected void _onNext(BaseCallModel callModel) {
                                    if (callModel.code == 200) {
                                        failure("提交成功,请在我的问吧中查看");
                                        mEtComment.setText("");
                                        KeyBorderUtil.hideSoftInput(WenBaDetailActivity.this, mEtComment);
                                    }
                                }
                            });
                }
            }
        });
        mWenBaDetailAdapter.setOnItemFollowListener(new WenBaDetailAdapter.OnItemFollowListener() {
            @Override
            public void onItemFollow(WenBa wenBa) {
                User user = CheckUser.checkUserIsExists();
                if (user != null) {
                    mBasePresenter.followedWenBa(user.getId(), wenBa.getId());
                    if (wenBa.getIsUp() == 0) {
                        wenBa.setIsUp(1);
                    } else {
                        wenBa.setIsUp(0);
                    }
                    int i = wenBa.getLikeCount() + 1;
                    wenBa.setLikeCount(i);
                    mWenBaDetailAdapter.notifyDataSetChanged();
                } else {
                    PromptLoginDialog dialog = new PromptLoginDialog(WenBaDetailActivity.this);
                    dialog.show();
                    dialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                        @Override
                        public void callBackPositive(DialogInterface dialog) {
                            startActivity(LoginActivity.class);
                        }
                    });
                }
            }
        });
        mEtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mIbSendComment.setImageResource(R.drawable.ic_no_send);
                } else {
                    mIbSendComment.setImageResource(R.drawable.ic_send_comment);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mWenBaDetailAdapter.setOnItemClickUserListener(new WenBaDetailAdapter.OnItemClickUserListener() {
            @Override
            public void onItemClickUserListener(int memberId) {
                User user = CheckUser.checkUserIsExists();
                if (user == null) {
                    PromptLoginDialog promptLoginDialog = new PromptLoginDialog(WenBaDetailActivity.this);
                    promptLoginDialog.show();
                    promptLoginDialog.setDialogSingleCallBack(dialog -> startActivity(LoginActivity.class));
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("memberId", memberId);
                    startActivity(UserInfoActivity.class, bundle);
                }
            }
        });

    }

    @Override
    public void setStatusBarColor() {
        super.setStatusBarColor();
        StatusBarUtil.setStatusBarColor(this,Color.BLACK);
    }

    @Override
    protected void createPresenter() {
        mAnimationSet = new AnimationSet(this, null);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.3f);
        alphaAnimation.setDuration(1000);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -50);
        translateAnimation.setDuration(1000);
        mAnimationSet.addAnimation(translateAnimation);
        mAnimationSet.addAnimation(alphaAnimation);

        mBasePresenter = new WenBaDetailPresenter(this);
        mWenBaId = getIntent().getIntExtra("wenBaId", 0);
        if (mWenBaId == 0) {
            failure("问吧id错误");
            onBackPressed();
            return;
        }
        User user = CheckUser.checkUserIsExists();
        if (user != null) {
            boolean aNew = mWenBaDetailAdapter.isNew();
            mBasePresenter.findWenBaDetailById(mWenBaId, user.getId());
            if (aNew) {
                mBasePresenter.refreshAskWithReplyOnNew(mCurrentPage, 10, mWenBaId, user.getId());
            }else {
                mBasePresenter.refreshAskWithReplyOnHot(mCurrentPage, 10, mWenBaId, user.getId());

            }
        } else {
            mBasePresenter.findWenBaDetailById(mWenBaId, 0);
            boolean aNew = mWenBaDetailAdapter.isNew();
            if (aNew) {
                mBasePresenter.refreshAskWithReplyOnNew(mCurrentPage, 10, mWenBaId, 0);
            }else {
                mBasePresenter.refreshAskWithReplyOnHot(mCurrentPage, 10, mWenBaId, 0);

            }
        }
    }

    public void setToolBarAlphaColor(int alpha) {
        if (alpha > maxDistance) {
            alpha = maxDistance;
            mToolBar.setBackgroundColor(Color.argb(alpha, 229, 57, 53));
            mTvTitle.setTextColor(Color.argb(alpha, 255, 255, 255));
        } else {
            mToolBar.setBackgroundColor(Color.argb(alpha, 229, 57, 53));
            mTvTitle.setTextColor(Color.argb(alpha, 255, 255, 255));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void setWenBaDetail(WenBa wenBa) {
        mWenBaDetailAdapter.setWenBa(wenBa);
        mTvTitle.setText(wenBa.getWelcomeTitle());
        mAdminId = wenBa.getUserId();
        User user = CheckUser.checkUserIsExists();
        if (user != null && user.getId() == mAdminId) {
            mRlAsk.setVisibility(View.GONE);
//            mWenBaDetailAdapter.setAdmin(true);
            mIvRightShare.setImageResource(R.drawable.ic_edit_question);
            mIvRightShare.setVisibility(View.VISIBLE);
            mIvRightShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isModify",true);
                    startActivity(ApplyAdminActivity.class,bundle);
                }
            });
        }
    }

    @Override
    public void addAskWithReplys(DataContainer<AskWithReply> dataContainer) {
        mWenBaDetailAdapter.setData(dataContainer.getList());
    }

    @Override
    public void loadMoreFail() {
        mPull2RecyclerView.loadMoreFail();
    }

    @Override
    public void loadMoreSuccess(DataContainer<AskWithReply> dataContainer) {
        mDataContainer = dataContainer;
        mWenBaDetailAdapter.setData(dataContainer.getList());
    }

    @Override
    public void loadNoMore() {
        mPull2RecyclerView.setNoMore(true);
    }

    @Override
    public void refreshData(DataContainer<AskWithReply> dataContainer) {
        mDataContainer = dataContainer;
        mWenBaDetailAdapter.refreshData(dataContainer.getList());
    }

    @Override
    public void refreshCompleted() {
        mPull2RecyclerView.refreshCompleted();
    }
}
