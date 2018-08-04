package com.shuzhengit.zhixin.bean;

import java.io.Serializable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/25 16:23
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class Dynamic implements Serializable {


    /**
     * commentCount : 2
     * commentId : 6
     * content : 测试4
     * contentId : 5
     * deleted : 0
     * gmtCreate : 2017-09-25 11:40:30
     * gmtModified : 2017-09-25 11:40:30
     * id : 1
     * likeCount : 2
     * memberId : 1
     * pic : http://www.runoob.com/wp-content/uploads/2014/06/kittens.jpg
     * title : 测试4
     * type : 0
     */

    private int commentCount;
    private int commentId;
    private String content;
    private int contentId;
    private int deleted;
    private String gmtCreate;
    private String gmtModified;
    private int id;
    private int likeCount;
    private int memberId;
    private String pic;
    private String title;
    private int type;
    private String userAvatar;
    private String userName;
    private String avatarUrl;
    private String elcIndexId;

    public String getElcIndexId() {
        return elcIndexId;
    }

    public void setElcIndexId(String elcIndexId) {
        this.elcIndexId = elcIndexId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return "Dynamic{" +
                "commentCount=" + commentCount +
                ", commentId=" + commentId +
                ", content='" + content + '\'' +
                ", contentId=" + contentId +
                ", deleted=" + deleted +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModified='" + gmtModified + '\'' +
                ", id=" + id +
                ", likeCount=" + likeCount +
                ", memberId=" + memberId +
                ", pic='" + pic + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", userAvatar='" + userAvatar + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
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

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
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

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
