package com.withpet_manager.Client;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.withpet_manager.*;
import java.util.ArrayList;


public class ClinetAdapter extends RecyclerView.Adapter<ClinetAdapter.Clientview> {
    private  ArrayList<Client> arrayList;
    private  Context context;
    private  ArrayList<String> arrylistkey;

    public ClinetAdapter(ArrayList<Client> arrayList,ArrayList<String> arrylistkey,Context context){
        this.arrayList = arrayList;
        this.context = context;
        this.arrylistkey = arrylistkey;
    }

    @NonNull
    @Override
    public Clientview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_item, parent, false);
        Clientview holder = new Clientview(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Clientview holder, final int position) { //final
        holder.username.setText(arrayList.get(position).getTitle());
        holder.write.setText(arrayList.get(position).getContext());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(),Client_view.class);
                intent.putExtra("category",arrayList.get(position).getCategory());
                intent.putExtra("title",arrayList.get(position).getTitle());
                intent.putExtra("context",arrayList.get(position).getContext());
                intent.putExtra("date",arrayList.get(position).getDate());
                intent.putExtra("feedname",arrayList.get(position).getFeedName());
                intent.putExtra("uid",arrayList.get(position).getUid());
                intent.putExtra("key",arrylistkey.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }


    public class Clientview extends RecyclerView.ViewHolder{
        TextView username;
        TextView write;
        public Clientview(@NonNull View itemView) {
            super(itemView);
            this.username = itemView.findViewById(R.id.clinet_usrname);
            this.write = itemView.findViewById(R.id.client_write);

        }

    }
}
