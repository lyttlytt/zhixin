package com.shuzhengit.zhixin.wenba.mine;

import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.WenBa;
import com.shuzhengit.zhixin.rx.RxSubscriber;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/15 14:51
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class MineFollowPresenter extends MineFollowWenBaContract.Presenter {

    private final MineFollowModel mModel;

    public MineFollowPresenter(MineFollowWenBaContract.View view) {
        super(view);
        mModel = new MineFollowModel();
    }

    @Override
    public void start() {

    }

    @Override
    void findMineFollowWenBa(int page, int pageSize, int userId) {
        mCompositeDisposable.add(
                mModel.fetchFollowWenBas(page, pageSize, userId)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                        .subscribeWith(new RxSubscriber<DataContainer<WenBa>>() {
                            @Override
                            protected void _onNext(DataContainer<WenBa> dataContainer) {
                                if (dataContainer.getList().size() == 0) {
                                    mView.onLoadNoMore();
                                } else {
                                    mView.onLoadMoreSuccess(dataContainer);
                                    mView.onLoadMoreCompleted();
                                }
                            }

                            @Override
                            protected void _onError(String message) {
                                super._onError(message);
                                mView.onLoadMoreFail();
                            }
                        })
        );
    }

    @Override
    void refreshFollowWenBa(int page, int pageSize, int userId) {
        mCompositeDisposable.add(
                mModel.fetchFollowWenBas(page, pageSize, userId)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<DataContainer<WenBa>>() {
                    @Override
                    protected void _onNext(DataContainer<WenBa> dataContainer) {
                        mView.onRefreshSuccess(dataContainer);
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        mView.failure(message);
                    }

                    @Override
                    protected void _onCompleted() {
                        super._onCompleted();
                        mView.onRefreshCompleted();
                    }
                })
        );
    }
}
