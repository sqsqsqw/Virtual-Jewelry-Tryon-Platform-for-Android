package com.Hashqi.try_on_client.Page.messageDetailPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.Hashqi.try_on_client.Component.SquareImageView;
import com.Hashqi.try_on_client.Page.goodPage.CommentItem;
import com.Hashqi.try_on_client.R;
import com.Hashqi.try_on_client.ServiceItem.CommunityCommentItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class MessageCommentAdapter extends ArrayAdapter {
    private final int resourceId;
    private String back_url;

    public MessageCommentAdapter(Context context, int textViewResource, List<CommunityCommentItem> objects, String url){
        super(context,textViewResource,objects);
        resourceId = textViewResource;
        back_url = url;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        CommunityCommentItem item = (CommunityCommentItem) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);

        SquareImageView avaView = view.findViewById(R.id.comm_userAva);
        TextView nameView = view.findViewById(R.id.username);
        TextView commView = view.findViewById(R.id.usercomm);
        nameView.setText(item.getUserName());
        commView.setText(item.getCommunityCommentContext());
        Glide.with(getContext()).load(back_url + item.getUserAva()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(avaView);

        return view;
    }
}
