package com.Hashqi.try_on_client.Page.addGoodsPage;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Hashqi.try_on_client.MyApp;
import com.Hashqi.try_on_client.Page.addCommunityPage.AddCommunityPageActivity;
import com.Hashqi.try_on_client.Page.goodPage.CommentItem;
import com.Hashqi.try_on_client.Page.logInPage.LogInPageActivity;
import com.Hashqi.try_on_client.Page.selfPage.SelfPageActivity;
import com.Hashqi.try_on_client.R;
import com.Hashqi.try_on_client.ServiceItem.UserInfo;
import com.Hashqi.try_on_client.Util.ContentUriUtil;
import com.Hashqi.try_on_client.Util.GsonUtil;
import com.Hashqi.try_on_client.Util.HttpAttribute;
import com.Hashqi.try_on_client.Util.PropertiesManager;
import com.Hashqi.try_on_client.Util.Request;
import com.Hashqi.try_on_client.Util.UploadUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wildma.pictureselector.PictureSelector;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AddGoodsPageActivity extends AppCompatActivity {

    MyApp myApp;
    EditText v1, v2, v3;
    Spinner v4;
    Button v5, v6, subGood;
    String img, model;
    ImageView back;
    List<String> classList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods_page);
        myApp = (MyApp)getApplication();

        if(myApp.getUserInfo() == null){
            Toast.makeText(AddGoodsPageActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddGoodsPageActivity.this, LogInPageActivity.class);
            startActivity(intent);
            finish();
            return ;
        }

        v1 = findViewById(R.id.value1);
        v2 = findViewById(R.id.value2);
        v3 = findViewById(R.id.value3);
        v4 = findViewById(R.id.value4);
        v5 = findViewById(R.id.value5);
        v6 = findViewById(R.id.value6);
        back = findViewById(R.id.iv_back_);
        subGood = findViewById(R.id.subGood);

        getClassList();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,classList);
        v4.setAdapter(adapter);
        setListener();

    }


    private void setListener(){
        OnClick onClick = new OnClick();
        back.setOnClickListener(onClick);
        v5.setOnClickListener(onClick);
        v6.setOnClickListener(onClick);
        subGood.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.iv_back_:
                    finish();
                    break;
                case R.id.value5:
                        PictureSelector
                                .create(AddGoodsPageActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                                .selectPicture(true, 200, 200, 1, 1);
                    break;
                case R.id.value6:
                    openSystemFile();
                    break;
                case R.id.subGood:
                    if(submitGood()){
                        Toast.makeText(AddGoodsPageActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                this.img = data.getStringExtra(PictureSelector.PICTURE_PATH);
            }
        }

        if(data.getClipData() != null) {//有选择多个文件
            Toast.makeText(AddGoodsPageActivity.this, "请选择单个文件", Toast.LENGTH_SHORT).show();

            int count = data.getClipData().getItemCount();
            int currentItem = 0;

            while(currentItem < count) {
                Uri imageUri = data.getClipData().getItemAt(currentItem).getUri();
                String imgpath = ContentUriUtil.getPath(this,imageUri);

                //do something with the image (save it to some directory or whatever you need to do with it here)
                currentItem = currentItem + 1;
            }
        } else if(data.getData() != null) {//只有一个文件咯
            model = ContentUriUtil.getPath(this, data.getData());
        }

    }

    private boolean subImg(String name){
        String back_url = PropertiesManager.getProps(AddGoodsPageActivity.this, "config.properties", "back_url");
        String picturePath = img;
        final Map<String, File> files = new HashMap<String, File>();
        final Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        files.put("img", new File(picturePath));
        try {
            final String request = UploadUtil.post(back_url + "/goods/img", params, files);
            final HttpAttribute httpAttribute = GsonUtil.GsonToBean(request, HttpAttribute.class);
            if(httpAttribute.getCode() == 0){
                Map<String, Object>tmp = (Map<String, Object>)httpAttribute.getData();
                img = String.valueOf(tmp.get("url"));
                return true;
            }
            else{
                Toast.makeText(AddGoodsPageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (IOException e) {
            Toast.makeText(AddGoodsPageActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }


    private boolean subMod(String name){
        String back_url = PropertiesManager.getProps(AddGoodsPageActivity.this, "config.properties", "back_url");
        String picturePath = model;
        final Map<String, File> files = new HashMap<String, File>();
        final Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        files.put("model", new File(picturePath));
        try {
            final String request = UploadUtil.post(back_url + "/goods/model", params, files);
            final HttpAttribute httpAttribute = GsonUtil.GsonToBean(request, HttpAttribute.class);
            if(httpAttribute.getCode() == 0){
                Map<String, Object>tmp = (Map<String, Object>)httpAttribute.getData();
                model = String.valueOf(tmp.get("url"));
                return true;
            }
            else{
                Toast.makeText(AddGoodsPageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (IOException e) {
            Toast.makeText(AddGoodsPageActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }

    private boolean submitGood(){
        String title = v1.getText().toString();
        String price = v2.getText().toString();
        String url = v3.getText().toString();
        String classSelected = v4.getSelectedItem().toString();
        Long userID = myApp.getUserInfo().getUserId();

        if(!subImg(title)){
            return false;
        }
        if(!subMod(title)){
            return false;
        }

        Submit submit = new Submit();
        submit.cid = String.valueOf(classList.indexOf(classSelected) + 1);
        submit.des = title;
        submit.img = img;
        submit.murl = model;
        submit.uid = userID.toString();
        submit.url = url;
        submit.price = price;

        String back_url = PropertiesManager.getProps(AddGoodsPageActivity.this, "config.properties", "back_url");
        HttpAttribute httpAttribute = Request.Post(GsonUtil.BeanToJson(submit), back_url + "/goods/set", AddGoodsPageActivity.this);
        if (httpAttribute.getCode() == 0) {
            return true;
        }
        else{
            Toast.makeText(AddGoodsPageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean getClassList() {
        String back_url = PropertiesManager.getProps(AddGoodsPageActivity.this, "config.properties", "back_url");
        HttpAttribute httpAttribute = Request.Post(null, back_url + "/class/gets", AddGoodsPageActivity.this);
        httpAttribute.show();
        classList = new ArrayList<String>();
        if (httpAttribute.getCode() == 0) {
            List<Object> data = (List<Object>)httpAttribute.getData();
            Iterator it = data.iterator();
            while(it.hasNext()){
                Map<String, Object> tmp = (Map<String, Object>)it.next();
                String a = String.valueOf(tmp.get("className"));
                classList.add(a);
            }
            return true;
        }
        else{
            Toast.makeText(AddGoodsPageActivity.this, httpAttribute.getMsg(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public void openSystemFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);//打开多个文件
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try{
            startActivityForResult(Intent.createChooser(intent,"请选择文件"),1);
        }catch (ActivityNotFoundException e){
            e.printStackTrace();
            Toast.makeText(this,"请安装文件管理器",Toast.LENGTH_SHORT);
        }
    }

    class Submit{
        String uid, cid, des, img, url, murl, price;
    }
}
