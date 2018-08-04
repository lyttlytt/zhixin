package com.shuzhengit.zhixin.index.document.local;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.library.base.BaseLazyLoadFragment;
import com.library.rx.RxBus2;
import com.library.util.LogUtils;
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
import com.shuzhengit.zhixin.view.ListDividerItemDecoration;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/19 20:17
 * E-mail:yuancongbin@gmail.com
 */

public class LocalSchoolFragment extends BaseLazyLoadFragment<LocalContract.Presenter> implements LocalContract
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
    private static final String TAG = "localSchoolFragment";
    private int mLocalSchoolCode;
    private int mCurrentPage = 1;
    private Unbinder mUnbinder;
    private boolean mModifySchoolId;

    public static LocalSchoolFragment getInstance(int schoolId) {
        LocalSchoolFragment documentFragment = new LocalSchoolFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EventCodeUtils.COLUMN_LOCAL_SCHOOL, schoolId);
        documentFragment.setArguments(bundle);
        return documentFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart(TAG);

        mModifySchoolId = (boolean) SPUtils.get(APP.getInstance(), CacheKeyManager.MODIFY_SCHOOL_ID, false);
        LogUtils.e(TAG,"modifySchoolId : " + mModifySchoolId);
        if (mModifySchoolId) {
            User user = CheckUser.checkUserIsExists();
            if (user == null) {
                mLocalSchoolCode = (int) SPUtils.get(APP.getInstance(), CacheKeyManager.ANONYMOUS_SCHOOL_ID, 0);
            } else {
                mLocalSchoolCode = user.getSchoolId();
            }
            mDocumentRecyclerView.refresh();
            mFlSelectSchool.setVisibility(View.GONE);
//            mBasePresenter.onRequestLocalSchool(mLocalSchoolCode, mCurrentPage);
            SPUtils.put(APP.getInstance(), CacheKeyManager.MODIFY_SCHOOL_ID, false);
        }else {
            User user = CheckUser.checkUserIsExists();
            if (user!=null && user.getSchoolId()!=0){
                mFlSelectSchool.setVisibility(View.GONE);
            }else {
                mLocalSchoolCode = (int) SPUtils.get(APP.getInstance(), CacheKeyManager.ANONYMOUS_SCHOOL_ID, 0);
                if (mLocalSchoolCode==0){
                    mFlSelectSchool.setVisibility(View.VISIBLE);
                }else {
                    mFlSelectSchool.setVisibility(View.GONE);
                }
            }
        }
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
        mUnbinder = ButterKnife.bind(this, view);
        mMultipleAdapter = new MultipleAdapter();
        mDocumentRecyclerView.setAdapter(mMultipleAdapter);
        mDocumentRecyclerView.addItemDecoration(new ListDividerItemDecoration());
        mLocalSchoolCode = getArguments().getInt(EventCodeUtils.COLUMN_LOCAL_SCHOOL);
        mIvEmpty.setImageResource(R.drawable.empty_logo);
        mDocumentRecyclerView.setEmptyView(mIvEmpty);
        mDocumentRecyclerView.setShowPullToRefresh(false);
        mDocumentRecyclerView.setShowLoadMore(false);
        mDocumentRecyclerView.setRefreshListener(() -> {
            mCurrentPage = 1;
            mBasePresenter.onRefreshLocalSchool(mLocalSchoolCode, mCurrentPage);
        });
        mDocumentRecyclerView.setLoadMoreListener(() ->
                {
                    mBasePresenter.onLoadMoreLocalSchool(mLocalSchoolCode, ++mCurrentPage);
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

        mBtnSelectSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SelectSchoolActivity.class);
            }
        });
        RxBus2.getDefault().toFlowable(EventType.class)
                .filter(new Predicate<EventType>() {
                    @Override
                    public boolean test(EventType eventType) throws Exception {
                        return eventType.getEventType().equals(EventCodeUtils.USER_SCHOOL_ID);
                    }
                }).map(new Function<EventType, Integer>() {
            @Override
            public Integer apply(EventType eventType) throws Exception {
                return (Integer) eventType.getEvent();
            }
        }).subscribeWith(new RxSubscriber<Integer>() {
            @Override
            protected void _onNext(Integer integer) {
                mLocalSchoolCode = integer;
                mCurrentPage=1;
                LogUtils.d(TAG,"school id : " + integer);
//                mBasePresenter.onRefreshLocalSchool(mLocalSchoolCode,mCurrentPage);
            }
        });
    }

    @Override
    protected void lazyLoadCreatePresenter() {
        if (mLocalSchoolCode == 0) {
            mFlSelectSchool.setVisibility(View.VISIBLE);
        } else {
            mBasePresenter = new LocalPresenter(this);
            mBasePresenter.onRequestLocalSchool(mLocalSchoolCode, mCurrentPage);
        }
//        if (mModifySchoolId) {
//            User user = CheckUser.checkUserIsExists();
//            if (user == null) {
//                mLocalSchoolCode = (int) SPUtils.get(APP.getInstance(), CacheKeyManager.ANONYMOUS_SCHOOL_ID, 0);
//            } else {
//                mLocalSchoolCode = user.getSchoolId();
//            }
////            mBasePresenter.onRequestLocalSchool(mLocalSchoolCode, mCurrentPage);
//            SPUtils.put(APP.getInstance(), CacheKeyManager.MODIFY_SCHOOL_ID, false);
//        }
    }


    @Override
    public void onRequestLocalSchoolSuccess(List<Document> schoolDocument) {
        mMultipleAdapter.addMore(schoolDocument);
    }

    @Override
    public void onRefreshLocalSchoolSuccess(List<Document> schoolDocument) {
        mMultipleAdapter.onRefresh(schoolDocument);
    }

    @Override
    public void onRequestLocalCitySuccess(List<Document> cityDocuments) {
    }

    @Override
    public void onRefreshLocalCitySuccess(List<Document> cityDocuments) {

    }

    @Override
    public void onLoadMoreLocalSchool(List<Document> schoolDocument) {
        mMultipleAdapter.addMore(schoolDocument);
    }

    @Override
    public void onLoadMoreLocalCity(List<Document> cityDocument) {

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

}
