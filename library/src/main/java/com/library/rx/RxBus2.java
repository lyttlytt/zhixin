package com.library.rx;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/6/21 16:37
 * E-mail:yuancongbin@gmail.com
 */
public class RxBus2 {
    // 主题
    private final FlowableProcessor<Object> mBus;
    // PublishSubject只会把在订阅发生的时间点之后来自原始Flowable的数据发射给观察者
    private RxBus2() {
        mBus = PublishProcessor.create().toSerialized();
    }

    public static RxBus2 getDefault() {
        return RxBusHolder.sInstance;
    }

    private static class RxBusHolder {
        private static final RxBus2 sInstance = new RxBus2();
    }


    // 提供了一个新的事件
    public void post(Object o) {
        mBus.onNext(o);
    }

    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    public <T> Flowable<T> toFlowable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }
    public boolean hasSubscribers(){
        return mBus.hasSubscribers();
    }
//    // 封装默认订阅
//    public <T> Flowable<T> toDefaultFlowable(Class<T> eventType, Consumer<T> act) {
//        return mBus.ofType(eventType).compose(RxSchedulersHelper.<T>io_main()).subscribe(act);
//    }
    public void unRegister(){
        mBus.onComplete();
    }
}
