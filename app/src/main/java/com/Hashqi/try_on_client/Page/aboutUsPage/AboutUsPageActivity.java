package com.Hashqi.try_on_client.Page.aboutUsPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.Hashqi.try_on_client.R;

public class AboutUsPageActivity extends AppCompatActivity {

    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_page);
        back = findViewById(R.id.iv_back_);
        setListener();
    }

    private void setListener(){
        OnClick onClick = new OnClick();
        back.setOnClickListener(onClick);
    }
    class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.iv_back_:
                    finish();
                    break;
            }
        }
    }
}
