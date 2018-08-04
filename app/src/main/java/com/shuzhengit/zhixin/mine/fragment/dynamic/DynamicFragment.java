package com.shuzhengit.zhixin.mine.fragment.dynamic;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.library.base.BaseFragment;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.Dynamic;
import com.shuzhengit.zhixin.index.document.detail.DocumentDetailActivity;
import com.shuzhengit.zhixin.question.question_detail.QuestionDetailActivity;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.util.SocializeMediaTypeUtils;
import com.shuzhengit.zhixin.util.SocializeShareUtils;
import com.shuzhengit.zhixin.view.ListDividerItemDecoration;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;

import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/22 14:09
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:用户信息 动态
 */

public class DynamicFragment extends BaseFragment<DynamicContract.Presenter> implements DynamicContract.View {

    private PullToRefreshRecyclerView mPull2RecyclerView;
    private DynamicAdapter mDynamicAdapter;
    private int mCurrentPage =1;
//    private User mUser;
    private int mQueryUserId;

    public static DynamicFragment getInstance(int queryUserId) {
        Bundle bundle = new Bundle();
        bundle.putInt("queryUserId",queryUserId);
        DynamicFragment dynamicFragment = new DynamicFragment();
        dynamicFragment.setArguments(bundle);
        return dynamicFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mQueryUserId = getArguments().getInt("queryUserId", 0);
//        mUser = CheckUser.checkUserIsExists();
        mPull2RecyclerView = (PullToRefreshRecyclerView) view.findViewById(R.id
                .pullToRefreshRecyclerView);
        mPull2RecyclerView.setShowPullToRefresh(false);
        mDynamicAdapter = new DynamicAdapter(getContext());
        mPull2RecyclerView.setAdapter(mDynamicAdapter);
        mPull2RecyclerView.addItemDecoration(new ListDividerItemDecoration());
        mPull2RecyclerView.setLoadMoreListener(new PullToRefreshRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
//                mBasePresenter.findDynamics(mUser.getId(),++mCurrentPage,10);
                mBasePresenter.findDynamics(mQueryUserId,++mCurrentPage,10);
            }
        });
        mDynamicAdapter.setOnItemClickListener(new DynamicAdapter.OnItemClickListener() {
            @Override
            public void onDocumentItemClick(int documentId,String esId) {
                Bundle bundle = new Bundle();
                bundle.putInt(EventCodeUtils.DOCUMENT_ID,documentId);
                bundle.putString("esId",esId);
                startActivity(DocumentDetailActivity.class, bundle);
            }

            @Override
            public void onQuestionItemClick(int questionId) {
                LogUtils.i("TAG",questionId + "---------");
                Bundle bundle = new Bundle();
                bundle.putInt("questionId",questionId);
                startActivity(QuestionDetailActivity.class,bundle);
            }

            @Override
            public void onItemShareClick(Dynamic dynamic) {
                new SocializeShareUtils.Builder()
                        .setSocializeMedia(SocializeMediaTypeUtils.QQ, SocializeMediaTypeUtils.WEIXIN,
                                SocializeMediaTypeUtils.WEIXIN_CIRCLE, SocializeMediaTypeUtils.SINA)
                        .setUMWebTitle(dynamic.getTitle())
                        .setUMWebUrl(APP.SHARE_URL)
                        .setUMWebDescription(dynamic.getContent())
                        .setUMWebThumb(R.mipmap.logo)
                        .buildUMWeb(mActivity)
                        .openShare(mActivity, new SocializeShareUtils.ShareListener(){
                            @Override
                            public void onCancel(Context context) {
                                super.onCancel(context);
                                failure("取消");
                            }
                        });
            }
        });
    }

    @Override
    protected void createPresenter() {
        mBasePresenter = new DynamicPresenter(this);
        mBasePresenter.findDynamics(mQueryUserId,mCurrentPage,10);
    }

    @Override
    public void setDynamics(List<Dynamic> dynamics) {
        mDynamicAdapter.addDynamics(dynamics);
    }

    @Override
    public void loadMoreCompleted() {
        mPull2RecyclerView.loadMoreCompleted();
    }

    @Override
    public void loadMoreFail(String message) {
        mPull2RecyclerView.loadMoreFail();
    }

    @Override
    public void loadMoreNoData() {
        mPull2RecyclerView.setNoMore(true);
    }
}
