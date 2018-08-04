package com.shuzhengit.zhixin.bean;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/11 08:53
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:登录成功后返回的信息
 */

public class Login {
    private User mUser;
    private String authorization;

    @Override
    public String toString() {
        return "Login{" +
                "mUser=" + mUser +
                ", authorization='" + authorization + '\'' +
                '}';
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public User getUser() {

        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }
}
