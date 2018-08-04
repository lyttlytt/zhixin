package com.shuzhengit.zhixin.mine.fragment.fans;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.library.base.BaseFragment;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.base.BaseRecyclerViewAdapter;
import com.shuzhengit.zhixin.base.BaseViewHolder;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.mine.user.UserInfoActivity;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.util.ResourceUtils;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/23 15:41
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class FansOrFollowFragment extends BaseFragment<FansOrFollowContract.Presenter> implements
        FansOrFollowContract.View {
    public static final int TYPE_FOLLOW = 1;
    public static final int TYPE_FANS = 2;
    @BindView(R.id.pullToRefreshRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    private int mType;
    private int mCurrentPage = 1;
    private BaseRecyclerViewAdapter<User> mAdapter;
    private User mUser;
    private int mMemberId;
    private Unbinder mUnbinder;
    private DataContainer<User> mDataContainer;
    public static FansOrFollowFragment getInstance(int type, int memberId) {
        Bundle bundle = new Bundle();
        bundle.putInt("TYPE", type);
        bundle.putInt("memberId", memberId);
        FansOrFollowFragment fansOrFollowFragment = new FansOrFollowFragment();
        fansOrFollowFragment.setArguments(bundle);
        return fansOrFollowFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(this,view);
        mType = getArguments().getInt("TYPE");
        mMemberId = getArguments().getInt("memberId");
        mUser = CheckUser.checkUserIsExists();
        mAdapter = new BaseRecyclerViewAdapter<User>(R.layout.item_fans) {
            @Override
            public void convert(BaseViewHolder holder, int position, User user) {
                holder.setText(R.id.tvUserName, user.getNickname());
                holder.setImageResource(mActivity, R.id.civUserAvatar, user.getAvatarUrl(), false);
                Button btnFollow = holder.getViewById(R.id.btnFollow);
                GradientDrawable background = (GradientDrawable) btnFollow.getBackground();
                background.setColor(ResourceUtils.getResourceColor(APP.getInstance(), R.color.grey300));
                btnFollow.setBackground(background);
                if (mType != TYPE_FOLLOW) {
                    holder.getViewById(R.id.btnFollow).setVisibility(View.GONE);
                } else {
                    holder.getViewById(R.id.btnFollow).setVisibility(View.VISIBLE);
                    btnFollow.setText("取消关注");
                    btnFollow.setTextColor(Color.BLACK);
                    btnFollow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBasePresenter.unFollow(mUser.getId(), user.getId());
                            mAdapter.getData().remove(user);
                            notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void onItemClick(View v, int position, User user) {
                Bundle bundle = new Bundle();
                bundle.putInt("memberId", user.getId());
                startActivity(UserInfoActivity.class, bundle);
            }
        };
        mPullToRefreshRecyclerView.setShowPullToRefresh(false);
        mPullToRefreshRecyclerView.setShowLoadMore(true);
        mPullToRefreshRecyclerView.setAdapter(mAdapter);
        mPullToRefreshRecyclerView.setRefreshListener(new PullToRefreshRecyclerView.RefreshListener() {
            @Override
            public void onRefresh() {
                if (mType==TYPE_FOLLOW){
                    mCurrentPage = 1;
                    mBasePresenter.findFollows(mMemberId,1);
                }else {
                    mCurrentPage = 1;
                    mBasePresenter.findFans(mMemberId,1);
                }
            }
        });
        mPullToRefreshRecyclerView.setLoadMoreListener(new PullToRefreshRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                LogUtils.i("TAG","loadMore");
                if (mDataContainer.isHasNextPage()){
                    if (mType==TYPE_FOLLOW){
                        mBasePresenter.findFollows(mMemberId,++mCurrentPage);
                    }else {
                        mBasePresenter.findFans(mMemberId,++mCurrentPage);
                    }
                }else {
                    if (mType==TYPE_FOLLOW){
                        loadNoMore();
                    }else {
                        loadNoMore();
                    }
                }
            }
        });
    }

    @Override
    protected void createPresenter() {
        mBasePresenter = new FansOrFollowPresenter(this);
        if (mType == TYPE_FOLLOW) {
            mBasePresenter.findFollows(mMemberId, mCurrentPage);
        } else {
            mBasePresenter.findFans(mMemberId, mCurrentPage);
        }
    }

    @Override
    public void findFansSuccess(DataContainer<User> dataContainer) {
        mDataContainer = dataContainer;
        if (dataContainer.getList().size() != 0) {
            mAdapter.addMoreData(dataContainer.getList());
        }
        mPullToRefreshRecyclerView.loadMoreFail();
    }

    @Override
    public void findFollowSuccess(DataContainer<User> dataContainer) {
        mDataContainer = dataContainer;
        if (dataContainer.getList().size() != 0) {
            mAdapter.addMoreData(dataContainer.getList());
        }
    }

    @Override
    public void addMoreFollows(DataContainer<User> dataContainer) {
        mDataContainer = dataContainer;
        mAdapter.addMoreData(dataContainer.getList());
    }

    @Override
    public void addMoreFans(DataContainer<User> dataContainer) {
        mDataContainer = dataContainer;
        mAdapter.addMoreData(dataContainer.getList());
    }

    @Override
    public void loadMoreFail() {
        mPullToRefreshRecyclerView.loadMoreFail();
    }

    @Override
    public void loadNoMore() {
        mPullToRefreshRecyclerView.setNoMore(true);
    }

    @Override
    public void loadMoreCompleted() {
        mPullToRefreshRecyclerView.loadMoreCompleted();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
