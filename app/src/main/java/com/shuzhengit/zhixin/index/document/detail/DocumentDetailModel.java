package com.shuzhengit.zhixin.index.document.detail;

import com.google.gson.Gson;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.Collect;
import com.shuzhengit.zhixin.bean.Comment;
import com.shuzhengit.zhixin.bean.Document;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.NetFlowableOnSubscribe;

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
 * Author：袁从斌 on 2017/8/16 16:08
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class DocumentDetailModel implements DocumentDetailContract.Model {
    @Override
    public Flowable<BaseCallModel<List<Comment>>> fetchComments(int documentId,int memberId, int page, int pageSize) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<List<Comment>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<List<Comment>>> subscriber) throws Exception {
                HttpProtocol.getApi()
                        .findCommentByPage(documentId,memberId, page, pageSize)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<List<Comment>>>(subscriber));
//                                new DisposableSubscriber<BaseCallModel<List<Comment>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<List<Comment>> listBaseCallModel) {
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

//    @Override
//    public Flowable<BaseCallModel> checkIsAgreeDocument(int documentId, int userId) {
//        return null;
//    }
//
//    @Override
//    public Flowable<BaseCallModel> checkIsCollectDocument(int documentId, int userId) {
//        return null;
//    }

    @Override
    public Flowable<BaseCallModel<String>> agreeComment(int commentId, int userId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<String>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<String>> e) throws Exception {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("commentId", commentId);
                jsonObject.put("memberId", userId);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        jsonObject.toString());
                HttpProtocol.getApi()
                        .commentLike(requestBody)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<String>>(e));
//                        .subscribe(new DisposableSubscriber<BaseCallModel<String>>() {
//                            @Override
//                            public void onNext(BaseCallModel<String> callModel) {
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
    public Flowable<BaseCallModel<List<Document>>> fetchRecommendDocument(int documentId) {
        return null;
    }

    @Override
    public Flowable<BaseCallModel<Comment>> releaseComment(String commentContent, int userId, int documentId, int
            upperCommentId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<Comment>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<Comment>> subscriber) throws Exception {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("commentContent", commentContent);
                jsonObject.put("commentId", userId);
                jsonObject.put("documentId", documentId);
                jsonObject.put("upperCommentId", upperCommentId);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        jsonObject.toString());
                HttpProtocol.getApi()
                        .comment(requestBody)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<Comment>>(subscriber));

//                        .subscribe(new DisposableSubscriber<BaseCallModel<Comment>>() {
//                            @Override
//                            public void onNext(BaseCallModel<Comment> commentBaseCallModel) {
//                                subscriber.onNext(commentBaseCallModel);
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
    public Flowable<BaseCallModel> collectOrCancelDocument(int documentId, int userId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel> subscriber) throws Exception {
                Collect collect = new Collect(documentId, userId);
                String json = new Gson().toJson(collect);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
                HttpProtocol.getApi()
                        .favouriteOrNotDocument(requestBody)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel>(subscriber));
//                        .subscribe(new DisposableSubscriber<BaseCallModel>() {
//                            @Override
//                            public void onNext(BaseCallModel baseCallModel) {
//                                subscriber.onNext(baseCallModel);
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
    public Flowable<BaseCallModel> agreeDocument(int document, int userId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel> subscriber) throws Exception {
                Collect collect = new Collect(document, userId);
                String json = new Gson().toJson(collect);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
                HttpProtocol.getApi()
                        .documentLike(requestBody)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel>(subscriber));
//                        .subscribe(new DisposableSubscriber<BaseCallModel>() {
//                            @Override
//                            public void onNext(BaseCallModel baseCallModel) {
//                                subscriber.onNext(baseCallModel);
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
    public Flowable<BaseCallModel<Document>> fetchDocumentDetail(String esId,int memberId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<Document>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<Document>> subscriber) throws Exception {
                HttpProtocol.getApi()
                        .findDocumentDetailByDocumentId(esId,memberId)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<Document>>(subscriber));
//                        .subscribe(new DisposableSubscriber<BaseCallModel<Document>>() {
//                            @Override
//                            public void onNext(BaseCallModel<Document> documentBaseCallModel) {
//                                subscriber.onNext(documentBaseCallModel);
//                            }
//
//                            @Override
//                            public void onError(Throwable t) {
//                                subscriber.onError(t);
//
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
}
