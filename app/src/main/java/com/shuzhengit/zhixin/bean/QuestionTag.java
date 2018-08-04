package com.shuzhengit.zhixin.bean;

import java.io.Serializable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/27 08:57
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class QuestionTag implements Serializable {
    private int id;
    private String title;

    @Override
    public String toString() {
        return "QuestionTag{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
