package com.shuzhengit.zhixin.bean;

import java.io.Serializable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/15 15:07
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class DocumentPicture implements Serializable{
//    private String path;
//    private String size;
//    private String type;
    private String ref;
    private String src;
    private String alt;
    private String pixel;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getPixel() {
        return pixel;
    }

    public void setPixel(String pixel) {
        this.pixel = pixel;
    }
}
