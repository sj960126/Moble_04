package com.withpet.newsfeed;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.withpet.*;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
        Glide.with(holder.itemView)
                    .load(myfeed.get(position).getImgUrl())
                    //.placeholder(R.drawable.dog)
                    .into(holder.img);
        holder.name.setText(myfeed.get(position).getId());
        holder.context.setText(myfeed.get(position).getContext());
        // 좋아요 버튼에 해당 개시글 이름을 tag에 저장
        holder.btnLike.setTag(R.integer.key_NewsName, myfeed.get(position).getNewsName());
        holder.btnLike.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return (myfeed != null ? myfeed.size():0);
    }

    //viewHolder = listItem
    public class FeedViewHolder extends RecyclerView.ViewHolder {
        //listitem
        TextView name, tvCountLike,context;
        ImageView img;
        CircleImageView replyImg;
        Button btnLike, btnReply, btnMenu, btnReplyEnter;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            btnLike = (Button) itemView.findViewById(R.id.mainBtn_like);
            btnReply = (Button) itemView.findViewById(R.id.mainBtn_reply);
            btnMenu =(Button) itemView.findViewById(R.id.newsBtn_menu);
            btnReplyEnter = (Button) itemView.findViewById(R.id.newsBtn_reply);
            replyImg = (CircleImageView) itemView.findViewById(R.id.newsIv_reply);

            //button 디폴트 이미지 설정
            btnLike.setBackgroundResource(R.drawable.iconlike);
            btnReply.setBackgroundResource(R.drawable.iconreply);
            btnMenu.setBackgroundResource(R.drawable.iconmenu);
            replyImg.setImageResource(R.drawable.dog);
            //Glide.with(this).load(R.drawable.sample).circleCrop().into(view2);

            this.name = itemView.findViewById(R.id.mainTv_name);
            this.img = itemView.findViewById(R.id.mainImage);
            this.context = itemView.findViewById(R.id.mainTv_context);
            this.tvCountLike = itemView.findViewById(R.id.mainTv_renum);
        }
    }

   View.OnClickListener onClickListener = new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           switch (view.getId()){
               case R.id.mainBtn_like:
                   String newsFeedName = ""+view.getTag(R.integer.key_NewsName);
                   like_click = !like_click;

                   //Like 테이블 파베 연동
                   FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                   FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                   DatabaseReference dbRef = firebaseDatabase.getReference("Like");

                   if(like_click){
                       view.setBackgroundResource(R.drawable.iconlike);
                       //Like 테이블에서 로그인 유저의 좋아요 삭제
                       dbRef.child(newsFeedName).child(user.getUid()).removeValue();
                   }
                   else {
                       view.setBackgroundResource(R.drawable.iconlike2);
                       //Like 테이블에서 로그인 유저의 좋아요 등록
                       dbRef.child(newsFeedName).child(user.getUid()).setValue(1)
                               .addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void aVoid) {
                                       // Write was successful!
                                       Toast.makeText(context, "좋아요!", Toast.LENGTH_SHORT).show();
                                   }
                               });
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
               case R.id.newsBtn_menu:
                   break;
               case R.id.newsBtn_reply:
                   break;
           }
       }
   };
}
