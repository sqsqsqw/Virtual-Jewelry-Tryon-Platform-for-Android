package com.Hashqi.try_on_client.Page.goodPage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Hashqi.try_on_client.Component.CustomRoundAngleImageView;
import com.Hashqi.try_on_client.Component.MyListView;
import com.Hashqi.try_on_client.MyApp;
import com.Hashqi.try_on_client.Page.addCommunityPage.AddCommunityPageActivity;
import com.Hashqi.try_on_client.Page.logInPage.LogInPageActivity;
import com.Hashqi.try_on_client.Page.shopDetailPage.ShopDetailPage;
import com.Hashqi.try_on_client.R;
import com.Hashqi.try_on_client.ServiceItem.GoodInfo;
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


public class GoodPageActivity extends AppCompatActivity {

    MyApp myApp;
    CustomRoundAngleImageView goodImage ;
    TextView goodTitle;
    TextView goodPrizeNum;
    ImageView back;
    RelativeLayout tryOnNowLayOut;
    MyListView commentList;
    Long goodID;
    List<CommentItem> commList;
    GoodInfo goodInfo;
    RelativeLayout clickToShop, clickToCollect, goodStore, addComm;
    Button button;
    EditText comm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_page);
        myApp = (MyApp)getApplication();
        String param = getIntent().getStringExtra("goodID");
        if(param == null){
            Toast.makeText(GoodPageActivity.this, "未发现商品序号", Toast.LENGTH_SHORT).show();
            finish();
            return ;
        }
        goodID = Long.parseLong(param);
        if(!getGoodsInfo(goodID)){
            finish();
            return ;
        }
        if(!getComment(goodID)){
            finish();
            return ;
        }

        myApp.insertHistoryInfo(goodInfo);

        comm = findViewById(R.id.commVal);
        goodImage = findViewById(R.id.GoodImage);
        addComm = findViewById(R.id.addComm);
        Glide.with(GoodPageActivity.this).load(goodInfo.getGoodImage()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(goodImage);
        goodTitle = findViewById(R.id.GoodTitle);
        button = findViewById(R.id.submit_good_comm);
        goodTitle.setText(goodInfo.getGoodDescription());
        goodPrizeNum = findViewById(R.id.GoodPrizeNum);
        goodPrizeNum.setText("￥" + goodInfo.getPrice());
        back = findViewById(R.id.iv_back_);
        clickToShop = findViewById(R.id.clickToShop);
        clickToCollect = findViewById(R.id.clickToCollect);
        tryOnNowLayOut = findViewById(R.id.tryOnNowBtn);
        goodStore = findViewById(R.id.good_store);
        commentList = findViewById(R.id.goodCommentList);
        String back_url = PropertiesManager.getProps(GoodPageActivity.this, "config.properties", "back_url");
        GoodCommentAdapter adapter = new GoodCommentAdapter(this, R.layout.listitem_goods_comment_layout, commList, back_url);
        commentList.setAdapter(adapter);
        comm.clearFocus();
        setListener();
    }
    private void setListener(){
        OnClick onClick = new OnClick();
        tryOnNowLayOut.setOnClickListener(onClick);
        goodStore.setOnClickListener(onClick);
        clickToShop.setOnClickListener(onClick);
        clickToCollect.setOnClickListener(onClick);
        back.setOnClickListener(onClick);
        button.setOnClickListener(onClick);
        addComm.setOnClickListener(onClick);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.tryOnNowBtn:
                    Uri content_url = Uri.parse(PropertiesManager.getProps(GoodPageActivity.this, "config.properties", "tryon_url"))
                            .buildUpon()
                            .appendQueryParameter("goodID", goodInfo.getGoodID().toString())
                            .build();
                    browser(content_url);
                    break;
                case R.id.iv_back_:
                    finish();
                    break;
                case R.id.clickToShop:
                    intent = new Intent(GoodPageActivity.this, ShopDetailPage.class);
                    intent.putExtra("userID", goodInfo.getSellerId() + "");
                    startActivity(intent);
                    break;
                case R.id.clickToCollect:
                    collect();
                    break;
                case R.id.good_store:
//                    System.out.println("------------------------" + goodInfo.getGoodUrl() + "?goodID=" + goodInfo.getGoodID());
//                    Uri url = Uri.parse(goodInfo.getGoodUrl() + "?goodID=" + goodInfo.getGoodID());
                    Uri uri = Uri.parse(goodInfo.getGoodUrl());
                    browser(uri);
                    break;
                case R.id.submit_good_comm:
                    String val = comm.getText().toString();
                    comm(val);
                    break;
                case R.id.addComm:
                    intent = new Intent(GoodPageActivity.this, AddCommunityPageActivity.class);
                    intent.putExtra("goodID", goodInfo.getGoodID() + "");
                    intent.putExtra("goodName", goodInfo.getGoodDescription() + "");
                    startActivity(intent);
                    break;
            }
        }
    }

    private boolean getGoodsInfo(Long goodID){
        String back_url = PropertiesManager.getProps(GoodPageActivity.this, "config.properties", "back_url");
        Collect param = new Collect();
        param.goodID = goodID;
        if(myApp.getUserInfo() == null){
            Intent intent = new Intent(GoodPageActivity.this, LogInPageActivity.class);
            startActivity(intent);
            return false;
        }
        param.userID = myApp.getUserInfo().getUserId();
        HttpAttribute httpAttribute = Request.Post(GsonUtil.BeanToJson(param),  back_url + "/goods/get/info", GoodPageActivity.this);
        if(httpAttribute.getCode() == 0) {
            httpAttribute.show();
            Map<String, Object> item =  (Map<String, Object>)httpAttribute.getData();
            goodInfo = new GoodInfo();
            goodInfo.setGoodID(Double.valueOf(String.valueOf(item.get("goodID"))).longValue());
            goodInfo.setGoodDescription(String.valueOf(item.get("goodDescription")));
            goodInfo.setGoodClassID(Double.valueOf(String.valueOf(item.get("goodClassID"))).longValue());
            goodInfo.setGoodClassName(String.valueOf(item.get("goodClassName")));
            goodInfo.setPrice(Double.valueOf(String.valueOf(item.get("price"))));
            goodInfo.setGoodCreateTime(String.valueOf(item.get("goodCreateTime")));
            goodInfo.setGoodUrl(String.valueOf(item.get("goodUrl")));
            goodInfo.setSellerId(Double.valueOf(String.valueOf(item.get("sellerId"))).longValue());
            goodInfo.setGoodModelUrl(String.valueOf(item.get("goodModelUrl")));
            try {
                String url = URLDecoder.decode(String.valueOf(item.get("goodImage")),"UTF-8");
                if(url.substring(0, 1).equals("/")){
                    url = back_url + url;
                }
                goodInfo.setGoodImage(url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return true;
        }
        else{
            Toast.makeText(GoodPageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
            if(httpAttribute.getCode() == 2007){
                Intent intent = new Intent(GoodPageActivity.this, LogInPageActivity.class);
                startActivity(intent);
            }
            return false;
        }
    }

    private boolean getComment(Long goodID){
        String back_url = PropertiesManager.getProps(GoodPageActivity.this, "config.properties", "back_url");
        Collect param = new Collect();
        param.goodID = goodID;
        if(myApp.getUserInfo() == null){
            Intent intent = new Intent(GoodPageActivity.this, LogInPageActivity.class);
            startActivity(intent);
            return false;
        }
        param.userID = myApp.getUserInfo().getUserId();
        HttpAttribute httpAttribute = Request.Post(GsonUtil.BeanToJson(param),  back_url + "/comment/get", GoodPageActivity.this);
        if(httpAttribute.getCode() == 0) {
            List<Object> data = (List<Object>)httpAttribute.getData();
            CommentItem item;
            Iterator it = data.iterator();
            commList = new ArrayList<CommentItem>();
            while(it.hasNext()){
                Map<String, Object> tmp = (Map<String, Object>)it.next();
                item = new CommentItem();
                try {
                    item.setAvaURL(URLDecoder.decode(String.valueOf(tmp.get("avaURL")), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                item.setUserName(String.valueOf(tmp.get("userName")));
                item.setComm(String.valueOf(tmp.get("comm")));
                commList.add(item);
            }
            return true;
        }
        else{
            Toast.makeText(GoodPageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    private boolean collect(){
        String back_url = PropertiesManager.getProps(GoodPageActivity.this, "config.properties", "back_url");
        Collect param = new Collect();
        param.goodID = goodInfo.getGoodID();
        param.userID = myApp.getUserInfo().getUserId();
        HttpAttribute httpAttribute = Request.Post(GsonUtil.BeanToJson(param),  back_url + "/usercollects/set", GoodPageActivity.this);
        if(httpAttribute.getCode() == 0) {
            Toast.makeText(GoodPageActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            Toast.makeText(GoodPageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
            if(httpAttribute.getCode() == 2007){
                Intent intent = new Intent(GoodPageActivity.this, LogInPageActivity.class);
                startActivity(intent);
            }
            return false;
        }
    }
    private boolean comm(String context){
        String back_url = PropertiesManager.getProps(GoodPageActivity.this, "config.properties", "back_url");
        Comm param = new Comm();
        param.goodID = goodInfo.getGoodID();
        param.userID = myApp.getUserInfo().getUserId();
        param.context = context;
        HttpAttribute httpAttribute = Request.Post(GsonUtil.BeanToJson(param),  back_url + "/comment/set", GoodPageActivity.this);
        if(httpAttribute.getCode() == 0) {
            Toast.makeText(GoodPageActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
            if(!getComment(goodID)){
                finish();
            }
            GoodCommentAdapter adapter = new GoodCommentAdapter(this, R.layout.listitem_goods_comment_layout, commList, back_url);
            commentList.setAdapter(adapter);
            return true;
        }
        else{
            Toast.makeText(GoodPageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
            if(httpAttribute.getCode() == 2007){
                Intent intent = new Intent(GoodPageActivity.this, LogInPageActivity.class);
                startActivity(intent);
            }
            return false;
        }
    }
        //唤醒qq浏览器进行试穿的功能
       private boolean browser(Uri url){
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(url);
            intent.setClassName("com.tencent.mtt", "com.tencent.mtt.MainActivity");
            startActivity(intent);
            return true;
        }

    class Collect{
        public Long goodID;
        public Long userID;
    }
    class Comm{
        public Long userID,goodID;
        public String context;
    }
}
