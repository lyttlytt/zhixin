package com.shuzhengit.zhixin.question.release_question;

import com.library.bean.BaseCallModel;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.Question;
import com.shuzhengit.zhixin.bean.QuestionCategory;
import com.shuzhengit.zhixin.bean.QuestionTag;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.NetFlowableOnSubscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/25 15:27
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class ReleaseQuestionModel implements ReleaseQuestionContract.Model {
    private static final String TAG = "ReleaseQuestionModel";
    @Override
    public Flowable<BaseCallModel<Question>> releaseQuestion(int memberId, String title, String description,
                                                   List<String> images, int categoryId, List<String>
                                                           questionTags) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<Question>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<Question>> e) throws Exception {
                StringBuffer tags = new StringBuffer();
                for (int i = 0; i < questionTags.size(); i++) {
                        if (i<questionTags.size()-1){
                            tags.append(questionTags.get(i)).append(",");
                        }else {
                            tags.append(questionTags.get(i));
                        }
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("description", description);
                jsonObject.put("title", title);
                jsonObject.put("userId", memberId);
                jsonObject.put("categoryId", categoryId);
                jsonObject.put("tags",tags.toString());
                if (images != null && images.size() != 0) {
                    JSONArray jsonArray = new JSONArray();
                    for (String image : images) {
                        jsonArray.put(image);
                    }
                    jsonObject.put("images", jsonArray);
                }
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"),
                        jsonObject.toString());
                HttpProtocol.getApi()
                        .releaseQuestion(requestBody)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<Question>>(e));
//                        .subscribe(new DisposableSubscriber<BaseCallModel<Question>>() {
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
//                .flatMap(new Function<BaseCallModel<Question>, Publisher<BaseCallModel>>() {
//                    @Override
//                    public Publisher<BaseCallModel> apply(BaseCallModel<Question> callModel) throws Exception {
//                        return Flowable.create(new FlowableOnSubscribe<BaseCallModel>() {
//                            @Override
//                            public void subscribe(FlowableEmitter<BaseCallModel> e) throws Exception {
//                                if (questionTag.size() != 0) {
//                                    JSONArray jsonArray = new JSONArray();
//                                    for (Integer integer : questionTag) {
//                                        JSONObject jo = new JSONObject();
//                                        jo.put("qId", callModel.getData().getId());
//                                    }
//                                    LogUtils.i("TAG", jsonArray.toString());
//                                    HttpProtocol.getApi()
//                                            .questionRelevanceTag(jsonArray.toString())
//                                            .subscribeWith(new DisposableSubscriber<BaseCallModel>() {
//                                                @Override
//                                                public void onNext(BaseCallModel callModel) {
//                                                    e.onNext(callModel);
//                                                }
//
//                                                @Override
//                                                public void onError(Throwable t) {
//                                                    e.onError(t);
//                                                }
//
//                                                @Override
//                                                public void onComplete() {
//                                                    e.onComplete();
//                                                }
//                                            });
//                                } else {
//                                    e.onNext(callModel);
//                                    e.onComplete();
//                                }
//                            }
//                        }, BackpressureStrategy.BUFFER);
//                    }
//                });
    }


    @Override
    public Flowable<BaseCallModel<DataContainer<QuestionCategory>>> fetchQuestionCategory() {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<DataContainer<QuestionCategory>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<DataContainer<QuestionCategory>>> e) throws Exception {
                HttpProtocol.getApi()
                        .findQuestionCategory()
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<DataContainer<QuestionCategory>>>(e));
//                        .subscribe(new DisposableSubscriber<BaseCallModel<DataContainer<QuestionCategory>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<DataContainer<QuestionCategory>>
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
    public Flowable<BaseCallModel<List<QuestionTag>>> fetchQuestionTag(String keyword) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<List<QuestionTag>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<List<QuestionTag>>> e) throws Exception {
                HttpProtocol.getApi()
                        .findQuestionKeyWords(keyword)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<List<QuestionTag>>>(e));
//                        .subscribeWith(new DisposableSubscriber<BaseCallModel<List<QuestionTag>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<List<QuestionTag>> listBaseCallModel) {
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
