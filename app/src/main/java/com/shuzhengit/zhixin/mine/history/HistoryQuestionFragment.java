package com.shuzhengit.zhixin.mine.history;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.library.base.BaseFragment;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.base.BaseRecyclerViewAdapter;
import com.shuzhengit.zhixin.base.BaseViewHolder;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.Document;
import com.shuzhengit.zhixin.bean.WenBa;
import com.shuzhengit.zhixin.util.GlideLoadImageUtils;
import com.shuzhengit.zhixin.util.ResourceUtils;
import com.shuzhengit.zhixin.view.CircleImageView;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;
import com.shuzhengit.zhixin.wenba.detail.WenBaDetailActivity;

import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/29 10:44
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class HistoryQuestionFragment extends BaseFragment<HistoryContract.Presenter> implements HistoryContract.View {
    private static final String TAG = "HistoryQuestionFragment";
    private PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    private TextView mTvNoContent;
    private int mMemberId;
    private BaseRecyclerViewAdapter<WenBa> mAdapter;
    private DataContainer<WenBa> mDataContainer;
    private int mCurrentPage = 1;

    public static HistoryQuestionFragment getInstance(int memberId) {
        Bundle bundle = new Bundle();
        bundle.putInt("memberId", memberId);
        HistoryQuestionFragment historyQuestionFragment = new HistoryQuestionFragment();
        historyQuestionFragment.setArguments(bundle);
        return historyQuestionFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mMemberId = getArguments().getInt("memberId");
        mPullToRefreshRecyclerView = (PullToRefreshRecyclerView) view.findViewById(R.id.pullToRefreshRecyclerView);
        mTvNoContent = (TextView) view.findViewById(R.id.tvNOContent);
        mTvNoContent.setText("暂无浏览历史");
        mPullToRefreshRecyclerView.setShowPullToRefresh(true);
        mPullToRefreshRecyclerView.setShowLoadMore(true);
        mPullToRefreshRecyclerView.setRefreshListener(new PullToRefreshRecyclerView.RefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage = 1;
                mBasePresenter.refreshQuestionByHistory(mMemberId);
            }
        });
        mAdapter = new BaseRecyclerViewAdapter<WenBa>(R
                .layout.item_wenba_follow) {
            @Override
            public void convert(BaseViewHolder holder, int position, WenBa wenBa) {
                CircleImageView civAvatar = holder.getViewById(R.id.civAvatar);
                GlideLoadImageUtils.loadImg(getHoldingActivity(), wenBa.getAvatarUrl(), civAvatar);
                holder.setText(R.id.tvAdminName, wenBa.getNickName());
                holder.setText(R.id.tvDescription, wenBa.getWelcomeTitle());
                holder.setText(R.id.tvType, String.format(ResourceUtils.getResourceString(APP.getInstance(), R.string
                        .wenBaType), wenBa.getColumnTitle()));
                holder.setText(R.id.tvCommentCount, String.format(ResourceUtils.getResourceString(APP.getInstance(), R
                        .string.userFollow), wenBa.getLikeCount()));
                holder.setText(R.id.tvAskCount, String.format(ResourceUtils.getResourceString(APP.getInstance(), R
                        .string.askCount), wenBa.getAskCount()));
            }

            @Override
            public void onItemClick(View v, int position, WenBa wenBa) {
                Bundle bundle = new Bundle();
                bundle.putInt("wenBaId", wenBa.getId());
                startActivity(WenBaDetailActivity.class, bundle);
            }
        };

//        mPullToRefreshRecyclerView.addItemDecoration(new ListDividerItemDecoration());
        mPullToRefreshRecyclerView.setAdapter(mAdapter);
        mPullToRefreshRecyclerView.setLoadMoreListener(new PullToRefreshRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                LogUtils.d(TAG, "has next page : " + mDataContainer.isHasNextPage());
                if (mDataContainer!=null && mDataContainer.isHasNextPage()) {
                    mBasePresenter.requestQuestionByHistory(mMemberId, ++mCurrentPage, 10);
                } else {
                    loadNoMore();
                }
            }
        });
//        mAdapter.setOnItemClickListener(new QuestionAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Question question) {
//                Bundle bundle = new Bundle();
//                bundle.putInt("questionId",question.getId());
//                startActivity(QuestionDetailActivity.class,bundle);
//            }
//        });
    }

    @Override
    protected void createPresenter() {
        mBasePresenter = new HistoryPresenter(this);
//        mBasePresenter.requestQuestionByHistory(mMemberId,1,10);
        mPullToRefreshRecyclerView.refresh();
    }

    @Override
    public void setDocuments(List<Document> documents) {

    }

    @Override
    public void refreshDocument(List<Document> documents) {
    }

    @Override
    public void setQuestions(DataContainer<WenBa> dataContainer) {
        mDataContainer = dataContainer;
        mAdapter.addMoreData(dataContainer.getList());
        if (!mDataContainer.isHasNextPage()) {
            mPullToRefreshRecyclerView.setNoMore(true);
        }

    }

    @Override
    public void refreshQuestions(DataContainer<WenBa> dataContainer) {
        mDataContainer = dataContainer;
        mAdapter.onRefreshData(dataContainer.getList());
//            mPullToRefreshRecyclerView.setShowLoadMore(true);
        refreshCompleted();
    }

    @Override
    public void refreshCompleted() {
        mPullToRefreshRecyclerView.refreshCompleted();
    }

    @Override
    public void loadMoreCompleted() {
        mPullToRefreshRecyclerView.loadMoreCompleted();
    }

    @Override
    public void loadMoreFail() {
        mPullToRefreshRecyclerView.loadMoreFail();
    }

    @Override
    public void loadNoMore() {
        mPullToRefreshRecyclerView.setNoMore(true);
    }
}
