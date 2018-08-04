package com.shuzhengit.zhixin.question.question_detail;

import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.Question;
import com.shuzhengit.zhixin.bean.QuestionAnswer;
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
 * Author：袁从斌 on 2017/9/27 20:40
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class QuestionDetailModel implements QuestionDetailContract.Model {
    @Override
    public Flowable<BaseCallModel<Question>> fetchQuestionDetail(int questionId,int memberId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<Question>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<Question>> e) throws Exception {
                HttpProtocol.getApi()
                        .findQuestionDetail(questionId,memberId)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<Question>>(e));
//                        .subscribeWith(new DisposableSubscriber<BaseCallModel<Question>>() {
//                            @Override
//                            public void onNext(BaseCallModel<Question> questionBaseCallModel) {
//                                e.onNext(questionBaseCallModel);
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
    public Flowable<BaseCallModel<DataContainer<QuestionAnswer>>> fetchQuestionAnswer(int page, int pageNum, int
            questionId, int userId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<DataContainer<QuestionAnswer>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<DataContainer<QuestionAnswer>>> e) throws Exception {
                HttpProtocol.getApi()
                        .fetchQuestionAnswer(page, pageNum, questionId, userId)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<DataContainer<QuestionAnswer>>>(e));
//                        .subscribeWith(new DisposableSubscriber<BaseCallModel<DataContainer<QuestionAnswer>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<DataContainer<QuestionAnswer>>
//                                                       dataContainerBaseCallModel) {
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
    public Flowable<BaseCallModel> agreeAnswer(int answerId, int userId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel> e) throws Exception {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("answerId", answerId);
                jsonObject.put("voteUserId", userId);
                jsonObject.put("type", 1);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                        jsonObject.toString());
                HttpProtocol.getApi()
                        .agreeAnswer(requestBody)
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
    public Flowable<BaseCallModel> followerQuestion(int likeUserId, int questionId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel> e) throws Exception {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("likeUserId", likeUserId);
                jsonObject.put("questionId", questionId);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                        jsonObject.toString());
                HttpProtocol.getApi()
                        .followedQuestion(requestBody)
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
