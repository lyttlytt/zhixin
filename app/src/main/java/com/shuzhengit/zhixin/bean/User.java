package com.shuzhengit.zhixin.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/9 15:50
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:用户信息
 */

public class User implements Serializable{

    /**
     * accountLevel : 0
     * avatarUrl : http://q.qlogo.cn/qqapp/101414611/7CB05CB5612DB74F3BB2A688F7B416C0/100
     * balance : 0
     * birthday :
     * deleted : 0
     * email :
     * fansCount : 0
     * gender : 1
     * gmtCreate : 2017-08-26 21:56:23
     * gmtModified : 2017-08-26 21:56:23
     * id : 7
     * lastLoginIp :
     * lastLoginTime : null
     * mobile :
     * name :
     * nickname : 被风吹走的冤大头
     * password :
     * qqOpenid : 7CB05CB5612DB74F3BB2A688F7B416C0
     * registerIp :
     * roles : []
     * salt :
     * schoolId : 0
     * sinaOpenid :
     * status : 1
     * totalScore : 0
     * totalTopup : 0
     * type : 3
     * wxOpenid :
     */

    private int accountLevel;
    private String avatarUrl;
    private int balance;
    private String birthday;
    private int deleted;
    private String email;
    private int fansCount;
    private int gender;
    private String gmtCreate;
    private String gmtModified;
    private int id;
    private String lastLoginIp;
    private Object lastLoginTime;
    private String mobile;
    private String name;
    private String nickname;
    private String password;
    private String qqOpenid;
    private String registerIp;
    private String salt;
    private int schoolId;
    private String sinaOpenid;
    private int status;
    private int totalScore;
    private int totalTopup;
    private int type;
    private String wxOpenid;
    private List<String> roles;
    private String schoolName;
    private int followingCount;
    private int followed;

    @Override
    public String toString() {
        return "User{" +
                "accountLevel=" + accountLevel +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", balance=" + balance +
                ", birthday='" + birthday + '\'' +
                ", deleted=" + deleted +
                ", email='" + email + '\'' +
                ", fansCount=" + fansCount +
                ", gender=" + gender +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModified='" + gmtModified + '\'' +
                ", id=" + id +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", mobile='" + mobile + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", qqOpenid='" + qqOpenid + '\'' +
                ", registerIp='" + registerIp + '\'' +
                ", salt='" + salt + '\'' +
                ", schoolId=" + schoolId +
                ", sinaOpenid='" + sinaOpenid + '\'' +
                ", status=" + status +
                ", totalScore=" + totalScore +
                ", totalTopup=" + totalTopup +
                ", type=" + type +
                ", wxOpenid='" + wxOpenid + '\'' +
                ", roles=" + roles +
                ", schoolName='" + schoolName + '\'' +
                ", followingCount=" + followingCount +
                ", followed=" + followed +
                '}';
    }

    public int getFollowed() {
        return followed;
    }

    public void setFollowed(int followed) {
        this.followed = followed;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getAccountLevel() {
        return accountLevel;
    }

    public void setAccountLevel(int accountLevel) {
        this.accountLevel = accountLevel;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
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

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Object getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Object lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQqOpenid() {
        return qqOpenid;
    }

    public void setQqOpenid(String qqOpenid) {
        this.qqOpenid = qqOpenid;
    }

    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getSinaOpenid() {
        return sinaOpenid;
    }

    public void setSinaOpenid(String sinaOpenid) {
        this.sinaOpenid = sinaOpenid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getTotalTopup() {
        return totalTopup;
    }

    public void setTotalTopup(int totalTopup) {
        this.totalTopup = totalTopup;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWxOpenid() {
        return wxOpenid;
    }

    public void setWxOpenid(String wxOpenid) {
        this.wxOpenid = wxOpenid;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

}
