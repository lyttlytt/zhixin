package com.shuzhengit.zhixin.mine.history;

import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.Document;
import com.shuzhengit.zhixin.bean.WenBa;
import com.shuzhengit.zhixin.rx.RxSubscriber;

import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/29 11:12
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class HistoryPresenter extends HistoryContract.Presenter {
    private static final String TAG = "HistoryPresenter";
    private final HistoryModel mModel;

    HistoryPresenter(HistoryContract.View view) {
        super(view);
        mModel = new HistoryModel();
    }

    @Override
    public void start() {

    }

    @Override
    void requestDocumentByHistory(int memberId, int page, int pageSize) {
        mCompositeDisposable.add(
                mModel.findDocumentByHistory(memberId, page, pageSize)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<List<Document>>() {
                    @Override
                    protected void _onNext(List<Document> documents) {
                        if (documents.size()==0) {
                            mView.loadNoMore();
                        } else {
                            mView.setDocuments(documents);
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        mView.loadMoreFail();
                    }

                    @Override
                    protected void _onCompleted() {
                        super._onCompleted();
                        mView.loadMoreCompleted();
                    }
                })
        );
    }

    @Override
    void refreshDocumentByHistory(int memberId) {
        mCompositeDisposable.add(
                mModel.findDocumentByHistory(memberId, 1, 10)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                        .subscribeWith(new RxSubscriber<List<Document>>() {
                            @Override
                            protected void _onNext(List<Document> documents) {
                                mView.refreshDocument(documents);
                            }

                            @Override
                            protected void _onError(String message) {
                                super._onError(message);
                                mView.failure(message);
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
    void requestQuestionByHistory(int memberId, int page, int pageSize) {
        mCompositeDisposable.add(
                mModel.findQuestionByHistory(memberId,page,10)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                        .subscribeWith(new RxSubscriber<DataContainer<WenBa>>() {
                            @Override
                            protected void _onNext(DataContainer<WenBa> dataContainer) {
                                mView.setQuestions(dataContainer);
                            }

                            @Override
                            protected void _onError(String message) {
                                super._onError(message);
                                mView.failure(message);
                            }

                            @Override
                            protected void _onCompleted() {
                                super._onCompleted();
                                LogUtils.d(TAG,"_completed ");
                                mView.loadMoreCompleted();
                            }
                        })
        );
    }

    @Override
    void refreshQuestionByHistory(int memberId) {
        mCompositeDisposable.add(
                mModel.findQuestionByHistory(memberId,1,10)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<DataContainer<WenBa>>() {
                    @Override
                    protected void _onNext(DataContainer<WenBa> dataContainer) {
                        mView.refreshQuestions(dataContainer);
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        mView.failure(message);
                    }

                    @Override
                    protected void _onCompleted() {
                        super._onCompleted();
                        mView.refreshCompleted();
                    }
                })
        );
    }
}
