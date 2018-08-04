package com.shuzhengit.zhixin.column;

import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.Column;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.NetFlowableOnSubscribe;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/21 14:13
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

 class ColumnModel implements ColumnContract.Model {
    @Override
    public Flowable<BaseCallModel<List<Column>>> fetchMineFollowColumn(int memberId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<List<Column>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<List<Column>>> subscriber) throws Exception {
                HttpProtocol.getApi()
                        .findUserFollowColumns(memberId)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<List<Column>>>(subscriber));
//                        .subscribe(new DisposableSubscriber<BaseCallModel<List<Column>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<List<Column>> pageContainerBaseCallModel) {
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
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<BaseCallModel<List<Column>>> fetchRecommendColumn(int memberId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<List<Column>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<List<Column>>> subscriber) throws Exception {
                HttpProtocol.getApi()
                        .findUserUnFollowColumns(memberId)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<List<Column>>>(subscriber));
//                        .subscribe(new DisposableSubscriber<BaseCallModel<List<Column>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<List<Column>> listBaseCallModel) {
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
        },BackpressureStrategy.BUFFER);
    }
}
