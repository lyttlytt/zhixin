package com.shuzhengit.zhixin.bean;

import java.io.Serializable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/18 10:22
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class Collect implements Serializable{
    private Integer documentId;
    private Integer memberId;

    public Collect(int documentId, int memberId) {
        this.documentId = documentId;
        this.memberId = memberId;
    }
}
