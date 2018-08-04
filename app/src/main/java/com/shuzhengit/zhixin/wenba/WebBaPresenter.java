package com.shuzhengit.zhixin.wenba;

import com.library.bean.BaseCallModel;
import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.WenBa;
import com.shuzhengit.zhixin.rx.RxSubscriber;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/8 08:46
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class WebBaPresenter extends WebBaContract.Presenter{

    private final WenBaModel mModel;

    public WebBaPresenter(WebBaContract.View view) {
        super(view);
        mModel = new WenBaModel();
    }

    @Override
    public void start() {

    }

    @Override
    void findWenBaLists(int userId, int page, int pageSize) {
        mCompositeDisposable.add(mModel.fetchWenBaLists(userId, page, pageSize)
        .compose(RxSchedulersHelper.io_main())
        .compose(RxResultHelper.handleResult())
        .subscribeWith(new RxSubscriber<DataContainer<WenBa>>() {
            @Override
            protected void _onNext(DataContainer<WenBa> wenBas) {
                if (wenBas.getList().size()!=0){
                    mView.loadMoreSuccess(wenBas);
                    mView.loadMoreCompleted();
                }else {
                    mView.loadNoMore();
                }
            }

            @Override
            protected void _onError(String message) {
                super._onError(message);
                mView.loadMoreFail();
            }
        }));
    }

    @Override
    void refreshWebBaList(int userId, int page, int pageSize) {
        mCompositeDisposable.add(mModel.fetchWenBaLists(userId, page, pageSize)
        .compose(RxResultHelper.handleResult())
        .compose(RxSchedulersHelper.io_main())
                .subscribeWith(new RxSubscriber<DataContainer<WenBa>>() {
                    @Override
                    protected void _onNext(DataContainer<WenBa> wenBaDataContainer) {
                        mView.refreshSuccess(wenBaDataContainer);
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        mView.refreshFail(message);
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
}
