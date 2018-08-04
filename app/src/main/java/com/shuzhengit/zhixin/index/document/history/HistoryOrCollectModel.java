package com.shuzhengit.zhixin.index.document.history;

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
 * Author：袁从斌 on 2017/8/21 16:43
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class HistoryOrCollectModel implements HistoryOrCollectContract.Model {
    @Override
    public Flowable<BaseCallModel<List<Document>>> findDocumentByHistory(int memberId, int page, int
            pageSize) {
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
    public Flowable<BaseCallModel<List<Document>>> findDocumentByCollect(int memberId, int page, int pageSize) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<List<Document>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<List<Document>>> subscriber) throws Exception {
                HttpProtocol.getApi()
                        .findDocumentsByFavourite(memberId, page, pageSize)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<List<Document>>>(subscriber));
//                        .subscribe(new DisposableSubscriber<BaseCallModel<List<Document>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<List<Document>> pageContainerBaseCallModel) {
//                                subscriber.onNext(pageContainerBaseCallModel);
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
        },BackpressureStrategy.BUFFER);
    }
}
