package com.shuzhengit.zhixin.mine.question;

import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.Question;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.NetFlowableOnSubscribe;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/29 22:37
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class MineQuestionModel implements MineQuestionContract.Model {
    @Override
    public Flowable<BaseCallModel<DataContainer<Question>>> fetchMineReleaseQuestionByUserId(int userId, int page,
                                                                                             int pageNum) {

        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<DataContainer<Question>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<DataContainer<Question>>> e) throws Exception {
                HttpProtocol.getApi()
                        .fetchMineQuestionByPage(page, pageNum, userId, 1)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<DataContainer<Question>>>(e));
//                        .subscribeWith(new DisposableSubscriber<BaseCallModel<DataContainer<Question>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<DataContainer<Question>> dataContainerBaseCallModel) {
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

    @Override
    public Flowable<BaseCallModel<DataContainer<Question>>> fetchMineReplyQuestionByUserId(int userId, int page, int
            pageNum) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<DataContainer<Question>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<DataContainer<Question>>> e) throws Exception {
                HttpProtocol.getApi()
                        .fetchMineQuestionByPage(page, pageNum, userId, 2)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<DataContainer<Question>>>(e));
//                        .subscribeWith(new DisposableSubscriber<BaseCallModel<DataContainer<Question>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<DataContainer<Question>> dataContainerBaseCallModel) {
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

    @Override
    public Flowable<BaseCallModel<DataContainer<Question>>> fetchMineFollowQuestionByUserId(int userId, int page, int
            pageNum) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<DataContainer<Question>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<DataContainer<Question>>> e) throws Exception {
                HttpProtocol.getApi()
                        .fetchUserFollowQuestion(page, pageNum, userId)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<DataContainer<Question>>>(e));
//                        .subscribeWith(new DisposableSubscriber<BaseCallModel<DataContainer<Question>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<DataContainer<Question>> dataContainerBaseCallModel) {
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
