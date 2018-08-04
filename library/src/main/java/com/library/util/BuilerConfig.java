package com.library.util;


import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Author：SEELE on 2016/8/1 14:09
 * Contact: studylifetime@sina.com
 */
public class BuilerConfig {


    private BuilerConfig() {
    }

    private static BuilerConfig sConfig = null;

    private static OkHttpClient.Builder builder = new OkHttpClient.Builder();

    /**
     * 开始构造
     *
     * @return
     */
    public static BuilerConfig newBuilder() {
        if (sConfig == null) {
            return new BuilerConfig();
        }
        return sConfig;
    }


    /**
     * 最后返回一个builder
     *
     * @return
     */
    public static OkHttpClient build() {
        if (null == builder) {
            return new OkHttpClient.Builder().build();
        }
        return builder.build();
    }


//    /**
//     * 添加拦截器
//     *
//     * @param
//     * @return
//     */
//    public static BuilerConfig addDownLoadInterceptor() {
//        Interceptor downloadInterceptor = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Response originalResponse = chain.proceed(chain.request());
//                return originalResponse
//                        .newBuilder()
//                        .body(new FileResponseBody(originalResponse))//将自定义的ResposeBody设置给它
//                        .build();
//            }
//        };
//
//        builder.addInterceptor(downloadInterceptor);
//        return sConfig;
//    }

    /**
     * 添加请求日志拦截器
     * @return
     */
    public static BuilerConfig addOKHTTPLoggingInterceptor(){
        builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC));
        return sConfig;
    }
    /**
     * 设置连接时间
     *
     * @param time 多少秒
     * @return
     */
    public static BuilerConfig setConnectTimeout(long time, TimeUnit timeUnit) {
        builder.connectTimeout(time, timeUnit);
        return sConfig;
    }

    /**
     * 设置读取时间
     *
     * @param time 多少秒
     * @return
     */
    public static BuilerConfig setReadTimeout(long time, TimeUnit timeUnit) {
        builder.readTimeout(time, timeUnit);
        return sConfig;
    }

    /**
     * 设置写入时间
     *
     * @param time 多少秒
     * @return
     */
    public static BuilerConfig setWriteTimeout(long time, TimeUnit timeUnit) {
        builder.writeTimeout(time, timeUnit);
        return sConfig;
    }



    /**
     * 设置失败后是否重连
     *
     * @param flag 多少秒
     * @return
     */
    public static BuilerConfig retryOnConnectionFailure(boolean flag) {
        builder.retryOnConnectionFailure(flag);
        return sConfig;
    }
    /**
     * 添加多个Headers
     *
     * @param HeaerMap
     * @return
     */
    public static BuilerConfig addHeaderMap(final Map<String, String> HeaerMap) {
        /**
         * 设置Hearder，代码略
         */
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();

                for (String key : HeaerMap.keySet()) {
                    builder = builder.addHeader(key, HeaerMap.get(key));
                }

                return chain.proceed(builder.build());
            }
        };
        //设置头
        builder.addInterceptor(headerInterceptor);

        return sConfig;
    }

    /**
     * 添加一个Header
     *
     * @param key
     * @param value
     * @return
     */
    public static BuilerConfig addHeader(final String key, final String value) {
        /**
         * 设置Hearder，代码略
         */
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();

                builder = builder.addHeader(key, value);

                return chain.proceed(builder.build());
            }
        };
        //设置头
        builder.addInterceptor(headerInterceptor);

        return sConfig;
    }


    /**
     * 添加一个Params
     *
     * @param key
     * @param value
     * @return
     */
    public static BuilerConfig addParam(final String key, final String value) {

        Interceptor addQueryParameterInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request request;
                HttpUrl.Builder builder = originalRequest.url().newBuilder();

                builder = builder.addQueryParameter(key, value);

                HttpUrl modifiedUrl = builder.build();

                request = originalRequest.newBuilder().url(modifiedUrl).build();
                return chain.proceed(request);
            }
        };

        builder.addInterceptor(addQueryParameterInterceptor);

        return sConfig;
    }


    /**
     * 添加多个Params
     *
     * @param ParamMap
     * @return
     */
    public static BuilerConfig addParamMap(final Map<String, String> ParamMap) {

        Interceptor addQueryParameterInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request request;
                HttpUrl.Builder builder = originalRequest.url().newBuilder();

                for (String key : ParamMap.keySet()) {
                    builder = builder.addQueryParameter(key, ParamMap.get(key));
                }

                HttpUrl modifiedUrl = builder.build();

                request = originalRequest.newBuilder().url(modifiedUrl).build();
                return chain.proceed(request);
            }
        };

        builder.addInterceptor(addQueryParameterInterceptor);

        return sConfig;
    }


    /**
     * 添加缓存处理机制
     *
     * @param context
     * @return
     */
    public static BuilerConfig addCacheInterceptor(final Context context, String filename, int size) {

        File cacheFile = new File(context.getExternalCacheDir(), filename);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * size);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetUtils.isNetworkAvailable(context)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (NetUtils.isNetworkAvailable(context)) {
                    int maxAge = 0;
                    // 有网络时 设置缓存超时时间0个小时
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("WuXiaolong")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("nyn")
                            .build();
                }
                return response;
            }
        };
        builder.cache(cache).addInterceptor(cacheInterceptor);

        return sConfig;
    }


}
