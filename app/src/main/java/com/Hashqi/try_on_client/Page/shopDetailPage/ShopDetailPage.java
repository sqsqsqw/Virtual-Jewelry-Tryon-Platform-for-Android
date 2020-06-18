package com.Hashqi.try_on_client.Page.shopDetailPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Hashqi.try_on_client.Component.MyListView;
import com.Hashqi.try_on_client.MyApp;
import com.Hashqi.try_on_client.Page.goodPage.GoodPageActivity;
import com.Hashqi.try_on_client.Page.historyPage.HistoryPageActivity;
import com.Hashqi.try_on_client.Page.homePage.HomeAdapter;
import com.Hashqi.try_on_client.Page.homePage.HomePageActivity;
import com.Hashqi.try_on_client.Page.logInPage.LogInPageActivity;
import com.Hashqi.try_on_client.Page.shopsPage.ShopItem;
import com.Hashqi.try_on_client.Page.shopsPage.ShopsPageActivity;
import com.Hashqi.try_on_client.R;
import com.Hashqi.try_on_client.ServiceItem.GoodInfo;
import com.Hashqi.try_on_client.ServiceItem.HomePageInfo;
import com.Hashqi.try_on_client.ServiceItem.UserInfo;
import com.Hashqi.try_on_client.Util.DoubleListViewManager;
import com.Hashqi.try_on_client.Util.GsonUtil;
import com.Hashqi.try_on_client.Util.HttpAttribute;
import com.Hashqi.try_on_client.Util.PropertiesManager;
import com.Hashqi.try_on_client.Util.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ShopDetailPage extends AppCompatActivity {
    private ImageView ava;
    private MyListView shopGoodsLeftList, shopGoodsRightList;
    private TextView shopName, shopNum;
    private List<ShopDetailItem> shopLeftInfo ;
    private List<ShopDetailItem> shopRightInfo ;
    private ShopItem shopInfo ;
    private Long userID;
    ImageView back;
    MyApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail_page);
        back = findViewById(R.id.iv_back_);
        ava = findViewById(R.id.shopava);
        shopName = findViewById(R.id.shopName);
        shopNum = findViewById(R.id.shopNum);
        String param = getIntent().getStringExtra("userID");
        if(param == null){
            Toast.makeText(ShopDetailPage.this, "未发现店铺号", Toast.LENGTH_SHORT).show();
            finish();
            return ;
        }
        userID = Double.valueOf(param).longValue();
        myApp = (MyApp)getApplication();
        if(myApp.getUserInfo() == null){
            Intent intent = new Intent(ShopDetailPage.this, LogInPageActivity.class);
            startActivity(intent);
            finish();
            return ;
        }

        if(shopInfo == null){
            if(!getShopInfo()){
                this.finish();
                return;
            }
        }
        String back_url = PropertiesManager.getProps(ShopDetailPage.this, "config.properties", "back_url");
        Glide.with(ShopDetailPage.this).load(back_url + shopInfo.getAva()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(ava);
        shopName.setText(shopInfo.getName());

        if(shopLeftInfo == null){
            if(!getShopList()){
                this.finish();
                return;
            }
        }
        setListener();
        shopNum.setText("总计" + (shopLeftInfo.size() + shopRightInfo.size()) + "件商品");

        shopGoodsLeftList = findViewById(R.id.shop_goods_left_list);
        shopGoodsRightList = findViewById(R.id.shop_goods_right_list);

        ShopDetailAdapter leftadapter = new ShopDetailAdapter(ShopDetailPage.this, R.layout.listitem_goods_card_layout, shopLeftInfo);
        ShopDetailAdapter rightadapter = new ShopDetailAdapter(ShopDetailPage.this, R.layout.listitem_goods_card_layout, shopRightInfo);

        shopGoodsLeftList.setAdapter(leftadapter);
        shopGoodsRightList.setAdapter(rightadapter);
        DoubleListViewManager.setListViewOnTouchAndScrollListener(shopGoodsLeftList, shopGoodsRightList);

        AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (parent.getId())
                {
                    case R.id.shop_goods_left_list:
                        expressitemClick(position, shopLeftInfo);
                        break;
                    case R.id.shop_goods_right_list:
                        expressitemClick(position, shopRightInfo);
                        break;
                }
            }
            public void expressitemClick(int position, List<ShopDetailItem> list){
                Intent intent = new Intent(ShopDetailPage.this, GoodPageActivity.class);
                intent.putExtra("goodID", list.get(position).getGoodID() + "");
                startActivity(intent);
            }
        };

        shopGoodsLeftList.setOnItemClickListener(itemClick);
        shopGoodsRightList.setOnItemClickListener(itemClick);
    }

    private boolean getShopList(){
        String back_url = PropertiesManager.getProps(ShopDetailPage.this, "config.properties", "back_url");
        GetShopInfo http = new GetShopInfo();
        http.userID = userID;
        HttpAttribute httpAttribute = Request.Post(GsonUtil.BeanToJson(http),  back_url + "/goods/get/shoplist", ShopDetailPage.this);
        if(httpAttribute.getCode() == 0) {
            List<Object> tmp = (List<Object>)httpAttribute.getData();
            List<ShopDetailItem> list = new ArrayList<ShopDetailItem>();
            ShopDetailItem info;
            Iterator it = tmp.iterator();

            shopLeftInfo = new ArrayList<ShopDetailItem>();
            shopRightInfo = new ArrayList<ShopDetailItem>();

            int length = tmp.size()%2 == 1 ? tmp.size()/2 + 1 : tmp.size()/2;
            boolean flag = false;

            while(it.hasNext()){
                info = new ShopDetailItem();
                Map<String, Object> a = (Map<String, Object>)it.next();
                info.setGoodID(Double.valueOf(String.valueOf(a.get("goodID"))).longValue());
                info.setGoodTitle(String.valueOf(a.get("goodDescription")));
                try {
                    String url = URLDecoder.decode(String.valueOf(a.get("goodImage")),"UTF-8");
                    if(url.substring(0, 1).equals("/")){
                        url = back_url + url;
                    }
                    info.setGoodImg(url);
                } catch (UnsupportedEncodingException e) {
                    Toast.makeText(ShopDetailPage.this, "内部错误", Toast.LENGTH_SHORT).show();
                    finish();
                }

                length--;
                list.add(info);
                if(length == 0 && flag == false){
                    shopLeftInfo = list;
                    length = tmp.size()/2;
                    list = new ArrayList<ShopDetailItem>();
                    flag = true;
                }
            }
            shopRightInfo = list;
            return true;
        }
        else{
            Toast.makeText(ShopDetailPage.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean getShopInfo(){
        String back_url = PropertiesManager.getProps(ShopDetailPage.this, "config.properties", "back_url");
        GetShopInfo http = new GetShopInfo();
        http.userID = userID;
        HttpAttribute httpAttribute = Request.Post(GsonUtil.BeanToJson(http),  back_url + "/user/getShopInfo", ShopDetailPage.this);
        if(httpAttribute.getCode() == 0) {
            Map<String, Object> shop = (Map<String, Object>)httpAttribute.getData();
            shopInfo = new ShopItem();
            shopInfo.setName(String.valueOf(shop.get("name")));
            shopInfo.setShopID(Double.valueOf(String.valueOf(shop.get("shopID"))).longValue());
            try {
                shopInfo.setAva(URLDecoder.decode(String.valueOf(shop.get("ava")), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return true;
        }
        else{
            Toast.makeText(ShopDetailPage.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    private void setListener(){
        OnClick onClick = new OnClick();
        back.setOnClickListener(onClick);
    }
    class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.iv_back_:
                    finish();
                    break;
            }
        }
    }

    class GetShopInfo{
        public Long userID;
    }
}
