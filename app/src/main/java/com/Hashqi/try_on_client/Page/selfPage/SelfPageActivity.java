package com.Hashqi.try_on_client.Page.selfPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Hashqi.try_on_client.MyApp;
import com.Hashqi.try_on_client.Page.aboutUsPage.AboutUsPageActivity;
import com.Hashqi.try_on_client.Page.addGoodsPage.AddGoodsPageActivity;
import com.Hashqi.try_on_client.Page.goodPage.GoodPageActivity;
import com.Hashqi.try_on_client.Page.historyPage.HistoryPageActivity;
import com.Hashqi.try_on_client.Page.homePage.HomePageActivity;
import com.Hashqi.try_on_client.Page.logInPage.LogInPageActivity;
import com.Hashqi.try_on_client.Page.messagePage.MessaegPageActivity;
import com.Hashqi.try_on_client.Page.selfCollectPage.SelfCollectPageActivity;
import com.Hashqi.try_on_client.Page.selfInfoPage.SelfInfoPageActivity;
import com.Hashqi.try_on_client.Page.shopsPage.ShopsPageActivity;
import com.Hashqi.try_on_client.Page.upgradePage.UpgradePageActivity;
import com.Hashqi.try_on_client.R;
import com.Hashqi.try_on_client.ServiceItem.UserInfo;
import com.Hashqi.try_on_client.Util.GsonUtil;
import com.Hashqi.try_on_client.Util.HttpAttribute;
import com.Hashqi.try_on_client.Util.PropertiesManager;
import com.Hashqi.try_on_client.Util.UploadUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wildma.pictureselector.PictureSelector;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class SelfPageActivity extends AppCompatActivity {
    private ImageButton toHome,toShop,toMessage;
    private Button logOut;
    private LinearLayout set1, set2, set3, set4, set5, set6;
    MyApp myApp;
    ImageView ava;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_page);
        myApp = (MyApp)getApplication();
        toHome = findViewById(R.id.selfToHome);
        toShop = findViewById(R.id.selfToShop);
        toMessage = findViewById(R.id.selfToMessage);
        logOut = findViewById(R.id.logOut);
        set1 = findViewById(R.id.set1);
        set2 = findViewById(R.id.set2);
        set3 = findViewById(R.id.set3);
        set4 = findViewById(R.id.set4);
        set5 = findViewById(R.id.set5);
        set6 = findViewById(R.id.set6);
        ava = findViewById(R.id.userAva);
        setListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        UserInfo user = myApp.getUserInfo();
        String back_url = PropertiesManager.getProps(SelfPageActivity.this, "config.properties", "back_url");
        if(user != null && user.getUserId() != 0L){
            new Thread(() -> {
                Glide.get(SelfPageActivity.this).clearDiskCache();
            }).start();
            Glide.get(this).clearMemory();
            Glide.with(this).load(back_url + user.getUserAvatar()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(ava);
            logOut.setText("退出登录");
            if(user.getRoleID() == 2){
                set6.setVisibility(View.VISIBLE);
            }
            else{
                set6.setVisibility(View.GONE);
            }
        }
        else{
            ava.setImageResource(R.drawable.default_ava);
            logOut.setText("点击登录");
            set6.setVisibility(View.GONE);
        }
    }

    private void setListener(){
        SelfPageActivity.OnClick onClick = new SelfPageActivity.OnClick();
        toHome.setOnClickListener(onClick);
        toShop.setOnClickListener(onClick);
        toMessage.setOnClickListener(onClick);
        logOut.setOnClickListener(onClick);
        set1.setOnClickListener(onClick);
        set2.setOnClickListener(onClick);
        set3.setOnClickListener(onClick);
        set4.setOnClickListener(onClick);
        set5.setOnClickListener(onClick);
        set6.setOnClickListener(onClick);
        ava.setOnClickListener(onClick);
    }
    class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.selfToHome:
                    intent = new Intent(SelfPageActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.selfToShop:
                    intent = new Intent(SelfPageActivity.this, ShopsPageActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.selfToMessage:
                    intent = new Intent(SelfPageActivity.this, MessaegPageActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.logOut:
                    if(myApp.getUserInfo() != null)
                        myApp.setUserInfo(null);
                    intent = new Intent(SelfPageActivity.this, LogInPageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.set1:
                    intent = new Intent(SelfPageActivity.this, SelfInfoPageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.set2:
                    intent = new Intent(SelfPageActivity.this, HistoryPageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.set3:
                    intent = new Intent(SelfPageActivity.this, SelfCollectPageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.set4:
                    intent = new Intent(SelfPageActivity.this, UpgradePageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.set5:
                    intent = new Intent(SelfPageActivity.this, AboutUsPageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.set6:
                    intent = new Intent(SelfPageActivity.this, AddGoodsPageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.userAva:
                    if(myApp.getUserInfo() != null)
                    PictureSelector
                            .create(SelfPageActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                            .selectPicture(true, 200, 200, 1, 1);
                    else {
                        Toast.makeText(SelfPageActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                        intent = new Intent(SelfPageActivity.this, LogInPageActivity.class);
                        startActivity(intent);
                    }
                    break;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                String back_url = PropertiesManager.getProps(SelfPageActivity.this, "config.properties", "back_url");
                String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
                final Map<String, File> files = new HashMap<String, File>();
                final Map<String, String> params = new HashMap<String, String>();
                params.put("userId", myApp.getUserInfo().getUserId().toString());
                files.put("avatar", new File(picturePath));
                try {
                    final String request = UploadUtil.post(back_url + "/user/avatar", params, files);
                    final HttpAttribute httpAttribute = GsonUtil.GsonToBean(request, HttpAttribute.class);
                    if(httpAttribute.getCode() == 0){
                        Toast.makeText(SelfPageActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        UserInfo userInfo = myApp.getUserInfo();
                        userInfo.setUserAvatar((String)httpAttribute.getData());
                        myApp.setUserInfo(userInfo);

                        Glide.with(this).load(back_url + (String)httpAttribute.getData()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(ava);
                    }
                    else{
                        Toast.makeText(SelfPageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(SelfPageActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SelfPageActivity.this,HomePageActivity.class);
        startActivity(intent);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
