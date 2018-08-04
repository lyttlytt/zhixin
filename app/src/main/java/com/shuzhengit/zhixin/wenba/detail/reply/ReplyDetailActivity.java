package com.shuzhengit.zhixin.wenba.detail.reply;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.library.util.KeyBorderUtil;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.AskComment;
import com.shuzhengit.zhixin.bean.AskWithReply;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.login.LoginActivity;
import com.shuzhengit.zhixin.mine.user.UserInfoActivity;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.view.PromptLoginDialog;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ReplyDetailActivity extends BaseActivity<ReplyContract.Presenter> implements ReplyContract.View {

    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.pullToRefreshRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    @BindView(R.id.etComment)
    EditText mEtComment;
    @BindView(R.id.ibSendComment)
    ImageButton mIbSendComment;
    private Unbinder mUnbinder;
    private ReplyAdapter mReplyAdapter;
    private AnimationSet mAnimationSet;
    private DataContainer<AskComment> mDataContainer;
    private int mCurrentPage = 1;
    private int mAskId;

    @Override
    protected int layoutId() {
        return R.layout.activity_reply_detail;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);

        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mTvTitle.setText("问题详情");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mPullToRefreshRecyclerView.setShowPullToRefresh(false);
        mPullToRefreshRecyclerView.setShowLoadMore(true);
        mReplyAdapter = new ReplyAdapter(this);
        mPullToRefreshRecyclerView.setAdapter(mReplyAdapter);
        mIbSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = CheckUser.checkUserIsExists();
                if (user == null) {
                    PromptLoginDialog promptLoginDialog = new PromptLoginDialog(ReplyDetailActivity.this);
                    promptLoginDialog.show();
                    promptLoginDialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                        @Override
                        public void callBackPositive(DialogInterface dialog) {
                            startActivity(LoginActivity.class);
                        }
                    });
                    return;
                }
                String s = mEtComment.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    failure("内容不能为空");
                    return;
                }
                AskWithReply askWithReply = mReplyAdapter.getAskWithReply();
                mBasePresenter.comment(s, askWithReply.getId(), askWithReply.getAnswerUserId(), user.getId());
                mEtComment.setText("");
                KeyBorderUtil.hideSoftInput(ReplyDetailActivity.this,mEtComment);
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
        mReplyAdapter.setOnItemAgreeAskListener(new ReplyAdapter.OnItemAgreeAskListener() {
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
                            mReplyAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                } else {
                    PromptLoginDialog promptLoginDialog = new PromptLoginDialog(ReplyDetailActivity.this);
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
        mReplyAdapter.setOnItemAgreeCommentListener(new ReplyAdapter.OnItemAgreeCommentListener() {
            @Override
            public void onItemAgreeAsk(AskComment askWithReply, TextView tvAddOne) {
                User user = CheckUser.checkUserIsExists();
                if (user != null) {
                    mBasePresenter.agreeComment(mAskId,askWithReply.getId(), user.getId());
                    tvAddOne.setVisibility(View.VISIBLE);
                    tvAddOne.startAnimation(mAnimationSet);
                    mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            tvAddOne.setVisibility(View.GONE);
                            askWithReply.setIsUp(1);
                            int i = askWithReply.getAgreeCount() + 1;
                            askWithReply.setAgreeCount(i);
                            mReplyAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                } else {
                    PromptLoginDialog promptLoginDialog = new PromptLoginDialog(ReplyDetailActivity.this);
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
        mPullToRefreshRecyclerView.setShowLoadMore(true);
        mPullToRefreshRecyclerView.setLoadMoreListener(new PullToRefreshRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mDataContainer!=null && mDataContainer.isHasNextPage()){
                    User user = CheckUser.checkUserIsExists();
                    mBasePresenter.findCommentById(++mCurrentPage,10,user==null?0:user.getId(),mAskId);
                }else {
                    noMore();
                }
            }
        });
        mReplyAdapter.setOnItemClickUserListener(new ReplyAdapter.OnItemClickUserListener() {
            @Override
            public void onItemClickUserListener(int memberId) {
                User user = CheckUser.checkUserIsExists();
                if (user == null) {
                    PromptLoginDialog promptLoginDialog = new PromptLoginDialog(ReplyDetailActivity.this);
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
    protected void createPresenter() {
        mAnimationSet = new AnimationSet(this, null);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.3f);
        alphaAnimation.setDuration(1000);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -50);
        translateAnimation.setDuration(1000);
        mAnimationSet.addAnimation(translateAnimation);
        mAnimationSet.addAnimation(alphaAnimation);

        mBasePresenter = new ReplyPresenter(this);
        mAskId = getIntent().getIntExtra("askId", 0);
        if (mAskId == 0) {
            failure("问题不存在");
            onBackPressed();
            return;
        }
        User user = CheckUser.checkUserIsExists();
        mBasePresenter.findAskById(mAskId,user==null?0:user.getId());
        mCurrentPage=1;
        mBasePresenter.refreshComments(mCurrentPage,10,user==null?0:user.getId(),mAskId);
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

    @Override
    public void setAskWithReplyInfo(AskWithReply askWithReplyInfo) {
        mReplyAdapter.setAskWithReply(askWithReplyInfo);
    }

    @Override
    public void commentSuccess() {
        mCurrentPage = 1;
        User user = CheckUser.checkUserIsExists();
        mBasePresenter.refreshComments(mCurrentPage,10,user==null?0:user.getId(),mAskId);
    }

    @Override
    public void refreshComments(DataContainer<AskComment> dataContainer) {
        mDataContainer = dataContainer;
        mReplyAdapter.onRefresh(dataContainer.getList());
    }

    @Override
    public void addMoreComments(DataContainer<AskComment> dataContainer) {
        mDataContainer = dataContainer;
        mReplyAdapter.addMore(dataContainer.getList());
    }

    @Override
    public void noMore() {
        mPullToRefreshRecyclerView.setNoMore(true);
    }

    @Override
    public void loadMoreFail() {
        mPullToRefreshRecyclerView.loadMoreFail();
    }

    @Override
    public void loadMoreComplete() {
        mPullToRefreshRecyclerView.loadMoreCompleted();
    }
}
