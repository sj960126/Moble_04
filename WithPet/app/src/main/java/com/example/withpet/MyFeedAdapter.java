package com.example.withpet;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

//MyFeedAdapter function
public class MyFeedAdapter extends RecyclerView.Adapter<MyFeedAdapter.FeedViewHolder> {

    private ArrayList<News> myfeed;
    private Context context; //선택한 activity action 내용

    //생성자
    public MyFeedAdapter(ArrayList<News> myfeed, Context context) {
        this.myfeed = myfeed;
        this.context = context;
    }


    //리스트뷰아이템 생성 실행되는 곳
    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_newsitem, parent, false);
        FeedViewHolder holder = new FeedViewHolder(view);
        return holder;
    }

    //매칭
    //이미지 서버에서 이미지 불러오기
    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        Log.i("url",""+myfeed.get(position).getImgUrl());
        Glide.with(holder.itemView)
                    .load(myfeed.get(position).getImgUrl())
                    //.placeholder(R.drawable.dog)
                    .into(holder.img);

        holder.name.setText(myfeed.get(position).getId());
        holder.context.setText(myfeed.get(position).getContext());
    }

    @Override
    public int getItemCount() {

        return (myfeed != null ? myfeed.size():0);
    }

    //viewHolder = listItem
    public class FeedViewHolder extends RecyclerView.ViewHolder {
        //listitem
        TextView name;
        ImageView img;
        TextView context;
        Button like;
        Button reply;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            like = (Button) itemView.findViewById(R.id.mainBtn_like);
            reply = (Button) itemView.findViewById(R.id.mainBtn_reply);

            like.setBackgroundResource(R.drawable.iconlike);
            reply.setBackgroundResource(R.drawable.iconreply);

            this.name = itemView.findViewById(R.id.mainTv_name);
            this.img = itemView.findViewById(R.id.mainImage);
            this.context = itemView.findViewById(R.id.mainTv_context);

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    like.setBackgroundResource(R.drawable.iconlike2);
                }
            });
            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reply.setBackgroundResource(R.drawable.iconreply2);
                }
            });
        }
    }

}