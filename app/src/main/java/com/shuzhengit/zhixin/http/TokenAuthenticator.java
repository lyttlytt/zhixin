package com.shuzhengit.zhixin.http;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.library.util.LogUtils;
import com.library.util.Retrofit2ConverterFactory;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.bean.Token;
import com.shuzhengit.zhixin.login.LoginModel;
import com.shuzhengit.zhixin.util.CacheKeyManager;
import com.shuzhengit.zhixin.util.CacheUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/1 16:20
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class TokenAuthenticator implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (tokenExpired(response)) {
            String newToken = refreshToken();
            LogUtils.i("TAG", "newToken : " + newToken);
            Request newRequest = chain.request().newBuilder()
                    .header("AccessToken", newToken)
                    .build();
            return chain.proceed(newRequest);
        }
        return response;
    }

    private String refreshToken() throws IOException {
        Token newToken;
        Token token = (Token) CacheUtils.getCacheManager(APP.getInstance()).getAsObject(CacheKeyManager.TOKEN);
        //请求log
        OkHttpClient build1 = new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel
                (HttpLoggingInterceptor
                        .Level.BODY)).build();
        Retrofit build = new Retrofit.Builder()
                .client(build1)
                .baseUrl(HttpProtocol.URL)
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(new Retrofit2ConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ApiService apiService = build.create(ApiService.class);
        retrofit2.Response<Token> refreshToken = apiService.refreshTokenSynchronized("refresh_token", LoginModel
                .CLIENTID, LoginModel.CLIENTSECRET, token
                .getRefresh_token()).execute();
        newToken = refreshToken.body();
        if (newToken != null) {
            String accessToken = newToken.getAccess_token();
            CacheUtils.getCacheManager(APP.getInstance()).remove("token");
            CacheUtils.getCacheManager(APP.getInstance()).put("token", newToken);
            return accessToken;
        } else {
            return "";
        }
//        }

    }

    /**
     * 判断是不是token过期
     */
    private boolean tokenExpired(Response response) {
        return response.code() == 401;
    }


}
