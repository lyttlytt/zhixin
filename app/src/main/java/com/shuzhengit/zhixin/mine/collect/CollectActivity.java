package com.shuzhengit.zhixin.mine.collect;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.Document;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.index.document.detail.DocumentDetailActivity;
import com.shuzhengit.zhixin.index.document.holder.MultipleAdapter;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.view.DocumentRemoveAnimator;
import com.shuzhengit.zhixin.view.ListDividerItemDecoration;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CollectActivity extends BaseActivity<CollectContract.Presenter> implements CollectContract.View {
    private static final String TAG = "CollectActivity";
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
    private Unbinder mUnbinder;
    private int mCurrentPage = 1;
    private MultipleAdapter mMultipleAdapter;
    private User mUser;

    @Override
    protected int layoutId() {
        return R.layout.activity_collect;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar!=null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        mToolBar.setNavigationOnClickListener(v -> onBackPressed());

        mTvTitle.setText("我的收藏");
        mMultipleAdapter = new MultipleAdapter();
        mPullToRefreshRecyclerView.setAdapter(mMultipleAdapter);
        mPullToRefreshRecyclerView.addItemDecoration(new ListDividerItemDecoration());
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
                mBasePresenter.refreshCollectDocument(mUser.getId());
            }
        });
        mPullToRefreshRecyclerView.setLoadMoreListener(new PullToRefreshRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                mBasePresenter.findCollectDocument(mUser.getId(),++mCurrentPage,10);
            }
        });
    }

    @Override
    protected void createPresenter() {
        mUser = CheckUser.checkUserIsExists();
        if (mUser==null){
            failure("请先登录");
            onBackPressed();
            return;
        }
        mBasePresenter = new CollectPresenter(this);
        mPullToRefreshRecyclerView.refresh();
    }

    @Override
    public void refreshCollectDocuments(List<Document> documents) {
        LogUtils.d(TAG,documents.toString());
        mMultipleAdapter.onRefresh(documents);
        if (documents.size()<10){
            mPullToRefreshRecyclerView.setShowLoadMore(false);
        }else {
            mPullToRefreshRecyclerView.setShowLoadMore(true);
        }
    }

    @Override
    public void setCollectDocuments(List<Document> documents) {
        LogUtils.i(TAG,documents.toString());
        mMultipleAdapter.addMore(documents);
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
    public void loadNoMore() {
        mPullToRefreshRecyclerView.setNoMore(true);
    }

    @Override
    public void loadMoreFail() {
        mPullToRefreshRecyclerView.loadMoreFail();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
