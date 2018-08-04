package com.shuzhengit.zhixin.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/27 20:11
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class QuestionAnswer implements Serializable {

    /**
     * agreeCount : 0
     * commentCount : 10
     * commentSwitch : 1
     * content : 测试评论1111111111111
     * deleted : 0
     * disagreeCount : 0
     * gmtCreate : null
     * gmtModified : null
     * id : 1
     * questionId : 1
     * readCount : 7
     * replayTime : 2017-09-21 10:03:41
     * replayUserAvatarUrl : http://www.runoob.com/wp-content/uploads/2014/06/kittens.jpg
     * replayUserId : 1
     * replayUserNickname : chf
     * status : 0
     */

    private int agreeCount;
    private int commentCount;
    private int commentSwitch;
    private String content;
    private int deleted;
    private int disagreeCount;
    private Object gmtCreate;
    private Object gmtModified;
    private int id;
    private int questionId;
    private int readCount;
    private String replayTime;
    private String replayUserAvatarUrl;
    private int replayUserId;
    private String replayUserNickname;
    private int status;
    //0 未点赞  1 支持 2 反对
    private int voteType;
    private List<DocumentPicture> allPic;

    @Override
    public String toString() {
        return "QuestionAnswer{" +
                "agreeCount=" + agreeCount +
                ", commentCount=" + commentCount +
                ", commentSwitch=" + commentSwitch +
                ", content='" + content + '\'' +
                ", deleted=" + deleted +
                ", disagreeCount=" + disagreeCount +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", id=" + id +
                ", questionId=" + questionId +
                ", readCount=" + readCount +
                ", replayTime='" + replayTime + '\'' +
                ", replayUserAvatarUrl='" + replayUserAvatarUrl + '\'' +
                ", replayUserId=" + replayUserId +
                ", replayUserNickname='" + replayUserNickname + '\'' +
                ", status=" + status +
                ", voteType=" + voteType +
                ", allPic=" + allPic +
                '}';
    }

    public int getVoteType() {
        return voteType;
    }

    public void setVoteType(int voteType) {
        this.voteType = voteType;
    }

    public int getAgreeCount() {
        return agreeCount;
    }

    public void setAgreeCount(int agreeCount) {
        this.agreeCount = agreeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getCommentSwitch() {
        return commentSwitch;
    }

    public void setCommentSwitch(int commentSwitch) {
        this.commentSwitch = commentSwitch;
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

    public int getDisagreeCount() {
        return disagreeCount;
    }

    public void setDisagreeCount(int disagreeCount) {
        this.disagreeCount = disagreeCount;
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

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
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

    public List<DocumentPicture> getAllPic() {
        return allPic;
    }

    public void setAllPic(List<DocumentPicture> allPic) {
        this.allPic = allPic;
    }
}
