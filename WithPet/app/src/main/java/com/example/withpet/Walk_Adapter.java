package com.example.withpet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class Walk_Adapter extends RecyclerView.Adapter<Walk_Adapter.CustomViewholder>{

    private  ArrayList<Walk_news> arrayList;
    private Context context;


    public Walk_Adapter(ArrayList<Walk_news> arrayList,Context context){
        this.arrayList =arrayList;
        this.context = context;

    }
    @NonNull
    @Override
    public CustomViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_walk_custom, parent, false);
        CustomViewholder holder = new CustomViewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewholder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProfile())
                .into(holder.iv_profile);
        holder.tv_title.setText(arrayList.get(position).getTitle());
        holder.tv_contents.setText(arrayList.get(position).getContext());

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewholder extends RecyclerView.ViewHolder {
        ImageView iv_profile;
        TextView tv_title;
        TextView tv_contents;

        public CustomViewholder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.imageView);
            this.tv_title = itemView.findViewById(R.id.walk_title);
            this.tv_contents = itemView.findViewById(R.id.walk_content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Toast.makeText(context, "제목 : " + arrayList.get(pos).getTitle() + "내용 : " + arrayList.get(pos).getContext(), Toast.LENGTH_SHORT).show();

                    if (pos != RecyclerView.NO_POSITION){
                        Toast.makeText(context, "hihi"+pos, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
