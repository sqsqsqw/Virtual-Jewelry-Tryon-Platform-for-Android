package com.Hashqi.try_on_client.Page.selfInfoPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.Hashqi.try_on_client.R;

import java.util.List;

public class SelfInfoAdapter extends ArrayAdapter {

    private final int resourceId;


    public SelfInfoAdapter (Context context, int textViewResource, List<SelfInfoItem> objects){
        super(context,textViewResource,objects);
        resourceId = textViewResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        SelfInfoItem item = (SelfInfoItem) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);

        TextView left = view.findViewById(R.id.leftkey);
        TextView right = view.findViewById(R.id.rightvalue);

        left.setText(item.getLeftName());
        right.setText(item.getRightVal());
        return view;
    }
}
