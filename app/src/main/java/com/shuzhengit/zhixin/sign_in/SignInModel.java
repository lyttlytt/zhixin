package com.shuzhengit.zhixin.sign_in;

import com.library.bean.BaseCallModel;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2018/1/29 10:51
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class SignInModel implements SignInContract.Model {
    @Override
    public Flowable<BaseCallModel> morningSignIn(int userId) {
        return null;
    }

    @Override
    public Flowable<BaseCallModel> nightSignIn(int userId) {
        return null;
    }
}
