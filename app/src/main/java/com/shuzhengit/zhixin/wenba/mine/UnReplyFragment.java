package com.shuzhengit.zhixin.wenba.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.library.base.BaseFragment;
import com.library.rx.RxBus2;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.base.BaseRecyclerViewAdapter;
import com.shuzhengit.zhixin.base.BaseViewHolder;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.EventType;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.bean.WenBa;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.util.GlideLoadImageUtils;
import com.shuzhengit.zhixin.util.ResourceUtils;
import com.shuzhengit.zhixin.util.TimeUtil;
import com.shuzhengit.zhixin.view.CircleImageView;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Predicate;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/10 15:45
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class UnReplyFragment extends BaseFragment<UnReplyContract.Presenter> implements UnReplyContract.View {
    private static final String TAG = "UnReplyFragment";
    @BindView(R.id.pullToRefreshRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    private Unbinder mUnbinder;
    private DataContainer<WenBa> mDataContainer;
    private int mCurrentPage = 1;
    private BaseRecyclerViewAdapter<WenBa> mAdapter;
    private RxSubscriber<EventType> mRxSubscriber;

    public static UnReplyFragment getInstance() {
        return new UnReplyFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_unreply;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(this, view);
        mPullToRefreshRecyclerView.setShowPullToRefresh(true);
        mPullToRefreshRecyclerView.setShowLoadMore(true);
        mAdapter = new BaseRecyclerViewAdapter<WenBa>(R
                .layout.item_wenba_follow) {
            @Override
            public void convert(BaseViewHolder holder, int position, WenBa wenBa) {
                holder.setText(R.id.tvAdminName,wenBa.getNickName());
                holder.setText(R.id.tvDescription,wenBa.getDescription());
                CircleImageView civ = holder.getViewById(R.id.civAvatar);
                if (TextUtils.isEmpty(wenBa.getAvatarUrl())){
                    civ.setImageResource(R.drawable.ic_normal_icon);
                }else {
                    GlideLoadImageUtils.loadImg(getHoldingActivity(),wenBa.getAvatarUrl(),civ);
                }
                holder.getViewById(R.id.tvType).setVisibility(View.VISIBLE);
                holder.getViewById(R.id.tvCommentCount).setVisibility(View.GONE);
                holder.getViewById(R.id.tvAskCount).setVisibility(View.GONE);
                holder.getViewById(R.id.line).setVisibility(View.GONE);
                holder.getViewById(R.id.line2).setVisibility(View.GONE);
                TextView viewById = holder.getViewById(R.id.tvType);
                viewById.setTextColor(ResourceUtils.getResourceColor(APP.getInstance(),R.color.grey700));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    Date parse = sdf.parse(wenBa.getGmtCreate());
                    String timeFormatText = TimeUtil.getTimeFormatText(parse);
                    viewById.setText(timeFormatText);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onItemClick(View v, int position, WenBa wenBa) {
                int id = wenBa.getId();
                Bundle bundle = new Bundle();
                bundle.putSerializable("wenba",wenBa);
                startActivity(ReplyQuestionActivity.class,bundle);
            }
        };


        mPullToRefreshRecyclerView.setAdapter(mAdapter);
        mPullToRefreshRecyclerView.setRefreshListener(new PullToRefreshRecyclerView.RefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage = 1;
                User user = CheckUser.checkUserIsExists();
                mBasePresenter.refreshUnReplys(mCurrentPage,10,user.getId());
            }
        });
        mPullToRefreshRecyclerView.setLoadMoreListener(new PullToRefreshRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mDataContainer!=null && mDataContainer.isHasNextPage()){
                    User user = CheckUser.checkUserIsExists();
                    mBasePresenter.findUnReplys(++mCurrentPage,10,user.getId());
                }else {
                    onLoadNoMore();
                }
            }
        });
    }

    @Override
    protected void createPresenter() {
        mBasePresenter = new UnReplyPresenter(this);
        mPullToRefreshRecyclerView.refresh();
        mRxSubscriber = RxBus2.getDefault().toFlowable(EventType.class)
                .filter(new Predicate<EventType>() {
                    @Override
                    public boolean test(EventType eventType) throws Exception {
                        return eventType.getEventType().equals(EventCodeUtils.REFRESH_REPLYS);
                    }
                }).subscribeWith(new RxSubscriber<EventType>() {
                    @Override
                    protected void _onNext(EventType eventType) {
                        LogUtils.d(TAG,"toFlowable.........");
                        mPullToRefreshRecyclerView.refresh();
                    }
                });
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
        mRxSubscriber.dispose();
        mUnbinder.unbind();
        super.onDestroy();
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
        mPullToRefreshRecyclerView.refreshCompleted();
    }

    @Override
    public void onLoadMoreSuccess(DataContainer<WenBa> dataContainer) {
        mDataContainer = dataContainer;
        mAdapter.addMoreData(dataContainer.getList());
    }

    @Override
    public void onLoadMoreFail() {
        mPullToRefreshRecyclerView.loadMoreFail();
    }

    @Override
    public void onLoadNoMore() {
        mPullToRefreshRecyclerView.setNoMore(true);
    }

    @Override
    public void onLoadMoreCompleted() {
        mPullToRefreshRecyclerView.refreshCompleted();
    }

}
