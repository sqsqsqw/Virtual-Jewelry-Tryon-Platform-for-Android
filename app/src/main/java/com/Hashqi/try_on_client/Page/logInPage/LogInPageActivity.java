package com.Hashqi.try_on_client.Page.logInPage;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Hashqi.try_on_client.MyApp;
import com.Hashqi.try_on_client.Page.registerPage.RegisterPageActivity;
import com.Hashqi.try_on_client.R;
import com.Hashqi.try_on_client.ServiceItem.UserInfo;
import com.Hashqi.try_on_client.Util.EncryptionMD5Manager;
import com.Hashqi.try_on_client.Util.GsonUtil;
import com.Hashqi.try_on_client.Util.HttpAttribute;
import com.Hashqi.try_on_client.Util.PropertiesManager;
import com.Hashqi.try_on_client.Util.Request;

import java.io.File;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.Map;


public class LogInPageActivity extends AppCompatActivity {
    private MyApp myapp;
    private Button btn;
    private EditText usert;
    private EditText passwordt;
    private TextView toReg;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myapp = (MyApp)getApplication();
        btn = findViewById(R.id.login_btn_login);
        usert=findViewById(R.id.logInUser);
        passwordt=findViewById(R.id.logInPassword);
        toReg = findViewById(R.id.toReg);
        back = findViewById(R.id.iv_back_);
        Drawable user = getResources().getDrawable(R.drawable.icon_user);
        Drawable password = getResources().getDrawable(R.drawable.icon_password);
        user.setBounds(0,0,43,43);
        password.setBounds(0,0,43,43);
        usert.setCompoundDrawables(user,null,null,null);
        passwordt.setCompoundDrawables(password,null,null,null);
        setListener();
    }
    private void setListener(){
        OnClick onClick = new OnClick();
        btn.setOnClickListener(onClick);
        toReg.setOnClickListener(onClick);
        back.setOnClickListener(onClick);
    }
    class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.login_btn_login:
                    if(login(usert.getText().toString(), passwordt.getText().toString())){
/*
                        intent = new Intent(LogInPageActivity.this, ShopDetailPage.class);
                        startActivity(intent);*/
                        finish();
                    }
                    break;
                case R.id.toReg:
                    intent = new Intent(LogInPageActivity.this, RegisterPageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.iv_back_:
                    finish();
                    break;
            }
        }

    }

    private boolean login(String name, String pwd){
        if("".equals(name) || "".equals(pwd)){
            Toast.makeText(LogInPageActivity.this, "请填写用户名和密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        Login loginitem = new Login();
        loginitem.name = name;
        loginitem.passwd = EncryptionMD5Manager.encryption(pwd);
        String str = GsonUtil.BeanToJson(loginitem);
        HttpAttribute httpAttribute = Request.Post(str, PropertiesManager.getProps(LogInPageActivity.this, "config.properties", "back_url") + "/user/login", LogInPageActivity.this);
        if(httpAttribute.getCode() == 0) {
            Map<String, Object> data =  (Map<String, Object>)httpAttribute.getData();
            UserInfo user = new UserInfo();
            user.setRoleID(Double.valueOf(String.valueOf(data.get("roleID"))).longValue());
            user.setUserAvatar(String.valueOf(data.get("userAvatar")));

            if (data.get("userEmail") != null) {
                user.setUserEmail(String.valueOf(data.get("userEmail")));
            }
            user.setUserId(Double.valueOf(String.valueOf(data.get("userId"))).longValue());
            user.setUserName(String.valueOf(data.get("userName")));
            if (data.get("userPhone") != null) {
                user.setUserPhone(Double.valueOf(String.valueOf(data.get("userPhone"))).longValue());
            }
            user.setUserSex(String.valueOf(data.get("userSex")));

            myapp.setUserInfo(user);
            return true;
        }
        else{
            Toast.makeText(LogInPageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    class Login{
        public String name;
        public String passwd;
    }

    class Find{
        public int id;
    }

    class Avatar{
        public File avatar;
        public int id;
    }

    class Role{
        public String id;
    }
}
