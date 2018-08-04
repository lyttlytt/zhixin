package com.shuzhengit.zhixin.http;


import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.library.util.LogUtils;
import com.library.util.NetUtils;
import com.library.util.Retrofit2ConverterFactory;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.Token;
import com.shuzhengit.zhixin.util.CheckUser;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;


/**
 * Created by 袁从斌-where on 2017/1/16.
 * http协议 对retrofit的封装
 */
public class HttpProtocol {
    private static ApiService sAPI = null;
    private static Retrofit sRetrofit;
    public static OkHttpClient sOkHttpClient;

//    http://192.168.1.175:9001/qabar/0_10/page
//    public static final String URL = "http://192.168.1.175:9001";
    public static final String URL = "https://api.uzhixin.com";
   //public static final String URL = "http://192.168.1.102:9999";

   // public static final String URL = "http://192.168.1.175:9001";


    private HttpProtocol() {
    }

    public static ApiService getApi() {
        if (sAPI == null) {
            initInstanceApi();
        }
        return sAPI;
    }


    private static void initInstanceApi() {
        if (sAPI == null) {
            sAPI = getRetrofit(URL).create(ApiService.class);
//            sAPI = getRetrofit("http://192.168.1.102:9001").create(ApiService.class);
        }
    }

    private static OkHttpClient getOkHttpClient() {
        int[] certificates = {R.raw.zhixin};
//        File cachePath = new File(APP.getInstance().getCacheDir(),
//                "cache");
//        //缓存文件为10MB
//        Cache cache = new Cache(cachePath, 1024 * 1024 * 10);
        if (sOkHttpClient == null) {
            sOkHttpClient = new OkHttpClient.Builder()
                    //请求log
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor
                            .Level.BODY))
                    .addInterceptor(new TokenAuthenticator())
                    .addInterceptor(addHeaderInterceptor())
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
            try {
                setCertificates(sOkHttpClient,APP.getInstance().getAssets().open("zhixin.cer"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sOkHttpClient;
    }

    public static void setCertificates(OkHttpClient okHttpClient, InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null) {
                        certificate.close();
                    }
                } catch (IOException e) {
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory
                    .getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            okHttpClient.newBuilder()
                    .sslSocketFactory(sslContext.getSocketFactory()).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static Retrofit getRetrofit(String url) {
        if (sRetrofit == null) {
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(getOkHttpClient())
//                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(new Retrofit2ConverterFactory())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return sRetrofit;
    }

    private static Interceptor addParamInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originRequest = chain.request();
                Request request;
                HttpUrl.Builder builder = originRequest.url().newBuilder();
//                builder = builder.addQueryParameter("mcode", DeviceUtil.getDeviceId(context.getApplicationContext()));
                HttpUrl modifiedUrl = builder.build();
                request = originRequest.newBuilder().url(modifiedUrl).build();
                return chain.proceed(request);
            }
        };
    }

    private static Interceptor addHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String s = chain.request().url().encodedPath();
                if (!"/api/v1/oauth-service/oauth/token".equals(s)) {
                    Request.Builder builder = chain.request().newBuilder();
                    Token token = CheckUser.checkTokenIsExists();
                    Request newRequest = builder.addHeader("AccessToken", token == null ? "" : token.getAccess_token())
                            .build();
                    LogUtils.i("token", newRequest.headers().toString());
                    return chain.proceed(newRequest);
                } else {
                    return chain.proceed(chain.request());
                }
            }
        };
    }

    private static Interceptor addCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetUtils.isNetworkAvailable(APP.getInstance())) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (NetUtils.isNetworkAvailable(APP.getInstance())) {
                    int maxAge = 0;
                    // 有网络时 设置缓存超时时间0个小时
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
//                            .removeHeader("WuXiaolong")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
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
    }
//    /**
//     * 添加一个Params
//     *
//     * @param key
//     * @param value
//     * @return
//     */
//    public static BuilerConfig addParam(final String key, final String value) {
//        Interceptor addQueryParameterInterceptor = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request originalRequest = chain.request();
//                Request request;
//                HttpUrl.Builder builder = originalRequest.url().newBuilder();
//
//                builder = builder.addQueryParameter(key, value);
//
//                HttpUrl modifiedUrl = builder.build();
//
//                request = originalRequest.newBuilder().url(modifiedUrl).build();
//                return chain.proceed(request);
//            }
//        };
//        builder.addInterceptor(addQueryParameterInterceptor);
//        return sConfig;
//    }

//    public static BuilerConfig addParam(final String key, final String value) {
//
//        Interceptor addQueryParameterInterceptor = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request originalRequest = chain.request();
//                Request request;
//                HttpUrl.Builder builder = originalRequest.url().newBuilder();
//
//                builder = builder.addQueryParameter(key, value);
//
//                HttpUrl modifiedUrl = builder.build();
//
//                request = originalRequest.newBuilder().url(modifiedUrl).build();
//                return chain.proceed(request);
//            }
//        };
//
//        builder.addInterceptor(addQueryParameterInterceptor);
//
//        return sConfig;
//    }


//网络传输协议保护机制
    protected static SSLSocketFactory getSSLSocketFactory(Context context, int[] certificates) {

        if (context == null) {
            throw new NullPointerException("context == null");
        }

        CertificateFactory certificateFactory;
            try {
                certificateFactory = CertificateFactory.getInstance("X.509");
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null, null);

                for (int i = 0; i < certificates.length; i++) {
                    InputStream certificate = context.getResources().openRawResource(certificates[i]);
                    keyStore.setCertificateEntry(String.valueOf(i), certificateFactory.generateCertificate(certificate));

                    if (certificate != null) {
                        certificate.close();
                    }
                }
                SSLContext sslContext = SSLContext.getInstance("TLS");
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);
                sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
                return sslContext.getSocketFactory();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
    }

//主机名验证
    protected static HostnameVerifier getHostnameVerifier(final String[] hostUrls) {

        HostnameVerifier TRUSTED_VERIFIER = new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                boolean ret = false;
                for (String host : hostUrls) {
                    if (host.equalsIgnoreCase(hostname)) {
                        ret = true;
                    }
                }
                return ret;
            }
        };

        return TRUSTED_VERIFIER;
    }
}
