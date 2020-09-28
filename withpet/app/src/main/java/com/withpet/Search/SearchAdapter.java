package com.withpet.Search;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.withpet.*;
import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewholder> {
    private ArrayList<ArrayList<String>> arrayList;
    private Context context;

    public SearchAdapter(ArrayList<ArrayList<String>> arrayList,Context context){
        this.arrayList = arrayList;
        this.context = context;
    }



    @NonNull
    @Override
    public SearchViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_feed_item,parent,false);
        SearchAdapter.SearchViewholder holder = new SearchAdapter.SearchViewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewholder holder, int position) {
        Log.i("사이즈"," "+arrayList.size());
        ArrayList<String> arr = arrayList.get(position);
        for(int i=0;i<holder.img_arr.size();i++){
            Glide.with(holder.itemView).load(arr.get(i)).into(holder.img_arr.get(i));
        }

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size():0);
    }


    public class SearchViewholder extends RecyclerView.ViewHolder{

        ArrayList<ImageView> img_arr = new ArrayList<ImageView>();
        public SearchViewholder(@NonNull View itemView) {
            super(itemView);
            img_arr.add((ImageView) itemView.findViewById(R.id.search_1));
            img_arr.add((ImageView) itemView.findViewById(R.id.search_2));
            img_arr.add((ImageView) itemView.findViewById(R.id.search_3));
            img_arr.add((ImageView) itemView.findViewById(R.id.search_4));
            img_arr.add((ImageView) itemView.findViewById(R.id.search_5));
            img_arr.add((ImageView) itemView.findViewById(R.id.search_6));
            img_arr.add((ImageView) itemView.findViewById(R.id.search_7));
        }
    }
}


