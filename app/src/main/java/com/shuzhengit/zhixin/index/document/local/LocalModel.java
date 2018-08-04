package com.shuzhengit.zhixin.index.document.local;

import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.Document;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.NetFlowableOnSubscribe;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/10/18 09:27
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class LocalModel implements LocalContract.Model {

    @Override
    public Flowable<BaseCallModel<List<Document>>> fetchLocalSchool(int schoolId, int page) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<List<Document>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<List<Document>>> e) throws Exception {
                HttpProtocol.getApi()
                        .findLocalSchoolDocuments(schoolId, page, 10)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<List<Document>>>(e));
//                                new DisposableSubscriber<BaseCallModel<List<Document>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<List<Document>> listBaseCallModel) {
//                                e.onNext(listBaseCallModel);
//                            }
//
//                            @Override
//                            public void onError(Throwable t) {
//                                e.onError(t);
//                            }
//
//                            @Override
//                            public void onComplete() {
//                                e.onComplete();
//                            }
//                        });
            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<BaseCallModel<List<Document>>> fetchLocalCity(String cityCode, int page) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<List<Document>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<List<Document>>> e) throws Exception {
                HttpProtocol.getApi().findLocalCityDocuments(cityCode, page, 10)
                        .subscribeWith(new NetFlowableOnSubscribe<>(e));
//                                new RxSubscriber<BaseCallModel<List<Document>>>() {
//                            @Override
//                            protected void _onNext(BaseCallModel<List<Document>> listBaseCallModel) {
//                                e.onNext(listBaseCallModel);
//                            }
//
//                            @Override
//                            protected void _onError(String message) {
//                                super._onError(message);
////                                e.onError(new Throwable(message));
//                            }
//
//                            @Override
//                            protected void _onCompleted() {
//                                super._onCompleted();
//                                e.onComplete();
//                            }
//                        });
//                HttpProtocol.getApi()
//                        .findLocalCityDocuments(cityCode, page, 10)
//                        .subscribeWith(new DisposableSubscriber<BaseCallModel<List<Document>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<List<Document>> listBaseCallModel) {
//                                e.onNext(listBaseCallModel);
//                            }
//
//                            @Override
//                            public void onError(Throwable t) {
//                                e.onError(t);
//                            }
//
//                            @Override
//                            public void onComplete() {
//                                e.onComplete();
//                            }
//                        });
            }
        }, BackpressureStrategy.BUFFER);
    }
}
