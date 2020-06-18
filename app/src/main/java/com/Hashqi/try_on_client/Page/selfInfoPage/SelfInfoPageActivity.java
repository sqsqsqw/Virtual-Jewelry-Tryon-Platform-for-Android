package com.Hashqi.try_on_client.Page.selfInfoPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Hashqi.try_on_client.Component.MyListView;
import com.Hashqi.try_on_client.MyApp;
import com.Hashqi.try_on_client.Page.logInPage.LogInPageActivity;
import com.Hashqi.try_on_client.Page.selfInfoChangePage.SelfInfoChangePageActivity;
import com.Hashqi.try_on_client.R;
import com.Hashqi.try_on_client.ServiceItem.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class SelfInfoPageActivity extends AppCompatActivity {

    MyListView selfInfo;
    MyApp myapp;
    List<SelfInfoItem> selfInfoLists;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_info_page);
        selfInfo = findViewById(R.id.selfInfoListView);
        myapp = (MyApp) getApplication();
        selfInfoLists = new ArrayList<SelfInfoItem>();
        back = findViewById(R.id.iv_back_);

        AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (parent.getId())
                {
                    case R.id.selfInfoListView:
                        expressitemClick(position, selfInfoLists);
                        break;
                }
            }
            public void expressitemClick(int position, List<SelfInfoItem> list){
                Intent intent = new Intent(SelfInfoPageActivity.this, SelfInfoChangePageActivity.class);
                intent.putExtra("param", selfInfoLists.get(position).getLeftName());
                intent.putExtra("value", selfInfoLists.get(position).getRightVal());
                startActivity(intent);
            }
        };
        selfInfo.setOnItemClickListener(itemClick);

        setListener();
    }



    private void setListener(){
        OnClick onClick = new OnClick();
        back.setOnClickListener(onClick);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setAdapter();
        SelfInfoAdapter adapter = new SelfInfoAdapter(SelfInfoPageActivity.this, R.layout.listitem_self_info_layout, selfInfoLists);
        selfInfo.setAdapter(adapter);
    }
    class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.iv_back_:
                    finish();
                    break;
            }
        }
    }

    private void setAdapter(){
        UserInfo userInfo = myapp.getUserInfo();
        if(userInfo == null){
            Toast.makeText(SelfInfoPageActivity.this, "请先登录", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SelfInfoPageActivity.this, LogInPageActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        selfInfoLists = new ArrayList<SelfInfoItem>();
        SelfInfoItem item = new SelfInfoItem();
        item.setLeftName("用户名");
        item.setRightVal(userInfo.getUserName());
        selfInfoLists.add(item);
        item = new SelfInfoItem();
        item.setLeftName("性别");
        item.setRightVal(userInfo.getUserSex());
        selfInfoLists.add(item);
        item = new SelfInfoItem();
        item.setLeftName("电话");
        item.setRightVal(userInfo.getUserPhone().toString());
        selfInfoLists.add(item);
        item = new SelfInfoItem();
        item.setLeftName("邮箱");
        item.setRightVal(userInfo.getUserEmail());
        selfInfoLists.add(item);
        item = new SelfInfoItem();
        item.setLeftName("角色");
        item.setRightVal(userInfo.getRoleID() == 2 ? "店铺用户" : "普通用户");
        selfInfoLists.add(item);
    }
}
