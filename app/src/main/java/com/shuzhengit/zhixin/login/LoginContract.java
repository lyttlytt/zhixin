package com.shuzhengit.zhixin.login;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.User;

import io.reactivex.Flowable;
import okhttp3.RequestBody;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/11 08:45
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class LoginContract {

    public interface View extends BaseView<LoginContract.Presenter> {
        /**
         * 登录成功后的回调
         */
        public void onLoginSuccess(User user);

        /**
         * 登录成功后需要完善资料
         */
        public void onLoginSuccessNeedEditUserInfo();
    }

    public static abstract class Presenter extends BasePresenter<LoginContract.View> {

        public Presenter(View view) {
            super(view);
            mView.setPresenter(this);
        }

        /**
         * 正常手机/密码登录
         * @param username 账号／三方openid
         * @param password 密码
         */
//        /**
//         * @param grantType   password
//         * @param username    账号/三方openid
//         * @param password    密码
//         * @param openid      三方openid
//         * @param accessToken 三方授权accessToken
//         * @param authPfType  password/qq/weixin
//         */
        public abstract void normalLogin(String username, String password);
//        public abstract void normalLogin(String grantType, String username, String password, String openid, String
//                accessToken, String authPfType);

        /**
//         * @param grantType   qq/weixin
         * @param openid      openid
         * @param accessToken 三方登录授权accessToken
         * @param authPfType  qq/weixin
         */
        public abstract void socialLogin(String openid,String username,String password,String
                accessToken, String authPfType);

        /**
         * 登录成功后发送用户数据
         */
        public abstract void postUserData(User user);
    }

    public interface Model {
        /**
         * 正常登录走这个接口
         *
         * @param username    手机号码
         * @param password 密码
         * @return
         */
        public Flowable<BaseCallModel<User>> doNormalLogin(String username, String password);

        /**
         * 三方登录走这个接口
         *
//         * @param grantType   qq/weixin
         * @param openid      openid
         * @param accessToken 三方登录授权accessToken
         * @param authPfType  qq/weixin
         */
//        public Flowable<Token> doSocialLogin(String grantType, String openid, String accessToken, String
        public Flowable<BaseCallModel<User>> doSocialLogin(String openid, String username, String password, String
                accessToken, String
                authPfType);

        /**
         * 登录成功后发送用户数据
         * @param userId  用户id
         * @param body lastLoginIp、lastLoginTime
         */
        public Flowable<BaseCallModel> postUserData(int userId, RequestBody body);


        public Flowable<BaseCallModel> getIP(User user);

    }
}
