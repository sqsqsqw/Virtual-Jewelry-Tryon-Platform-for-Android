package com.Hashqi.try_on_client.ServiceItem;

public class HomePageInfo {

    private Long homepageID;            //主页id
    private String homepageTitle;      //主页标题
    private String homepageImage;      //主页图片
    private Long homepageGoodId;        //主页地址
    private String homepageState;      //主页状态

    public HomePageInfo(Long homepageID, String homepageTitle, String homepageImage, Long homepageGoodId, String homepageState) {
        this.homepageID = homepageID;
        this.homepageTitle = homepageTitle;
        this.homepageImage = homepageImage;
        this.homepageGoodId = homepageGoodId;
        this.homepageState = homepageState;
    }

    public HomePageInfo() {
    }

    public Long getHomepageID() {
        return homepageID;
    }

    public void setHomepageID(Long homepageID) {
        this.homepageID = homepageID;
    }

    public String getHomepageTitle() {
        return homepageTitle;
    }

    public void setHomepageTitle(String homepageTitle) {
        this.homepageTitle = homepageTitle;
    }

    public String getHomepageImage() {
        return homepageImage;
    }

    public void setHomepageImage(String homepageImage) {
        this.homepageImage = homepageImage;
    }

    public Long getHomepageGoodId() {
        return homepageGoodId;
    }

    public void setHomepageGoodId(Long homepageGoodId) {
        this.homepageGoodId = homepageGoodId;
    }

    public String getHomepageState() {
        return homepageState;
    }

    public void setHomepageState(String homepageState) {
        this.homepageState = homepageState;
    }
}
