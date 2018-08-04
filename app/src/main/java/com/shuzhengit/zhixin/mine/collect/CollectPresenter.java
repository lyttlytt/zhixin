package com.shuzhengit.zhixin.mine.collect;

import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.bean.Document;
import com.shuzhengit.zhixin.rx.RxSubscriber;

import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/29 11:33
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class CollectPresenter extends CollectContract.Presenter {
    private static final String TAG = "CollectPresenter";
    private final CollectModel mModel;

    public CollectPresenter(CollectContract.View view) {
        super(view);
        mModel = new CollectModel();
    }

    @Override
    public void start() {

    }

    @Override
    public void refreshCollectDocument(int memberId) {
        mCompositeDisposable.add(
                mModel.findDocumentByCollect(memberId, 1, 10)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                        .subscribeWith(new RxSubscriber<List<Document>>() {
                            @Override
                            protected void _onNext(List<Document> documents) {
                                mView.refreshCollectDocuments(documents);
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
    public void findCollectDocument(int memberId, int page, int pageNum) {
        mCompositeDisposable.add(
                mModel.findDocumentByCollect(memberId, page, pageNum)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                        .subscribeWith(new RxSubscriber<List<Document>>() {
                            @Override
                            protected void _onNext(List<Document> documents) {
                                LogUtils.d(TAG, documents.toString());
                                if (documents.size() == 0) {
                                    mView.loadNoMore();
                                } else {
                                    mView.setCollectDocuments(documents);
                                }
                            }

                            @Override
                            protected void _onError(String message) {
                                super._onError(message);
                                LogUtils.d(TAG, message);
                                mView.loadMoreFail();
                            }

                            @Override
                            protected void _onCompleted() {
                                super._onCompleted();
                                LogUtils.d(TAG, "completed");
                                mView.loadMoreCompleted();
                            }
                        })
        );
    }
}
