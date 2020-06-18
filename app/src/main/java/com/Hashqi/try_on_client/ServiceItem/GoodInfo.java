package com.Hashqi.try_on_client.ServiceItem;

import java.util.Date;

public class GoodInfo {

    private Long goodID;                //商品id
    private Long goodClassID;         //商品类别id
    private String goodClassName;     //商品类别
    private String goodImage;          //商品照片
    private String goodDescription;    //商品描述
    private Double price;                  //价格
    private String goodCreateTime;      //商品创建时间
    private String goodUrl;            //商品售卖地址
    private String goodModelUrl;      //商品模型地址
    private Long sellerId;              //卖家id

    public Long getGoodID() {
        return goodID;
    }

    public void setGoodID(Long goodID) {
        this.goodID = goodID;
    }

    public Long getGoodClassID() {
        return goodClassID;
    }

    public void setGoodClassID(Long goodClassID) {
        this.goodClassID = goodClassID;
    }

    public String getGoodClassName() {
        return goodClassName;
    }

    public void setGoodClassName(String goodClassName) {
        this.goodClassName = goodClassName;
    }

    public String getGoodImage() {
        return goodImage;
    }

    public void setGoodImage(String goodImage) {
        this.goodImage = goodImage;
    }

    public String getGoodDescription() {
        return goodDescription;
    }

    public void setGoodDescription(String goodDescription) {
        this.goodDescription = goodDescription;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getGoodCreateTime() {
        return goodCreateTime;
    }

    public void setGoodCreateTime(String goodCreateTime) {
        this.goodCreateTime = goodCreateTime;
    }

    public String getGoodUrl() {
        return goodUrl;
    }

    public void setGoodUrl(String goodUrl) {
        this.goodUrl = goodUrl;
    }

    public String getGoodModelUrl() {
        return goodModelUrl;
    }

    public void setGoodModelUrl(String goodModelUrl) {
        this.goodModelUrl = goodModelUrl;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }
}
