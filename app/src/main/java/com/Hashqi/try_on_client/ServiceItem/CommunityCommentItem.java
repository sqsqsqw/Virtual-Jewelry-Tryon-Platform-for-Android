package com.Hashqi.try_on_client.ServiceItem;

public class CommunityCommentItem {

    private Long communityCommentID;
    private Long userID;
    private String userAva;
    private String communityCommentContext;
    private String userName;

    public Long getCommunityCommentID() {
        return communityCommentID;
    }

    public void setCommunityCommentID(Long communityCommentID) {
        this.communityCommentID = communityCommentID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getUserAva() {
        return userAva;
    }

    public void setUserAva(String userAva) {
        this.userAva = userAva;
    }

    public String getCommunityCommentContext() {
        return communityCommentContext;
    }

    public void setCommunityCommentContext(String communityCommentContext) {
        this.communityCommentContext = communityCommentContext;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
