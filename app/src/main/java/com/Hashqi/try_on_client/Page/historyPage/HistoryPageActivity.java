package com.Hashqi.try_on_client.Page.historyPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Hashqi.try_on_client.Component.MyListView;
import com.Hashqi.try_on_client.MyApp;
import com.Hashqi.try_on_client.Page.goodPage.GoodPageActivity;
import com.Hashqi.try_on_client.Page.messagePage.MessaegPageActivity;
import com.Hashqi.try_on_client.R;
import com.Hashqi.try_on_client.ServiceItem.GoodInfo;
import com.Hashqi.try_on_client.Util.PropertiesManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HistoryPageActivity extends AppCompatActivity {

    MyApp myapp;
    ListView historyListView;
    ImageView back;
    List<HistoryItem> historyItems = new ArrayList<HistoryItem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);
        historyListView = findViewById(R.id.historyList);
        back = findViewById(R.id.iv_back_);
        setAdapter();
        String back_url = PropertiesManager.getProps(HistoryPageActivity.this, "config.properties", "back_url");
        HistoryAdapter adapter = new HistoryAdapter(HistoryPageActivity.this, R.layout.listitem_shop_simple_layout,historyItems,back_url);
        historyListView.setAdapter(adapter);

        AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (parent.getId())
                {
                    case R.id.historyList:
                        expressitemClick(position, historyItems);
                        break;
                }
            }
            public void expressitemClick(int position, List<HistoryItem> list){
                Intent intent = new Intent(HistoryPageActivity.this, GoodPageActivity.class);
                intent.putExtra("goodID",list.get(position).getGoodID() + "");
                startActivity(intent);
            }
        };
        historyListView.setOnItemClickListener(itemClick);

        setListener();
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

    private void setAdapter(){
        myapp = (MyApp) getApplication();
        List<GoodInfo> goodInfos=  myapp.getHistoryInfo();
        if(goodInfos == null || goodInfos.size() == 0){
            Toast.makeText(HistoryPageActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
            return;
        }
        Iterator it = goodInfos.iterator();
        HistoryItem item = new HistoryItem();
        while (it.hasNext()){
            GoodInfo goodInfo = (GoodInfo)it.next();
            item.setAvaURL(goodInfo.getGoodImage());
            item.setGoodName(goodInfo.getGoodDescription());
            item.setShopName("￥" + goodInfo.getPrice());
            item.setGoodID(goodInfo.getGoodID());

            historyItems.add(item);
        }
    }
}
