package com.shuzhengit.zhixin.index.document.local;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.library.base.BaseLazyLoadFragment;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.Document;
import com.shuzhengit.zhixin.index.document.detail.DocumentDetailActivity;
import com.shuzhengit.zhixin.index.document.holder.MultipleAdapter;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.view.ListDividerItemDecoration;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/19 20:17
 * E-mail:yuancongbin@gmail.com
 */

public class LocalCityFragment extends BaseLazyLoadFragment<LocalContract.Presenter> implements LocalContract
        .View {
    @BindView(R.id.pullToRefreshRecyclerView)
    PullToRefreshRecyclerView mDocumentRecyclerView;
    @BindView(R.id.ivEmpty)
    ImageView mIvEmpty;
    private MultipleAdapter mMultipleAdapter;
    private static final String TAG = "localCityFragment";
    private String mLocalCity;
    private int mCurrentPage = 1;
    private Unbinder mUnbinder;

    public static LocalCityFragment getInstance(String localCity) {
        LocalCityFragment documentFragment = new LocalCityFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EventCodeUtils.COLUMN_LOCAL_CITY, localCity);
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
        mDocumentRecyclerView.addItemDecoration(new ListDividerItemDecoration());
        mLocalCity = getArguments().getString(EventCodeUtils.COLUMN_LOCAL_CITY);
        mIvEmpty.setImageResource(R.drawable.empty_logo);
        mDocumentRecyclerView.setEmptyView(mIvEmpty);
        mDocumentRecyclerView.setShowPullToRefresh(true);
        mDocumentRecyclerView.setShowLoadMore(false);
        mDocumentRecyclerView.setRefreshListener(() -> {
            mCurrentPage = 1;
            mBasePresenter.onRefreshLocalCity(mLocalCity);
        });
        mDocumentRecyclerView.setLoadMoreListener(() ->
                {
                    mBasePresenter.onLoadMoreLocalCity(mLocalCity, ++mCurrentPage);
                }
        );
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
        mBasePresenter = new LocalPresenter(this);
        mCurrentPage =1;
        mBasePresenter.onRefreshLocalCity(mLocalCity);
    }


    @Override
    public void onRequestLocalSchoolSuccess(List<Document> schoolDocument) {

    }

    @Override
    public void onRefreshLocalSchoolSuccess(List<Document> schoolDocument) {

    }

    @Override
    public void onRequestLocalCitySuccess(List<Document> cityDocuments) {
        if (cityDocuments.size()<10){
            mDocumentRecyclerView.setShowLoadMore(false);
        }
        mMultipleAdapter.addMore(cityDocuments);
    }

    @Override
    public void onRefreshLocalCitySuccess(List<Document> cityDocuments) {
        mMultipleAdapter.onRefresh(cityDocuments);
    }

    @Override
    public void onLoadMoreLocalSchool(List<Document> schoolDocument) {

    }

    @Override
    public void onLoadMoreLocalCity(List<Document> cityDocument) {
        mMultipleAdapter.addMore(cityDocument);
    }

    @Override
    public void onRefreshFail(String errorMessage) {
        failure(errorMessage);
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

    @Override
    public void onLoadMoreComplete() {
        mDocumentRecyclerView.loadMoreCompleted();
    }

    @Override
    public void onRefreshComplete() {
        mDocumentRecyclerView.refreshCompleted();
    }

    public void smoothTop(){
        mDocumentRecyclerView.smoothScrollToPosition(0);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
