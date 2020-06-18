package com.Hashqi.try_on_client.ServiceItem;

public class CommunityDistributeItem {

    private Long communityDistributeID;
    private String communityDistributeTitle;
    private String communityDistributeContext;
    private Long userID;
    private Long goodID;

    public Long getCommunityDistributeID() {
        return communityDistributeID;
    }

    public void setCommunityDistributeID(Long communityDistributeID) {
        this.communityDistributeID = communityDistributeID;
    }

    public String getCommunityDistributeTitle() {
        return communityDistributeTitle;
    }

    public void setCommunityDistributeTitle(String communityDistributeTitle) {
        this.communityDistributeTitle = communityDistributeTitle;
    }

    public String getCommunityDistributeContext() {
        return communityDistributeContext;
    }

    public void setCommunityDistributeContext(String communityDistributeContext) {
        this.communityDistributeContext = communityDistributeContext;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getGoodID() {
        return goodID;
    }

    public void setGoodID(Long goodID) {
        this.goodID = goodID;
    }
}
