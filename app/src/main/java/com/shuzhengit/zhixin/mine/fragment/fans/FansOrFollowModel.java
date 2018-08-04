package com.shuzhengit.zhixin.mine.fragment.fans;

import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.DataContainer;
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
 * Author：袁从斌 on 2017/9/23 16:10
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class FansOrFollowModel implements FansOrFollowContract.Model {
    @Override
    public Flowable<BaseCallModel<DataContainer<User>>> findFansByPage(int memberId, int page) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<DataContainer<User>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<DataContainer<User>>> e) throws Exception {
                HttpProtocol.getApi()
                        .findFansById(memberId, page, 10)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<DataContainer<User>>>(e));
//                        .subscribe(new DisposableSubscriber<BaseCallModel<DataContainer<User>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<DataContainer<User>> dataContainerBaseCallModel) {
//                                e.onNext(dataContainerBaseCallModel);
//                            }
//
//                            @Override
//                            public void onError(Throwable t) {
//                                e.onError(t);
//
//                            }
//
//                            @Override
//                            public void onComplete() {
//                                e.onComplete();
//
//                            }
//                        });
            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<BaseCallModel<DataContainer<User>>> findFollowByPage(int memberId, int page) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<DataContainer<User>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<DataContainer<User>>> e) throws Exception {
                HttpProtocol.getApi()
                        .findFollowById(memberId, page, 10)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<DataContainer<User>>>(e));
//                        .subscribe(new DisposableSubscriber<BaseCallModel<DataContainer<User>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<DataContainer<User>> dataContainerBaseCallModel) {
//                                e.onNext(dataContainerBaseCallModel);
//                            }
//
//                            @Override
//                            public void onError(Throwable t) {
//                                e.onError(t);
//
//                            }
//
//                            @Override
//                            public void onComplete() {
//                                e.onComplete();
//
//                            }
//                        });
            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<BaseCallModel> unFollowUser(int memberId, int followerId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel> e) throws Exception {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("memberId", memberId);
                jsonObject.put("followerId", followerId);
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
