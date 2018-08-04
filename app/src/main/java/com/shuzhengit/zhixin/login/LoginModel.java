package com.shuzhengit.zhixin.login;

import com.alibaba.fastjson.JSONObject;
import com.library.bean.BaseCallModel;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.bean.Basic;
import com.shuzhengit.zhixin.bean.IPModel;
import com.shuzhengit.zhixin.bean.Token;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.NetFlowableOnSubscribe;
import com.shuzhengit.zhixin.util.CacheKeyManager;
import com.shuzhengit.zhixin.util.CacheUtils;

import org.reactivestreams.Publisher;

import java.text.SimpleDateFormat;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/11 10:01
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class LoginModel implements LoginContract.Model {
    public static final String CLIENTID = "84146dc8d3cfca7affd5c6d4741e0119";
    public static final String CLIENTSECRET = "43ef6a44fb65aaf5864edcefbaa1f062";

    @Override
    public Flowable<BaseCallModel<User>> doNormalLogin(String username, String password) {
        return Flowable.create((FlowableOnSubscribe<Token>) e -> HttpProtocol.getApi()
                .passwordLogin(CLIENTID, CLIENTSECRET, "password", username, password)
                .subscribe(new NetFlowableOnSubscribe<Token>(e){
                    @Override
                    public void onNext(Token token) {
                        if (!e.isCancelled()) {
                            CacheUtils.getCacheManager(APP.getInstance()).remove(CacheKeyManager.TOKEN);
                            CacheUtils.getCacheManager(APP.getInstance()).put(CacheKeyManager.TOKEN, token);
                            e.onNext(token);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (!e.isCancelled()) {
                            if ("HTTP 400 Bad Request".equals(t.getMessage())) {
                                e.onError(new Throwable("账号或密码错误"));
                            }
                            LogUtils.i("TAG", t.getMessage());
                        }
                    }
                }
//                .subscribe(new DisposableSubscriber<Token>() {
//                    @Override
//                    public void onNext(Token token) {
//                        if (!e.isCancelled()) {
//                            CacheUtils.getCacheManager(APP.getInstance()).remove(CacheKeyManager.TOKEN);
//                            CacheUtils.getCacheManager(APP.getInstance()).put(CacheKeyManager.TOKEN, token);
//                            e.onNext(token);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        if (!e.isCancelled()) {
//                            if ("HTTP 400 Bad Request".equals(t.getMessage())) {
//                                e.onError(new Throwable("账号或密码错误"));
//                            }
//                            LogUtils.i("TAG", t.getMessage());
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        if (!e.isCancelled()) {
//                            e.onComplete();
//                        }
//                    }
//                }
                ), BackpressureStrategy.BUFFER)
                .flatMap(new Function<Token, Publisher<BaseCallModel<Basic>>>() {
                    @Override
                    public Publisher<BaseCallModel<Basic>> apply(Token token) throws Exception {
                        return Flowable.create(e -> HttpProtocol.getApi()
                                .findUserInfoByToken(token.getAccess_token())
                                .subscribe(new NetFlowableOnSubscribe<BaseCallModel<Basic>>(e){
                                    @Override
                                    public void onNext(BaseCallModel<Basic> basicBaseCallModel) {
                                        if (!e.isCancelled()) {
                                            basicBaseCallModel.getData().setAccessToken(token.getAccess_token());
                                            e.onNext(basicBaseCallModel);
                                        }
                                    }
                                }),BackpressureStrategy.BUFFER);
//                                .subscribe(new DisposableSubscriber<BaseCallModel<Basic>>() {
//                                    @Override
//                                    public void onNext(BaseCallModel<Basic> basicBaseCallModel) {
//                                        if (!e.isCancelled()) {
//                                            basicBaseCallModel.getData().setAccessToken(token.getAccess_token());
//                                            e.onNext(basicBaseCallModel);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable t) {
//                                        if (!e.isCancelled()) {
//                                            e.onError(t);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onComplete() {
//                                        if (!e.isCancelled()) {
//                                            e.onComplete();
//                                        }
//                                    }
//                                }), BackpressureStrategy.BUFFER);
                    }
                })
                .flatMap(new Function<BaseCallModel<Basic>, Publisher<BaseCallModel<User>>>() {
                    @Override
                    public Publisher<BaseCallModel<User>> apply(BaseCallModel<Basic> basicBaseCallModel) throws
                            Exception {
                        Basic data = basicBaseCallModel.getData();
                        return Flowable.create(e -> HttpProtocol.getApi()
                                .findUserInfoById(data.getId(),data.getId() + "")
                                .subscribe(new NetFlowableOnSubscribe<BaseCallModel<User>>(e){
                                    @Override
                                    public void onNext(BaseCallModel<User> userBaseCallModel) {
                                        if (!e.isCancelled()) {
                                            CacheUtils.getCacheManager(APP.getInstance()).put(CacheKeyManager.USER,
                                                    userBaseCallModel.getData());
                                            e.onNext(userBaseCallModel);
                                        }
                                    }
                                }),BackpressureStrategy.BUFFER);
//                                .subscribe(new DisposableSubscriber<BaseCallModel<User>>() {
//                                    @Override
//                                    public void onNext(BaseCallModel<User> userBaseCallModel) {
//                                        if (!e.isCancelled()) {
//                                            CacheUtils.getCacheManager(APP.getInstance()).put(CacheKeyManager.USER,
//                                                    userBaseCallModel.getData());
//                                            e.onNext(userBaseCallModel);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable t) {
//                                        if (!e.isCancelled()) {
//                                            e.onError(t);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onComplete() {
//                                        if (!e.isCancelled()) {
//                                            e.onComplete();
//                                        }
//                                    }
//                                }), BackpressureStrategy.BUFFER);
                    }
                });
//        return Flowable.create(new FlowableOnSubscribe<Token>() {
//            @Override
//            public void subscribe(FlowableEmitter<Token> e) throws Exception {
//                HttpProtocol.getApi()
//                        .passwordLogin(clientId, clientSecret, "password", username, password)
//                        .subscribe(new DisposableSubscriber<Token>() {
//                            @Override
//                            public void onNext(Token token) {
//                                e.onNext(token);
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
//            }
//        }, BackpressureStrategy.BUFFER)
//
//                .flatMap(new Function<Token, Publisher<BaseCallModel<User>>>() {
//                    @Override
//                    public Publisher<BaseCallModel<User>> apply(Token token) throws Exception {
//                        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<User>>() {
//                            @Override
//                            public void subscribe(FlowableEmitter<BaseCallModel<User>> e) throws Exception {
//                                HttpProtocol.getApi()
//                                        .findUserInfoById(token.getAccess_token(), username)
//                                        .subscribe(new DisposableSubscriber<BaseCallModel<User>>() {
//                                            @Override
//                                            public void onNext(BaseCallModel<User> userBaseCallModel) {
//                                                APP.sCacheManager.put("token", token);
//                                                e.onNext(userBaseCallModel);
//                                            }
//
//                                            @Override
//                                            public void onError(Throwable t) {
//                                                e.onError(t);
//                                            }
//
//                                            @Override
//                                            public void onComplete() {
//                                                e.onComplete();
//                                            }
//                                        });
//                            }
//                        }, BackpressureStrategy.BUFFER);
//                    }
//
//                });
    }

    @Override
    public Flowable<BaseCallModel<User>> doSocialLogin(
            String openid, String username, String password,
            String accessToken, String authPfType) {
        return Flowable.create((FlowableOnSubscribe<Token>) e -> HttpProtocol.getApi()
                .socialLogin(CLIENTID, CLIENTSECRET, "password", username, password, openid, accessToken,
                        authPfType)
                .subscribe(new NetFlowableOnSubscribe<Token>(e){
                    @Override
                    public void onNext(Token token) {
                        if (!e.isCancelled()) {
                            CacheUtils.getCacheManager(APP.getInstance()).put("token", token);
                            e.onNext(token);
                        }
                    }
                }),BackpressureStrategy.BUFFER)
//                .subscribe(new DisposableSubscriber<Token>() {
//                    @Override
//                    public void onNext(Token token) {
//                        if (!e.isCancelled()) {
//                            CacheUtils.getCacheManager(APP.getInstance()).put("token", token);
//                            e.onNext(token);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        if (!e.isCancelled()) {
//                            e.onError(t);
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        if (!e.isCancelled()) {
//                            e.onComplete();
//                        }
//                    }
//                }), BackpressureStrategy.BUFFER)
                .flatMap(new Function<Token, Publisher<BaseCallModel<Basic>>>() {
                    @Override
                    public Publisher<BaseCallModel<Basic>> apply(Token token) throws Exception {
                        return Flowable.create(e -> HttpProtocol.getApi()
                                .findUserInfoByToken(token.getAccess_token())
                                .subscribe(new NetFlowableOnSubscribe<BaseCallModel<Basic>>(e){
                                    @Override
                                    public void onNext(BaseCallModel<Basic> basicBaseCallModel) {
                                        if (!e.isCancelled()) {
                                            basicBaseCallModel.getData().setAccessToken(token.getAccess_token());
                                            e.onNext(basicBaseCallModel);
                                        }
                                    }
                                })
//                                .subscribe(new DisposableSubscriber<BaseCallModel<Basic>>() {
//                                    @Override
//                                    public void onNext(BaseCallModel<Basic> basicBaseCallModel) {
//                                        if (!e.isCancelled()) {
//                                            basicBaseCallModel.getData().setAccessToken(token.getAccess_token());
//                                            e.onNext(basicBaseCallModel);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable t) {
//                                        if (!e.isCancelled()) {
//                                            e.onError(t);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onComplete() {
//                                        if (!e.isCancelled()) {
//                                            e.onComplete();
//                                        }
//                                    }
//                                })
                                , BackpressureStrategy.BUFFER);
                    }
                })
                .flatMap(new Function<BaseCallModel<Basic>, Publisher<BaseCallModel<User>>>() {
                    @Override
                    public Publisher<BaseCallModel<User>> apply(BaseCallModel<Basic> basicBaseCallModel) throws
                            Exception {
                        return Flowable.create(subscriber -> {
                            Basic data = basicBaseCallModel.getData();
                            HttpProtocol.getApi()
                                    .findUserInfoById(data.getId(),data.getId() + "")
                                    .subscribe(new NetFlowableOnSubscribe<BaseCallModel<User>>(subscriber){
                                        @Override
                                        public void onNext(BaseCallModel<User> userBaseCallModel) {
                                            if (!subscriber.isCancelled()){
                                                CacheUtils.getCacheManager(APP.getInstance()).put(CacheKeyManager.USER,
                                                        userBaseCallModel.getData());
                                                subscriber.onNext(userBaseCallModel);
                                            }
                                        }
                                    });
//                                    .subscribe(new DisposableSubscriber<BaseCallModel<User>>() {
//                                        @Override
//                                        public void onNext(BaseCallModel<User> userBaseCallModel) {
//                                            CacheUtils.getCacheManager(APP.getInstance()).put(CacheKeyManager.USER,
//                                                    userBaseCallModel.getData());
//                                            subscriber.onNext(userBaseCallModel);
//                                        }
//
//                                        @Override
//                                        public void onError(Throwable t) {
//                                            subscriber.onError(t);
//                                        }
//
//                                        @Override
//                                        public void onComplete() {
//                                            subscriber.onComplete();
//                                        }
//                                    });
                        }, BackpressureStrategy.BUFFER);
                    }
                });
//        return Flowable.create(new FlowableOnSubscribe<Token>() {
//            @Override
//            public void subscribe(FlowableEmitter<Token> e) throws Exception {
//                HttpProtocol
//                        .getApi()
//                        .socialLogin(clientId, clientSecret, "password", username, password, openid, accessToken,
//                                authPfType)
//                        .subscribe(new DisposableSubscriber<Token>() {
//                            @Override
//                            public void onNext(Token token) {
//                                APP.sCacheManager.put("token", token);
//                                e.onNext(token);
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
//            }
//        }, BackpressureStrategy.BUFFER)
//                .flatMap(new Function<Token, Publisher<BaseCallModel<User>>>() {
//                    @Override
//                    public Publisher<BaseCallModel<User>> apply(Token token) throws Exception {
//                        return Flowable.create(new FlowableOnSubscribe<BaseCallModel<User>>() {
//                            @Override
//                            public void subscribe(FlowableEmitter<BaseCallModel<User>> subscriber) throws Exception {
//                                HttpProtocol.getApi()
//                                        .findUserInfoById(token.getAccess_token(), openid)
//                                        .subscribe(new DisposableSubscriber<BaseCallModel<User>>() {
//                                            @Override
//                                            public void onNext(BaseCallModel<User> userBaseCallModel) {
//                                                subscriber.onNext(userBaseCallModel);
//                                            }
//
//                                            @Override
//                                            public void onError(Throwable t) {
//                                                subscriber.onError(t);
//                                            }
//
//                                            @Override
//                                            public void onComplete() {
//                                                subscriber.onComplete();
//                                            }
//                                        });
//                            }
//                        }, BackpressureStrategy.BUFFER);
//                    }
//                });
    }

    @Override
    public Flowable<BaseCallModel> postUserData(int userId, RequestBody body) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel> e) throws Exception {
                    HttpProtocol.getApi()
                            .postUserData(userId, body)
                            .subscribe(new NetFlowableOnSubscribe<BaseCallModel>(e));
            }
        },BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<BaseCallModel> getIP(User user) {
        return Flowable.create(new FlowableOnSubscribe<IPModel>() {
            @Override
            public void subscribe(FlowableEmitter<IPModel> e) throws Exception {
              HttpProtocol.getApi()
                      .getIP(APP.USER_AGENT)
                      .subscribe(new NetFlowableOnSubscribe<IPModel>(e));
            }
        },BackpressureStrategy.BUFFER)
                .flatMap(new Function<IPModel, Publisher<BaseCallModel>>() {
                    @Override
                    public Publisher<BaseCallModel> apply(IPModel ipModel) throws Exception {
                        return Flowable.create(new FlowableOnSubscribe<BaseCallModel>() {
                            @Override
                            public void subscribe(FlowableEmitter<BaseCallModel> e) throws Exception {
                                if (ipModel.getData()!=null) {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("lastLoginIp", ipModel.getData().getIp());
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
                                    String format = sdf.format(System.currentTimeMillis());
                                    jsonObject.put("lastLoginTime", format);
                                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject
                                            .toString());
                                    HttpProtocol.getApi()
                                            .postUserData(user.getId(),requestBody)
                                            .subscribe(new NetFlowableOnSubscribe<BaseCallModel>(e));
                                }else {
                                    e.onError(new Throwable("获取ip失败"));
                                    e.onComplete();
                                }
                            }
                        },BackpressureStrategy.BUFFER);
                    }
                });
    }
}
