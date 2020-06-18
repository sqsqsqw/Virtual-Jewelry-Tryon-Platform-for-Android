package com.Hashqi.try_on_client.Page.addCommunityPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Hashqi.try_on_client.MyApp;
import com.Hashqi.try_on_client.Page.goodPage.GoodPageActivity;
import com.Hashqi.try_on_client.Page.logInPage.LogInPageActivity;
import com.Hashqi.try_on_client.Page.messagePage.Contacts;
import com.Hashqi.try_on_client.Page.messagePage.MessaegPageActivity;
import com.Hashqi.try_on_client.Page.selfPage.SelfPageActivity;
import com.Hashqi.try_on_client.Page.upgradePage.UpgradePageActivity;
import com.Hashqi.try_on_client.R;
import com.Hashqi.try_on_client.ServiceItem.UserInfo;
import com.Hashqi.try_on_client.Util.GsonUtil;
import com.Hashqi.try_on_client.Util.HttpAttribute;
import com.Hashqi.try_on_client.Util.PropertiesManager;
import com.Hashqi.try_on_client.Util.Request;

import java.util.List;

public class AddCommunityPageActivity extends AppCompatActivity {

    TextView goodIDView, goodNameView, titleView, contextView;
    ImageView back;
    Button addComm;
    MyApp myApp;
    String goodID, goodName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_community_page);
        goodID = getIntent().getStringExtra("goodID");
        if(goodID == null){
            Toast.makeText(AddCommunityPageActivity.this, "未发现商品序号", Toast.LENGTH_SHORT).show();
            finish();
            return ;
        }

        goodName = getIntent().getStringExtra("goodName");
        if(goodName == null){
            Toast.makeText(AddCommunityPageActivity.this, "未发现商品名称", Toast.LENGTH_SHORT).show();
            finish();
            return ;
        }
        myApp = (MyApp) getApplication();
        goodNameView = findViewById(R.id.goodName);
        goodIDView = findViewById(R.id.goodID);
        back = findViewById(R.id.iv_back_);
        addComm = findViewById(R.id.clickToAddComm);
        titleView = findViewById(R.id.value1);
        contextView = findViewById(R.id.value2);

        goodNameView.setText(goodName);
        goodIDView.setText(goodID);

        setListener();

    }

    private void setListener(){
        OnClick onClick = new OnClick();
        back.setOnClickListener(onClick);
        addComm.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.iv_back_:
                    finish();
                    break;
                case R.id.clickToAddComm:
                    if(submit())
                        finish();
                    break;
            }
        }
    }


    private boolean submit() {
        String back_url = PropertiesManager.getProps(AddCommunityPageActivity.this, "config.properties", "back_url");
        Submit param = new Submit();
        param.goodID = Double.valueOf(this.goodID).longValue();
        param.userID = myApp.getUserInfo().getUserId();
        param.title = titleView.getText().toString();
        param.context = contextView.getText().toString();
        HttpAttribute httpAttribute = Request.Post(GsonUtil.BeanToJson(param), back_url + "/communityDistribute/set", AddCommunityPageActivity.this);
        httpAttribute.show();
        if (httpAttribute.getCode() == 0) {
            Toast.makeText(AddCommunityPageActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            Toast.makeText(AddCommunityPageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    class Submit{
        public Long goodID, userID;
        public String title, context;
    }
}
