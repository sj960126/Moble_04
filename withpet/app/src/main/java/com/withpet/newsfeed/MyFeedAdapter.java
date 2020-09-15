package com.withpet.newsfeed;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.withpet.*;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

//MyFeedAdapter function
public class MyFeedAdapter extends RecyclerView.Adapter<MyFeedAdapter.FeedViewHolder> {

    private ArrayList<News> myfeed;
    private Context context; //선택한 activity action 내용
    private boolean like_click = false;
    private Intent nextReply;
    private EditText et;
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
        //Glide.with(holder.itemView).load(myfeed.get(position).getImgUrl()).circleCrop().into(holder.loginUserImg);
        holder.name.setText(myfeed.get(position).getId());
        holder.context.setText(myfeed.get(position).getContext());
        // 좋아요 버튼에 해당 개시글 이름을 tag에 저장
        holder.btnLike.setTag(R.integer.key_NewsName, myfeed.get(position).getNewsName());
        holder.btnLike.setOnClickListener(onClickListener);
        // 댓글 버튼에 해당 개시글 이름을 tag에 저장
        holder.btnReply.setTag(R.integer.key_NewsName, myfeed.get(position).getNewsName());
        holder.btnReply.setOnClickListener(onClickListener);

        holder.btnMenu.setOnClickListener(onClickListener);
        holder.btnReplyEnter.setTag(R.integer.key_NewsName, myfeed.get(position).getNewsName());
        holder.btnReplyEnter.setOnClickListener(onClickListener);
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
        CircleImageView loginUserImg;
        Button btnLike;
        Button btnReply, btnMenu, btnReplyEnter;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            btnLike = (Button) itemView.findViewById(R.id.mainBtn_like);
            btnReply = (Button) itemView.findViewById(R.id.mainBtn_reply);
            btnMenu =(Button) itemView.findViewById(R.id.newsBtn_menu);
            btnReplyEnter = (Button) itemView.findViewById(R.id.newsBtn_reply);
            loginUserImg = (CircleImageView) itemView.findViewById(R.id.newsIv_reply);
            et =(EditText) itemView.findViewById(R.id.newsEt_reply);

            //button 디폴트 이미지 설정
            btnLike.setBackgroundResource(R.drawable.iconlike);
            btnReply.setBackgroundResource(R.drawable.iconreply);
            btnMenu.setBackgroundResource(R.drawable.iconmenu);
            //loginUserImg.setImageResource(R.drawable.userdefault);

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
                   FirebaseDatabase firebaseDatabaseLike = FirebaseDatabase.getInstance();
                   FirebaseUser userLike = FirebaseAuth.getInstance().getCurrentUser();
                   DatabaseReference dbRefLike = firebaseDatabaseLike.getReference("Like");

                   if(like_click){
                       view.setBackgroundResource(R.drawable.iconlike);
                       //Like 테이블에서 로그인 유저의 좋아요 삭제
                       dbRefLike.child(newsFeedName).child(userLike.getUid()).removeValue();
                   }
                   else {
                       view.setBackgroundResource(R.drawable.iconlike2);
                       //Like 테이블에서 로그인 유저의 좋아요 등록
                       dbRefLike.child(newsFeedName).child(userLike.getUid()).setValue(1)
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
                   //게시글 번호
                   String newsFeedReply = ""+view.getTag(R.integer.key_NewsName);
                   //댓글페이지에 현재 게시글 내용 전달
                   nextReply = new Intent(context, ReplyActivity.class);
                   nextReply.putExtra("boardName", newsFeedReply);
                   context.startActivity(nextReply);
                   break;
               case R.id.newsBtn_menu:
                   break;
               case R.id.newsBtn_reply:
                   //게시글 번호
                   String newsReplyEnter = ""+view.getTag(R.integer.key_NewsName);
                   //Reply 테이블 파베 연동
                   FirebaseDatabase firebaseDatabaseReply = FirebaseDatabase.getInstance();
                   FirebaseUser userReply = FirebaseAuth.getInstance().getCurrentUser();
                   DatabaseReference dbRefReply = firebaseDatabaseReply.getReference("Reply");

                   Reply reply = new Reply("tq", userReply.getUid());

                   dbRefReply.child(newsReplyEnter).child(userReply.getUid()).setValue(reply).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {
                           Toast.makeText(context, "댓글을 등록했습니다.", Toast.LENGTH_SHORT).show();
                       }
                   });
                   break;
           }
       }
   };
}
