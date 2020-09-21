package com.withpet.walk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;
import com.withpet.main.*;

import java.util.ArrayList;

public class Walk_Adapter extends RecyclerView.Adapter<Walk_Adapter.CustomViewholder>{

    private  ArrayList<Walk_boardUpload> arrayList;
    private Context context;
    private Activity activity;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private String walkboard_title;
    private String walkboard_content;
    public Walk_Adapter(ArrayList<Walk_boardUpload> arrayList,Context context,Activity activity){
        this.arrayList =arrayList;
        this.context = context;
        this.activity = activity;

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

        Log.i("adapter:", arrayList.get(position).getWalkboard_title());
        Log.i("adapter:", arrayList.get(position).getWalkboard_content());

        holder.tv_title.setText(arrayList.get(position).getWalkboard_title());
        holder.tv_contents.setText(arrayList.get(position).getWalkboard_content());

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }
//recyclerview 산책 메인
    public class CustomViewholder extends RecyclerView.ViewHolder {
      //  ImageView iv_profile;
        TextView tv_title;
        TextView tv_contents;

        public CustomViewholder(@NonNull final View itemView) {
            super(itemView);
            this.tv_title = itemView.findViewById(R.id.walk_title);
            this.tv_contents = itemView.findViewById(R.id.walk_content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();

                 //   String board_nb = Integer.toString(arrayList.get(pos).getWalkboard_nb());


                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("frag",5); // 작성한 글 frag로 가기위해 intent값 전달
                    intent.putExtra("board_nb",arrayList.get(pos).getWalkboard_nb());
                    context.startActivity(intent);


                    if (pos != RecyclerView.NO_POSITION){

                    }
                }
            });
        }
    }
}
