package com.withpet.walk;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.withpet.R;

import java.util.ArrayList;

public class Walk_ReplyAdapter extends RecyclerView.Adapter<Walk_ReplyAdapter.CustomViewholder> {

    private ArrayList<Walk_ReplyUpload> arrayList;
    private Context context;


    public Walk_ReplyAdapter(ArrayList<Walk_ReplyUpload> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_walk_replyitem, parent, false);
        CustomViewholder holder = new CustomViewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewholder holder, int position) {
        SharedPreferences preferences = context.getSharedPreferences(arrayList.get(position).getWalk_uid(), Context.MODE_PRIVATE);
        String nickName = preferences.getString("nickName", "host");
        holder.name_Tv.setText(nickName);
        holder.reply_Tv.setText(arrayList.get(position).getReply());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewholder extends RecyclerView.ViewHolder {
        TextView name_Tv;
        TextView reply_Tv;
        public CustomViewholder(@NonNull View itemView) {
            super(itemView);
            this.name_Tv = itemView.findViewById(R.id.tv_replyname);
            this.reply_Tv = itemView.findViewById(R.id.tv_reply);
        }
    }
}
