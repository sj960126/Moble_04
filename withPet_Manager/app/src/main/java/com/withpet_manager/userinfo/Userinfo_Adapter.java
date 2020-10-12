package com.withpet_manager.userinfo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.withpet_manager.R;
import com.withpet_manager.main.MainActivity;

import java.util.ArrayList;

public class Userinfo_Adapter extends RecyclerView.Adapter<Userinfo_Adapter.CustomViewholder> {
    private ArrayList<Userinfo_getset> arrayList;
    private Context context;

    public Userinfo_Adapter(ArrayList<Userinfo_getset> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public CustomViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_userinfoitem, parent, false);
        CustomViewholder holder = new CustomViewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewholder holder, int position) {
        holder.user_name.setText(arrayList.get(position).getName());
        holder.user_nickname.setText(arrayList.get(position).getNickname());

        Glide.with(holder.itemView)
                .load(arrayList.get(position).getImgUri())
                .into(holder.user_img);
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }


    public class CustomViewholder extends RecyclerView.ViewHolder {
        ImageView user_img;
        TextView user_name;
        TextView user_nickname;
        public CustomViewholder(@NonNull final View itemView) {
            super(itemView);

            this.user_img = itemView.findViewById(R.id.userimg);
            this.user_name = itemView.findViewById(R.id.user_name);
            this.user_nickname = itemView.findViewById(R.id.user_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), MainActivity.class);
                    intent.putExtra("frag",4);
                    intent.putExtra("uid", arrayList.get(getAdapterPosition()).getUid());
                    context.startActivity(intent);
                    Log.i("adpater uid :: ", arrayList.get(getAdapterPosition()).getUid());
                    Toast.makeText(itemView.getContext(), ""+arrayList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
