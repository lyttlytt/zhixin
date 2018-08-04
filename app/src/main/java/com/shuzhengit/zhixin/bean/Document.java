package com.shuzhengit.zhixin.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/14 17:24
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class Document implements Serializable {

    /**
     * abs : string
     * auditUserId : 0
     * authorId : 2
     * authorName : string
     * autidStatus : 1
     * commentCount : 0
     * commentFlag : 1
     * content : string
     * deleted : 1
     * firstPicPath : string
     * gmtCreate : 2017-08-04 16:35:54
     * gmtModified : 2017-08-07 13:55:13
     * id : 1
     * keyword : string
     * likeCount : 0
     * publishStatus : 1
     * publishTime : 2017-08-04 16:35:32
     * publisherId : 0
     * publisherName : string
     * showTime : 2017-08-04 16:35:32
     * subTitle : string
     * suorceId : 0
     * suorceName : string
     * title : string
     * topFlag : 1
     * type : 1
     */

    private String abs;
    private int auditUserId;
    private int authorId;
    private String authorName;
    private int autidStatus;
    private int commentCount;
    private int commentFlag;
    private String content;
    private int deleted;
    private List<DocumentPicture> firstPicPath;
    private String gmtCreate;
    private String gmtModified;
    private int id;
    private String keyword;
    private int likeCount;
    private int publishStatus;
    private String publishTime;
    private int publisherId;
    private String publisherName;
    private String showTime;
    private String subTitle;
    private int sourceId;
    private String sourceName;
    private String title;
    private int topFlag;
    private int type;
    private int isFavorite;
    private int isLike;
    private List<DocumentPicture> allPic;
    private int readCount;
    private String elcIndexId;
    private int isGrap;

    public int getIsGrap() {
        return isGrap;
    }

    public void setIsGrap(int isGrap) {
        this.isGrap = isGrap;
    }

    public String getElcIndexId() {
        return elcIndexId;
    }

    public void setElcIndexId(String elcIndexId) {
        this.elcIndexId = elcIndexId;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getAbs() {
        return abs;
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public int getAuditUserId() {
        return auditUserId;
    }

    public void setAuditUserId(int auditUserId) {
        this.auditUserId = auditUserId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getAutidStatus() {
        return autidStatus;
    }

    public void setAutidStatus(int autidStatus) {
        this.autidStatus = autidStatus;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getCommentFlag() {
        return commentFlag;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public void setCommentFlag(int commentFlag) {
        this.commentFlag = commentFlag;
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

    public List<DocumentPicture> getFirstPicPath() {
        return firstPicPath;
    }

    public void setFirstPicPath(List<DocumentPicture> firstPicPath) {
        this.firstPicPath = firstPicPath;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(int publishStatus) {
        this.publishStatus = publishStatus;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTopFlag() {
        return topFlag;
    }

    public void setTopFlag(int topFlag) {
        this.topFlag = topFlag;
    }

    public int getType() {
        return type;
    }

    public List<DocumentPicture> getAllPic() {
        return allPic;
    }

    public void setAllPic(List<DocumentPicture> allPic) {
        this.allPic = allPic;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Document{" +
                "abs='" + abs + '\'' +
                ", auditUserId=" + auditUserId +
                ", authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", autidStatus=" + autidStatus +
                ", commentCount=" + commentCount +
                ", commentFlag=" + commentFlag +
                ", content='" + content + '\'' +
                ", deleted=" + deleted +
                ", firstPicPath='" + firstPicPath + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModified='" + gmtModified + '\'' +
                ", id=" + id +
                ", keyword='" + keyword + '\'' +
                ", likeCount=" + likeCount +
                ", publishStatus=" + publishStatus +
                ", publishTime='" + publishTime + '\'' +
                ", publisherId=" + publisherId +
                ", publisherName='" + publisherName + '\'' +
                ", showTime='" + showTime + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", sourceId=" + sourceId +
                ", sourceName='" + sourceName + '\'' +
                ", title='" + title + '\'' +
                ", topFlag=" + topFlag +
                ", type=" + type +
                ", isFavorite=" + isFavorite +
                ", isLike=" + isLike +
                ", allPic=" + allPic +
                '}';
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
