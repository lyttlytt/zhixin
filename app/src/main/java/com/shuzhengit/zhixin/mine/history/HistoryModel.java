package com.shuzhengit.zhixin.mine.history;

import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.Document;
import com.shuzhengit.zhixin.bean.WenBa;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.NetFlowableOnSubscribe;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/29 11:13
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class HistoryModel implements HistoryContract.Model {
    @Override
    public Flowable<BaseCallModel<List<Document>>> findDocumentByHistory(int memberId, int page, int pageSize) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<List<Document>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<List<Document>>> subscriber) throws Exception {

                HttpProtocol.getApi()
                        .findDocumentByHistory(memberId, page, pageSize)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<List<Document>>>(subscriber));
//                        .subscribe(new DisposableSubscriber<BaseCallModel<List<Document>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<List<Document>> listBaseCallModel) {
//                                subscriber.onNext(listBaseCallModel);
//                            }
//
//                            @Override
//                            public void onError(Throwable t) {
//                                subscriber.onError(t);
//                            }
//
//                            @Override
//                            public void onComplete() {
//                                subscriber.onComplete();
//                            }
//                        });
            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<BaseCallModel<DataContainer<WenBa>>> findQuestionByHistory(int memberId, int page, int
            pageSize) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<DataContainer<WenBa>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<DataContainer<WenBa>>> e) throws Exception {
                HttpProtocol.getApi()
                        .fetchHistoryWenBa(page, pageSize, memberId)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<DataContainer<WenBa>>>(e));
//                        .subscribeWith(new DisposableSubscriber<BaseCallModel<DataContainer<WenBa>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<DataContainer<WenBa>> dataContainerBaseCallModel) {
//                                e.onNext(dataContainerBaseCallModel);
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
