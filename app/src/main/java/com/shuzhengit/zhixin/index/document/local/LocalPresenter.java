package com.shuzhengit.zhixin.index.document.local;

import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.bean.Document;
import com.shuzhengit.zhixin.rx.RxSubscriber;

import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/10/18 09:26
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class LocalPresenter extends LocalContract.Presenter {

    private final LocalModel mLocalModel;

    public LocalPresenter(LocalContract.View view) {
        super(view);
        mLocalModel = new LocalModel();
//        RxBus2.getDefault().toFlowable(EventType.class)
//                .filter(new Predicate<EventType>() {
//                    @Override
//                    public boolean test(EventType eventType) throws Exception {
//                        return eventType.getEventType().equals(EventCodeUtils.USER_SCHOOL_ID);
//                    }
//                })
//                .map(new Function<EventType, Integer>() {
//                    @Override
//                    public Integer apply(EventType eventType) throws Exception {
//                        return (Integer) eventType.getEvent();
//                    }
//                })
//                .subscribeWith(new RxSubscriber<Integer>() {
//                    @Override
//                    protected void _onNext(Integer integer) {
//                        onRefreshLocalSchool(integer,1);
//                    }
//                });
    }

    @Override
    public void onRefreshLocalSchool(int schoolId, int page) {
        mCompositeDisposable.add(mLocalModel.fetchLocalSchool(schoolId, page)
        .compose(RxSchedulersHelper.io_main())
        .compose(RxResultHelper.handleResult())
        .subscribeWith(new RxSubscriber<List<Document>>() {
            @Override
            protected void _onNext(List<Document> documents) {
                mView.onRefreshLocalSchoolSuccess(documents);
            }

            @Override
            protected void _onError(String message) {
                super._onError(message);
                mView.onRefreshFail(message);
            }

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.onRefreshComplete();
            }
        }));
    }

    @Override
    public void onLoadMoreLocalSchool(int schoolId, int page) {
        mCompositeDisposable.add(mLocalModel.fetchLocalSchool(schoolId, page)
        .compose(RxSchedulersHelper.io_main())
        .compose(RxResultHelper.handleResult())
        .subscribeWith(new RxSubscriber<List<Document>>() {
            @Override
            protected void _onNext(List<Document> documents) {
                mView.onLoadMoreLocalSchool(documents);
            }

            @Override
            protected void _onError(String message) {
                super._onError(message);
                mView.onLoadMoreFail(message);
            }

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.onLoadMoreComplete();
            }
        }));
    }

    @Override
    public void onRequestLocalSchool(int schoolId, int page) {
        mCompositeDisposable.add(mLocalModel.fetchLocalSchool(schoolId, page)
        .compose(RxSchedulersHelper.io_main())
        .compose(RxResultHelper.handleResult())
        .subscribeWith(new RxSubscriber<List<Document>>() {
            @Override
            protected void _onNext(List<Document> documents) {
                mView.onRequestLocalSchoolSuccess(documents);
            }
        }));
    }

    @Override
    void onRefreshLocalCity(String cityCode) {
        mCompositeDisposable.add(mLocalModel.fetchLocalCity(cityCode,1)
        .compose(RxSchedulersHelper.io_main())
        .compose(RxResultHelper.handleResult())
        .subscribeWith(new RxSubscriber<List<Document>>() {
            @Override
            protected void _onNext(List<Document> document) {
                mView.onRefreshLocalCitySuccess(document);
            }

            @Override
            protected void _onError(String message) {
                super._onError(message);
                mView.onRefreshFail(message);
            }

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.onRefreshComplete();
            }
        }));
    }

    @Override
    void onLoadMoreLocalCity(String cityCode, int page) {
        mCompositeDisposable.add(mLocalModel.fetchLocalCity(cityCode, page)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<List<Document>>() {
                    @Override
                    protected void _onNext(List<Document> documents) {
                        if (documents.size()==0){
                            mView.onLoadNoMore();
                        }else {
                            mView.onRequestLocalCitySuccess(documents);
                            mView.onLoadMoreComplete();
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        mView.onLoadMoreFail(message);
                    }

                    @Override
                    protected void _onCompleted() {
                        super._onCompleted();
//                        mView.onLoadMoreComplete();
                    }
                }));
    }

    @Override
    void onRequestLocalCity(String cityCode, int page) {
        mCompositeDisposable.add(mLocalModel.fetchLocalCity(cityCode, page)
        .compose(RxSchedulersHelper.io_main())
        .compose(RxResultHelper.handleResult())
        .subscribeWith(new RxSubscriber<List<Document>>() {
            @Override
            protected void _onNext(List<Document> documents) {
                mView.onRequestLocalCitySuccess(documents);
            }
        }));
    }

    @Override
    public void start() {

    }

}
