package com.Hashqi.try_on_client.Page.goodPage;

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

public class GoodCommentAdapter extends ArrayAdapter {
    private final int resourceId;
    private String back_url;

    public GoodCommentAdapter(Context context, int textViewResource, List<CommentItem> objects, String url){
        super(context,textViewResource,objects);
        resourceId = textViewResource;
        back_url = url;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        CommentItem item = (CommentItem) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);

        SquareImageView avaView = view.findViewById(R.id.comm_userAva);
        TextView nameView = view.findViewById(R.id.username);
        TextView commView = view.findViewById(R.id.usercomm);

        nameView.setText(item.getUserName());
        commView.setText(item.getComm());
        Glide.with(getContext()).load(back_url + item.getAvaURL()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(avaView);

        return view;
    }
}
