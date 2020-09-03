package com.example.withpet;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Walk_imageAdapter extends ArrayAdapter {
    Context mContext;
    ArrayList<Walk_news> walkfeed = new ArrayList<Walk_news>();
    Walk_imageAdapter adpater;

    public Walk_imageAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = View.inflate(mContext, R.layout.activity_walk_custom, null);
        } else {
            view = convertView;
        }
        Walk_news news = walkfeed.get(position);
        TextView title = (TextView) view.findViewById(R.id.walk_contents);
        TextView content = (TextView) view.findViewById(R.id.walk_content);
        ImageView img = (ImageView) view.findViewById(R.id.imageView);

        title.setText(news.getTitle());
        content.setText(news.getContext());
        img.setImageResource(news.getImgUri());

        return view;
    }
}
