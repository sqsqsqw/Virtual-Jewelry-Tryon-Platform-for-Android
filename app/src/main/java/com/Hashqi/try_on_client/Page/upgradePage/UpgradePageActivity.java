package com.Hashqi.try_on_client.Page.upgradePage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Hashqi.try_on_client.MyApp;
import com.Hashqi.try_on_client.Page.logInPage.LogInPageActivity;
import com.Hashqi.try_on_client.R;
import com.Hashqi.try_on_client.ServiceItem.UserInfo;
import com.Hashqi.try_on_client.Util.GsonUtil;
import com.Hashqi.try_on_client.Util.HttpAttribute;
import com.Hashqi.try_on_client.Util.PropertiesManager;
import com.Hashqi.try_on_client.Util.Request;

public class UpgradePageActivity extends AppCompatActivity {

    ImageView back ;
    Button click;
    MyApp myApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_page);
        back = findViewById(R.id.iv_back_);
        click = findViewById(R.id.clicktoup);
        OnClick onClick = new OnClick();
        back.setOnClickListener(onClick);
        click.setOnClickListener(onClick);
        myApp = (MyApp)getApplication();
    }

    class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.iv_back_:
                    finish();
                    break;
                case R.id.clicktoup:
                    if(myApp.getUserInfo() == null){
                        Toast.makeText(UpgradePageActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                        intent = new Intent(UpgradePageActivity.this, LogInPageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        if(upgrade(myApp.getUserInfo().getUserId())){
                            UserInfo info = myApp.getUserInfo();
                            info.setRoleID(2L);
                            myApp.setUserInfo(info);

                            finish();
                        }
                    }
                    break;
            }
        }
    }
    private boolean upgrade(Long userID){
        String back_url = PropertiesManager.getProps(UpgradePageActivity.this, "config.properties", "back_url");
        Upgrade upgrade = new Upgrade();
        upgrade.userId = userID.toString();
        String str = GsonUtil.BeanToJson(upgrade);
        HttpAttribute httpAttribute = Request.Post(str,  back_url + "/user/upgrade", UpgradePageActivity.this);
        if(httpAttribute.getCode() == 0) {
            Toast.makeText(UpgradePageActivity.this, "升级成功", Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            Toast.makeText(UpgradePageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    class Upgrade{
        public String userId;
    }
}
