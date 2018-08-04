package com.shuzhengit.zhixin.bean;

import java.io.Serializable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/14 15:39
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class AskWithReply implements Serializable {
    private int answerCount;
    private int answerId;
    private int answerStatus;
    private int answerUserId;
    private String avatarUrl;
    private int categoryId;
    private String content;
    private int cost;
    private String description;
    private String gmtCreate;
    private int id;
    private int imageType;
    private int isLike;
    private int likeCount;
    private String nickName;
    private String replayNickName;
    private String replayavatarUrl;
    private int userId;
    private String profession;
    private int isSupport;
    private int readCount;
    private int replayUserId;


    @Override
    public String toString() {
        return "AskWithReply{" +
                "answerCount=" + answerCount +
                ", answerId=" + answerId +
                ", answerStatus=" + answerStatus +
                ", answerUserId=" + answerUserId +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", categoryId=" + categoryId +
                ", content='" + content + '\'' +
                ", cost=" + cost +
                ", description='" + description + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", id=" + id +
                ", imageType=" + imageType +
                ", isLike=" + isLike +
                ", likeCount=" + likeCount +
                ", nickName='" + nickName + '\'' +
                ", replayNickName='" + replayNickName + '\'' +
                ", replayavatarUrl='" + replayavatarUrl + '\'' +
                ", userId=" + userId +
                ", profession='" + profession + '\'' +
                ", isSupport=" + isSupport +
                ", readCount=" + readCount +
                ", replyUserId=" + replayUserId +
                '}';
    }

    public int getReplayUserId() {
        return replayUserId;
    }

    public void setReplayUserId(int replayUserId) {
        this.replayUserId = replayUserId;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public int getIsSupport() {
        return isSupport;
    }

    public void setIsSupport(int isSupport) {
        this.isSupport = isSupport;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getAnswerStatus() {
        return answerStatus;
    }

    public void setAnswerStatus(int answerStatus) {
        this.answerStatus = answerStatus;
    }

    public int getAnswerUserId() {
        return answerUserId;
    }

    public void setAnswerUserId(int answerUserId) {
        this.answerUserId = answerUserId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
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

    public int getImageType() {
        return imageType;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getReplayNickName() {
        return replayNickName;
    }

    public void setReplayNickName(String replayNickName) {
        this.replayNickName = replayNickName;
    }

    public String getReplayavatarUrl() {
        return replayavatarUrl;
    }

    public void setReplayavatarUrl(String replayavatarUrl) {
        this.replayavatarUrl = replayavatarUrl;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
