package com.shuzhengit.zhixin.index.document;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.library.base.BaseLazyLoadFragment;
import com.library.rx.RxBus2;
import com.library.util.SPUtils;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.Document;
import com.shuzhengit.zhixin.bean.EventType;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.index.document.detail.DocumentDetailActivity;
import com.shuzhengit.zhixin.index.document.holder.MultipleAdapter;
import com.shuzhengit.zhixin.mine.SelectSchoolActivity;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.shuzhengit.zhixin.util.CacheKeyManager;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Predicate;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/19 20:17
 * E-mail:yuancongbin@gmail.com
 */

public class DocumentFragment extends BaseLazyLoadFragment<DocumentContract.Presenter> implements DocumentContract
        .View {
    @BindView(R.id.pullToRefreshRecyclerView)
    PullToRefreshRecyclerView mDocumentRecyclerView;
    @BindView(R.id.ivEmpty)
    ImageView mIvEmpty;
    @BindView(R.id.flSelectSchool)
    FrameLayout mFlSelectSchool;
    @BindView(R.id.selectSchool)
    Button mBtnSelectSchool;
    private MultipleAdapter mMultipleAdapter;
    private static final String TAG = "DocumentFragment";
    private int mCurrentPage = 1;
    private String mColumnCode;
    private Unbinder mUnbinder;
    private RxSubscriber<EventType> mRxSubscriber;


    public String getColumnCode() {
        return mColumnCode;
    }

    public static DocumentFragment getInstance(String columnCode) {
        DocumentFragment documentFragment = new DocumentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EventCodeUtils.DOCUMENT_FRAGMENT_VALUE, columnCode);
        documentFragment.setArguments(bundle);
        return documentFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        //统计页面
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_document;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(this,view);
        mMultipleAdapter = new MultipleAdapter();
        mDocumentRecyclerView.setAdapter(mMultipleAdapter);
//        mDocumentRecyclerView.addItemDecoration(new ListDividerItemDecoration());
        mColumnCode = getArguments().getString(EventCodeUtils.DOCUMENT_FRAGMENT_VALUE);
        mIvEmpty.setImageResource(R.drawable.empty_logo);
        mDocumentRecyclerView.setEmptyView(mIvEmpty);
        mDocumentRecyclerView.setShowPullToRefresh(true);
        mDocumentRecyclerView.setShowLoadMore(false);
        mDocumentRecyclerView.setRefreshListener(() -> {
            mCurrentPage = 1;
            mBasePresenter.onRefreshDocuments(mColumnCode, mCurrentPage);
        });
        mDocumentRecyclerView.setLoadMoreListener(() ->
                {
                    mBasePresenter.onLoadMoreDocuments(mColumnCode, ++mCurrentPage);
                }
        );
        mMultipleAdapter.setOnTypeOneItemClickListener(new MultipleAdapter.OnTypeOneItemClickListener() {
            @Override
            public void onTypeOneItemClickListener(Document document) {
                Bundle bundle = new Bundle();
                bundle.putInt(EventCodeUtils.DOCUMENT_ID, document.getId());
                bundle.putString("esId",document.getElcIndexId());
//                bundle.putInt("id", document.getId());
//                startActivity(WebViewActivity.class);
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
//                bundle.putInt("id", document.getId());
//                startActivity(WebViewActivity.class);
            }
        });
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
        mMultipleAdapter.setOnTypeFourItemClickListener(new MultipleAdapter.OnTypeFourItemClickListener() {
            @Override
            public void onTypeFourItemClickListener(Document document) {
                Bundle bundle = new Bundle();
                bundle.putInt(EventCodeUtils.DOCUMENT_ID, document.getId());
                bundle.putString("esId",document.getElcIndexId());
                startActivity(DocumentDetailActivity.class, bundle);
            }
        });
    }

    @Override
    protected void lazyLoadCreatePresenter() {
        mBasePresenter = new DocumentPresenter(this);
        mCurrentPage =1;
        if (mColumnCode.equals("benxiao")||mColumnCode.equals("zhoubian")) {
            User user = CheckUser.checkUserIsExists();
            int schoolId;
            if (user != null) {
                schoolId = user.getSchoolId();
            } else {
                schoolId = (int) SPUtils.get(APP.getInstance(), CacheKeyManager.ANONYMOUS_SCHOOL_ID, 0);
            }
            if (schoolId!=0){
                mBasePresenter.onRefreshDocuments(mColumnCode, mCurrentPage);
            }else {
                mFlSelectSchool.setVisibility(View.VISIBLE);
                mBtnSelectSchool.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRxSubscriber = new RxSubscriber<EventType>() {
                            @Override
                            protected void _onNext(EventType integer) {
                                mFlSelectSchool.setVisibility(View.GONE);
                                mBasePresenter.onRefreshDocuments(mColumnCode, mCurrentPage);
                            }
                        };
                        RxBus2.getDefault().toFlowable(EventType.class)
                                .filter(new Predicate<EventType>() {
                                    @Override
                                    public boolean test(EventType eventType) throws Exception {
                                        return eventType.getEventType().equals(EventCodeUtils.USER_SCHOOL_ID);
                                    }
                                }).subscribeWith(mRxSubscriber);
                        startActivity(SelectSchoolActivity.class);
                    }
                });
            }
        }else {
            mBasePresenter.onRefreshDocuments(mColumnCode, mCurrentPage);
        }
        mDocumentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState){
                    case RecyclerView.SCROLL_STATE_IDLE: // The RecyclerView is not currently scrolling.
                        //当屏幕停止滚动，加载图片
                        try {
                            if(getContext() != null) {
                                Glide.with(getContext()).resumeRequests();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING: // The RecyclerView is currently being dragged by
                        // outside input such as user touch input.
                        //当屏幕滚动且用户使用的触碰或手指还在屏幕上，停止加载图片
                        try {
                            if(getContext() != null) {
                                Glide.with(getContext()).pauseRequests();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING: // The RecyclerView is currently animating to a final
                        // position while
                        // not under outside control.
                        //由于用户的操作，屏幕产生惯性滑动，停止加载图片
                        try {
                            if(getContext() != null) {
                                Glide.with(getContext()).pauseRequests();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    @Override
    public void onRequestDocumentsSuccess(List<Document> documents) {
        mMultipleAdapter.addMore(documents);
    }

    @Override
    public void onRefreshDocumentsSuccess(List<Document> documents) {
        mMultipleAdapter.onRefresh(documents);
        if (documents.size()<10){
            mDocumentRecyclerView.setShowLoadMore(true);
//            mDocumentRecyclerView.setNoMore(true);
//            mDocumentRecyclerView.setShowLoadMore(false);
//            onLoadNoMore();
        }else {
            mDocumentRecyclerView.setShowLoadMore(true);
        }
        mDocumentRecyclerView.refreshCompleted();
    }

    @Override
    public void onRefreshDocumentFail(String errorMessage) {
        failure(errorMessage);
        mDocumentRecyclerView.refreshCompleted();
    }

    @Override
    public void onLoadMoreSuccess(List<Document> documents) {
        mMultipleAdapter.addMore(documents);
        mDocumentRecyclerView.loadMoreCompleted();
    }

    @Override
    public void onLoadNoMore() {
        mDocumentRecyclerView.setNoMore(true);
    }

    @Override
    public void onLoadMoreFail(String errorMessage) {
        failure(errorMessage);
        mDocumentRecyclerView.loadMoreFail();
    }
    public void smoothTop(){
        mDocumentRecyclerView.smoothScrollToPosition(0);
//        mDocumentRecyclerView.refresh();
        mCurrentPage = 1;
        mBasePresenter.onRefreshDocuments(mColumnCode, mCurrentPage);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (mRxSubscriber!=null && !mRxSubscriber.isDisposed()){
            mRxSubscriber.dispose();
        }
    }
}
