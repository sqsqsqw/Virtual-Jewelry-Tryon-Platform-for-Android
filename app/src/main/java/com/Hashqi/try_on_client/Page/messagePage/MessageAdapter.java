package com.Hashqi.try_on_client.Page.messagePage;

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



public class MessageAdapter extends ArrayAdapter {
    private final int resourceId;
    private String back_url;

    public MessageAdapter(Context context, int textViewResource, List<Contacts> objects, String url){
        super(context,textViewResource,objects);
        resourceId = textViewResource;
        back_url = url;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        Contacts contacts = (Contacts) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        ImageView contactImage = (ImageView) view.findViewById(R.id.contacts_image);
        TextView contactName = (TextView) view.findViewById(R.id.contacts_title);
        TextView contactMessage = (TextView) view.findViewById(R.id.contacts_message);
        Glide.with(getContext()).load(back_url + contacts.getImageUrl()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(contactImage);
        contactName.setText(contacts.getName());
        contactMessage.setText(contacts.getMessage());
        return view;
    }
}
