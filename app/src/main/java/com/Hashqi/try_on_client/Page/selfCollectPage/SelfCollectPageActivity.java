package com.Hashqi.try_on_client.Page.selfCollectPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Hashqi.try_on_client.Component.MyListView;
import com.Hashqi.try_on_client.MyApp;
import com.Hashqi.try_on_client.Page.goodPage.GoodPageActivity;
import com.Hashqi.try_on_client.Page.logInPage.LogInPageActivity;
import com.Hashqi.try_on_client.Page.shopDetailPage.ShopDetailPage;
import com.Hashqi.try_on_client.R;
import com.Hashqi.try_on_client.Util.GsonUtil;
import com.Hashqi.try_on_client.Util.HttpAttribute;
import com.Hashqi.try_on_client.Util.PropertiesManager;
import com.Hashqi.try_on_client.Util.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SelfCollectPageActivity extends AppCompatActivity {

    MyApp myApp;
    MyListView collectList;
    ImageView back;
    List<CollectItem> collectItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_collect_page);
        collectList = findViewById(R.id.collectList);
        back = findViewById(R.id.iv_back_);
        myApp = (MyApp)getApplication();
        setListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(myApp.getUserInfo() == null){
            Toast.makeText(SelfCollectPageActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SelfCollectPageActivity.this, LogInPageActivity.class);
            startActivity(intent);
            finish();
            return ;
        }
        if(!getCollect(myApp.getUserInfo().getUserId())){
            finish();
            return ;
        }
        CollectAdapter adapter = new CollectAdapter(SelfCollectPageActivity.this, R.layout.listitem_shop_simple_layout, collectItems);
        collectList.setAdapter(adapter);

        AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (parent.getId())
                {
                    case R.id.collectList:
                        expressitemClick(position, collectItems);
                        break;
                }
            }
            public void expressitemClick(int position, List<CollectItem> list){
                Intent intent = new Intent(SelfCollectPageActivity.this, GoodPageActivity.class);
                intent.putExtra("goodID",list.get(position).getGoodID() + "");
                startActivity(intent);
            }
        };
        collectList.setOnItemClickListener(itemClick);
    }

    private boolean getCollect(Long userId){
        String back_url = PropertiesManager.getProps(SelfCollectPageActivity.this, "config.properties", "back_url");
        GetCollect item = new GetCollect();
        item.userID = userId.toString();
        System.out.println(item.userID);
        String str = GsonUtil.BeanToJson(item);
        HttpAttribute httpAttribute = Request.Post(str, back_url + "/usercollects/get", SelfCollectPageActivity.this);
        if(httpAttribute.getCode() == 0) {
            List<Object> map = (List<Object>)httpAttribute.getData();
            collectItems = new ArrayList<CollectItem>();
            Iterator it = map.iterator();
            while(it.hasNext()){
                Map<String, Object> tmp = (Map<String, Object>)it.next();
                CollectItem collectItem = new CollectItem();
                collectItem.setShopName(String.valueOf(tmp.get("shopName")));
                collectItem.setGoodName(String.valueOf(tmp.get("goodName")));
                collectItem.setGoodID(Double.valueOf(String.valueOf(tmp.get("goodID"))).longValue());

                try {
                    String url = URLDecoder.decode(String.valueOf(tmp.get("avaURL")),"UTF-8");
                    if(url.substring(0, 1).equals("/")){
                        url = back_url + url;
                    }
                    collectItem.setAvaURL(url);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                collectItems.add(collectItem);
            }
            return true;
        }
        else{
            if(httpAttribute.getData() != null)
                Toast.makeText(SelfCollectPageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
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
    class GetCollect{
        public String userID;
    }
}
