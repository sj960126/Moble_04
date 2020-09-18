package com.withpet.newsfeed;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.withpet.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

//ReplyAdapter function
public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {

    private ArrayList<Reply> reply;
    private Context context; //선택한 activity action 내용

    //생성자
    public ReplyAdapter(ArrayList<Reply> reply, Context context) {
        this.reply = reply;
        this.context = context;
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_replyitem, parent, false);
        ReplyViewHolder holder = new ReplyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        //Glide.with(holder.itemView).load(myfeed.get(position).getImgUrl()).circleCrop().into(holder.loginUserImg);
        holder.name.setText(reply.get(position).getUid());
        holder.context.setText(reply.get(position).getContext());
    }

    @Override
    public int getItemCount() {
        return (reply != null ? reply.size():0);
    }

    public class ReplyViewHolder extends RecyclerView.ViewHolder {
        TextView name, context;
        CircleImageView img;

    public ReplyViewHolder(@NonNull View itemView) {
        super(itemView);

        this.name = itemView.findViewById(R.id.replyTv_nickname);
        //img = itemView.findViewById(R.id.mainImage);
        this.context = itemView.findViewById(R.id.replyTv_re);
        this.img = itemView.findViewById(R.id.replyCiv_re);

        img.setImageResource(R.drawable.userdefault);

    }
}
}
