package com.example.withpet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {
    private ArrayList<String> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView layout;
        public MyViewHolder(CardView l) {
            super(l);
            layout = l;
        }
    }

    public GalleryAdapter(ArrayList<String> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GalleryAdapter.MyViewHolder onCreateViewHolder( ViewGroup parent,
                                                     int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_galleryitems, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {
        ImageView iv = holder.layout.findViewById(R.id.mainIv_gal);
        Bitmap bmp = BitmapFactory.decodeFile(mDataset.get(position));
        iv.setImageBitmap(bmp);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

