package com.shuzhengit.zhixin.wenba.detail.reply;

import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.AskComment;
import com.shuzhengit.zhixin.bean.AskWithReply;
import com.shuzhengit.zhixin.bean.DataContainer;
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
 * Author：袁从斌 on 2017/11/15 20:35
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class ReplyModel implements ReplyContract.Model {
    @Override
    public Flowable<BaseCallModel> postComment(String content, int askId, int wenBaId, int replayUserId) {
//        {
//            "answerId": 0,   问题id
//            "content": "string",
//                "questionId": 0,  问吧id
//            "replayUserId": 0
//        }
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel> e) throws Exception {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("answerId", askId);
                jsonObject.put("questionId", wenBaId);
                jsonObject.put("content", content);
                jsonObject.put("replayUserId", replayUserId);
                RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                        jsonObject.toString());
                HttpProtocol.getApi()
                        .postComment(body)
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

    @Override
    public Flowable<BaseCallModel<AskWithReply>> findAskWithReplyById(int askId, int userId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<AskWithReply>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<AskWithReply>> e) throws Exception {
                HttpProtocol.getApi()
                        .findAskWithReplyById(askId, userId)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<AskWithReply>>(e));
//                        .subscribeWith(new DisposableSubscriber<BaseCallModel<AskWithReply>>() {
//                            @Override
//                            public void onNext(BaseCallModel<AskWithReply> askWithReplyBaseCallModel) {
//                                e.onNext(askWithReplyBaseCallModel);
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

    @Override
    public Flowable<BaseCallModel<DataContainer<AskComment>>> fetchComments(int page, int pageSize, int userId, int
            askId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<DataContainer<AskComment>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<DataContainer<AskComment>>> e) throws Exception {
                HttpProtocol.getApi()
                        .fetchAskComments(page, pageSize, askId, userId)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<DataContainer<AskComment>>>(e));
//                        .subscribeWith(new DisposableSubscriber<BaseCallModel<DataContainer<AskComment>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<DataContainer<AskComment>> dataContainerBaseCallModel) {
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
    public Flowable<BaseCallModel> agreeComment(int askId, int commentId, int userId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel> e) throws Exception {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("answerId", askId);
                jsonObject.put("commentId", commentId);
                jsonObject.put("voteUserId", userId);
                RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                        jsonObject.toString());
                HttpProtocol.getApi()
                        .agreeAskComment(body)
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
