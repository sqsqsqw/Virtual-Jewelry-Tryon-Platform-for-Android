package com.Hashqi.try_on_client.Page.historyPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.Hashqi.try_on_client.Component.SquareImageView;
import com.Hashqi.try_on_client.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter {

    private final int resourceId;
    private String back_url;

    public HistoryAdapter (Context context, int textViewResource, List<HistoryItem> objects, String back_url){
        super(context,textViewResource,objects);
        resourceId = textViewResource;
        this.back_url = back_url;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        HistoryItem item = (HistoryItem) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        SquareImageView shopava = view.findViewById(R.id.shopava);
        TextView goodName = view.findViewById(R.id.goodName);
        TextView shopName = view.findViewById(R.id.shopName);

        Glide.with(getContext()).load(item.getAvaURL()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(shopava);
        goodName.setText(item.getGoodName());
        shopName.setText(item.getShopName());
        return view;
    }

}
