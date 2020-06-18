package com.Hashqi.try_on_client.Page.selfInfoChangePage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class SelfInfoChangePageActivity extends AppCompatActivity {
    String param,value;
    TextView keyView;
    EditText valView;
    ImageView back;
    Button btn;
    MyApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_info_change_page);
        param = getIntent().getStringExtra("param");
        myApp = (MyApp) getApplication();
        if(param == null ){
            Toast.makeText(SelfInfoChangePageActivity.this, "未发现信息", Toast.LENGTH_SHORT).show();
            finish();
            return ;
        }
        if( param == "角色"){
            finish();
            return ;
        }
        value = getIntent().getStringExtra("value");
        if(value == null){
            Toast.makeText(SelfInfoChangePageActivity.this, "未发现信息", Toast.LENGTH_SHORT).show();
            finish();
            return ;
        }
        keyView = findViewById(R.id.key);
        valView = findViewById(R.id.value);
        back = findViewById(R.id.iv_back_);
        keyView.setText(param);
        valView.setText(value);
        btn = findViewById(R.id.submit_change_info);
        setListener();
    }

    private void setListener(){
        OnClick onClick = new OnClick();
        back.setOnClickListener(onClick);
        btn.setOnClickListener(onClick);

    }
    class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.iv_back_:
                    finish();
                    break;
                case R.id.submit_change_info:
                    if(change())
                        finish();
                    break;
            }
        }
    }
    private boolean change(){
        String back_url = PropertiesManager.getProps(SelfInfoChangePageActivity.this, "config.properties", "back_url");
        ChangeInfo param = new ChangeInfo();
        param.key = this.param;
        param.uid = myApp.getUserInfo().getUserId().toString();
        param.val = valView.getText().toString();

        if(param.val.equals(this.value)){
            Toast.makeText(SelfInfoChangePageActivity.this, "未改动", Toast.LENGTH_SHORT).show();
            return false;
        }
        System.out.println(GsonUtil.BeanToJson(param));

        HttpAttribute httpAttribute = Request.Post(GsonUtil.BeanToJson(param),  back_url + "/user/change", SelfInfoChangePageActivity.this);
        if(httpAttribute.getCode() == 0) {
            Toast.makeText(SelfInfoChangePageActivity.this, "改动成功", Toast.LENGTH_SHORT).show();

            UserInfo user = myApp.getUserInfo();

            switch (this.param){
                case "用户名":
                    user.setUserName(param.val);
                    break;
                case "性别":
                    user.setUserSex(param.val);
                    break;
                case "电话":
                    user.setUserPhone(Long.valueOf(param.val));
                    break;
                case "邮箱":
                    user.setUserEmail(param.val);
                    break;
            }
            myApp.setUserInfo(user);
            return true;
        }
        else{
            Toast.makeText(SelfInfoChangePageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
            if(httpAttribute.getCode() == 2007){
                Intent intent = new Intent(SelfInfoChangePageActivity.this, LogInPageActivity.class);
                startActivity(intent);
            }
            return false;
        }
    }
    class ChangeInfo{
        public String val,key,uid;
    }
}
