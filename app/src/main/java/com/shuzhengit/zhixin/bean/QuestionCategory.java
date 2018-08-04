package com.shuzhengit.zhixin.bean;

import java.io.Serializable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/25 17:25
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:问答栏目
 */

public class QuestionCategory implements Serializable{

    /**
     * deleted : 0
     * gmtCreate : 2017-09-22 15:16:30
     * gmtModified : 2017-09-22 15:16:30
     * id : 6
     * name : 社会
     */

    private int deleted;
    private String gmtCreate;
    private String gmtModified;
    private int id;
    private String name;

    @Override
    public String toString() {
        return "QuestionCategory{" +
                "deleted=" + deleted +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModified='" + gmtModified + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
