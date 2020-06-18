package com.Hashqi.try_on_client.Page.registerPage;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Hashqi.try_on_client.R;
import com.Hashqi.try_on_client.Util.EncryptionMD5Manager;
import com.Hashqi.try_on_client.Util.GsonUtil;
import com.Hashqi.try_on_client.Util.HttpAttribute;
import com.Hashqi.try_on_client.Util.PropertiesManager;
import com.Hashqi.try_on_client.Util.Request;

public class RegisterPageActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText pwdEditText;
    private EditText pwdAgainEditText;
    private ImageView backBtn;
    private Button regBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        nameEditText = findViewById(R.id.regUser);
        pwdEditText = findViewById(R.id.regPassword);
        pwdAgainEditText = findViewById(R.id.regPasswordAgain);
        backBtn = findViewById(R.id.iv_back_);
        regBtn = findViewById(R.id.reg_btn_reg);
        Drawable user = getResources().getDrawable(R.drawable.icon_user);
        Drawable password = getResources().getDrawable(R.drawable.icon_password);
        user.setBounds(0,0,43,43);
        password.setBounds(0,0,43,43);
        nameEditText.setCompoundDrawables(user,null,null,null);
        pwdEditText.setCompoundDrawables(password,null,null,null);
        pwdAgainEditText.setCompoundDrawables(password,null,null,null);
        setListener();
    }
    private void setListener(){
        OnClick onclick = new OnClick();
        regBtn.setOnClickListener(onclick);
        backBtn.setOnClickListener(onclick);
    }
    class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.reg_btn_reg:
                    String name = nameEditText.getText().toString();
                    String pwd = pwdEditText.getText().toString();
                    String pwdAgain = pwdAgainEditText.getText().toString();
                    if(pwd.equals(pwdAgain)) {
                        if(reg(name, pwd))
                            RegisterPageActivity.this.finish();
                    }
                    else{
                        Toast.makeText(RegisterPageActivity.this, "请填写相同的密码", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.iv_back_:
                    RegisterPageActivity.this.finish();
                    break;
            }
        }
    }

    private boolean reg(String name, String pwd){
        if("".equals(name) || "".equals(pwd)){
            Toast.makeText(RegisterPageActivity.this, "请填写用户名和密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        Reg regitem = new Reg();
        regitem.name = name;
        regitem.passwd = EncryptionMD5Manager.encryption(pwd);
        String str = GsonUtil.BeanToJson(regitem);
        HttpAttribute httpAttribute = Request.Post(str, PropertiesManager.getProps(RegisterPageActivity.this, "config.properties", "back_url") + "/user/reg", RegisterPageActivity.this);
        if(httpAttribute.getCode() == 0) {
            Toast.makeText(RegisterPageActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            Toast.makeText(RegisterPageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    class Reg{
        public String name;
        public String passwd;
    }
}
