package com.shuzhengit.zhixin.question.answer;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
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
import com.library.bean.BaseCallModel;
import com.library.rx.RxSchedulersHelper;
import com.library.util.DeviceUtil;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.AnswerComment;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.QuestionAnswer;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.index.document.PhotoViewActivity;
import com.shuzhengit.zhixin.login.LoginActivity;
import com.shuzhengit.zhixin.mine.user.UserInfoActivity;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.util.ResourceUtils;
import com.shuzhengit.zhixin.view.PromptLoginDialog;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AnswerDetailActivity extends BaseActivity<AnswerContract.Presenter> implements AnswerContract.View {
    private static final String TAG = "AnswerDetailActivity";
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
    private User mUser;
    private Unbinder mUnbinder;
    private QuestionAnswer mAnswer;
    private String mQuestionTitle;
    private AnswerCommentAdapter mAnswerCommentAdapter;
    private DataContainer<AnswerComment> mDataContainer;
    private int mCurrentPage = 1;
    private int mQuestionId;
    private AnimationSet mAnimationSet;
    private int mAnswerId;

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
        return R.layout.activity_answer_detail;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar!=null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mUser = CheckUser.checkUserIsExists();
        mAnswer = (QuestionAnswer) getIntent().getSerializableExtra("answer");
        mQuestionTitle = getIntent().getStringExtra("questionTitle");
        mQuestionId = getIntent().getIntExtra("questionId", 0);
        mAnswerId = getIntent().getIntExtra("answerId", 0);
        mAnswerCommentAdapter = new AnswerCommentAdapter(this);
        mPullToRefreshRecyclerView.setAdapter(mAnswerCommentAdapter);
        mAnswerCommentAdapter.setQuestionTitle(mQuestionTitle);
        mPullToRefreshRecyclerView.setShowPullToRefresh(false);
        mAnswerCommentAdapter.setOnContentItemImage(new AnswerCommentAdapter.OnContentItemImage() {
            @Override
            public void onContentItemImage(String url) {
                Bundle bundle = new Bundle();
                bundle.putString(EventCodeUtils.DOCUMENT_PICTURE, url);
                startActivity(PhotoViewActivity.class, bundle);
            }
        });

        mPullToRefreshRecyclerView.setLoadMoreListener(new PullToRefreshRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mUser == null) {
                    mBasePresenter.findAnswerComments(mAnswer.getId(), ++mCurrentPage, 10, 0);
                } else {
                    mBasePresenter.findAnswerComments(mAnswer.getId(), ++mCurrentPage, 10, mUser.getId());
                }
            }
        });
        mAnswerCommentAdapter.setOnItemAgreeCommentClickListener(new AnswerCommentAdapter.OnItemAgreeCommentClickListener() {
            @Override
            public void onItemAgreeComment(AnswerComment answerComment, TextView tvAgree, TextView tvAddOne) {

                User user = CheckUser.checkUserIsExists();
                if (user != null) {
                    tvAddOne.setVisibility(View.VISIBLE);
                    tvAddOne.startAnimation(mAnimationSet);
                    mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            tvAddOne.setVisibility(View.GONE);
                            Integer integer = answerComment.getAgreeCount() + 1;
                            answerComment.setAgreeCount(integer);
                            tvAgree.setText(String.valueOf(answerComment.getAgreeCount()));

                            Drawable likeIcon = ResourceUtils.getResourceDrawable(APP.getInstance(), R.drawable
                                    .ic_like);
                            tvAgree.setTextColor(ResourceUtils.getResourceColor(APP.getInstance(), R.color.blue700));
                            likeIcon.setBounds(0, 0, likeIcon.getMinimumWidth(), likeIcon.getMinimumHeight());
                            tvAgree.setCompoundDrawables(likeIcon, null, null, null);
                            answerComment.setIsUp(1);
                            tvAgree.setClickable(false);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    mBasePresenter.agreeComment(answerComment.getAnswerId(),answerComment.getId(), user.getId());
                } else {
                    PromptLoginDialog promptLoginDialog = new PromptLoginDialog(AnswerDetailActivity.this);
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
        mAnswerCommentAdapter.setOnItemUserClickListener(new AnswerCommentAdapter.OnItemUserClickListener() {
            @Override
            public void onItemUserClick(int memberId) {
                User user = CheckUser.checkUserIsExists();
                if (user != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("memberId",memberId);
                    startActivity(UserInfoActivity.class, bundle);
                } else {
                    PromptLoginDialog promptLoginDialog = new PromptLoginDialog(AnswerDetailActivity.this);
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

        mIbSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = CheckUser.checkUserIsExists();
                if (user == null) {
                    PromptLoginDialog promptLoginDialog = new PromptLoginDialog(AnswerDetailActivity.this);
                    promptLoginDialog.show();
                    promptLoginDialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                        @Override
                        public void callBackPositive(DialogInterface dialog) {
                            startActivity(LoginActivity.class);
                        }
                    });
                    return;
                }
                String comment = mEtComment.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    failure("评论不能为空!");
                    return;
                }
                mBasePresenter.addAnswerComment(comment, mAnswer.getId(),mQuestionId,user.getId());
                mEtComment.setText("");
                DeviceUtil.closeKeybord(AnswerDetailActivity.this);
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
    }

    @Override
    protected void createPresenter() {
        if (mUser != null) {
            addHistory();
        }
        mAnimationSet = new AnimationSet(this, null);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.3f);
        alphaAnimation.setDuration(1000);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -50);
        translateAnimation.setDuration(1000);
        mAnimationSet.addAnimation(translateAnimation);
        mAnimationSet.addAnimation(alphaAnimation);
        mBasePresenter = new AnswerCommentPresenter(this);
        if (mUser == null) {
            mBasePresenter.findAnswerComments(mAnswer.getId(), mCurrentPage, 10, 0);
        } else {
            mBasePresenter.findAnswerComments(mAnswer.getId(), mCurrentPage, 10, mUser.getId());
        }

        mBasePresenter.findAnswerDetailById(mAnswerId);
    }

    private void addHistory() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("answerId", mAnswer.getId());
            jsonObject.put("readUserId", mUser.getId());
            jsonObject.put("readTimeCount", 1);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                    jsonObject.toString());
            HttpProtocol.getApi()
                    .addHistoryAnswer(requestBody)
                    .compose(RxSchedulersHelper.io_main())
                    .subscribeWith(new RxSubscriber<BaseCallModel>() {
                        @Override
                        protected void _onNext(BaseCallModel callModel) {

                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadMoreCompleted() {
        mPullToRefreshRecyclerView.loadMoreCompleted();
    }

    @Override
    public void loadNoMore() {
        mPullToRefreshRecyclerView.setNoMore(true);
    }

    @Override
    public void loadMoreFail() {
        mPullToRefreshRecyclerView.loadMoreFail();
    }

    @Override
    public void setMoreData(DataContainer<AnswerComment> dataContainer) {
        mDataContainer = dataContainer;
        mAnswerCommentAdapter.addMore(dataContainer.getList());
    }

    @Override
    public void refreshAnswerComment(DataContainer<AnswerComment> dataContainer) {
        mDataContainer = dataContainer;
        mAnswerCommentAdapter.refreshAnswerComment(dataContainer.getList());
    }

    @Override
    public void addAnswerCommentSuccess() {
        mBasePresenter.refreshAnswerComment(mAnswer.getId(),mUser.getId());
    }

    @Override
    public void setAnswerDetail(QuestionAnswer questionAnswer) {
        mAnswerCommentAdapter.setQuestionAnswer(questionAnswer);
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
}
