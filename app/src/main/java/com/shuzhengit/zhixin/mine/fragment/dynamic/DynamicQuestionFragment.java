package com.shuzhengit.zhixin.mine.fragment.dynamic;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.library.base.BaseFragment;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.base.BaseRecyclerViewAdapter;
import com.shuzhengit.zhixin.base.BaseViewHolder;
import com.shuzhengit.zhixin.bean.AskWithReply;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.util.GlideLoadImageUtils;
import com.shuzhengit.zhixin.util.ResourceUtils;
import com.shuzhengit.zhixin.util.TimeUtil;
import com.shuzhengit.zhixin.view.CircleImageView;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;
import com.shuzhengit.zhixin.wenba.detail.WenBaDetailActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/22 14:13
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:动态问答
 */

public class DynamicQuestionFragment extends BaseFragment<DynamicQuestionContract.Presenter> implements
        DynamicQuestionContract.View {
    @BindView(R.id.pullToRefreshRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    private Unbinder mUnbinder;
    private int mQueryUserId;
    private BaseRecyclerViewAdapter<AskWithReply> mAdapter;
    private DataContainer<AskWithReply> mDataContainer;
    private int mCurrentPage =1;

    public static DynamicQuestionFragment getInstance(int queryUserId) {
        Bundle bundle = new Bundle();
        bundle.putInt("queryUserId",queryUserId);
        DynamicQuestionFragment dynamicQuestionFragment = new DynamicQuestionFragment();
        dynamicQuestionFragment.setArguments(bundle);
        return dynamicQuestionFragment;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(this, view);
        mQueryUserId = getArguments().getInt("queryUserId", 0);

        mPullToRefreshRecyclerView.setBackgroundColor(ResourceUtils.getResourceColor(APP.getInstance(),R.color.grey100));
        mPullToRefreshRecyclerView.setShowPullToRefresh(true);
        mPullToRefreshRecyclerView.setShowLoadMore(true);
        mPullToRefreshRecyclerView.setRefreshListener(new PullToRefreshRecyclerView.RefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage=1;
                User user = CheckUser.checkUserIsExists();
                mBasePresenter.findDynamicQuestion(mCurrentPage,10,mQueryUserId);
            }
        });
        mPullToRefreshRecyclerView.setLoadMoreListener(new PullToRefreshRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mDataContainer!=null && mDataContainer.isHasNextPage()){
                    User user = CheckUser.checkUserIsExists();
                    mBasePresenter.findDynamicQuestion(++mCurrentPage,10,mQueryUserId);
                }else {
                    loadMoreNoData();
                }
            }
        });
        mAdapter = new BaseRecyclerViewAdapter<AskWithReply>(R.layout
                .item_wenba_release_question) {

            @Override
            public void convert(BaseViewHolder holder, int position, AskWithReply askWithReply) {
                holder.setText(R.id.tvAdmin,askWithReply.getReplayNickName());
                holder.setText(R.id.tvProfession,askWithReply.getProfession());
                holder.setText(R.id.tvQuestionUserName,askWithReply.getNickName());
                holder.setText(R.id.tvQuestionInfo,askWithReply.getDescription());
                CircleImageView civAskAvatar = holder.getViewById(R.id.civQuestionAvatar);
                if (TextUtils.isEmpty(askWithReply.getAvatarUrl())){
                    civAskAvatar.setImageResource(R.drawable.ic_normal_icon);
                }else {
                    GlideLoadImageUtils.loadImg(getHoldingActivity(),askWithReply.getAvatarUrl() , civAskAvatar);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    Date parse = sdf.parse(askWithReply.getGmtCreate());
                    String timeFormatText = TimeUtil.getTimeFormatText(parse);
                    holder.setText(R.id.tvDate,timeFormatText);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (askWithReply.getAnswerStatus()!=0){
                    holder.getViewById(R.id.rlAdmin).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tvAdminName,askWithReply.getReplayNickName());
                    CircleImageView adminAvatar = holder.getViewById(R.id.civAdminAvatar);
                    if (TextUtils.isEmpty(askWithReply.getReplayavatarUrl())){
                        adminAvatar.setImageResource(R.drawable.ic_normal_icon);
                    }else {
                        GlideLoadImageUtils.loadImg(getHoldingActivity(),askWithReply.getReplayavatarUrl() , adminAvatar);
                    }
                    holder.setText(R.id.tvReplyInfo,askWithReply.getContent());

                }else {
//                    holder.getViewById(R.id.tvAdminName).setVisibility(View.GONE);
//                    holder.getViewById(R.id.civAdminAvatar).setVisibility(View.GONE);
//                    holder.getViewById(R.id.tvReplyInfo).setVisibility(View.GONE);
//                    holder.getViewById(R.id.viewLine).setVisibility(View.GONE);
                    holder.getViewById(R.id.rlAdmin).setVisibility(View.GONE);
                }
            }

            @Override
            public void onItemClick(View v, int position, AskWithReply askWithReply) {
                Bundle bundle = new Bundle();
                bundle.putInt("wenBaId",askWithReply.getAnswerUserId());
                startActivity(WenBaDetailActivity.class,bundle);
            }
        };
        mPullToRefreshRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void createPresenter() {
        mBasePresenter = new DynamicQuestionPresenter(this);
        mBasePresenter.findDynamicQuestion(mQueryUserId,mCurrentPage,10);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public void setDynamicQuestion(DataContainer<AskWithReply> dataContainer) {
        mDataContainer = dataContainer;
        mAdapter.addMoreData(dataContainer.getList());
    }

    @Override
    public void loadMoreCompleted() {
        mPullToRefreshRecyclerView.loadMoreCompleted();
    }

    @Override
    public void loadMoreFail(String message) {
        mPullToRefreshRecyclerView.loadMoreFail();
    }

    @Override
    public void loadMoreNoData() {
        mPullToRefreshRecyclerView.setNoMore(true);
    }
}
