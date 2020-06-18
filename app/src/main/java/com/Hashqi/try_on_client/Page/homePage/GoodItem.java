package com.Hashqi.try_on_client.Page.homePage;

public class GoodItem {
    private String l_imageURL,l_name;
    private String r_imageURL,r_name;

    public GoodItem() {
    }

    public GoodItem(String l_imageURL, String l_name, String r_imageURL, String r_name) {
        this.l_imageURL = l_imageURL;
        this.l_name = l_name;
        this.r_imageURL = r_imageURL;
        this.r_name = r_name;
    }

    public String getL_imageURL() {
        return l_imageURL;
    }

    public void setL_imageURL(String l_imageURL) {
        this.l_imageURL = l_imageURL;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public String getR_imageURL() {
        return r_imageURL;
    }

    public void setR_imageURL(String r_imageURL) {
        this.r_imageURL = r_imageURL;
    }

    public String getR_name() {
        return r_name;
    }

    public void setR_name(String r_name) {
        this.r_name = r_name;
    }
}
