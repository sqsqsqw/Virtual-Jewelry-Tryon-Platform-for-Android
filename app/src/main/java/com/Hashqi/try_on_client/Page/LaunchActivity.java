package com.Hashqi.try_on_client.Page;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.Hashqi.try_on_client.Page.homePage.HomePageActivity;
import com.Hashqi.try_on_client.R;


public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动界面
        setContentView(R.layout.activity_launch);
        Integer time = 1500;    //设置等待时间，单位为毫秒
        Handler handler = new Handler();
        //当计时结束时，跳转至主界面
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LaunchActivity.this, HomePageActivity.class));
                LaunchActivity.this.finish();
            }
        }, time);
    }
}
