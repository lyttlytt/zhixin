package com.shuzhengit.zhixin.bean;

import java.io.Serializable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/27 16:14
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class UploadImg implements Serializable {
    private int id;
    private String url;
    private long createData;

    @Override
    public String toString() {
        return "UploadImg{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", createData=" + createData +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getCreateData() {
        return createData;
    }

    public void setCreateData(long createData) {
        this.createData = createData;
    }
}
