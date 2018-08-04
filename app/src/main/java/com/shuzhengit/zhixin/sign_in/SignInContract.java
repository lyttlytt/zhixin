package com.shuzhengit.zhixin.sign_in;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2018/1/29 10:02
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class SignInContract {
    interface View extends BaseView<SignInContract.Presenter> {
        /**
         * 早起打卡成功后的回调
         */
        void morningSignInSuccess();

        /**
         * 早睡打卡成功后的回调
         */
        void nightSignInSuccess();
    }

    static abstract class Presenter extends BasePresenter<SignInContract.View> {

        public Presenter(View view) {
            super(view);
            view.setPresenter(this);
        }

        /**
         * 向服务器请求早起打卡
         * @param userId 用户id
         */
        abstract void requestMorningSignIn(int userId);

        /**
         * 向服务器请求早睡打卡
         * @param userId 用户id
         */
        abstract void requestNightSignIn(int userId);
    }

    interface Model {
        Flowable<BaseCallModel> morningSignIn(int userId);

        Flowable<BaseCallModel> nightSignIn(int userId);
    }
}
