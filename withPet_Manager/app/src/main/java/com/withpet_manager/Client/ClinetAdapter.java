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

    public ClinetAdapter(ArrayList<Client> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Clientview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_item, parent, false);
        Clientview holder = new Clientview(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Clientview holder, int position) {
        holder.username.setText(arrayList.get(position).getTitle());
        holder.write.setText(arrayList.get(position).getContext());
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();

                }
            });
        }

    }
}
