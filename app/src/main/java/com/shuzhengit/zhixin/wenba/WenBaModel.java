package com.shuzhengit.zhixin.wenba;

import com.library.bean.BaseCallModel;
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
 * Author：袁从斌 on 2017/11/8 08:59
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class WenBaModel implements WebBaContract.Model {
    @Override
    public Flowable<BaseCallModel<DataContainer<WenBa>>> fetchWenBaLists(int userId, int page, int pageSize) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<DataContainer<WenBa>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<DataContainer<WenBa>>> e) throws Exception {
                HttpProtocol.getApi()
                        .fetchWenBaLists(page, pageSize, userId)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<DataContainer<WenBa>>>(e));
//                        .subscribe(new DisposableSubscriber<BaseCallModel<DataContainer<WenBa>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<DataContainer<WenBa>> dataContainerBaseCallModel) {
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
}
