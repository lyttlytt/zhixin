package com.shuzhengit.zhixin.bean;

import java.io.Serializable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/17 12:49
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class ApplyAdmin implements Serializable{
    private int answerCount;
    private int askCount;
    private String auditResult;
    private String avatarUrl;

    private String barImage;
    private int categoryId;
    private String columnTitle;
    private String description;
    private String gmtCreate;
    private int id;
    private int likeCount;
    private String profession;
    private String nickName;

    private int readCount;
//    status 0 审核中 1通过 2 未通过
    private int status;
    private int userId;
    private String welcomeTitle;

    @Override
    public String toString() {
        return "ApplyAdmin{" +
                "answerCount=" + answerCount +
                ", askCount=" + askCount +
                ", auditResult='" + auditResult + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", barImage='" + barImage + '\'' +
                ", categoryId=" + categoryId +
                ", columnTitle='" + columnTitle + '\'' +
                ", description='" + description + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", id=" + id +
                ", likeCount=" + likeCount +
                ", profession='" + profession + '\'' +
                ", nickName='" + nickName + '\'' +
                ", readCount=" + readCount +
                ", status=" + status +
                ", userId=" + userId +
                ", welcomeTitle='" + welcomeTitle + '\'' +
                '}';
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public int getAskCount() {
        return askCount;
    }

    public void setAskCount(int askCount) {
        this.askCount = askCount;
    }

    public String getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(String auditResult) {
        this.auditResult = auditResult;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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

    public String getColumnTitle() {
        return columnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
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
}
