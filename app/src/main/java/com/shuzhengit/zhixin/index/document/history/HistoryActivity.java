//package com.shuzhengit.zhixin.index.document.history;
//
//import android.os.Bundle;
//import android.support.v7.app.ActionBar;
//import android.support.v7.widget.Toolbar;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.library.base.BaseActivity;
//import com.shuzhengit.zhixin.R;
//import com.shuzhengit.zhixin.bean.Document;
//import com.shuzhengit.zhixin.bean.User;
//import com.shuzhengit.zhixin.index.document.detail.DocumentDetailActivity;
//import com.shuzhengit.zhixin.index.document.holder.MultipleAdapter;
//import com.shuzhengit.zhixin.util.CheckUser;
//import com.shuzhengit.zhixin.util.EventCodeUtils;
//import com.shuzhengit.zhixin.view.DocumentRemoveAnimator;
//import com.shuzhengit.zhixin.view.ListDividerItemDecoration;
//import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;
//import com.umeng.analytics.MobclickAgent;
//
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.Unbinder;
//
//public class HistoryActivity extends BaseActivity<HistoryOrCollectContract.Presenter> implements
//        HistoryOrCollectContract.View {
//    private static final String TAG = "HistoryActivity";
//
//    @BindView(R.id.tvTitle)
//    TextView mTvTitle;
//    @BindView(R.id.tvRight)
//    TextView mTvRight;
//    @BindView(R.id.ivRightShare)
//    ImageView mIvRightShare;
//    @BindView(R.id.toolBar)
//    Toolbar mToolBar;
//    @BindView(R.id.pullToRefreshRecyclerView)
//    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
//    @BindView(R.id.tvNOContent)
//    TextView mTvNOContent;
//    @BindView(R.id.llNoContent)
//    LinearLayout mLlNoContent;
//    private int mCurrentPage = 1;
//    private boolean mIsHistory;
//    private MultipleAdapter mMultipleAdapter;
//    private List<Document> mPageContainer;
//    private User mUser;
//    private Unbinder mUnbinder;
//
//    public void onResume() {
//        super.onResume();
//        //统计页面
//        MobclickAgent.onPageStart(TAG);
//        //统计时长
//        MobclickAgent.onResume(this);
//
//    }
//
//    public void onPause() {
//        super.onPause();
//        //统计页面
//        MobclickAgent.onPageEnd(TAG);
//        //统计页面的时长
//        MobclickAgent.onPause(this);
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mUnbinder.unbind();
//    }
//
//    @Override
//    protected int layoutId() {
//        return R.layout.activity_history;
//    }
//
//    @Override
//    protected void initView() {
//        mUnbinder = ButterKnife.bind(this);
//        mTvTitle = (TextView) findViewById(R.id.tvTitle);
//        mTvRight = (TextView) findViewById(R.id.tvRight);
//        mIvRightShare = (ImageView) findViewById(R.id.ivRightShare);
//        mToolBar = (Toolbar) findViewById(R.id.toolBar);
//        mPullToRefreshRecyclerView = (PullToRefreshRecyclerView) findViewById(R.id.pullToRefreshRecyclerView);
//        mTvNOContent = (TextView) findViewById(R.id.tvNOContent);
//        mLlNoContent = (LinearLayout) findViewById(R.id.llNoContent);
//
//
//        setSupportActionBar(mToolBar);
//        ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowTitleEnabled(false);
//        mToolBar.setNavigationOnClickListener(v -> onBackPressed());
//        mIsHistory = getIntent().getBooleanExtra(EventCodeUtils.IS_HISTORY, false);
//        mUser = CheckUser.checkUserIsExists();
//        if (mIsHistory) {
//            mTvTitle.setText("浏览历史");
//        } else {
//            mTvTitle.setText("我的收藏");
//        }
//        mPullToRefreshRecyclerView.setShowPullToRefresh(false);
//        mPullToRefreshRecyclerView.setShowLoadMore(false);
//        mPullToRefreshRecyclerView.setLoadMoreListener(new PullToRefreshRecyclerView.LoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                mBasePresenter.requestMoreDocument(mUser.getId(), ++mCurrentPage, 10, mIsHistory);
//            }
//        });
//        mPullToRefreshRecyclerView.setRefreshListener(new PullToRefreshRecyclerView.RefreshListener() {
//            @Override
//            public void onRefresh() {
//                mCurrentPage = 1;
//                mBasePresenter.refreshDocument(mUser.getId(), mCurrentPage, 10, mIsHistory);
//            }
//        });
//        mPullToRefreshRecyclerView.setEmptyView(mLlNoContent);
//        if (mIsHistory)
//            mTvNOContent.setText("暂无浏览历史");
//        else
//            mTvNOContent.setText("暂无收藏");
//        mMultipleAdapter = new MultipleAdapter();
//        mPullToRefreshRecyclerView.setAdapter(mMultipleAdapter);
//        mPullToRefreshRecyclerView.addItemDecoration(new ListDividerItemDecoration());
//        mPullToRefreshRecyclerView.setItemAnimator(new DocumentRemoveAnimator());
//        mMultipleAdapter.setOnTypeThreeItemClickListener(new MultipleAdapter.OnTypeThreeItemClickListener() {
//            @Override
//            public void onTypeThreeItemClickListener(Document document) {
//                Bundle bundle = new Bundle();
//                bundle.putInt(EventCodeUtils.DOCUMENT_ID, document.getId());
//                startActivity(DocumentDetailActivity.class, bundle);
//            }
//
//            @Override
//            public void onTypeThreeRemoveItemListener(Document document) {
//
//            }
//        });
//        mMultipleAdapter.setOnTypeOneItemClickListener(new MultipleAdapter.OnTypeOneItemClickListener() {
//            @Override
//            public void onTypeOneItemClickListener(Document document) {
//                Bundle bundle = new Bundle();
//                bundle.putInt(EventCodeUtils.DOCUMENT_ID, document.getId());
//                startActivity(DocumentDetailActivity.class, bundle);
//            }
//        });
//        mMultipleAdapter.setOnTypeTwoItemClickListener(new MultipleAdapter.OnTypeTwoItemClickListener() {
//            @Override
//            public void onTypeTwoItemClickListener(Document document) {
//                Bundle bundle = new Bundle();
//                bundle.putInt(EventCodeUtils.DOCUMENT_ID, document.getId());
//                startActivity(DocumentDetailActivity.class, bundle);
//            }
//        });
//    }
//
//    @Override
//    protected void createPresenter() {
//        mBasePresenter = new HistoryOrCollectPresenter(this);
//
//        if (mUser != null) {
//            if (mIsHistory)
//                mBasePresenter.requestDocumentByHistory(mUser.getId(), mCurrentPage, 10);
//            else
//                mBasePresenter.requestDocumentByCollect(mUser.getId(), mCurrentPage, 10);
//        }
//    }
//
//
//    @Override
//    public void findDocumentByHistorySuccess(List<Document> documentPageContainer) {
//        mPullToRefreshRecyclerView.setShowPullToRefresh(true);
//        mPullToRefreshRecyclerView.setShowLoadMore(true);
//        mMultipleAdapter.addMore(documentPageContainer);
//    }
//
//    @Override
//    public void findDocumentByCollectSuccess(List<Document> documentPageContainer) {
////        mMultipleAdapter = new MultipleAdapter();
//        mPullToRefreshRecyclerView.setShowPullToRefresh(true);
//        mPullToRefreshRecyclerView.setShowLoadMore(true);
//        mMultipleAdapter.addMore(documentPageContainer);
//    }
//
//    @Override
//    public void refreshSuccess(List<Document> documentPageContainer) {
//        mMultipleAdapter.onRefresh(documentPageContainer);
//        refreshFail();
//    }
//
//    @Override
//    public void refreshFail() {
//        mPullToRefreshRecyclerView.refreshCompleted();
//    }
//
//    @Override
//    public void loadMoreSuccess(List<Document> documentPageContainer) {
//        mMultipleAdapter.addMore(documentPageContainer);
//        mPullToRefreshRecyclerView.loadMoreCompleted();
//    }
//
//    @Override
//    public void loadNoMore() {
//        mPullToRefreshRecyclerView.setNoMore(true);
//    }
//
//    @Override
//    public void loadFail() {
//        mPullToRefreshRecyclerView.loadMoreFail();
//    }
//
//}
