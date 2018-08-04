package com.shuzhengit.zhixin.wenba.mine;

import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.bean.AskWithReply;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.rx.RxSubscriber;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/15 15:41
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class MineReleaseAskPresenter extends MineReleaseContract.Presenter {

    private final MineAskModel mModel;

    public MineReleaseAskPresenter(MineReleaseContract.View view) {
        super(view);
        mModel = new MineAskModel();
    }

    @Override
    public void start() {

    }

    @Override
    void findMineAsks(int page, int pageSize, int userId) {
        mCompositeDisposable.add(
                mModel.fetchAsks(page, pageSize, userId).
                        compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<DataContainer<AskWithReply>>() {
                    @Override
                    protected void _onNext(DataContainer<AskWithReply> dataContainer) {
                        if (dataContainer.getList().size()!=0){
                            mView.onLoadMoreSuccess(dataContainer);
                            mView.onLoadMoreCompleted();
                        }else {
                            mView.onLoadNoMore();
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
    void refreshAsks(int page, int pageSize, int userId) {
        mCompositeDisposable.add(
                mModel.fetchAsks(page, pageSize, userId)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<DataContainer<AskWithReply>>() {
                    @Override
                    protected void _onNext(DataContainer<AskWithReply> dataContainer) {
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
