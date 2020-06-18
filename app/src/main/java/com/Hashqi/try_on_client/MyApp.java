package com.Hashqi.try_on_client;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import com.Hashqi.try_on_client.Page.homePage.GoodItem;
import com.Hashqi.try_on_client.Page.shopsPage.ShopItem;
import com.Hashqi.try_on_client.ServiceItem.GoodInfo;
import com.Hashqi.try_on_client.ServiceItem.HomePageInfo;
import com.Hashqi.try_on_client.ServiceItem.UserInfo;

public class MyApp extends Application {
    private UserInfo userInfo = null;
    private List<HomePageInfo> homepageLeftInfo = null;
    private List<HomePageInfo> homepageRightInfo = null;
    private List<ShopItem> shopItemList = null;
    private List<GoodInfo> historyInfo = new ArrayList<GoodInfo>();

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<HomePageInfo> getHomepageLeftInfo() {
        return homepageLeftInfo;
    }

    public void setHomepageLeftInfo(List<HomePageInfo> homepageLeftInfo) {
        this.homepageLeftInfo = homepageLeftInfo;
    }
    public void insertHomepageLeftInfo(HomePageInfo homepageLeftInfo) {
        if(this.homepageLeftInfo == null)
            this.homepageLeftInfo = new ArrayList<HomePageInfo>();
        this.homepageLeftInfo.add(homepageLeftInfo);
    }

    public List<HomePageInfo> getHomepageRightInfo() {
        return homepageRightInfo;
    }

    public void setHomepageRightInfo(List<HomePageInfo> homepageRightInfo) {
        this.homepageRightInfo = homepageRightInfo;
    }
    public void insertHomepageRightInfo(HomePageInfo homepageRightInfo) {
        if(this.homepageRightInfo == null)
            this.homepageRightInfo = new ArrayList<HomePageInfo>();
        this.homepageRightInfo.add(homepageRightInfo);
    }

    public void setHistoryInfo(List<GoodInfo> historyInfo) {
        this.historyInfo = historyInfo;
    }

    public List<GoodInfo> getHistoryInfo() {
        return historyInfo;
    }

    public void setHistoryInfo(GoodInfo historyInfo) {
        this.historyInfo.add(historyInfo);
    }

    public void insertHistoryInfo(GoodInfo historyInfo) {
        if(this.historyInfo == null)
            this.historyInfo = new ArrayList<GoodInfo>();
        this.historyInfo.add(historyInfo);
    }

    public List<ShopItem> getShopItemList() {
        return shopItemList;
    }

    public void setShopItemList(List<ShopItem> shopItemList) {
        this.shopItemList = shopItemList;
    }
}
