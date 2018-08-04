package com.shuzhengit.zhixin.util;

import com.library.util.LogUtils;
import com.shuzhengit.zhixin.APP;

import java.io.Serializable;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Function;


/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/11 13:58
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:缓存工具类
 */

public class RxRetrofitCacheUtil {
//    /**
//     * @param context
//     * @param cacheKey     缓存key值
//     * @param expireTile   过期时间 0 表示有缓存就读,没有就从网络取
//     * @param fromNetWork  从网络取的Flowable
//     * @param forceRefresh 是否强制刷新
//     * @param <T>
//     * @return
//     */
//    public static <T> Flowable<T> load(Context context, String cacheKey, long expireTile, Flowable<T> fromNetWork,
//                                       boolean forceRefresh) {
//        Flowable<T> fromCache = Flowable.create(new FlowableOnSubscribe<T>() {
//            @Override
//            public void subscribe(FlowableEmitter<T> e) throws Exception {
//                T cache = (T) Hawk.get(cacheKey);
//                if (cache == null) {
//                    e.onNext(cache);
//                } else {
//                    e.onComplete();
//                }
//            }
//        }, BackpressureStrategy.BUFFER).compose(RxSchedulersHelper.io_main());
////        if ()
//        return null;
//    }

    /**
     * @param cacheKey     缓存的key
     * @param fromNetWork
     * @param isSave       是否缓存
     * @param forceRefresh 强制刷新
     * @param <T>
     * @return
     */
    public static <T> Flowable<T> load(String cacheKey, Flowable<T> fromNetWork,
                                       boolean isSave, boolean forceRefresh) {
        Flowable<T> fromCache = Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> e) throws Exception {
                T cache = (T) CacheUtils.getCacheManager(APP.getInstance()).getAsObject(cacheKey);
                if (cache != null) {
                    LogUtils.i("ZhiXinFragment","from cache data : " + cache.toString());
                    e.onNext(cache);
                } else {
                    e.onComplete();
                }
            }
        }, BackpressureStrategy.BUFFER);
        //是否缓存
        if (isSave) {
            fromNetWork = fromNetWork.map(new Function<T, T>() {
                @Override
                public T apply(T t) throws Exception {
//                    Hawk.put(cacheKey, t);

                    CacheUtils.getCacheManager(APP.getInstance()).put(cacheKey, (Serializable) t,100);
                    return t;
                }
            });
        }
        //强制刷新
        if (forceRefresh) {
            return fromNetWork;
        }else {
            return Flowable.concat(fromCache,fromNetWork).take(1);
        }
    }

    /**
     *
     * @param cacheKey 缓存键
     * @param fromNetWork  走网络
     * @param isSave 是否缓存
     * @param forceRefresh 是否强刷
     * @param saveTime 缓存时间
     * @param <T>
     * @return
     */
    public static <T> Flowable<T> load(String cacheKey, Flowable<T> fromNetWork,
                                       boolean isSave, boolean forceRefresh,int saveTime) {
        Flowable<T> fromCache = Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> e) throws Exception {
                T cache = (T) CacheUtils.getCacheManager(APP.getInstance()).getAsObject(cacheKey);
                if (cache != null) {
                    LogUtils.i("ZhiXinFragment","from cache data : " + cache.toString());
                    e.onNext(cache);
                } else {
                    e.onComplete();
                }
            }
        }, BackpressureStrategy.BUFFER);
        //是否缓存
        if (isSave) {
            fromNetWork = fromNetWork.map(new Function<T, T>() {
                @Override
                public T apply(T t) throws Exception {
//                    Hawk.put(cacheKey, t);

                    CacheUtils.getCacheManager(APP.getInstance()).put(cacheKey, (Serializable) t,saveTime);
                    return t;
                }
            });
        }
        //强制刷新
        if (forceRefresh) {
            return fromNetWork;
        }else {
            return Flowable.concat(fromCache,fromNetWork).take(1);
        }
    }
}
