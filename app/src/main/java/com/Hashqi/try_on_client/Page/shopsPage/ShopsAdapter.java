package com.Hashqi.try_on_client.Page.shopsPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Hashqi.try_on_client.Component.SquareImageView;
import com.Hashqi.try_on_client.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShopsAdapter extends ArrayAdapter {

    private final int resourceId;
    private String back_url;

    public ShopsAdapter (Context context, int textViewResource, List<ShopItem> objects, String back_url){
        super(context,textViewResource,objects);
        resourceId = textViewResource;
        this.back_url = back_url;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ShopItem item = (ShopItem) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        ImageView ava = view.findViewById(R.id.shopAva);
        TextView name = view.findViewById(R.id.shopName);
        List<SquareImageView> viewList = new ArrayList<SquareImageView>();
        viewList.add((SquareImageView) view.findViewById(R.id.shop_image_1));
        viewList.add((SquareImageView) view.findViewById(R.id.shop_image_2));
        viewList.add((SquareImageView) view.findViewById(R.id.shop_image_3));
        viewList.add((SquareImageView) view.findViewById(R.id.shop_image_4));
        viewList.add((SquareImageView) view.findViewById(R.id.shop_image_5));
        viewList.add((SquareImageView) view.findViewById(R.id.shop_image_6));

        name.setText(item.getName());
        Glide.with(getContext()).load(item.getAva()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(ava);
        List<String> list = item.getImgs();

        Iterator it1 = list.iterator();
        Iterator it2 = viewList.iterator();
        while(it1.hasNext() && it2.hasNext()){
            SquareImageView imageView = (SquareImageView)it2.next();
            String url = null;
            try {
                url = URLDecoder.decode(String.valueOf(it1.next()), "utf-8");
                if(url.substring(0, 1).equals("/")){
                    url = back_url + url;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Glide.with(getContext()).load(url).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
            System.out.println(url);
        }

        return view;
    }
}
