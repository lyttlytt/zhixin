package com.shuzhengit.zhixin.question.answer;

import com.alibaba.fastjson.JSONObject;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.AnswerComment;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.QuestionAnswer;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.NetFlowableOnSubscribe;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/28 16:09
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class AnswerCommentModel implements AnswerContract.Model {
    @Override
    public Flowable<BaseCallModel<DataContainer<AnswerComment>>> fetchAnswerComments(int answerId, int page, int
            pageNum, int userId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<DataContainer<AnswerComment>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<DataContainer<AnswerComment>>> e) throws Exception {
                HttpProtocol.getApi()
                        .fetchAnswerComment(page, pageNum, answerId, userId)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<DataContainer<AnswerComment>>>(e));
//                        .subscribeWith(new DisposableSubscriber<BaseCallModel<DataContainer<AnswerComment>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<DataContainer<AnswerComment>> dataContainerBaseCallModel) {
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
    public Flowable<BaseCallModel<AnswerComment>> addAnswerComment(String content, int answerId, int questionId, int
            userId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<AnswerComment>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<AnswerComment>> e) throws Exception {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("answerId", answerId);
                jsonObject.put("content", content);
                jsonObject.put("questionId", questionId);
                jsonObject.put("replayUserId", userId);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                        jsonObject.toString());
                HttpProtocol.getApi()
                        .addAnswerComment(requestBody)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<AnswerComment>>(e));
//                        .subscribeWith(new DisposableSubscriber<BaseCallModel<AnswerComment>>() {
//                            @Override
//                            public void onNext(BaseCallModel<AnswerComment> answerCommentBaseCallModel) {
//                                e.onNext(answerCommentBaseCallModel);
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
    public Flowable<BaseCallModel> agreeComment(int answerId, int commentId, int userId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel> e) throws Exception {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("answerId", answerId);
                jsonObject.put("commentId", commentId);
                jsonObject.put("type", 1);
                jsonObject.put("voteUserId", userId);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                        jsonObject.toString());
                HttpProtocol.getApi()
                        .agreeAnswerComment(requestBody)
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
    public Flowable<BaseCallModel<QuestionAnswer>> fetchAnswerById(int answerId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<QuestionAnswer>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<QuestionAnswer>> e) throws Exception {
                HttpProtocol.getApi()
                        .fetchAnswerDetailById(answerId)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<QuestionAnswer>>(e));
//                        .subscribe(new DisposableSubscriber<BaseCallModel<QuestionAnswer>>() {
//                            @Override
//                            public void onNext(BaseCallModel<QuestionAnswer> questionAnswerBaseCallModel) {
//                                e.onNext(questionAnswerBaseCallModel);
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
