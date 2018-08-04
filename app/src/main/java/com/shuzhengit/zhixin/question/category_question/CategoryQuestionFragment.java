//package com.shuzhengit.zhixin.question.category_question;
//
//import android.os.Bundle;
//import android.view.View;
//
//import com.library.base.BaseLazyLoadFragment;
//import com.shuzhengit.zhixin.R;
//import com.shuzhengit.zhixin.bean.DataContainer;
//import com.shuzhengit.zhixin.bean.Question;
//import com.shuzhengit.zhixin.question.question_detail.QuestionDetailActivity;
//import com.shuzhengit.zhixin.util.EventCodeUtils;
//import com.shuzhengit.zhixin.view.DynamicDividerItemDecoration;
//import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;
//import com.umeng.analytics.MobclickAgent;
//
//import butterknife.ButterKnife;
//import butterknife.Unbinder;
//
///**
// * Created by 江苏镇江树正科技 .
// * Author：袁从斌 on 2017/9/18 10:54
// * E-mail:yuancongbin@gmail.com
// * <p>
// * 功能描述:单独分类的问答
// */
//
//public class CategoryQuestionFragment extends BaseLazyLoadFragment<CategoryQuestionContract.Presenter> implements
//        CategoryQuestionContract.View {
//    private static final String TAG = "ChildQuestionFragment";
//    private PullToRefreshRecyclerView mPullToRefreshRecyclerView;
//    private Unbinder mUnbinder;
//    private int mCategoryId;
//    private int mCurrentPage = 1;
//    private CategoryQuestionAdapter mCategoryQuestionAdapter;
//    private DataContainer<Question> mDataContainer;
//
//    public static CategoryQuestionFragment getInstance(int categoryId) {
//        CategoryQuestionFragment categoryQuestionFragment = new CategoryQuestionFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(EventCodeUtils.QUESITON_TYPE, categoryId);
//        categoryQuestionFragment.setArguments(bundle);
//        return categoryQuestionFragment;
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.fragment_document;
//    }
//
//    @Override
//    protected void initView(View view, Bundle savedInstanceState) {
//        mUnbinder = ButterKnife.bind(this, view);
//        mCategoryId = getArguments().getInt(EventCodeUtils.QUESITON_TYPE);
//        mPullToRefreshRecyclerView = (PullToRefreshRecyclerView) view.findViewById(R.id.pullToRefreshRecyclerView);
//        mPullToRefreshRecyclerView.setShowPullToRefresh(true);
//        mPullToRefreshRecyclerView.setShowLoadMore(false);
//        mPullToRefreshRecyclerView.setRefreshListener(new PullToRefreshRecyclerView.RefreshListener() {
//            @Override
//            public void onRefresh() {
//                mCurrentPage = 1;
//                mBasePresenter.refreshQuestion(mCategoryId);
//            }
//        });
//        mCategoryQuestionAdapter = new CategoryQuestionAdapter(getContext());
//        mPullToRefreshRecyclerView.addItemDecoration(new DynamicDividerItemDecoration());
//        mPullToRefreshRecyclerView.setAdapter(mCategoryQuestionAdapter);
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
//        mCategoryQuestionAdapter.setOnItemClickListener(new CategoryQuestionAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Question question) {
//                Bundle bundle = new Bundle();
//                bundle.putInt("questionId",question.getId());
//                startActivity(QuestionDetailActivity.class,bundle);
//            }
//        });
//    }
//
//    @Override
//    protected void lazyLoadCreatePresenter() {
//        MobclickAgent.onPageStart(TAG);
//        mBasePresenter = new CategoryPresenter(this);
//        mPullToRefreshRecyclerView.refresh();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        //统计页面
//        MobclickAgent.onPageEnd(TAG);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mUnbinder.unbind();
//    }
//
//    @Override
//    public void refreshQuestions(DataContainer<Question> dataContainer) {
//        mDataContainer = dataContainer;
//        mPullToRefreshRecyclerView.setShowLoadMore(true);
//        mCategoryQuestionAdapter.refresh(dataContainer.getList());
//    }
//
//    @Override
//    public void loadMoreQuestions(DataContainer<Question> dataContainer) {
//        mDataContainer = dataContainer;
//        mCategoryQuestionAdapter.addMore(dataContainer.getList());
//    }
//
//    @Override
//    public void refreshCompleted() {
//        mPullToRefreshRecyclerView.refreshCompleted();
//    }
//
//    @Override
//    public void loadMoreCompleted() {
//        mPullToRefreshRecyclerView.loadMoreCompleted();
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
//
