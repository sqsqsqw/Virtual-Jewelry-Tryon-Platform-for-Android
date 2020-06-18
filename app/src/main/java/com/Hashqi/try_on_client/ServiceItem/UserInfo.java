package com.Hashqi.try_on_client.ServiceItem;

import java.sql.Date;

public class UserInfo {
    private Long userId;
    private String userName;
    private String userPwd;
    private String userSex;
    private String userAvatar;
    private Long userPhone;
    private String userEmail;
    private Long roleID;

    public Long getUserId() {
        return userId == null ? 0L : userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName == null ? "" : userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd == null ? "" : userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserSex() {
        return userSex == null ? "" : userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserAvatar() {
        return userAvatar == null ? "" : userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public Long getUserPhone() {
        return userPhone == null ? 0L : userPhone;
    }

    public void setUserPhone(Long userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail == null ? "" : userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getRoleID() {
        return roleID == null ? 0L : roleID;
    }

    public void setRoleID(Long roleID) {
        this.roleID = roleID;
    }

}
