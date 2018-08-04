package com.shuzhengit.zhixin.question.question_detail;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.library.bean.BaseCallModel;
import com.library.rx.RxBus2;
import com.library.rx.RxSchedulersHelper;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.EventType;
import com.shuzhengit.zhixin.bean.Question;
import com.shuzhengit.zhixin.bean.QuestionAnswer;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.login.LoginActivity;
import com.shuzhengit.zhixin.mine.user.UserInfoActivity;
import com.shuzhengit.zhixin.question.answer.AddAnswerActivity;
import com.shuzhengit.zhixin.question.answer.AnswerDetailActivity;
import com.shuzhengit.zhixin.question.holder.QuestionDetailAdapter;
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
import io.reactivex.functions.Predicate;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class QuestionDetailActivity extends BaseActivity<QuestionDetailContract.Presenter> implements
        QuestionDetailContract.View {

    private static final String TAG = "QuestionDetailActivity";
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
    @BindView(R.id.fabAddAnswer)
    FloatingActionButton mFabAddAnswer;
    private Drawable mQuestionDecoration;
    private Unbinder mUnbinder;
    private QuestionDetailAdapter mQuestionDetailAdapter;
    private DataContainer<QuestionAnswer> mAnswers;
    private int mQuestionId;
    private int mCurrentPage = 1;

    private User mUser;
    private AnimationSet mAnimationSet;
    private Question mQuestionDetail;
    private RxSubscriber<EventType> mRxSubscriber;
    private RxSubscriber<EventType> mReleaseSubscriber;

    @Override
    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart(TAG);
        //统计时长
        MobclickAgent.onResume(this);
        addRxBus();

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
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mRxSubscriber.dispose();
        mReleaseSubscriber.dispose();
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_question;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        mUser = CheckUser.checkUserIsExists();
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        mQuestionId = getIntent().getIntExtra("questionId", -1);
        mTvTitle.setText("问答详情");
        mToolBar.setNavigationOnClickListener(v -> onBackPressed());
        mPullToRefreshRecyclerView.setShowPullToRefresh(false);
        mPullToRefreshRecyclerView.setShowLoadMore(true);
        mQuestionDetailAdapter = new QuestionDetailAdapter(QuestionDetailActivity.this);
        mPullToRefreshRecyclerView.setAdapter(mQuestionDetailAdapter);
        mQuestionDecoration = ContextCompat.getDrawable(this, R.drawable.shape_question_detail_decoration);
        mPullToRefreshRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                int parentLeft = parent.getPaddingLeft();
                int parentRight = parent.getWidth() - parent.getPaddingRight();
                int childCount = parent.getChildCount();
                for (int i = 1; i < childCount - 1; i++) {
                    View childAt = parent.getChildAt(i);
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childAt.getLayoutParams();
                    int parentTop = childAt.getBottom() + layoutParams.bottomMargin;
                    int parentBottom = parentTop + mQuestionDecoration.getIntrinsicHeight();
                    mQuestionDecoration.setBounds(parentLeft, parentTop, parentRight, parentBottom);
                    mQuestionDecoration.draw(canvas);
                }
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = mQuestionDecoration.getIntrinsicHeight();
            }
        });

        mPullToRefreshRecyclerView.setLoadMoreListener(new PullToRefreshRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mAnswers.isHasNextPage()) {
                    if (mUser == null) {
                        mBasePresenter.findQuestionAnswers(++mCurrentPage, 10, mQuestionId, 0);
                    } else {
                        mBasePresenter.findQuestionAnswers(++mCurrentPage, 10, mQuestionId, mUser.getId());
                    }
                } else {
                    loadNoMore();
                }
            }
        });
        mQuestionDetailAdapter.setOnQuestionFollowerClickListener(new QuestionDetailAdapter
                .OnQuestionFollowerClickListener() {
            @Override
            public void onFollowQuestion(Question question1, Question question, TextView tvFollowed) {
                User user = CheckUser.checkUserIsExists();


                if (user != null) {
                    mBasePresenter.followedQuestion(user.getId(), question.getId());
                    if (question.getIsLike() == 0) {
                        question.setIsLike(1);
                        tvFollowed.setText("已关注");
                        tvFollowed.setTextColor(ResourceUtils.getResourceColor(APP.getInstance(), R.color
                                .grey500));
                    } else {
                        question.setIsLike(0);
                        tvFollowed.setText("+ 添加关注");
                        tvFollowed.setTextColor(ResourceUtils.getResourceColor(APP.getInstance(), R.color
                                .colorPrimary));
                    }
                } else {
                    PromptLoginDialog promptLoginDialog = new PromptLoginDialog(QuestionDetailActivity.this);
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

        mQuestionDetailAdapter.setOnAnswerItemListener(new QuestionDetailAdapter.OnAnswerItemListener() {
            @Override
            public void onItemClick(QuestionAnswer questionAnswer, String questionTitle, int questionId) {
                LogUtils.i(TAG, questionAnswer.toString());
                Bundle bundle = new Bundle();
                bundle.putSerializable("answer", questionAnswer);
                int id = questionAnswer.getId();
                bundle.putInt("answerId", id);
                bundle.putString("questionTitle", questionTitle);
                bundle.putInt("questionId", questionId);
                startActivity(AnswerDetailActivity.class, bundle);
            }

            @Override
            public void onItemAgree(QuestionAnswer questionAnswer, TextView tvAgree, TextView tvAddOne) {

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
                            Integer integer = questionAnswer.getAgreeCount() + 1;
                            questionAnswer.setAgreeCount(integer);
                            tvAgree.setText(String.valueOf(questionAnswer.getAgreeCount()));

                            Drawable likeIcon = ResourceUtils.getResourceDrawable(APP.getInstance(), R.drawable
                                    .ic_like);
                            tvAgree.setTextColor(ResourceUtils.getResourceColor(APP.getInstance(), R.color.colorPrimary));
                            likeIcon.setBounds(0, 0, likeIcon.getMinimumWidth(), likeIcon.getMinimumHeight());
                            tvAgree.setCompoundDrawables(likeIcon, null, null, null);
                            questionAnswer.setVoteType(1);
                            tvAgree.setClickable(false);
//                            mDocumentDetailAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    mBasePresenter.agreeAnswer(questionAnswer.getId(), user.getId());
                } else {
                    PromptLoginDialog promptLoginDialog = new PromptLoginDialog(QuestionDetailActivity.this);
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

        mQuestionDetailAdapter.setOnItemUserClickListener(new QuestionDetailAdapter.OnItemUserClickListener() {
            @Override
            public void onItemUserClick(int userId) {
                User user = CheckUser.checkUserIsExists();
                if (user != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("memberId", userId);
                    startActivity(UserInfoActivity.class, bundle);
                } else {
                    PromptLoginDialog promptLoginDialog = new PromptLoginDialog(QuestionDetailActivity.this);
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


        mFabAddAnswer.setOnClickListener(v -> {
            if (mUser == null) {
                PromptLoginDialog promptLoginDialog = new PromptLoginDialog(QuestionDetailActivity.this);
                promptLoginDialog.show();
                promptLoginDialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                    @Override
                    public void callBackPositive(DialogInterface dialog) {
                        startActivity(LoginActivity.class);
                    }
                });
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("questionId", mQuestionId);
                bundle.putInt("memberId", mUser.getId());
                startActivity(AddAnswerActivity.class, bundle);
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

        if (mQuestionId == -1) {
            failure("该问题已被删除");
            onBackPressed();
            return;
        }
        if (mUser != null) {
            addHistory();

        }
        mBasePresenter = new QuestionDetailPresenter(this);
        if (mUser == null) {
            mBasePresenter.findQuestionDetail(mQuestionId, 0);
            mBasePresenter.findQuestionAnswers(mCurrentPage, 10, mQuestionId, 0);
        } else {
            mBasePresenter.findQuestionDetail(mQuestionId, mUser.getId());
            mBasePresenter.findQuestionAnswers(mCurrentPage, 10, mQuestionId, mUser.getId());
        }
//        if (mBasePresenter != null)
//            addRxBus();
    }

    private void addRxBus() {
        mRxSubscriber = new RxSubscriber<EventType>() {
            @Override
            protected void _onNext(EventType eventType) {
                LogUtils.i(TAG, "refresh Answer");
                if (mUser != null && mBasePresenter != null) {
                    int answerCount = mQuestionDetail.getAnswerCount();
                    mQuestionDetail.setAnswerCount(++answerCount);
                    mBasePresenter.refreshAnswer(mQuestionId, mUser.getId());
                } else {
                    int answerCount = mQuestionDetail.getAnswerCount();
                    mQuestionDetail.setAnswerCount(++answerCount);
                    if (mBasePresenter != null) {
                        mBasePresenter.refreshAnswer(mQuestionId, 0);
                    }
                }
                mReleaseSubscriber.dispose();
            }
        };


        mReleaseSubscriber = new RxSubscriber<EventType>() {
            @Override
            protected void _onNext(EventType eventType) {
                mRxSubscriber.dispose();
                dispose();
            }
        };

        //                    mBasePresenter.findQuestionDetail(mQuestionId, mUser.getId());
//                    mBasePresenter.findQuestionDetail(mQuestionId, 0);
//        mRxSubscriber = new RxSubscriber<EventType>() {
//            @Override
//            protected void _onNext(EventType eventType) {
//                LogUtils.i(TAG,"refresh Answer");
//                if (mUser != null && mBasePresenter!=null) {
//                    int answerCount = mQuestionDetail.getAnswerCount();
//                    mQuestionDetail.setAnswerCount(++answerCount);
////                    mBasePresenter.findQuestionDetail(mQuestionId, mUser.getId());
//                    mBasePresenter.refreshAnswer(mQuestionId, mUser.getId());
//                } else {
//                    int answerCount = mQuestionDetail.getAnswerCount();
//                    mQuestionDetail.setAnswerCount(++answerCount);
////                    mBasePresenter.findQuestionDetail(mQuestionId, 0);
//                    mBasePresenter.refreshAnswer(mQuestionId, 0);
//                }
//                mReleaseSubscriber.dispose();
//                dispose();
//            }
//        };

        RxBus2.getDefault().toFlowable(EventType.class)
                .compose(RxSchedulersHelper.io_main())
                .filter(new Predicate<EventType>() {
                    @Override
                    public boolean test(EventType eventType) throws Exception {
                        return eventType.getEventType().equals(EventCodeUtils.REFRESH_ANSWER);
                    }
                }).subscribeWith(mRxSubscriber);

//        mReleaseSubscriber = new RxSubscriber<EventType>() {
//            @Override
//            protected void _onNext(EventType eventType) {
//                mRxSubscriber.dispose();
//                dispose();
//            }
//        };

        RxBus2.getDefault().toFlowable(EventType.class)
                .filter(new Predicate<EventType>() {
                    @Override
                    public boolean test(EventType eventType) throws Exception {
                        return eventType.getEventType().equals(EventCodeUtils.RELEASE_REFRESH_QUESTION_ANSWER);
                    }
                }).subscribeWith(mReleaseSubscriber);
    }

    private void addHistory() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("readTimeCount", 1);
            jsonObject.put("readUserId", mUser.getId());
            jsonObject.put("questionId", mQuestionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonObject
                .toString());
        HttpProtocol.getApi()
                .addHistoryQuestion(requestBody)
                .compose(RxSchedulersHelper.io_main())
                .subscribeWith(new RxSubscriber<BaseCallModel>() {
                    @Override
                    protected void _onNext(BaseCallModel callModel) {

                    }
                });
    }

    @Override
    public void setQuestionDetail(Question questionDetail) {
        mQuestionDetail = questionDetail;
        mQuestionDetailAdapter.setQuestion(questionDetail);
    }

    @Override
    public void setQuestionAnswers(DataContainer<QuestionAnswer> answers) {
        mAnswers = answers;
        mQuestionDetailAdapter.addMoreQuestionAnswer(answers.getList());
    }

    @Override
    public void refreshQuestionAnswer(DataContainer<QuestionAnswer> answers) {
        mAnswers = answers;
        mQuestionDetailAdapter.refreshQuestionAnswer(answers.getList());
    }

    @Override
    public void loadMoreFail() {
        mPullToRefreshRecyclerView.loadMoreFail();
    }

    @Override
    public void loadMoreComplete() {
        LogUtils.i("QuestionDetailPresenter", "completed");
        mPullToRefreshRecyclerView.loadMoreCompleted();
    }

    @Override
    public void loadNoMore() {
        mPullToRefreshRecyclerView.setNoMore(true);
    }

}
