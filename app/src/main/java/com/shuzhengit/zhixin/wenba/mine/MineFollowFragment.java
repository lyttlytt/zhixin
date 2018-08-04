package com.shuzhengit.zhixin.wenba.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.library.base.BaseFragment;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.base.BaseRecyclerViewAdapter;
import com.shuzhengit.zhixin.base.BaseViewHolder;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.bean.WenBa;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.util.GlideLoadImageUtils;
import com.shuzhengit.zhixin.util.ResourceUtils;
import com.shuzhengit.zhixin.view.CircleImageView;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;
import com.shuzhengit.zhixin.wenba.detail.WenBaDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/10 09:30
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class MineFollowFragment extends BaseFragment<MineFollowWenBaContract.Presenter> implements
        MineFollowWenBaContract.View {

    @BindView(R.id.pullRecyclerView)
    PullToRefreshRecyclerView mPullRecyclerView;
    private Unbinder mUnbinder;
    private BaseRecyclerViewAdapter<WenBa> mAdapter;
    private DataContainer<WenBa> mDataContainer;
    private int mCurrentPage =1;
    public static MineFollowFragment getInstance() {
        return new MineFollowFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wenba_recyclerview;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(this, view);
        mPullRecyclerView.setBackgroundColor(ResourceUtils.getResourceColor(APP.getInstance(), R.color.grey100));
        mAdapter = new BaseRecyclerViewAdapter<WenBa>(R.layout.item_wenba_follow) {
            @Override
            public void convert(BaseViewHolder holder, int position, WenBa wenBa) {
                CircleImageView civAvatar = holder.getViewById(R.id.civAvatar);
                GlideLoadImageUtils.loadImg(getHoldingActivity(), wenBa.getAvatarUrl(), civAvatar);
                holder.setText(R.id.tvAdminName, wenBa.getNickName());
                holder.setText(R.id.tvDescription, wenBa.getWelcomeTitle());
                holder.setText(R.id.tvType, String.format(ResourceUtils.getResourceString(APP.getInstance(), R.string
                        .wenBaType), wenBa.getColumnTitle()));
                holder.setText(R.id.tvCommentCount,String.format(ResourceUtils.getResourceString(APP.getInstance(),R
                        .string.userFollow),wenBa.getLikeCount()));
                holder.setText(R.id.tvAskCount,String.format(ResourceUtils.getResourceString(APP.getInstance(),R
                        .string.askCount),wenBa.getAskCount()));
            }

            @Override
            public void onItemClick(View v, int position, WenBa wenBa) {
                Bundle bundle = new Bundle();
                bundle.putInt("wenBaId",wenBa.getId());
                startActivity(WenBaDetailActivity.class,bundle);
            }
        };
        mPullRecyclerView.setAdapter(mAdapter);
        mPullRecyclerView.setShowPullToRefresh(true);
        mPullRecyclerView.setShowLoadMore(true);
        mPullRecyclerView.setRefreshListener(new PullToRefreshRecyclerView.RefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage = 1;
                User user = CheckUser.checkUserIsExists();
                mBasePresenter.refreshFollowWenBa(mCurrentPage,10,user.getId());
            }
        });
        mPullRecyclerView.setLoadMoreListener(new PullToRefreshRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mDataContainer.isHasNextPage()){
                    User user = CheckUser.checkUserIsExists();
                    mBasePresenter.findMineFollowWenBa(++mCurrentPage,10,user.getId());
                }else {
                    onLoadNoMore();
                }
            }
        });
    }

    @Override
    protected void createPresenter() {
        mBasePresenter = new MineFollowPresenter(this);
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
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public void onRefreshSuccess(DataContainer<WenBa> dataContainer) {
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
    public void onLoadMoreSuccess(DataContainer<WenBa> dataContainer) {
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
