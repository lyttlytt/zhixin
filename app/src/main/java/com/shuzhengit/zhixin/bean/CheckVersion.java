package com.shuzhengit.zhixin.bean;

import java.io.Serializable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/25 17:52
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:检查版本更新
 */

public class CheckVersion implements Serializable {

    /**
     * id : 3
     * versionCode : 3
     * versionName : v0.0.3
     * downloadUri : http://www.zhixin.com/download/app
     * description : 测试第3版本
     * gmtCreate : 1506331904000
     * gmtModified : null
     * latestTag : 1
     * deleted : 0
     */

    private int id;
    private int versionCode;
    private String versionName;
    private String downloadUri;
    private String description;
    private long gmtCreate;
    private Object gmtModified;
    private int latestTag;
    private int deleted;

    @Override
    public String toString() {
        return "CheckVersion{" +
                "id=" + id +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", downloadUri='" + downloadUri + '\'' +
                ", description='" + description + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", latestTag=" + latestTag +
                ", deleted=" + deleted +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Object getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Object gmtModified) {
        this.gmtModified = gmtModified;
    }

    public int getLatestTag() {
        return latestTag;
    }

    public void setLatestTag(int latestTag) {
        this.latestTag = latestTag;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
}
