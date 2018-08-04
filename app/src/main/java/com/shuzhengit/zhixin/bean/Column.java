package com.shuzhengit.zhixin.bean;

import java.io.Serializable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/21 09:39
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class Column implements Serializable {

    /**
     * code : 1111
     * columnAuthorId : 1
     * columnAuthorName : 2222
     * columnTitle : 3333
     * deleted : 0
     * description : 33333333333333333333
     * gmtCreate : 2017-08-07 16:04:51
     * gmtModified : 2017-08-07 16:04:51
     * id : 1
     * isDefaultLike : 1
     * level : 1
     * orderNo : 1
     * upperColumnId : 0
     * upperColumnTitle : 4444444
     */

    private String code;
    private Integer columnAuthorId;
    private String columnAuthorName;
    private String columnTitle;
    private Integer deleted;
    private String description;
    private String gmtCreate;
    private String gmtModified;
    private Integer id;
    private Integer isDefaultLike;
    private Integer level;
    private Integer orderNo;
    private Integer upperColumnId;
    private String upperColumnTitle;

    public Integer getModifiable() {
        return modifiable;
    }

    public void setModifiable(Integer modifiable) {
        this.modifiable = modifiable;
    }

    private Integer modifiable;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getColumnAuthorId() {
        return columnAuthorId;
    }

    public void setColumnAuthorId(Integer columnAuthorId) {
        this.columnAuthorId = columnAuthorId;
    }

    public String getColumnAuthorName() {
        return columnAuthorName;
    }

    public void setColumnAuthorName(String columnAuthorName) {
        this.columnAuthorName = columnAuthorName;
    }

    public String getColumnTitle() {
        return columnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsDefaultLike() {
        return isDefaultLike;
    }

    public void setIsDefaultLike(Integer isDefaultLike) {
        this.isDefaultLike = isDefaultLike;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getUpperColumnId() {
        return upperColumnId;
    }

    public void setUpperColumnId(Integer upperColumnId) {
        this.upperColumnId = upperColumnId;
    }

    public String getUpperColumnTitle() {
        return upperColumnTitle;
    }

    public void setUpperColumnTitle(String upperColumnTitle) {
        this.upperColumnTitle = upperColumnTitle;
    }

    @Override
    public String toString() {
        return "Column{" +
                "code='" + code + '\'' +
                ", columnAuthorId=" + columnAuthorId +
                ", columnAuthorName='" + columnAuthorName + '\'' +
                ", columnTitle='" + columnTitle + '\'' +
                ", deleted=" + deleted +
                ", description='" + description + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModified='" + gmtModified + '\'' +
                ", id=" + id +
                ", isDefaultLike=" + isDefaultLike +
                ", level=" + level +
                ", orderNo=" + orderNo +
                ", upperColumnId=" + upperColumnId +
                ", upperColumnTitle='" + upperColumnTitle + '\'' +
                ", modifiable=" + modifiable +
                '}';
    }
}
