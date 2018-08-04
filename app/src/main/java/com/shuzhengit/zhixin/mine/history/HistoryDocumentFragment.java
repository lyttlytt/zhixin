package com.shuzhengit.zhixin.mine.history;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.library.base.BaseFragment;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.Document;
import com.shuzhengit.zhixin.bean.WenBa;
import com.shuzhengit.zhixin.index.document.detail.DocumentDetailActivity;
import com.shuzhengit.zhixin.index.document.holder.MultipleAdapter;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.view.DocumentRemoveAnimator;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;

import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/29 10:39
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class HistoryDocumentFragment extends BaseFragment<HistoryContract.Presenter> implements HistoryContract.View{

    private PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    private TextView mTvNoContent;
    private int mMemberId;
    private int mCurrentPage =1;
    private MultipleAdapter mMultipleAdapter;

    public static HistoryDocumentFragment getInstance(int memberId){
        Bundle bundle = new Bundle();
        bundle.putInt("memberId",memberId);
        HistoryDocumentFragment historyDocumentFragment = new HistoryDocumentFragment();
        historyDocumentFragment.setArguments(bundle);
        return historyDocumentFragment;
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

        mMultipleAdapter = new MultipleAdapter();
        mPullToRefreshRecyclerView.setAdapter(mMultipleAdapter);
//        mPullToRefreshRecyclerView.addItemDecoration(new ListDividerItemDecoration());
        mPullToRefreshRecyclerView.setItemAnimator(new DocumentRemoveAnimator());
        mMultipleAdapter.setOnTypeThreeItemClickListener(new MultipleAdapter.OnTypeThreeItemClickListener() {
            @Override
            public void onTypeThreeItemClickListener(Document document) {
                Bundle bundle = new Bundle();
                bundle.putInt(EventCodeUtils.DOCUMENT_ID, document.getId());
                bundle.putString("esId",document.getElcIndexId());
                startActivity(DocumentDetailActivity.class, bundle);
            }

            @Override
            public void onTypeThreeRemoveItemListener(Document document) {

            }
        });
        mMultipleAdapter.setOnTypeOneItemClickListener(new MultipleAdapter.OnTypeOneItemClickListener() {
            @Override
            public void onTypeOneItemClickListener(Document document) {
                Bundle bundle = new Bundle();
                bundle.putInt(EventCodeUtils.DOCUMENT_ID, document.getId());
                bundle.putString("esId",document.getElcIndexId());
                startActivity(DocumentDetailActivity.class, bundle);
            }
        });
        mMultipleAdapter.setOnTypeTwoItemClickListener(new MultipleAdapter.OnTypeTwoItemClickListener() {
            @Override
            public void onTypeTwoItemClickListener(Document document) {
                Bundle bundle = new Bundle();
                bundle.putInt(EventCodeUtils.DOCUMENT_ID, document.getId());
                bundle.putString("esId",document.getElcIndexId());
                startActivity(DocumentDetailActivity.class, bundle);
            }
        });
        mPullToRefreshRecyclerView.setRefreshListener(new PullToRefreshRecyclerView.RefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage =1;
                mBasePresenter.refreshDocumentByHistory(mMemberId);
            }
        });
        mPullToRefreshRecyclerView.setLoadMoreListener(new PullToRefreshRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                mBasePresenter.requestDocumentByHistory(mMemberId,++mCurrentPage,10);
            }
        });
    }

    @Override
    protected void createPresenter() {
        mBasePresenter = new HistoryPresenter(this);
        mPullToRefreshRecyclerView.refresh();
    }

    @Override
    public void setDocuments(List<Document> documents) {
        mMultipleAdapter.addMore(documents);
    }

    @Override
    public void refreshDocument(List<Document> documents) {
        mMultipleAdapter.onRefresh(documents);
    }

    @Override
    public void setQuestions(DataContainer<WenBa> dataContainer) {

    }

    @Override
    public void refreshQuestions(DataContainer<WenBa> dataContainer) {

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
