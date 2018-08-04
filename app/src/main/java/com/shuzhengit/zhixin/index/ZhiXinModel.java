package com.shuzhengit.zhixin.index;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.library.bean.BaseCallModel;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.bean.Column;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.NetFlowableOnSubscribe;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/14 13:33
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

class ZhiXinModel implements ZhiXinContract.Model {
    @Override
    public Flowable<BaseCallModel<List<Column>>> findColumns(int memberId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<List<Column>>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<List<Column>>> subscriber) throws
                    Exception {
                HttpProtocol.getApi()
                        .findUserFollowColumns(memberId)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel<List<Column>>>(subscriber));
//                        .subscribe(new DisposableSubscriber<BaseCallModel<List<Column>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<List<Column>> pageContainerBaseCallModel) {
//                                LogUtils.e("TAG", pageContainerBaseCallModel.getData().toString());
//                                subscriber.onNext(pageContainerBaseCallModel);
//                            }
//
//                            @Override
//                            public void onError(Throwable t) {
//                                t.printStackTrace();
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
//        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<List<Column>>>() {
//            @Override
//            public void subscribe(FlowableEmitter<BaseCallModel<List<Column>>> subscriber) throws Exception {
//                HttpProtocol.getApi()
//                        .findUserFollowColumns(memberId)
//                        .subscribe(new DisposableSubscriber<BaseCallModel<PageContainer<Column>>>() {
//                            @Override
//                            public void onNext(BaseCallModel<PageContainer<Column>> pageContainerBaseCallModel) {
//                                subscriber.onNext(pageContainerBaseCallModel);
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
////                        .subscribe(new DisposableSubscriber<BaseCallModel<List<Column>>>() {
////                            @Override
////                            public void onNext(BaseCallModel<List<Column>> baseCallModel) {
////                                subscriber.onNext(baseCallModel);
////                            }
////
////                            @Override
////                            public void onError(Throwable t) {
////                                subscriber.onError(t);
////                                t.printStackTrace();
////                            }
////
////                            @Override
////                            public void onComplete() {
////                                subscriber.onComplete();
////                            }
////                        });
//            }
//        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<BaseCallModel<String>> updateColumns(int memberId, JSONArray updateColumns) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < updateColumns.size(); i++) {
            JSONObject update = new JSONObject();
            JSONObject jsonObject1 = updateColumns.getJSONObject(i);
            try {
//                for (int j = 0; j < jsonArray.length(); j++) {
//                    JSONObject jsonObject = jsonArray.optJSONObject(j);
//                    int columnId = jsonObject.optInt("columnId");
//                    if (columnId==jsonObject1.optInt("id")){
//                    }
//                }
                update.put("sortNo", i);
                update.put("columnId", jsonObject1.getIntValue("id"));
                jsonArray.add(update);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        LogUtils.i("TAG", jsonArray.toString());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonArray
                .toString());
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<String>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel<String>> e) throws Exception {
                HttpProtocol.getApi()
                        .batchUpdateColumn(memberId, requestBody)
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
    public Flowable<BaseCallModel> reSubscriberColumn(int memberId) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel> e) throws Exception {
                HttpProtocol.getApi()
                        .reSubscriberColumn(memberId,"application/json; charset=utf-8")
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
