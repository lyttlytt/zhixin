package com.shuzhengit.zhixin.login;

import com.library.bean.BaseCallModel;
import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.shuzhengit.zhixin.util.CacheUtils;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/11 09:56
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class LoginPresenter extends LoginContract.Presenter {

    private final LoginModel mLoginModel;

    public LoginPresenter(LoginContract.View view) {
        super(view);
        mLoginModel = new LoginModel();
    }

    @Override
    public void normalLogin(String username, String password) {
        mCompositeDisposable.add(
                mLoginModel.doNormalLogin(username, password)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                        .subscribeWith(new RxSubscriber<User>(mView) {
                            @Override
                            protected void _onNext(User user) {
                                postUserData(user);
//                                mView.onLoginSuccess(user);
                            }
                        })
        );

    }

    @Override
//    public void socialLogin(String grantType, String openid, String accessToken, String authPfType) {
    public void socialLogin(String openid, String username, String password, String accessToken, String authPfType) {
        mCompositeDisposable.add(
                mLoginModel.doSocialLogin(openid, username, password, accessToken, authPfType)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                        .subscribeWith(new RxSubscriber<User>() {
                            @Override
                            protected void _onNext(User user) {
                                CacheUtils.getCacheManager(APP.getInstance()).put("user", user);
//                                if (user.getSchoolId() == 0)
//                                    mView.onLoginSuccessNeedEditUserInfo();
//                                else
                                postUserData(user);
//                                    mView.onLoginSuccess(user);
                            }
                        })
        );
    }

    @Override
    public void postUserData(User user) {
        mCompositeDisposable.add(
                mLoginModel.getIP(user)
                .compose(RxSchedulersHelper.io_main())
                .subscribeWith(new RxSubscriber<BaseCallModel>() {
                    @Override
                    protected void _onNext(BaseCallModel ipModel) {
                    }

                    @Override
                    protected void _onError(String message) {
                    }

                    @Override
                    protected void _onCompleted() {
                        super._onCompleted();
                        mView.onLoginSuccess(user);
                    }
                })

        );
    }

    @Override
    public void start() {

    }


}
