package com.Hashqi.try_on_client.Page.searchPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Hashqi.try_on_client.Component.MyListView;
import com.Hashqi.try_on_client.Page.goodPage.GoodPageActivity;
import com.Hashqi.try_on_client.Page.selfCollectPage.CollectAdapter;
import com.Hashqi.try_on_client.Page.selfCollectPage.CollectItem;
import com.Hashqi.try_on_client.Page.selfCollectPage.SelfCollectPageActivity;
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

public class SearchPageActivity extends AppCompatActivity {
    EditText search;
    MyListView searchListView;
    List<CollectItem> searchList;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        search = findViewById(R.id.searchBar);
        searchListView = findViewById(R.id.searchList);
        back = findViewById(R.id.iv_back_);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String str = search.getText().toString();
                if(!getSearch(str)){
                    finish();
                    return false;
                }
                CollectAdapter adapter = new CollectAdapter(SearchPageActivity.this, R.layout.listitem_shop_simple_layout, searchList);
                searchListView.setAdapter(adapter);
                return false;
            }
        });


        AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (parent.getId())
                {
                    case R.id.searchList:
                        expressitemClick(position, searchList);
                        break;
                }
            }
            public void expressitemClick(int position, List<CollectItem> list){
                Intent intent = new Intent(SearchPageActivity.this, GoodPageActivity.class);
                intent.putExtra("goodID",list.get(position).getGoodID() + "");
                startActivity(intent);
            }
        };
        searchListView.setOnItemClickListener(itemClick);
        setListener();
    }


    private boolean getSearch(String str){
        String back_url = PropertiesManager.getProps(SearchPageActivity.this, "config.properties", "back_url");
        GetSearch item = new GetSearch();
        item.str = str;
        HttpAttribute httpAttribute = Request.Post(GsonUtil.BeanToJson(item), back_url + "/goods/search", SearchPageActivity.this);
        if(httpAttribute.getCode() == 0) {
            List<Object> map = (List<Object>)httpAttribute.getData();
            searchList = new ArrayList<CollectItem>();
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
                searchList.add(collectItem);
            }
            return true;
        }
        else{
            if(httpAttribute.getData() != null)
                Toast.makeText(SearchPageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
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
    class GetSearch{
        public String str;
    }



}
