package com.Hashqi.try_on_client.Page.shopDetailPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Hashqi.try_on_client.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class ShopDetailAdapter extends ArrayAdapter {

    private final int resourceId;

    public ShopDetailAdapter (Context context, int textViewResource, List<ShopDetailItem> objects){
        super(context,textViewResource,objects);
        resourceId = textViewResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ShopDetailItem item = (ShopDetailItem) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);

        ImageView lGoodImage = (ImageView) view.findViewById(R.id.LGoodImage);
        TextView lGoodName = (TextView) view.findViewById(R.id.LGoodName);
        Glide.with(getContext()).load(item.getGoodImg()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(lGoodImage);
        lGoodName.setText(item.getGoodTitle());
        return view;
    }
}
