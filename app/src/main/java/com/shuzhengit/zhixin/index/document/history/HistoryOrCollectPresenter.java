package com.shuzhengit.zhixin.index.document.history;

import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.bean.Document;
import com.shuzhengit.zhixin.rx.RxSubscriber;

import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/21 16:28
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class HistoryOrCollectPresenter extends HistoryOrCollectContract.Presenter {

    private final HistoryOrCollectModel mHistoryOrCollectModel;

    public HistoryOrCollectPresenter(HistoryOrCollectContract.View view) {
        super(view);
        mHistoryOrCollectModel = new HistoryOrCollectModel();

    }

    @Override
    public void start() {

    }

    @Override
    public void requestDocumentByHistory(int memberId, int page, int pageSize) {
        mCompositeDisposable.add(
                mHistoryOrCollectModel.findDocumentByHistory(memberId, page, pageSize)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                        .subscribeWith(new RxSubscriber<List<Document>>() {
                            @Override
                            protected void _onNext(List<Document> documents) {
                                mView.findDocumentByHistorySuccess(documents);
                            }
                        })
        );
    }

    @Override
    public void requestDocumentByCollect(int memberId, int page, int pageSize) {
        mCompositeDisposable.add(
                mHistoryOrCollectModel.findDocumentByCollect(memberId, page, pageSize)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                        .subscribeWith(new RxSubscriber<List<Document>>() {
                            @Override
                            protected void _onNext(List<Document> documents) {
                                mView.findDocumentByCollectSuccess(documents);
                            }
                        })
        );
    }

    @Override
    void requestMoreDocument(int memberId, int page, int pageSize, boolean isHistory) {
        if (isHistory) {
            mCompositeDisposable.add(mHistoryOrCollectModel.findDocumentByHistory(memberId, page, pageSize).compose
                    (RxSchedulersHelper.io_main())
                    .compose(RxResultHelper.handleResult())
                    .subscribeWith(new RxSubscriber<List<Document>>() {
                        @Override
                        protected void _onNext(List<Document> documents) {
                            if (documents.size() == 0) {
                                mView.loadNoMore();
                            } else {
                                mView.loadMoreSuccess(documents);
                            }
                        }

                        @Override
                        protected void _onError(String message) {
                            super._onError(message);
                            mView.loadFail();
                        }
                    }));
        } else {
            mCompositeDisposable.add(mHistoryOrCollectModel.findDocumentByCollect(memberId, page, pageSize).compose
                    (RxSchedulersHelper.io_main())
                    .compose(RxResultHelper.handleResult())
                    .subscribeWith(new RxSubscriber<List<Document>>() {
                        @Override
                        protected void _onNext(List<Document> documents) {
                            if (documents.size() == 0) {
                                mView.loadNoMore();
                            } else {
                                mView.loadMoreSuccess(documents);
                            }
                        }

                        @Override
                        protected void _onError(String message) {
                            super._onError(message);
                            mView.loadFail();
                        }
                    }));
        }
    }

    @Override
    void refreshDocument(int memberId, int page, int pageSize, boolean isHistory) {
        if (isHistory) {
            mCompositeDisposable.add(mHistoryOrCollectModel.findDocumentByHistory(memberId, page, pageSize).compose
                    (RxSchedulersHelper.io_main())
                    .compose(RxResultHelper.handleResult())
                    .subscribeWith(new RxSubscriber<List<Document>>() {
                        @Override
                        protected void _onNext(List<Document> documents) {
                            mView.refreshSuccess(documents);
                        }

                        @Override
                        protected void _onError(String message) {
                            super._onError(message);
                            mView.refreshFail();
                        }
                    }));
        } else {
            mCompositeDisposable.add(mHistoryOrCollectModel.findDocumentByCollect(memberId, page, pageSize).compose
                    (RxSchedulersHelper.io_main())
                    .compose(RxResultHelper.handleResult())
                    .subscribeWith(new RxSubscriber<List<Document>>() {
                        @Override
                        protected void _onNext(List<Document> documents) {
                            mView.refreshSuccess(documents);
                        }

                        @Override
                        protected void _onError(String message) {
                            super._onError(message);
                            mView.refreshFail();
                        }
                    }));
        }
    }
}
