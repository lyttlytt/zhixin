package com.shuzhengit.zhixin.bean;

import java.io.Serializable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/12/4 16:33
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class ModifyBarInfo implements Serializable{

    private String barImage;
    private int categoryId;
    private int deleted;
    private String description;
    private String gmtCreate;
    private String gmtModified;
    private int id;
    private String profession;
//    status 0 审核中 1通过 2 未通过
    private int status;
    private int userId;
    private String welcomeTitle;
    private String auditResult;
    private String columnTitle;

    public String getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(String auditResult) {
        this.auditResult = auditResult;
    }

    public String getColumnTitle() {
        return columnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }

    public String getBarImage() {
        return barImage;
    }

    public void setBarImage(String barImage) {
        this.barImage = barImage;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getWelcomeTitle() {
        return welcomeTitle;
    }

    public void setWelcomeTitle(String welcomeTitle) {
        this.welcomeTitle = welcomeTitle;
    }

    @Override
    public String toString() {
        return "ModifyBarInfo{" +
                "barImage='" + barImage + '\'' +
                ", categoryId=" + categoryId +
                ", deleted=" + deleted +
                ", description='" + description + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModified='" + gmtModified + '\'' +
                ", id=" + id +
                ", profession='" + profession + '\'' +
                ", status=" + status +
                ", userId=" + userId +
                ", welcomeTitle='" + welcomeTitle + '\'' +
                ", auditResult='" + auditResult + '\'' +
                ", columnTitle='" + columnTitle + '\'' +
                '}';
    }
}
