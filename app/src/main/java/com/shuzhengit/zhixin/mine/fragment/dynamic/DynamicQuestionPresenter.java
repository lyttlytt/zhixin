package com.shuzhengit.zhixin.mine.fragment.dynamic;

import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.bean.AskWithReply;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.rx.RxSubscriber;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/29 22:20
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class DynamicQuestionPresenter extends DynamicQuestionContract.Presenter {

    private final DynamicQuestionModel mModel;

    public DynamicQuestionPresenter(DynamicQuestionContract.View view) {
        super(view);
        mModel = new DynamicQuestionModel();
    }

    @Override
    public void start() {

    }

    @Override
    public void findDynamicQuestion(int memberId, int page, int pageNum) {
        mCompositeDisposable.add(mModel.fetchDynamicQuestion(memberId, page, pageNum)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
        .subscribeWith(new RxSubscriber<DataContainer<AskWithReply>>() {
            @Override
            protected void _onNext(DataContainer<AskWithReply> dataContainer) {
                mView.setDynamicQuestion(dataContainer);
            }

            @Override
            protected void _onError(String message) {
                super._onError(message);
                mView.loadMoreFail(message);
            }

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.loadMoreCompleted();
            }
        }));
//                .subscribeWith(new RxSubscriber<List<Dynamic>>() {
//                    @Override
//                    protected void _onNext(List<Dynamic> dynamics) {
//                        if (dynamics.size() == 0)
//                            mView.loadMoreNoData();
//                        else
//                            mView.setDynamicQuestion(dynamics);
//                    }
//
//                    @Override
//                    protected void _onError(String message) {
//                        super._onError(message);
//                        mView.loadMoreFail(message);
//                    }
//
//                    @Override
//                    protected void _onCompleted() {
//                        super._onCompleted();
//                        mView.loadMoreCompleted();
//                    }
//                }));
    }
}
