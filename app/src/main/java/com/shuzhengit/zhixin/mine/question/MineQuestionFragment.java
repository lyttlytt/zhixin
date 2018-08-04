//package com.shuzhengit.zhixin.mine.question;
//
//import android.os.Bundle;
//import android.view.View;
//
//import com.library.base.BaseFragment;
//import com.shuzhengit.zhixin.R;
//import com.shuzhengit.zhixin.bean.DataContainer;
//import com.shuzhengit.zhixin.bean.Question;
//import com.shuzhengit.zhixin.bean.User;
//import com.shuzhengit.zhixin.question.QuestionAdapter;
//import com.shuzhengit.zhixin.question.question_detail.QuestionDetailActivity;
//import com.shuzhengit.zhixin.util.CheckUser;
//import com.shuzhengit.zhixin.view.ListDividerItemDecoration;
//import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.Unbinder;
//
///**
// * Created by 江苏镇江树正科技 .
// * Author：袁从斌 on 2017/9/29 22:28
// * E-mail:yuancongbin@gmail.com
// * <p>
// * 功能描述:
// */
//
//public class MineQuestionFragment extends BaseFragment<MineQuestionContract.Presenter> implements
//        MineQuestionContract.View {
//
//    @BindView(R.id.pullToRefreshRecyclerView)
//    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
//    private Unbinder mUnbinder;
//    public static final int TYPE_RELEASE = 0;
//    public static final int TYPE_ANSWER = 1;
//    public static final int TYPE_FOLLOW = 2;
//    private int mCurrentPage = 1;
//    private User mUser;
//    private int mType;
//    private QuestionAdapter mCategoryQuestionAdapter;
//    private DataContainer<Question> mDataContainer;
//    public static MineQuestionFragment getInstance(int type) {
//        Bundle bundle = new Bundle();
//        bundle.putInt("type", type);
//        MineQuestionFragment mineQuestionFragment = new MineQuestionFragment();
//        mineQuestionFragment.setArguments(bundle);
//        return mineQuestionFragment;
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
//        mPullToRefreshRecyclerView.setShowPullToRefresh(true);
//        mCategoryQuestionAdapter = new QuestionAdapter(getContext());
////        mPullToRefreshRecyclerView.addItemDecoration(new DynamicDividerItemDecoration());
//        mPullToRefreshRecyclerView.addItemDecoration(new ListDividerItemDecoration());
//        mPullToRefreshRecyclerView.setAdapter(mCategoryQuestionAdapter);
//        mCategoryQuestionAdapter.setOnItemClickListener(new QuestionAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Question question) {
//                Bundle bundle = new Bundle();
//                bundle.putInt("questionId",question.getId());
//                startActivity(QuestionDetailActivity.class,bundle);
//            }
//        });
//        mPullToRefreshRecyclerView.setRefreshListener(new PullToRefreshRecyclerView.RefreshListener() {
//            @Override
//            public void onRefresh() {
//                mCurrentPage=1;
//                switch (mType) {
//                    case TYPE_RELEASE:
//                        mBasePresenter.refreshMineReleaseQuestionByUserId(mUser.getId());
//                        break;
//                    case TYPE_ANSWER:
//                        mBasePresenter.refreshMineReplyQuestionByUserId(mUser.getId());
//                        break;
//                    case TYPE_FOLLOW:
//                    default:mBasePresenter.refreshMineFollowQuestionByUserId(mUser.getId());
//                }
//            }
//        });
//        mPullToRefreshRecyclerView.setLoadMoreListener(new PullToRefreshRecyclerView.LoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                if (mDataContainer.isHasNextPage()){
//                    switch (mType) {
//                        case TYPE_RELEASE:
//                            mBasePresenter.findMineReleaseQuestionByUserId(mUser.getId(), ++mCurrentPage, 10);
//                            break;
//                        case TYPE_ANSWER:
//                            mBasePresenter.findMineReplyQuestionByUserId(mUser.getId(),++mCurrentPage,10);
//                            break;
//                        case TYPE_FOLLOW:
//                        default:mBasePresenter.findMienFollowQuestionByUserId(mUser.getId(),++mCurrentPage,10);
//                    }
//                }else {
//                    loadNoMore();
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void createPresenter() {
//        mType = getArguments().getInt("type");
//        mUser = CheckUser.checkUserIsExists();
//        if (mUser == null) {
//            failure("请先登录");
//            return;
//        }
//        mBasePresenter = new MineQuestionPresenter(this);
//        switch (mType) {
//            case TYPE_RELEASE:
//                mBasePresenter.findMineReleaseQuestionByUserId(mUser.getId(), mCurrentPage, 10);
//                break;
//            case TYPE_ANSWER:
//                mBasePresenter.findMineReplyQuestionByUserId(mUser.getId(),mCurrentPage,10);
//                break;
//            case TYPE_FOLLOW:
//            default:mBasePresenter.findMienFollowQuestionByUserId(mUser.getId(),mCurrentPage,10);
//        }
//    }
//
//
//    @Override
//    public void loadMoreCompleted() {
//        mPullToRefreshRecyclerView.loadMoreCompleted();
//    }
//
//    @Override
//    public void setQuestions(DataContainer<Question> dataContainer) {
//        mDataContainer = dataContainer;
//        mCategoryQuestionAdapter.addMore(dataContainer.getList());
//        if (dataContainer.getList().size()<10){
//            mPullToRefreshRecyclerView.setShowLoadMore(false);
//        }else {
//            mPullToRefreshRecyclerView.setShowLoadMore(false);
//        }
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
//    public void refreshCompleted() {
//        mPullToRefreshRecyclerView.refreshCompleted();
//    }
//
//    @Override
//    public void refreshData(DataContainer<Question> refreshData) {
//        mDataContainer = refreshData;
//        mCategoryQuestionAdapter.refresh(refreshData.getList());
//        if (refreshData.getList().size()<10){
//            mPullToRefreshRecyclerView.setShowLoadMore(false);
//        }else {
//            mPullToRefreshRecyclerView.setShowLoadMore(false);
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mUnbinder.unbind();
//    }
//
//}
