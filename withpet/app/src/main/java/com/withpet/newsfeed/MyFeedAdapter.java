package com.withpet.newsfeed;

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
import com.withpet.*;

import java.util.ArrayList;

//MyFeedAdapter function
public class MyFeedAdapter extends RecyclerView.Adapter<MyFeedAdapter.FeedViewHolder> {

    private ArrayList<News> myfeed;
    private Context context; //선택한 activity action 내용
    private boolean like_click = false;
    private boolean reply_click = false;

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
        Button btnLike;
        Button btnReply;
        //button 클릭시 확인
        int btn1;
        int btn2;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            btnLike = (Button) itemView.findViewById(R.id.mainBtn_like);
            btnReply = (Button) itemView.findViewById(R.id.mainBtn_reply);

            //button 디폴트 이미지 설정
            btnLike.setBackgroundResource(R.drawable.iconlike);
            btnReply.setBackgroundResource(R.drawable.iconreply);

            this.name = itemView.findViewById(R.id.mainTv_name);
            this.img = itemView.findViewById(R.id.mainImage);
            this.context = itemView.findViewById(R.id.mainTv_context);
            btnLike.setOnClickListener(onClickListener);
            btnReply.setOnClickListener(onClickListener);
        }

    }

   View.OnClickListener onClickListener = new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           switch (view.getId()){
               case R.id.mainBtn_like:
                   int likecount = 0;
                   like_click = !like_click;
                   //button 클릭할때마다 이미지 변경
                   if(like_click){
                       view.setBackgroundResource(R.drawable.iconlike);
                   }
                   else {
                       view.setBackgroundResource(R.drawable.iconlike2);
                   }
                   break;
               case R.id.mainBtn_reply:
                   reply_click = !reply_click;
                   if(reply_click){
                       view.setBackgroundResource(R.drawable.iconreply);
                   }
                   else{
                       view.setBackgroundResource(R.drawable.iconreply2);
                   }
                   break;
           }
       }
   };



}
