package com.shuzhengit.zhixin.http;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/10 09:07
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述: 请求缓存的拦截器
 */

public class CacheControlInterceptor {
    private static final String TAG = "CacheControlInterceptor";
    private static final int TIMEOUT_CONNECT = 5;
    //离线的时候为7天的缓存
    private static final int TIMEOUT_DISCONNECT = 60 * 60 * 24 * 7;

    //有网时的拦截器
    public static final Interceptor REWRITE_RESPONSE_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            //获取retrofit @headers里面的参数,参数可自定义,自定义的是cache,注意@headers里面对应
            String cache = chain.request().header("cache");
            if (TextUtils.isEmpty(cache)) {
                cache = String.valueOf(0);
            }

            Response originalResponse = chain.proceed(chain.request());
            Request request = chain.request();
          //  LogUtils.i("===========from network==============");
            //LogUtils.i(request.toString());
            //LogUtils.i("===========from network==============");
            String cacheControl = originalResponse.header("Cache-Control");
            //如果cacheControl为空,就让他TIMEOUT_CONNECT秒的缓存,这里面的cacheControl是服务器返回的
            if (cacheControl == null) {
                //如果cache没值,缓存时间为TIMEOUT_CONNECT,有的话就为cache的值
//                if (TextUtils.isEmpty(cache)) {
//                    cache = TIMEOUT_CONNECT + "";
//                }
                originalResponse = originalResponse.newBuilder()
//                        .header("Cache_Control", "public, max-age=" + cache)
                        .header("Cache-Control", "public, max-age=" + cache)
                        .removeHeader("Pragma")
                        .build();
                return originalResponse;
            } else {
                return originalResponse;
            }
        }
    };

    //没网时的拦截器
    public static final Interceptor REWRITE_RESPONSE_INTERCEPTOR_OFFLINE = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            //LogUtils.i("===============OFFLINE================");
            //LogUtils.i(request.toString());
            //LogUtils.i("===============OFFLINE================");
//            String forceNetwork = chain.request().header("forceNetwork");
//            if (!TextUtils.isEmpty(forceNetwork)) {
//                if (forceNetwork.equals("true")) {
//                    LogUtils.i("强刷");
//                    request = chain.request()
//                            .newBuilder()
//                            .cacheControl(new CacheControl.Builder().maxAge(0, TimeUnit.MINUTES).build())
////                        .header("Cache-Control", "no-cache")
//                            .removeHeader("forceNetwork")
//                            .cacheControl(CacheControl.FORCE_NETWORK)
//                            .build();
//                }
//            }
//            //离线的时候为7天的缓存
//            if (!NetUtils.isNetworkAvailable(APP.getInstance())) {
//                request = request.newBuilder()
//                        .cacheControl(CacheControl.FORCE_CACHE)
//                        .header("Cache-Control", "public, only-if-cached,max-stale=" + TIMEOUT_DISCONNECT)
//                        .build();
//            }
            return chain.proceed(request);
        }
    };
    //强刷
    public static final Interceptor FORCE_CACHE_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            String forceNetwork = chain.request().header("forceNetwork");
            if (TextUtils.isEmpty(forceNetwork) || "false".equals(forceNetwork)) {
              //  LogUtils.i(TAG, "有缓存就走缓存没缓存就走网络");
                return chain.proceed(chain.request());
            } else {
              //  LogUtils.i(TAG, "强刷");
                Request request = chain.request();
                request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK)
                        .header("Cache-Control", "no-cache")
                        .removeHeader("forceNetwork").build();
                return chain.proceed(request);
            }
        }
    };
}
