package com.Hashqi.try_on_client.Page.messageDetailPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Hashqi.try_on_client.Component.MyListView;
import com.Hashqi.try_on_client.Component.SquareImageView;
import com.Hashqi.try_on_client.MyApp;
import com.Hashqi.try_on_client.Page.goodPage.GoodCommentAdapter;
import com.Hashqi.try_on_client.Page.goodPage.GoodPageActivity;
import com.Hashqi.try_on_client.Page.homePage.HomePageActivity;
import com.Hashqi.try_on_client.Page.logInPage.LogInPageActivity;
import com.Hashqi.try_on_client.Page.messagePage.Contacts;
import com.Hashqi.try_on_client.Page.messagePage.MessaegPageActivity;
import com.Hashqi.try_on_client.Page.selfPage.SelfPageActivity;
import com.Hashqi.try_on_client.Page.shopDetailPage.ShopDetailPage;
import com.Hashqi.try_on_client.Page.shopsPage.ShopsPageActivity;
import com.Hashqi.try_on_client.R;
import com.Hashqi.try_on_client.ServiceItem.CommunityCommentItem;
import com.Hashqi.try_on_client.ServiceItem.CommunityDistributeItem;
import com.Hashqi.try_on_client.Util.GsonUtil;
import com.Hashqi.try_on_client.Util.HttpAttribute;
import com.Hashqi.try_on_client.Util.PropertiesManager;
import com.Hashqi.try_on_client.Util.Request;
import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MessageDetailPageActivity extends AppCompatActivity {

    CommunityDistributeItem communityDistributeItem;
    MyApp myApp;
    String ava;
    Long ID;
    SquareImageView avaView;
    TextView usernameView, contentView;
    LinearLayout clickToGood;
    EditText commEdit;
    Button button;
    List<CommunityCommentItem> commList;
    ImageView back;
    MyListView cComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail_page);

        avaView = findViewById(R.id.userAva);
        usernameView = findViewById(R.id.username);
        contentView = findViewById(R.id.content);
        clickToGood = findViewById(R.id.clickToShop);
        commEdit = findViewById(R.id.commEdit);
        button = findViewById(R.id.clickToSubmit);
        back = findViewById(R.id.iv_back_);
        cComment = findViewById(R.id.cComment);

        myApp = (MyApp)getApplication();
        if(myApp.getUserInfo() == null){
            Intent intent = new Intent(MessageDetailPageActivity.this, LogInPageActivity.class);
            startActivity(intent);
            finish();
            return ;
        }

        String back_url = PropertiesManager.getProps(MessageDetailPageActivity.this, "config.properties", "back_url");
        String param = getIntent().getStringExtra("ID");
        if(param == null){
            Toast.makeText(MessageDetailPageActivity.this, "未发现序号", Toast.LENGTH_SHORT).show();
            finish();
            return ;
        }
        ID = Long.valueOf(param);

        ava = getIntent().getStringExtra("ava");
        if(ava == null){
            Toast.makeText(MessageDetailPageActivity.this, "未发现序号", Toast.LENGTH_SHORT).show();
            finish();
            return ;
        }

        if(!getDisInfo(ID)){
            finish();
            return ;
        };

        Glide.with(this).load(back_url + ava).into(avaView);
        usernameView.setText(communityDistributeItem.getCommunityDistributeTitle());
        contentView.setText(communityDistributeItem.getCommunityDistributeContext());
        setListener();
        if(!getCommList(ID)){
            finish();
            return ;
        }
        MessageCommentAdapter adapter = new MessageCommentAdapter(this, R.layout.listitem_goods_comment_layout, commList, back_url);
        cComment.setAdapter(adapter);

    }


    private boolean getDisInfo(Long id){

        String back_url = PropertiesManager.getProps(MessageDetailPageActivity.this, "config.properties", "back_url");
        GetDisInfo param = new GetDisInfo();
        param.id = id;
        HttpAttribute httpAttribute = Request.Post(GsonUtil.BeanToJson(param),  back_url + "/communityDistribute/getInfo", MessageDetailPageActivity.this);
        if(httpAttribute.getCode() == 0) {
            communityDistributeItem = new CommunityDistributeItem();
            Map<String, Object> tmp = (Map<String, Object>)httpAttribute.getData();
            communityDistributeItem.setCommunityDistributeID(Double.valueOf(String.valueOf(tmp.get("communityDistributeID"))).longValue());
            communityDistributeItem.setCommunityDistributeContext(String.valueOf(tmp.get("communityDistributeContext")));
            communityDistributeItem.setCommunityDistributeTitle(String.valueOf(tmp.get("communityDistributeTitle")));
            communityDistributeItem.setGoodID(Double.valueOf(String.valueOf(tmp.get("goodID"))).longValue());
            communityDistributeItem.setUserID(Double.valueOf(String.valueOf(tmp.get("userID"))).longValue());

            return true;
        }
        else{
            Toast.makeText(MessageDetailPageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
            if(httpAttribute.getCode() == 2007){
                Intent intent = new Intent(MessageDetailPageActivity.this, LogInPageActivity.class);
                startActivity(intent);
            }
            return false;
        }
    }


    private void setListener(){
        OnClick onClick = new OnClick();
        clickToGood.setOnClickListener(onClick);
        button.setOnClickListener(onClick);
        back.setOnClickListener(onClick);
    }
    public class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.clickToShop:
                    intent = new Intent(MessageDetailPageActivity.this, GoodPageActivity.class);
                    intent.putExtra("goodID",communityDistributeItem.getGoodID() + "");
                    startActivity(intent);
                    break;
                case R.id.clickToSubmit:
                    comm(commEdit.getText().toString());
                    break;
                case R.id.iv_back_:
                    finish();
                    break;
            }
        }
    }

    private boolean getCommList(Long did){

        String back_url = PropertiesManager.getProps(MessageDetailPageActivity.this, "config.properties", "back_url");
        GetList param = new GetList();
        param.did = did;
        HttpAttribute httpAttribute = Request.Post(GsonUtil.BeanToJson(param),  back_url + "/communitycomment/get", MessageDetailPageActivity.this);
        if(httpAttribute.getCode() == 0) {
            List<Object> data = (List<Object>)httpAttribute.getData();
            CommunityCommentItem item;
            Iterator it = data.iterator();
            commList = new ArrayList<CommunityCommentItem>();
            while(it.hasNext()){
                Map<String, Object> tmp = (Map<String, Object>)it.next();
                item = new CommunityCommentItem();
                item.setCommunityCommentContext(String.valueOf(tmp.get("communityCommentContext")));
                item.setCommunityCommentID(Double.valueOf(String.valueOf(tmp.get("communityCommentID"))).longValue());
                try {
                    item.setUserAva(URLDecoder.decode(String.valueOf(tmp.get("userAva")), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                item.setUserID(Double.valueOf(String.valueOf(tmp.get("userID"))).longValue());
                item.setUserName(String.valueOf(tmp.get("userName")));
                commList.add(item);
            }
            return true;
        }
        else{
            Toast.makeText(MessageDetailPageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
            if(httpAttribute.getCode() == 2007){
                Intent intent = new Intent(MessageDetailPageActivity.this, LogInPageActivity.class);
                startActivity(intent);
            }
            return false;
        }
    }



    private boolean comm(String context){
        String back_url = PropertiesManager.getProps(MessageDetailPageActivity.this, "config.properties", "back_url");
        Comm param = new Comm();
        param.dID = communityDistributeItem.getCommunityDistributeID();
        param.userID = myApp.getUserInfo().getUserId();
        param.context = context;
        HttpAttribute httpAttribute = Request.Post(GsonUtil.BeanToJson(param),  back_url + "/communitycomment/set", MessageDetailPageActivity.this);
        if(httpAttribute.getCode() == 0) {
           Toast.makeText(MessageDetailPageActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
            if(!getCommList(ID)){
                finish();
            }
            MessageCommentAdapter adapter = new MessageCommentAdapter(this, R.layout.listitem_goods_comment_layout, commList, back_url);
            cComment.setAdapter(adapter);
            return true;
        }
        else{
            Toast.makeText(MessageDetailPageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
            if(httpAttribute.getCode() == 2007){
                Intent intent = new Intent(MessageDetailPageActivity.this, LogInPageActivity.class);
                startActivity(intent);
            }
            return false;
        }
    }

    class GetDisInfo{
        private Long id;
    }

    class GetList{
        private Long did;
    }
    class Comm{
        public Long userID,dID;
        public String context;
    }

}
