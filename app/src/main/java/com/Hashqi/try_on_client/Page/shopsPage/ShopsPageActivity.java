package com.Hashqi.try_on_client.Page.shopsPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Hashqi.try_on_client.Component.MyListView;
import com.Hashqi.try_on_client.MyApp;
import com.Hashqi.try_on_client.Page.homePage.HomePageActivity;
import com.Hashqi.try_on_client.Page.messagePage.MessaegPageActivity;
import com.Hashqi.try_on_client.Page.selfPage.SelfPageActivity;
import com.Hashqi.try_on_client.Page.shopDetailPage.ShopDetailPage;
import com.Hashqi.try_on_client.R;
import com.Hashqi.try_on_client.Util.HttpAttribute;
import com.Hashqi.try_on_client.Util.PropertiesManager;
import com.Hashqi.try_on_client.Util.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ShopsPageActivity extends AppCompatActivity {
    private ImageButton toHome,toMessage,toSelf;
    private MyListView shopListView;
    private MyApp myapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_page);
        myapp = (MyApp)getApplication();
        toHome = findViewById(R.id.shopToHome);
        toMessage = findViewById(R.id.shopToMessage);
        toSelf = findViewById(R.id.shopToSelf);
        shopListView = findViewById(R.id.shopItem);
        if(myapp.getShopItemList() == null){
            if(!getShop()){
                finish();
                return ;
            }
        }
        String back_url = PropertiesManager.getProps(ShopsPageActivity.this, "config.properties", "back_url");

        ShopsAdapter adapter = new ShopsAdapter(ShopsPageActivity.this, R.layout.listitem_shop_item_layout, myapp.getShopItemList(), back_url);
        shopListView.setAdapter(adapter);
        setListener();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!getShop()){
            finish();
            return ;
        }
        String back_url = PropertiesManager.getProps(ShopsPageActivity.this, "config.properties", "back_url");
        ShopsAdapter adapter = new ShopsAdapter(ShopsPageActivity.this, R.layout.listitem_shop_item_layout, myapp.getShopItemList(), back_url);
        shopListView.setAdapter(adapter);
    }

    private void setListener(){
        ShopsPageActivity.OnClick onClick = new ShopsPageActivity.OnClick();
        toHome.setOnClickListener(onClick);
        toMessage.setOnClickListener(onClick);
        toSelf.setOnClickListener(onClick);

        AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (parent.getId())
                {
                    case R.id.shopItem:
                        expressitemClick(position, myapp.getShopItemList());
                        break;
                }
            }
            public void expressitemClick(int position, List<ShopItem> list){
                Intent intent = new Intent(ShopsPageActivity.this, ShopDetailPage.class);
                intent.putExtra("userID", list.get(position).getShopID() + "");
                startActivity(intent);
            }
        };
        shopListView.setOnItemClickListener(itemClick);
    }
    class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.shopToHome:
                    intent = new Intent(ShopsPageActivity.this, HomePageActivity.class);
                    finish();
                    break;
                case R.id.shopToMessage:
                    intent = new Intent(ShopsPageActivity.this, MessaegPageActivity.class);
                    finish();
                    break;
                case R.id.shopToSelf:
                    intent = new Intent(ShopsPageActivity.this, SelfPageActivity.class);
                    finish();
                    break;
            }
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ShopsPageActivity.this,HomePageActivity.class);
        startActivity(intent);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    public boolean getShop(){
        String back_url = PropertiesManager.getProps(ShopsPageActivity.this, "config.properties", "back_url");
        HttpAttribute httpAttribute = Request.Post(null,  back_url + "/user/getshop", ShopsPageActivity.this);
        if(httpAttribute.getCode() == 0) {
            httpAttribute.show();
            List<Object> data =  (List<Object>)httpAttribute.getData();
            List<ShopItem> items = new ArrayList<ShopItem>();
            Iterator it = data.iterator();
            ShopItem item ;
            while(it.hasNext()){
                item = new ShopItem();
                Map<String, Object> tmp = (Map<String, Object>)it.next();
                try {
                    item.setAva(back_url + URLDecoder.decode(String.valueOf(tmp.get("ava")), "UTF-8"));
                    item.setImgs((List<String>)tmp.get("imgs"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                item.setName(String.valueOf(tmp.get("name")));
                item.setShopID(Double.valueOf(String.valueOf(tmp.get("shopID"))).longValue());
                items.add(item);
            }
            myapp.setShopItemList(items);
            return true;
        }
        else{
            Toast.makeText(ShopsPageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
