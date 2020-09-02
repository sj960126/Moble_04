package com.example.withpet;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter {

    private Context context;

    public int[] galleryArray ={
            R.drawable.dog,R.drawable.dog,R.drawable.dog,R.drawable.dog,R.drawable.dog,
            R.drawable.dog,R.drawable.dog,R.drawable.dog,R.drawable.dog,R.drawable.dog,
            R.drawable.dog,R.drawable.dog,R.drawable.dog,R.drawable.dog,R.drawable.dog
    };

    public GalleryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return galleryArray.length;
    }

    @Override
    public Object getItem(int position) {
        return galleryArray[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ImageView iv = new ImageView(context);
        iv.setImageResource(galleryArray[position]);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setLayoutParams(new GridView.LayoutParams(340,350));

        return iv;
    }
}
