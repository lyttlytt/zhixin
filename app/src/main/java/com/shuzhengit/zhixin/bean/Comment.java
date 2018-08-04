package com.shuzhengit.zhixin.bean;

import java.io.Serializable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/15 15:42
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class Comment implements Serializable {
//    {
//        "data": [
//        {
//            "commentContent": "评论文章",
    //评论用户的id
//                "commentId": 0,
    //评论文章这个为空
//                "commentName": "",
    //标识属于那一级的评论/文章的id
//                "upperCommentId": 0
//        },
//        {
    //给评论文章的评论 进行二次评论
//            "commentContent": "给评论文章的评论回复",
    //用户ID
//                "commentId": 0,
    //二次评论这个为空
//                "commentName": "",
    //标识属于那一级的评论/文章的id
//                "upperCommentId": 1
//        },
//        {
    //给评论文章的评论中的评论进行三次评论
//            "commentContent": "给评论文章的评论回复进行回复",
    //用户id
//                "commentId": 0,
    //评论文章的评论中的评论人昵称
//                "commentName": "评论文章的评论回复人",
    //标识属于那一级的评论/评论文章的评论中的评论id
//                "upperCommentId": 2
//        }
//    ]
//    }

    /**
     * commentContent : 测试
     * commentCount : 0
     * commentId : 0
     * commentName : string
     * commentStatus : 0
     * deleted : 0
     * ducomentId : 0
     * gmtCreate : 2017-08-15 15:33:53
     * gmtModified : 2017-08-15 15:33:53
     * id : 2
     * likeCount : 0
     * upperCommentId : 0
     */
    private String avatar;
    private Integer isLike;
    private String commentContent;
    private Integer commentCount;
    private Integer commentId;
    private String commentName;
    private Integer commentStatus;
    private Integer deleted;
    private Integer documentId;
    private String gmtCreate;
    private String gmtModified;
    private Integer id;
    private Integer likeCount;
    private Integer upperCommentId;
    private String path;

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getCommentName() {
        return commentName;
    }

    public void setCommentName(String commentName) {
        this.commentName = commentName;
    }

    public Integer getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(Integer commentStatus) {
        this.commentStatus = commentStatus;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getUpperCommentId() {
        return upperCommentId;
    }

    public void setUpperCommentId(Integer upperCommentId) {
        this.upperCommentId = upperCommentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getIsLike() {
        return isLike;
    }

    public void setIsLike(Integer isLike) {
        this.isLike = isLike;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "avatar='" + avatar + '\'' +
                ", isLike=" + isLike +
                ", commentContent='" + commentContent + '\'' +
                ", commentCount=" + commentCount +
                ", commentId=" + commentId +
                ", commentName='" + commentName + '\'' +
                ", commentStatus=" + commentStatus +
                ", deleted=" + deleted +
                ", documentId=" + documentId +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModified='" + gmtModified + '\'' +
                ", id=" + id +
                ", likeCount=" + likeCount +
                ", upperCommentId=" + upperCommentId +
                ", path='" + path + '\'' +
                '}';
    }
}
