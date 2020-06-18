package com.Hashqi.try_on_client.Page.homePage;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Hashqi.try_on_client.MyApp;
import com.Hashqi.try_on_client.Page.goodPage.GoodPageActivity;
import com.Hashqi.try_on_client.Page.messagePage.MessaegPageActivity;
import com.Hashqi.try_on_client.Page.searchPage.SearchPageActivity;
import com.Hashqi.try_on_client.Page.selfPage.SelfPageActivity;
import com.Hashqi.try_on_client.Page.shopsPage.ShopsPageActivity;
import com.Hashqi.try_on_client.R;
import com.Hashqi.try_on_client.ServiceItem.HomePageInfo;
import com.Hashqi.try_on_client.Util.DoubleListViewManager;
import com.Hashqi.try_on_client.Util.HttpAttribute;
import com.Hashqi.try_on_client.Util.PropertiesManager;
import com.Hashqi.try_on_client.Util.Request;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class HomePageActivity extends AppCompatActivity implements OnBannerListener {
    private MyApp myapp;
    private Banner banner;
    private ArrayList<Drawable> list_path;
    private ArrayList<String> list_title;
    private ImageButton toShop,toMessage,toSelf;
    private ImageButton toback;
    private ListView homeGoodsLeftList, homeGoodsRightList;
    private LinearLayout searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        myapp = (MyApp)getApplication();
        initView();
        //初始化list
        //initItems();
        if(myapp.getHomepageLeftInfo() == null){
            if(!getHomePage()){
                this.finish();
                return;
            }
        }
        HomeAdapter leftadapter = new HomeAdapter(HomePageActivity.this, R.layout.listitem_goods_card_layout, myapp.getHomepageLeftInfo());
        homeGoodsLeftList = findViewById(R.id.home_goods_left_list);
        homeGoodsLeftList.setAdapter(leftadapter);
        HomeAdapter rightadapter = new HomeAdapter(HomePageActivity.this, R.layout.listitem_goods_card_layout, myapp.getHomepageRightInfo());
        homeGoodsRightList = findViewById(R.id.home_goods_right_list);
        homeGoodsRightList.setAdapter(rightadapter);

        DoubleListViewManager.setListViewOnTouchAndScrollListener(homeGoodsLeftList, homeGoodsRightList);

        AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (parent.getId())
                {
                    case R.id.home_goods_left_list:
                        expressitemClick(position, myapp.getHomepageLeftInfo());
                        break;
                    case R.id.home_goods_right_list:
                        expressitemClick(position, myapp.getHomepageRightInfo());
                        break;
                }
            }
            public void expressitemClick(int position, List<HomePageInfo> list){
                Intent intent = new Intent(HomePageActivity.this, GoodPageActivity.class);
                System.out.println(list.get(position).getHomepageGoodId());
                intent.putExtra("goodID",list.get(position).getHomepageGoodId() + "");
                startActivity(intent);
            }
        };

        homeGoodsLeftList.setOnItemClickListener(itemClick);
        homeGoodsRightList.setOnItemClickListener(itemClick);
        //初始化其他组件
        toShop = findViewById(R.id.homeToShop);
        toMessage = findViewById(R.id.homeToMessage);
        toSelf = findViewById(R.id.homeToSelf);
        searchBar = findViewById(R.id.searchBar);
        setListener();

        Drawable drawable = getDrawable(R.drawable.search_icon);
        drawable.setBounds(0,0,40,40);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!getHomePage()){
            this.finish();
            return;
        }
        HomeAdapter leftadapter = new HomeAdapter(HomePageActivity.this, R.layout.listitem_goods_card_layout, myapp.getHomepageLeftInfo());
        homeGoodsLeftList.setAdapter(leftadapter);
        HomeAdapter rightadapter = new HomeAdapter(HomePageActivity.this, R.layout.listitem_goods_card_layout, myapp.getHomepageRightInfo());
        homeGoodsRightList.setAdapter(rightadapter);
    }

    //轮播图的代码开始
    private void initView() {
        banner = (Banner) findViewById(R.id.homePageSlide);
        //放图片地址的集合
        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();

        list_path.add(getResources().getDrawable(R.drawable.ad_001));
        list_path.add(getResources().getDrawable(R.drawable.ad_002));
        list_path.add(getResources().getDrawable(R.drawable.ad_003));
        list_title.add("微星电脑");
        list_title.add("送福利！");
        list_title.add("过年大吉！");

        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new HomePageActivity.MyLoader());
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();
    }
    //轮播图的监听方法
    @Override
    public void OnBannerClick(int position) {
        Log.i("tag", "你点了第"+position+"张轮播图");
    }
    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
//            Glide.with(context).load((String) path).into(imageView);
            imageView.setImageDrawable((Drawable) path);
        }
    }
    //轮播图的代码结束
    private void setListener(){
        OnClick onClick = new OnClick();
        toShop.setOnClickListener(onClick);
        toMessage.setOnClickListener(onClick);
        toSelf.setOnClickListener(onClick);
        searchBar.setOnClickListener(onClick);
    }
    class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = null;
            boolean flag = true;
            switch (v.getId()){
                case R.id.homeToShop:
                    intent = new Intent(HomePageActivity.this,ShopsPageActivity.class);
                    finish();
                    startActivity(intent);
                    break;
                case R.id.homeToMessage:
                    intent = new Intent(HomePageActivity.this,MessaegPageActivity.class);
                    finish();
                    startActivity(intent);
                    break;
                case R.id.homeToSelf:
                    intent = new Intent(HomePageActivity.this,SelfPageActivity.class);
                    finish();
                    startActivity(intent);
                    break;
                case R.id.searchBar:
                    intent = new Intent(HomePageActivity.this, SearchPageActivity.class);
                    startActivity(intent);
                    flag = false;
                    break;

            }
            if(flag)HomePageActivity.this.finish();
        }
    }

    private boolean getHomePage(){
        String back_url = PropertiesManager.getProps(HomePageActivity.this, "config.properties", "back_url");
        HttpAttribute httpAttribute = Request.Post(null,  back_url + "/homepageinfo/get", HomePageActivity.this);
        httpAttribute.show();
        if(httpAttribute.getCode() == 0) {

            List<Object> tmp = (List<Object>)httpAttribute.getData();
            List<HomePageInfo> list = new ArrayList<HomePageInfo>();
            HomePageInfo info;
            Iterator it = tmp.iterator();
            myapp.setHomepageRightInfo(new ArrayList<HomePageInfo>());
            myapp.setHomepageLeftInfo(new ArrayList<HomePageInfo>());

            myapp.setHomepageRightInfo(new ArrayList<HomePageInfo>());
            myapp.setHomepageLeftInfo(new ArrayList<HomePageInfo>());
            int length = tmp.size()%2 == 1 ? tmp.size()/2 + 1 : tmp.size()/2;
            boolean flag = false;

            while(it.hasNext()){
                info = new HomePageInfo();
                Map<String, Object> a = (Map<String, Object>)it.next();
                info.setHomepageID(Double.valueOf(String.valueOf(a.get("homepageID"))).longValue());
                info.setHomepageGoodId(Double.valueOf(String.valueOf(a.get("homepageGoodId"))).longValue());
                info.setHomepageTitle(String.valueOf(a.get("homepageTitle")));
                try {
                    String url = URLDecoder.decode(String.valueOf(a.get("homepageImage")),"UTF-8");
                    if(url.substring(0, 1).equals("/")){
                        url = back_url + url;
                    }
                    info.setHomepageImage(url);
                } catch (UnsupportedEncodingException e) {
                    finish();
                }
                info.setHomepageState(String.valueOf(a.get("homepageState")));

                length--;
                list.add(info);
                if(length == 0 && flag == false){
                    myapp.setHomepageLeftInfo(list);
                    length = tmp.size()/2;
                    list = new ArrayList<HomePageInfo>();
                    flag = true;
                }
            }
            myapp.setHomepageRightInfo(list);


            return true;
        }
        else{
            Toast.makeText(HomePageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
/*
    public void initItems(){
        HomePageInfo itm1 = new HomePageInfo(1L,
                "菠萝眼镜派对用品眼镜 搞怪眼镜派对装饰眼镜 海边 夏威夷",
                "https://g-search3.alicdn.com/img/bao/uploaded/i4/i2/2206597486006/O1CN019giz7x1uEnLmb9qUC_!!0-item_pic.jpg_180x180.jpg_.webp",
                2L,
                "1");
        myapp.insertHomepageLeftInfo(itm1);
        myapp.insertHomepageLeftInfo(itm1);
        myapp.insertHomepageLeftInfo(itm1);
        myapp.insertHomepageLeftInfo(itm1);
        myapp.insertHomepageRightInfo(itm1);
        myapp.insertHomepageRightInfo(itm1);
        myapp.insertHomepageRightInfo(itm1);
        myapp.insertHomepageRightInfo(itm1);
    }
*/
}