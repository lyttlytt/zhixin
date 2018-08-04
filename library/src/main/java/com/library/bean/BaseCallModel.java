package com.library.bean;

import java.io.Serializable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/26 17:12
 * E-mail:yuancongbin@gmail.com
 */

public class BaseCallModel<T> implements Serializable{
    //请求成功
    public static final int SUCCESS=200;
    //返回状态
    public boolean ok;
    //响应代码
    public int code;
    //返回信息
    public String message;
    //返回指定数据
    private T data;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "ok=" + ok +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
