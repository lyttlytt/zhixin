package com.shuzhengit.zhixin.bean;

import java.io.Serializable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/28 16:04
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class AnswerComment implements Serializable {

    /**
     * agreeCount : 0
     * answerId : 1
     * commentId : 0
     * content : 测试评论1
     * deleted : 0
     * gmtCreate : null
     * gmtModified : null
     * id : 1
     * isUp : 0
     * questionId : 1
     * replayCount : 0
     * replayTime : 2017-09-21 14:32:07
     * replayUserAvatarUrl : http://www.runoob.com/wp-content/uploads/2014/06/kittens.jpg
     * replayUserId : 1
     * replayUserNickname : chf
     * status : 0
     */

    private int agreeCount;
    private int answerId;
    private int commentId;
    private String content;
    private int deleted;
    private Object gmtCreate;
    private Object gmtModified;
    private int id;
    private int isUp;
    private int questionId;
    private int replayCount;
    private String replayTime;
    private String replayUserAvatarUrl;
    private int replayUserId;
    private String replayUserNickname;
    private int status;

    @Override
    public String toString() {
        return "AnswerComment{" +
                "agreeCount=" + agreeCount +
                ", answerId=" + answerId +
                ", commentId=" + commentId +
                ", content='" + content + '\'' +
                ", deleted=" + deleted +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", id=" + id +
                ", isUp=" + isUp +
                ", questionId=" + questionId +
                ", replayCount=" + replayCount +
                ", replayTime='" + replayTime + '\'' +
                ", replayUserAvatarUrl='" + replayUserAvatarUrl + '\'' +
                ", replayUserId=" + replayUserId +
                ", replayUserNickname='" + replayUserNickname + '\'' +
                ", status=" + status +
                '}';
    }

    public int getAgreeCount() {
        return agreeCount;
    }

    public void setAgreeCount(int agreeCount) {
        this.agreeCount = agreeCount;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getIsUp() {
        return isUp;
    }

    public void setIsUp(int isUp) {
        this.isUp = isUp;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getReplayCount() {
        return replayCount;
    }

    public void setReplayCount(int replayCount) {
        this.replayCount = replayCount;
    }

    public String getReplayTime() {
        return replayTime;
    }

    public void setReplayTime(String replayTime) {
        this.replayTime = replayTime;
    }

    public String getReplayUserAvatarUrl() {
        return replayUserAvatarUrl;
    }

    public void setReplayUserAvatarUrl(String replayUserAvatarUrl) {
        this.replayUserAvatarUrl = replayUserAvatarUrl;
    }

    public int getReplayUserId() {
        return replayUserId;
    }

    public void setReplayUserId(int replayUserId) {
        this.replayUserId = replayUserId;
    }

    public String getReplayUserNickname() {
        return replayUserNickname;
    }

    public void setReplayUserNickname(String replayUserNickname) {
        this.replayUserNickname = replayUserNickname;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
