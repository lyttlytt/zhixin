//package com.shuzhengit.zhixin.question;
//
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v7.app.ActionBar;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.library.base.BaseLazyLoadFragment;
//import com.shuzhengit.zhixin.R;
//import com.shuzhengit.zhixin.bean.DataContainer;
//import com.shuzhengit.zhixin.bean.Question;
//import com.shuzhengit.zhixin.bean.User;
//import com.shuzhengit.zhixin.login.LoginActivity;
//import com.shuzhengit.zhixin.question.question_detail.QuestionDetailActivity;
//import com.shuzhengit.zhixin.question.release_question.ReleaseQuestionActivity;
//import com.shuzhengit.zhixin.util.CheckUser;
//import com.shuzhengit.zhixin.view.PromptLoginDialog;
//import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;
//import com.umeng.analytics.MobclickAgent;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.Unbinder;
//
///**
// * Created by 江苏镇江树正科技 .
// * Author：袁从斌 on 2017/7/18 17:47
// * E-mail:yuancongbin@gmail.com
// */
//
//public class QuestionFragment extends BaseLazyLoadFragment<QuestionContract.Presenter> implements QuestionContract
//        .View {
//    private static final String TAG = "QuestionFragment";
//    @BindView(R.id.tvTitle)
//    TextView mTvTitle;
//    @BindView(R.id.tvRight)
//    TextView mTvRight;
//    @BindView(R.id.ivRightShare)
//    ImageView mIvRightShare;
//    @BindView(R.id.toolBar)
//    Toolbar mToolBar;
//    @BindView(R.id.fabEditQuestion)
//    FloatingActionButton mFabEditQuestion;
//    @BindView(R.id.pullToRefreshRecyclerView)
//    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
//    @BindView(R.id.ivEmpty)
//    ImageView mIvEmpty;
//    private int mCurrentPage = 1;
//    private int mCategoryId = 1;
//
//    private Unbinder mUnbinder;
//    private QuestionAdapter mQuestionAdapter;
//    private DataContainer<Question> mDataContainer;
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        //统计页面
//        MobclickAgent.onPageEnd(TAG);
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.fragment_question;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mUnbinder.unbind();
//    }
//
//    @Override
//    protected void initView(View view, Bundle savedInstanceState) {
//        mUnbinder = ButterKnife.bind(this, view);
//
//        getHoldingActivity().setSupportActionBar(mToolBar);
//        ActionBar actionBar = getHoldingActivity().getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(false);
//        mTvTitle.setText("问答");
//        mFabEditQuestion.setOnClickListener(v -> {
//            User user = CheckUser.checkUserIsExists();
//            if (user != null) {
//                startActivity(ReleaseQuestionActivity.class);
//            } else {
//                PromptLoginDialog promptLoginDialog = new PromptLoginDialog(mActivity);
//                promptLoginDialog.show();
//                promptLoginDialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
//                    @Override
//                    public void callBackPositive(DialogInterface dialog) {
//                        startActivity(LoginActivity.class);
//                    }
//                });
//            }
//        });
//        mPullToRefreshRecyclerView.setEmptyView(mIvEmpty);
//        mPullToRefreshRecyclerView.setShowPullToRefresh(true);
//        mPullToRefreshRecyclerView.setShowLoadMore(false);
//        mPullToRefreshRecyclerView.setRefreshListener(new PullToRefreshRecyclerView.RefreshListener() {
//            @Override
//            public void onRefresh() {
//                mCurrentPage = 1;
//                mBasePresenter.refreshQuestion(mCategoryId);
//            }
//        });
//        mQuestionAdapter = new QuestionAdapter(getContext());
////        mPullToRefreshRecyclerView.addItemDecoration(new ListDividerItemDecoration());
//        mPullToRefreshRecyclerView.setAdapter(mQuestionAdapter);
//        mPullToRefreshRecyclerView.setLoadMoreListener(new PullToRefreshRecyclerView.LoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                if (mDataContainer.isHasNextPage()) {
//                    mBasePresenter.findQuestions(++mCurrentPage, 10, mCategoryId);
//                } else {
//                    loadNoMore();
//                }
//            }
//        });
//        mQuestionAdapter.setOnItemClickListener(new QuestionAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Question question) {
//                Bundle bundle = new Bundle();
//                bundle.putInt("questionId", question.getId());
//                startActivity(QuestionDetailActivity.class, bundle);
//            }
//        });
//    }
//
//
//    @Override
//    protected void lazyLoadCreatePresenter() {
//        MobclickAgent.onPageStart(TAG);
//        mBasePresenter = new QuestionPresenter(this);
//        mBasePresenter.start();
//        mCurrentPage =1;
//        mBasePresenter.refreshQuestion(mCategoryId);
////        mBasePresenter.findQuestions(mCurrentPage,10,mCategoryId);
//    }
//
//    @Override
//    public void refreshQuestions(DataContainer<Question> dataContainer) {
//        mDataContainer = dataContainer;
//        if (dataContainer.isHasNextPage()) {
//            mPullToRefreshRecyclerView.setShowLoadMore(true);
//        }else {
//            mPullToRefreshRecyclerView.setShowLoadMore(false);
//        }
//        mQuestionAdapter.refresh(dataContainer.getList());
//    }
//
//    @Override
//    public void loadMoreQuestions(DataContainer<Question> dataContainer) {
//        mDataContainer = dataContainer;
//        mQuestionAdapter.addMore(dataContainer.getList());
//    }
//
//    @Override
//    public void refreshCompleted() {
//        mPullToRefreshRecyclerView.refreshCompleted();
//    }
//
//    @Override
//    public void loadMoreCompleted() {
//        mPullToRefreshRecyclerView.refreshCompleted();
//
//    }
//
//    @Override
//    public void loadMoreFail() {
//        mPullToRefreshRecyclerView.loadMoreFail();
//    }
//
//    @Override
//    public void loadNoMore() {
//        mPullToRefreshRecyclerView.setNoMore(true);
//    }
//
//    @Override
//    public void refreshQuestions() {
//        mPullToRefreshRecyclerView.refresh();
//    }
//}
