package com.shuzhengit.zhixin.mine.user;

import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.User;
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
 * Author：袁从斌 on 2017/9/22 14:55
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class UserModel implements UserInfoContract.Model {
    @Override
    public Flowable<BaseCallModel<User>> findUserById(int memberId,int queryUserId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<User>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<User>> e) throws Exception {
                HttpProtocol.getApi()
                        .findUserInfoById(memberId,queryUserId+"")
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<User>>(e));
//                        .subscribe(new DisposableSubscriber<BaseCallModel<User>>() {
//                            @Override
//                            public void onNext(BaseCallModel<User> userBaseCallModel) {
//                                e.onNext(userBaseCallModel);
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
    public Flowable<BaseCallModel> follower(int memberId, int followerId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel> e) throws Exception {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("memberId",memberId);
                jsonObject.put("followerId",followerId);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        jsonObject.toString());
                HttpProtocol.getApi()
                        .followUser(requestBody)
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
    public Flowable<BaseCallModel> unFollower(int memberId, int followerId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel> e) throws Exception {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("memberId",memberId);
                jsonObject.put("followerId",followerId);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        jsonObject.toString());
                HttpProtocol.getApi()
                        .unFollowUser(requestBody)
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
