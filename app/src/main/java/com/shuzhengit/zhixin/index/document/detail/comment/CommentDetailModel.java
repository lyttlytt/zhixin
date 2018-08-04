package com.shuzhengit.zhixin.index.document.detail.comment;

import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.Comment;
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
 * Author：袁从斌 on 2017/9/7 09:22
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class CommentDetailModel implements CommentDetailContract.Model {
    @Override
    public Flowable<BaseCallModel<Comment>> fetchMasterComment(int id,int memberId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<Comment>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<Comment>> e) throws Exception {
                HttpProtocol.getApi()
                        .findCommentById(id,memberId)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<Comment>>(e));

//                        .subscribe(new DisposableSubscriber<BaseCallModel<Comment>>() {
//                            @Override
//                            public void onNext(BaseCallModel<Comment> commentBaseCallModel) {
//                                e.onNext(commentBaseCallModel);
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
    public Flowable<BaseCallModel<List<Comment>>> fetchSecondaryComments(int masterId,int memberId, int page, int
            pageSize) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<List<Comment>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<List<Comment>>> e) throws Exception {
                HttpProtocol.getApi()
                        .findSecondaryComment(masterId,memberId, page, pageSize)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<List<Comment>>>(e));
//                        .subscribe(new DisposableSubscriber<BaseCallModel<List<Comment>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<List<Comment>> listBaseCallModel) {
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


    @Override
    public Flowable<BaseCallModel<Comment>> releaseComment(String commentContent, int userId,int documentId, int
            masterCommentId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<Comment>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<Comment>> e) throws Exception {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("commentContent", commentContent);
                jsonObject.put("commentId", userId);
                jsonObject.put("documentId", documentId);
                jsonObject.put("upperCommentId", masterCommentId);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        jsonObject.toString());
                HttpProtocol.getApi()
                        .comment(requestBody)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<Comment>>(e));
//                        .subscribe(new DisposableSubscriber<BaseCallModel<Comment>>() {
//                            @Override
//                            public void onNext(BaseCallModel<Comment> commentBaseCallModel) {
//                                e.onNext(commentBaseCallModel);
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
        },BackpressureStrategy.BUFFER);
    }
}
