package com.shuzhengit.zhixin.rx;

import io.reactivex.FlowableEmitter;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/12/3 21:32
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class NetFlowableOnSubscribe<T> extends DisposableSubscriber<T> {
    FlowableEmitter<T> mFlowableEmitter;

    public NetFlowableOnSubscribe(FlowableEmitter<T> flowableEmitter) {
        mFlowableEmitter = flowableEmitter;
    }

    @Override
    public void onNext(T t) {
        if (!mFlowableEmitter.isCancelled()){
            mFlowableEmitter.onNext(t);
        }
    }

    @Override
    public void onError(Throwable t) {
        if (!mFlowableEmitter.isCancelled()){
            mFlowableEmitter.onError(t);
        }
    }

    @Override
    public void onComplete() {
        if (!mFlowableEmitter.isCancelled()){
            mFlowableEmitter.onComplete();
        }
    }
}
