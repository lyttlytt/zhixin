package com.shuzhengit.zhixin.bean;

import java.io.Serializable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/1 17:48
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class AuthoritiesBean implements Serializable{
    private String authority;

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
