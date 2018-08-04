package com.shuzhengit.zhixin.wenba.detail;

import com.library.bean.BaseCallModel;
import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.bean.AskWithReply;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.WenBa;
import com.shuzhengit.zhixin.rx.RxSubscriber;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/14 10:36
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class WenBaDetailPresenter extends WenBaDetailContract.Presenter{

    private final WenBaDetailModel mModel;

    public WenBaDetailPresenter(WenBaDetailContract.View view) {
        super(view);
        mModel = new WenBaDetailModel();
    }

    @Override
    public void start() {

    }

    @Override
    void findWenBaDetailById(int wenBaId, int userId) {
        mCompositeDisposable.add(mModel.fetchWenBa(wenBaId, userId)
        .compose(RxSchedulersHelper.io_main())
        .compose(RxResultHelper.handleResult())
        .subscribeWith(new RxSubscriber<WenBa>() {
            @Override
            protected void _onNext(WenBa wenBa) {
                mView.setWenBaDetail(wenBa);
            }
        }));
    }

    @Override
    void findAskWithReplysOnNew(int page, int pageSize, int wenBaId,int userId) {
        mCompositeDisposable.add(
                mModel.fetchAskAndReplysOnNew(page, pageSize, wenBaId,userId)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<DataContainer<AskWithReply>>() {
                    @Override
                    protected void _onNext(DataContainer<AskWithReply> dataContainer) {
                        if (dataContainer.getList().size()==0){
                            mView.loadNoMore();
                        }else {
                            mView.loadMoreSuccess(dataContainer);
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        mView.failure(message);
                        mView.loadMoreFail();
                    }
                })
        );
    }

    @Override
    void findAskWithReplysOnHot(int page, int pageSize, int wenBaId,int userId) {
        mCompositeDisposable.add(
                mModel.fetchAskAndReplysOnHot(page, pageSize, wenBaId,userId)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                        .subscribeWith(new RxSubscriber<DataContainer<AskWithReply>>() {
                            @Override
                            protected void _onNext(DataContainer<AskWithReply> dataContainer) {
                                if (dataContainer.getList().size()==0){
                                    mView.loadNoMore();
                                }else {
                                    mView.loadMoreSuccess(dataContainer);
                                }
                            }

                            @Override
                            protected void _onError(String message) {
                                super._onError(message);
                                mView.failure(message);
                                mView.loadMoreFail();
                            }
                        })
        );
    }
    @Override
    void refreshAskWithReplyOnNew(int page, int pageSize, int wenBaId,int userId) {
        mCompositeDisposable.add(
                mModel.fetchAskAndReplysOnNew(page, pageSize, wenBaId,userId)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                        .subscribeWith(new RxSubscriber<DataContainer<AskWithReply>>() {
                            @Override
                            protected void _onNext(DataContainer<AskWithReply> dataContainer) {
                               mView.refreshData(dataContainer);
                            }

                            @Override
                            protected void _onCompleted() {
                                super._onCompleted();
                                mView.refreshCompleted();
                            }
                        })
        );
    }

    @Override
    void refreshAskWithReplyOnHot(int page, int pageSize, int wenBaId,int userId) {
        mCompositeDisposable.add(
                mModel.fetchAskAndReplysOnHot(page, pageSize, wenBaId,userId)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                        .subscribeWith(new RxSubscriber<DataContainer<AskWithReply>>() {
                            @Override
                            protected void _onNext(DataContainer<AskWithReply> dataContainer) {
                                mView.refreshData(dataContainer);
                            }

                            @Override
                            protected void _onCompleted() {
                                super._onCompleted();
                                mView.refreshCompleted();
                            }
                        })
        );
    }

    @Override
    void followedWenBa(int userId, int wenBaId) {
        mCompositeDisposable.add(mModel.followedWenBa(userId, wenBaId)
                .compose(RxSchedulersHelper.io_main())
                .subscribeWith(new RxSubscriber<BaseCallModel>() {
                    @Override
                    protected void _onNext(BaseCallModel callModel) {

                    }
                }));
    }

    @Override
    void agreeAsk(int askId, int userId) {
        mCompositeDisposable.add(
                mModel.agreeAsk(askId, userId)
                .compose(RxSchedulersHelper.io_main())
                .subscribeWith(new RxSubscriber<BaseCallModel>() {
                    @Override
                    protected void _onNext(BaseCallModel callModel) {

                    }
                })
        );
    }
}
