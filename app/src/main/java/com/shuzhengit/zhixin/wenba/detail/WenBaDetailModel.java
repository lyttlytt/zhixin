package com.shuzhengit.zhixin.wenba.detail;

import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.AskWithReply;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.WenBa;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.NetFlowableOnSubscribe;

import org.json.JSONObject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/14 10:36
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class WenBaDetailModel implements WenBaDetailContract.Model {
    @Override
    public Flowable<BaseCallModel<WenBa>> fetchWenBa(int wenBaId, int userId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<WenBa>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<WenBa>> e) throws Exception {
                HttpProtocol.getApi()
                        .fetchWenBaDetail(wenBaId, userId)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<WenBa>>(e));
//                        .subscribe(new DisposableSubscriber<BaseCallModel<WenBa>>() {
//                            @Override
//                            public void onNext(BaseCallModel<WenBa> wenBaBaseCallModel) {
//                                e.onNext(wenBaBaseCallModel);
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
    public Flowable<BaseCallModel<DataContainer<AskWithReply>>> fetchAskAndReplysOnNew(int page, int pageSize, int
            wenBaId, int userId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<DataContainer<AskWithReply>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<DataContainer<AskWithReply>>> e) throws Exception {
                HttpProtocol.getApi()
                        .fetchAskWithReply(page, pageSize, wenBaId, userId,0)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<DataContainer<AskWithReply>>>(e));
//                        .subscribe(new DisposableSubscriber<BaseCallModel<DataContainer<AskWithReply>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<DataContainer<AskWithReply>> dataContainerBaseCallModel) {
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
    public Flowable<BaseCallModel<DataContainer<AskWithReply>>> fetchAskAndReplysOnHot(int page, int pageSize, int
            wenBaId, int userId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<DataContainer<AskWithReply>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<DataContainer<AskWithReply>>> e) throws Exception {
                HttpProtocol.getApi()
                        .fetchAskWithReply(page, pageSize, wenBaId, userId,1)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<DataContainer<AskWithReply>>>(e));
//                        .subscribe(new DisposableSubscriber<BaseCallModel<DataContainer<AskWithReply>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<DataContainer<AskWithReply>> dataContainerBaseCallModel) {
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
    public Flowable<BaseCallModel> followedWenBa(int userId, int wenBaId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel> e) throws Exception {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("likeUserId", userId);
                jsonObject.put("questionId", wenBaId);
                RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                        jsonObject.toString());
                HttpProtocol.getApi()
                        .followWenBa(body)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel>(e));
//                        .subscribe(new DisposableSubscriber<BaseCallModel>() {
//                            @Override
//                            public void onNext(BaseCallModel callModel) {
//                                e.onNext(callModel);
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
    public Flowable<BaseCallModel> agreeAsk(int askId, int userId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel> e) throws Exception {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("answerId", askId);
                jsonObject.put("voteUserId", userId);
                RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                        jsonObject.toString());
                HttpProtocol.getApi()
                        .agreeAsk(body)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel>(e));
//                        .subscribeWith(new DisposableSubscriber<BaseCallModel>() {
//                            @Override
//                            public void onNext(BaseCallModel callModel) {
//                                e.onNext(callModel);
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
