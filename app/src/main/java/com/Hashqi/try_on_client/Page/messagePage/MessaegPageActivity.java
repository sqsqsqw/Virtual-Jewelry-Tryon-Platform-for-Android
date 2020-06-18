package com.Hashqi.try_on_client.Page.messagePage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Hashqi.try_on_client.Page.addCommunityPage.AddCommunityPageActivity;
import com.Hashqi.try_on_client.Page.homePage.HomePageActivity;
import com.Hashqi.try_on_client.Page.messageDetailPage.MessageDetailPageActivity;
import com.Hashqi.try_on_client.Page.selfCollectPage.CollectItem;
import com.Hashqi.try_on_client.Page.selfPage.SelfPageActivity;
import com.Hashqi.try_on_client.Page.shopDetailPage.ShopDetailPage;
import com.Hashqi.try_on_client.Page.shopsPage.ShopsPageActivity;
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


public class MessaegPageActivity extends AppCompatActivity {
    private List<Contacts> contactsList = new ArrayList<Contacts>();
    private ImageButton toHome,toShop,toSelf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaeg_page);
        //initContacts();
        if(!getMessage()){
            finish();
            return ;
        }

        String back_url = PropertiesManager.getProps(MessaegPageActivity.this, "config.properties", "back_url");
        MessageAdapter adapter = new MessageAdapter(MessaegPageActivity.this,R.layout.listitem_message_contacts_layout,contactsList, back_url);
        ListView listView = findViewById(R.id.messageListView);
        listView.setAdapter(adapter);
        toHome = findViewById(R.id.messageToHome);
        toShop = findViewById(R.id.messageToShop);
        toSelf = findViewById(R.id.messageToSelf);
        setListener();

        AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (parent.getId())
                {
                    case R.id.messageListView:
                        expressitemClick(position);
                        break;
                }
            }
            public void expressitemClick(int position){
                Intent intent = new Intent(MessaegPageActivity.this, MessageDetailPageActivity.class);
                intent.putExtra("ID",contactsList.get(position).getID() + "");
                intent.putExtra("ava",contactsList.get(position).getImageUrl() + "");
                startActivity(intent);
            }
        };
        listView.setOnItemClickListener(itemClick);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!getMessage()){
            finish();
            return ;
        }
    }

    /*
        private void initContacts(){
            Contacts shop1 = new Contacts(getResources().getString(R.string.shop_001),getResources().getString(R.string.shopMessage_001),getResources().getString(R.string.shopTime_001),R.drawable.shop1_0);
            Contacts shop2 = new Contacts(getResources().getString(R.string.shop_002),getResources().getString(R.string.shopMessage_002),getResources().getString(R.string.shopTime_002),R.drawable.shop2_0);
            Contacts shop3 = new Contacts(getResources().getString(R.string.shop_003),getResources().getString(R.string.shopMessage_003),getResources().getString(R.string.shopTime_003),R.drawable.shop3_0);
            contactsList.add(shop1);
            contactsList.add(shop2);
            contactsList.add(shop3);
        }
    */
    private void setListener(){
        MessaegPageActivity.OnClick onClick = new MessaegPageActivity.OnClick();
        toHome.setOnClickListener(onClick);
        toShop.setOnClickListener(onClick);
        toSelf.setOnClickListener(onClick);
    }
    public  class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.messageToHome:
                    intent = new Intent(MessaegPageActivity.this, HomePageActivity.class);
                    finish();
                    break;
                case R.id.messageToShop:
                    intent = new Intent(MessaegPageActivity.this, ShopsPageActivity.class);
                    finish();
                    break;
                case R.id.messageToSelf:
                    intent = new Intent(MessaegPageActivity.this, SelfPageActivity.class);
                    finish();
                    break;
            }
            startActivity(intent);
            overridePendingTransition(0, 0);
            MessaegPageActivity.this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MessaegPageActivity.this,HomePageActivity.class);
        startActivity(intent);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }


    private boolean getMessage() {
        String back_url = PropertiesManager.getProps(MessaegPageActivity.this, "config.properties", "back_url");
        HttpAttribute httpAttribute = Request.Post(null, back_url + "/communityDistribute/get", MessaegPageActivity.this);
        httpAttribute.show();
        if (httpAttribute.getCode() == 0) {
            List<Object> map = (List<Object>)httpAttribute.getData();
            List<Contacts> lists = new ArrayList<Contacts>();
            Contacts contacts ;
            Iterator it = map.iterator();
            while(it.hasNext()){
                Map<String, Object> tmp = (Map<String, Object>)it.next();
                contacts = new Contacts(
                        String.valueOf(tmp.get("name")),
                        String.valueOf(tmp.get("message")),
                        String.valueOf(tmp.get("imageUrl")),
                        Double.valueOf(String.valueOf(tmp.get("ID"))).longValue()
                );
                lists.add(contacts);
            }

            if(lists.size() == 0)
                Toast.makeText(MessaegPageActivity.this, "暂无帖子", Toast.LENGTH_SHORT).show();
            contactsList = lists;
            return true;
        }
        else{
            Toast.makeText(MessaegPageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
