package com.shuzhengit.zhixin.index.document;

import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.bean.Document;
import com.shuzhengit.zhixin.rx.RxSubscriber;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/14 15:49
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class DocumentPresenter extends DocumentContract.Presenter {

    private final DocumentModel mDocumentModel;

    public DocumentPresenter(DocumentContract.View view) {
        super(view);
        mDocumentModel = new DocumentModel();
    }

    @Override
    public void start() {

    }

    @Override
    public void onRefreshDocuments(String columnCode, int page) {
        mCompositeDisposable.add(mDocumentModel
                .requestDocuments(columnCode, page)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .onErrorReturn(new Function<Throwable, List<Document>>() {
                    @Override
                    public List<Document> apply(Throwable throwable) throws Exception {
                        return new ArrayList<Document>();
                    }
                })
                .subscribeWith(new RxSubscriber<List<Document>>() {
                    @Override
                    protected void _onNext(List<Document> channelPageContainer) {
                        mView.onRefreshDocumentsSuccess(channelPageContainer);
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        mView.onRefreshDocumentFail(message);
                    }
                }));
    }

    @Override
    public void onLoadMoreDocuments(String columnCode, int page) {
        mCompositeDisposable.add(mDocumentModel
                .requestDocuments(columnCode, page)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<List<Document>>() {
                    @Override
                    protected void _onNext(List<Document> documents) {
                        if (documents.size() == 0) {
                            mView.onLoadNoMore();
                        } else {
                            mView.onLoadMoreSuccess(documents);
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        mView.onLoadMoreFail(message);
                    }
                }));
    }

    @Override
    public void onRequestDocuments(String columnCode, int page) {
        mCompositeDisposable.add(mDocumentModel.requestDocuments(columnCode,page)
        .compose(RxSchedulersHelper.io_main())
        .compose(RxResultHelper.handleResult())
        .subscribeWith(new RxSubscriber<List<Document>>() {
            @Override
            protected void _onNext(List<Document> documents) {
                mView.onRequestDocumentsSuccess(documents);
            }

            @Override
            protected void _onError(String message) {
                super._onError(message);
//                mView.failure(message);
            }
        }));
    }
}
