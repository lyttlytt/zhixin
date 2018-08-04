package com.shuzhengit.zhixin.wenba.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * Author：袁从斌 on 2017/11/10 15:00
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class MineReleaseAskFragment extends BaseFragment<MineReleaseContract.Presenter> implements
        MineReleaseContract.View {
    @BindView(R.id.pullRecyclerView)
    PullToRefreshRecyclerView mPullRecyclerView;
    private Unbinder mUnbinder;
    private BaseRecyclerViewAdapter<AskWithReply> mAdapter;
    private DataContainer<AskWithReply> mDataContainer;
    private int mCurrentPage =1;
    public static MineReleaseAskFragment getInstance(){
        return new MineReleaseAskFragment();
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wenba_recyclerview;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(this, view);
        mPullRecyclerView.setBackgroundColor(ResourceUtils.getResourceColor(APP.getInstance(),R.color.grey100));
        mPullRecyclerView.setShowPullToRefresh(true);
        mPullRecyclerView.setShowLoadMore(true);
        mPullRecyclerView.setRefreshListener(new PullToRefreshRecyclerView.RefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage=1;
                User user = CheckUser.checkUserIsExists();
                mBasePresenter.refreshAsks(mCurrentPage,10,user.getId());
            }
        });
        mPullRecyclerView.setLoadMoreListener(new PullToRefreshRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mDataContainer!=null && mDataContainer.isHasNextPage()){
                    User user = CheckUser.checkUserIsExists();
                    mBasePresenter.findMineAsks(++mCurrentPage,10,user.getId());
                }else {
                    onLoadNoMore();
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
        mPullRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void createPresenter() {
        mBasePresenter = new MineReleaseAskPresenter(this);
        mPullRecyclerView.refresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void onRefreshSuccess(DataContainer<AskWithReply> dataContainer) {
        mDataContainer = dataContainer;
        mAdapter.onRefreshData(dataContainer.getList());
    }

    @Override
    public void onRefreshFail() {

    }

    @Override
    public void onRefreshCompleted() {
        mPullRecyclerView.refreshCompleted();
    }

    @Override
    public void onLoadMoreSuccess(DataContainer<AskWithReply> dataContainer) {
        mDataContainer = dataContainer;
        mAdapter.addMoreData(dataContainer.getList());
    }

    @Override
    public void onLoadMoreFail() {
        mPullRecyclerView.loadMoreFail();
    }

    @Override
    public void onLoadNoMore() {
        mPullRecyclerView.setNoMore(true);
    }

    @Override
    public void onLoadMoreCompleted() {
        mPullRecyclerView.loadMoreCompleted();
    }
}
