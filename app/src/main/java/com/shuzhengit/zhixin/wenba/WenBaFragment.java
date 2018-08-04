package com.shuzhengit.zhixin.wenba;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.base.BaseLazyLoadFragment;
import com.library.bean.BaseCallModel;
import com.library.rx.RxBus2;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.ApplyAdmin;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.EventType;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.bean.WenBa;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.login.LoginActivity;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.view.PromptLoginDialog;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;
import com.shuzhengit.zhixin.wenba.detail.WenBaDetailActivity;
import com.shuzhengit.zhixin.wenba.mine.MineWenBaActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Predicate;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/8 08:42
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class WenBaFragment extends BaseLazyLoadFragment<WebBaContract.Presenter> implements WebBaContract.View {
    private static final String TAG = "WenBaFragment";
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.ivEmpty)
    ImageView mIvEmpty;
    @BindView(R.id.pullToRefreshRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;

    private Unbinder mUnbinder;
    private WenBaAdapter mWenBaAdapter;
    private DataContainer<WenBa> mDataContainer;
    private int mCurrentPage = 1;
    private RxSubscriber<EventType> mRxSubscriber;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wenba;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(this, view);

        getHoldingActivity().setSupportActionBar(mToolBar);
        ActionBar actionBar = getHoldingActivity().getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        mTvTitle.setText("问吧");

        mPullToRefreshRecyclerView.setEmptyView(mIvEmpty);
        mPullToRefreshRecyclerView.setShowPullToRefresh(true);
        mPullToRefreshRecyclerView.setShowLoadMore(true);
        mWenBaAdapter = new WenBaAdapter(getActivity());
        mPullToRefreshRecyclerView.setAdapter(mWenBaAdapter);
        mWenBaAdapter.setOnItemClickListener(new WenBaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(WenBa wenBa) {
                Bundle bundle = new Bundle();
                bundle.putInt("wenBaId", wenBa.getId());
                startActivity(WenBaDetailActivity.class, bundle);
            }
        });
        mWenBaAdapter.setOnClickMineWenBaListener(new WenBaAdapter.OnClickMineWenBaListener() {
            @Override
            public void onClick() {
                User user = CheckUser.checkUserIsExists();
                if (user!=null) {
                    startActivity(MineWenBaActivity.class);
                }else {
                    PromptLoginDialog dialog = new PromptLoginDialog(getHoldingActivity());
                    dialog.show();
                    dialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                        @Override
                        public void callBackPositive(DialogInterface dialog) {
                            startActivity(LoginActivity.class);
                        }
                    });
                }

            }
        });
        mWenBaAdapter.setOnClickMoreWenBaListener(new WenBaAdapter.OnClickMoreWenBaListener() {
            @Override
            public void onClick() {
                failure("更多问吧正在审核中");
//                startActivity(MoreWenBaActivity.class);

            }
        });
        mTvRight.setText("入驻");
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = CheckUser.checkUserIsExists();
                if (user != null) {
                    startActivity(ApplyAdminActivity.class);
                }else {
                    PromptLoginDialog dialog = new PromptLoginDialog(getHoldingActivity());
                    dialog.show();
                    dialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                        @Override
                        public void callBackPositive(DialogInterface dialog) {
                            startActivity(LoginActivity.class);
                        }
                    });
                }
            }
        });
        mPullToRefreshRecyclerView.setRefreshListener(new PullToRefreshRecyclerView.RefreshListener() {
            @Override
            public void onRefresh() {
                User user = CheckUser.checkUserIsExists();
                mCurrentPage = 1;
                if (user != null) {
                    mBasePresenter.refreshWebBaList(user.getId(), mCurrentPage, 10);
                } else {
                    mBasePresenter.refreshWebBaList(0, mCurrentPage, 10);
                }
            }
        });
        mPullToRefreshRecyclerView.setLoadMoreListener(new PullToRefreshRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mDataContainer != null && mDataContainer.isHasNextPage()) {

                    User user = CheckUser.checkUserIsExists();
                    if (user != null) {
                        mBasePresenter.refreshWebBaList(user.getId(), ++mCurrentPage, 10);
                    } else {
                        mBasePresenter.refreshWebBaList(0, ++mCurrentPage, 10);
                    }
                } else {
                    loadNoMore();
                }
            }
        });
        mWenBaAdapter.setOnItemFollowListener(new WenBaAdapter.OnItemFollowListener() {
            @Override
            public void onItemFollow(WenBa wenBa) {
                User user = CheckUser.checkUserIsExists();
                if (user != null) {
                    mBasePresenter.followedWenBa(user.getId(), wenBa.getId());
                    if (wenBa.getIsUp() == 1) {
                        wenBa.setIsUp(0);
                    } else {
                        wenBa.setIsUp(1);
                    }
                    mWenBaAdapter.notifyDataSetChanged();
                } else {
                    PromptLoginDialog dialog = new PromptLoginDialog(getHoldingActivity());
                    dialog.show();
                    dialog.setDialogSingleCallBack(new PromptLoginDialog.DialogSingleCallBack() {
                        @Override
                        public void callBackPositive(DialogInterface dialog) {
                            startActivity(LoginActivity.class);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void lazyLoadCreatePresenter() {
        MobclickAgent.onPageStart(TAG);
        mBasePresenter = new WebBaPresenter(this);
        mPullToRefreshRecyclerView.refresh();
        User user = CheckUser.checkUserIsExists();
        if (user != null) {
            HttpProtocol.getApi()
                    .checkIsAdmin(user.getId())
                    .compose(RxSchedulersHelper.io_main())
                    .subscribeWith(new RxSubscriber<BaseCallModel<ApplyAdmin>>() {
                        @Override
                        protected void _onNext(BaseCallModel<ApplyAdmin> baseCallModel) {
                            ApplyAdmin applyAdmin = baseCallModel.getData();
                            if (applyAdmin!=null && applyAdmin.getStatus()==1) {
                                mTvRight.setVisibility(View.GONE);
                            } else {
                                mTvRight.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        } else {
            mTvRight.setVisibility(View.VISIBLE);
        }
        mRxSubscriber = new RxSubscriber<EventType>() {
            @Override
            protected void _onNext(EventType eventType) {
                mTvRight.setVisibility(View.GONE);
                mCurrentPage = 1;
                User user = CheckUser.checkUserIsExists();
                mBasePresenter.refreshWebBaList(user == null ? 0 : user.getId(), mCurrentPage, 10);
            }
        };
        RxBus2.getDefault().toFlowable(EventType.class)
                .filter(new Predicate<EventType>() {
                    @Override
                    public boolean test(EventType eventType) throws Exception {
                        return eventType.getEventType().equals(EventCodeUtils.REFRESH_WENBA);
                    }
                })
                .subscribeWith(mRxSubscriber);

    }

    @Override
    public void onPause() {
        super.onPause();
        //统计页面
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mRxSubscriber!=null && !mRxSubscriber.isDisposed()) {
            mRxSubscriber.dispose();
        }
        super.onDestroy();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void refreshSuccess(DataContainer<WenBa> data) {
        mDataContainer = data;
        mWenBaAdapter.refresh(data.getList());
    }

    @Override
    public void refreshFail(String message) {
        failure(message);
    }

    @Override
    public void refreshCompleted() {
        mPullToRefreshRecyclerView.refreshCompleted();
    }

    @Override
    public void loadMoreSuccess(DataContainer<WenBa> data) {
        mDataContainer = data;
        mWenBaAdapter.addMore(data.getList());
    }

    @Override
    public void loadMoreFail() {
        mPullToRefreshRecyclerView.loadMoreFail();
    }

    @Override
    public void loadMoreCompleted() {
        mPullToRefreshRecyclerView.loadMoreCompleted();
    }

    @Override
    public void loadNoMore() {
        mPullToRefreshRecyclerView.setNoMore(true);
    }
}
