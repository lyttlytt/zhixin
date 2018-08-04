package com.shuzhengit.zhixin.bean;

import java.io.Serializable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/8 14:53
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class School implements Serializable {

    private int baiduCityCode;
    private int deleted;
    private Object gmtCreate;
    private Object gmtModified;
    private int id;
    private String location;
    private String name;

    public int getBaiduCityCode() {
        return baiduCityCode;
    }

    public void setBaiduCityCode(int baiduCityCode) {
        this.baiduCityCode = baiduCityCode;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public Object getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Object gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Object getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Object gmtModified) {
        this.gmtModified = gmtModified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
