package com.withpet.newsfeed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;
import com.withpet.main.*;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

//MyFeedAdapter function
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private ArrayList<Feed> myfeed;
    private Context context; //선택한 activity action 내용
    private boolean like_click = false;
    private Intent nextReply, modify;
    private String newFeedMenu;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseUser loginUser = FirebaseAuth.getInstance().getCurrentUser();
    private ArrayList<Feed> choiceModify;
    private User userinfo;

    //생성자
    public FeedAdapter(ArrayList<Feed> myfeed, Context context) {
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
    public void onBindViewHolder(@NonNull final FeedViewHolder holder, int position) {

        //각 게시글의 닉네임, 프로필이미지
        SharedPreferences preferences = context.getSharedPreferences(myfeed.get(position).getUid(), Context.MODE_PRIVATE);
        String nickName = preferences.getString("nickName", "host");
        String feeduserImg = preferences.getString("img","");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //추가 부분
        userinfo = new User();
        userinfo.setUid(myfeed.get(position).getUid());
        userinfo.setNickname(nickName);
        userinfo.setImgUrl(feeduserImg);



        //로그인한 사용자의 프로필이미지
        SharedPreferences sharedPreferences = context.getSharedPreferences(firebaseUser.getUid(), Context.MODE_PRIVATE);
        String loginImg = sharedPreferences.getString("img", "");
       // Log.i("login img", ""+ loginImg);

        Glide.with(holder.itemView)
                    .load(myfeed.get(position).getImgUrl())
                    .into(holder.img);

        Glide.with(holder.itemView).load(loginImg).circleCrop().into(holder.loginUserImg);
        holder.name.setText(nickName);
        //추가 부분
        holder.name.setOnClickListener(onClickListener);
        holder.name.setTag(R.integer.userinfo, userinfo);
        holder.context.setText(myfeed.get(position).getContext());

        // 좋아요 버튼에 해당 개시글 이름을 tag에 저장
        holder.btnLike.setTag(R.integer.key_NewsName, myfeed.get(position).getNewsName());
        holder.btnLike.setOnClickListener(onClickListener);

        // 댓글 버튼에 해당 개시글 이름을 tag에 저장
        holder.btnReply.setTag(R.integer.key_NewsName, myfeed.get(position).getNewsName());
        holder.btnReply.setOnClickListener(onClickListener);

        // 메뉴 버튼에 해당 게시글 이름, UID 저장
        holder.btnMenu.setTag(R.integer.key_NewsName, myfeed.get(position).getNewsName());
        holder.btnMenu.setTag(R.integer.feed_Uid, myfeed.get(position).getUid());
        holder.btnMenu.setOnClickListener(onClickListener);

        // 댓글 버튼에 해당 개시글 이름을 tag에 저장
        holder.btnReplyEnter.setTag(R.integer.key_NewsName, myfeed.get(position).getNewsName());
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
        EditText etReply;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            btnLike = (Button) itemView.findViewById(R.id.mainBtn_like);
            btnReply = (Button) itemView.findViewById(R.id.mainBtn_reply);
            btnMenu =(Button) itemView.findViewById(R.id.newsBtn_menu);
            btnReplyEnter = (Button) itemView.findViewById(R.id.newsBtn_reply);
            loginUserImg = (CircleImageView) itemView.findViewById(R.id.newsIv_reply);
            etReply =(EditText) itemView.findViewById(R.id.newsEt_reply);

            //button 디폴트 이미지 설정
            btnLike.setBackgroundResource(R.drawable.iconlike);
            btnReply.setBackgroundResource(R.drawable.iconreply);
            btnMenu.setBackgroundResource(R.drawable.iconmenu);

            this.name = itemView.findViewById(R.id.mainTv_name);
            this.img = itemView.findViewById(R.id.mainImage);
            this.context = itemView.findViewById(R.id.mainTv_context);
            this.tvCountLike = itemView.findViewById(R.id.mainTv_count);

            //댓글 작성 버튼 이벤트
            // holder.btnReplyEnter.setOnClickListener(onClickListener); :: 이방법으로 진행할경우 EditText 가 Null 됨ㅠㅠ
            btnReplyEnter.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View view) {
                    String strReply = etReply.getText().toString();

                    //게시글 번호
                    String newsReplyEnter = ""+view.getTag(R.integer.key_NewsName);

                    //댓글 번호
                    long now = System.currentTimeMillis();
                    Date mDate = new Date(now);
                    SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String getTime = mFormat.format(mDate);

                    //Reply 테이블 파베 연동
                    DatabaseReference dbRefReply = firebaseDatabase.getReference("Reply");
                    Reply inputReply = new Reply(loginUser.getUid()+getTime,loginUser.getUid(),newsReplyEnter, strReply);
                    dbRefReply.child(newsReplyEnter).child(loginUser.getUid()+getTime).setValue(inputReply);
                }
            });
        }
    }

   View.OnClickListener onClickListener = new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           switch (view.getId()){
               case R.id.mainBtn_like:
                   final String newsFeedName = ""+view.getTag(R.integer.key_NewsName);
                   like_click = !like_click;
                   //Like 테이블 파베 연동
                   DatabaseReference dbRefLike = firebaseDatabase.getReference("Like");
                   if(like_click){
                       view.setBackgroundResource(R.drawable.iconlike);
                       //Like 테이블에서 로그인 유저의 좋아요 삭제
                       dbRefLike.child(newsFeedName).child(loginUser.getUid()).removeValue();
                   }
                   else {
                       view.setBackgroundResource(R.drawable.iconlike2);
                       //Like 테이블에서 로그인 유저의 좋아요 등록
                       dbRefLike.child(newsFeedName).child(loginUser.getUid()).setValue(1);
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
                   //게시글 UID
                   String newsFeedUid = "" +view.getTag(R.integer.feed_Uid);
                   //게시글 번호
                   newFeedMenu = ""+view.getTag(R.integer.key_NewsName);
                   FirebaseUser loginUser = FirebaseAuth.getInstance().getCurrentUser();
                   //로그인한 회원이 작성한 게시글일 경우
                   if(newsFeedUid.equals(loginUser.getUid())){
                       //팝업메뉴 객체 생성
                       PopupMenu popupMenu = new PopupMenu(context, view);
                       //레이아웃xml에 정의한 메뉴 가져오기
                       MenuInflater inflater = popupMenu.getMenuInflater();
                       Menu menu = popupMenu.getMenu();
                       //메뉴item 연결
                       inflater.inflate(R.menu.feedmenuitem_my, menu);

                       popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                           @Override
                           public boolean onMenuItemClick(MenuItem item) {
                               switch(item.getItemId()){
                                   case R.id.feedmenu_del:
                                       //선택한 게시글 삭제
                                       DatabaseReference feedRemove  = firebaseDatabase.getReference("Feed");
                                       feedRemove.child(newFeedMenu).removeValue();
                                       //선택한 게시글 좋아요 삭제
                                       DatabaseReference likeRemove = firebaseDatabase.getReference("Like");
                                       likeRemove.child(newFeedMenu).removeValue();
                                       //선택한 게시글 댓글 삭제
                                       DatabaseReference replyRemove = firebaseDatabase.getReference("Reply");
                                       replyRemove.child(newFeedMenu).removeValue();
                                       Toast.makeText(context, "해당 게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                       break;
                                   case R.id.feedmenu_mod:
                                       DatabaseReference feedModify = firebaseDatabase.getReference("Feed");
                                       choiceModify = new ArrayList<>();
                                       feedModify.child(newFeedMenu).addListenerForSingleValueEvent(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                                               Feed feed = new Feed();
                                               choiceModify.add(0,snapshot.getValue(Feed.class));
                                               modify = new Intent(context, FeedWriteActivity.class);
                                               modify.putExtra("feedName",choiceModify.get(0).getNewsName());
                                               modify.putExtra("feedContext",choiceModify.get(0).getContext());
                                               modify.putExtra("feedImg",choiceModify.get(0).getImgUrl());
                                               modify.putExtra("feedUid",choiceModify.get(0).getUid());
                                               modify.putExtra("feedDate",choiceModify.get(0).getDate());
                                               context.startActivity(modify);
                                           }

                                           @Override
                                           public void onCancelled(@NonNull DatabaseError error) {

                                           }
                                       });
                                       break;
                               }
                               return false;
                           }
                       });
                        //메뉴 보여주기
                       popupMenu.show();
                   }
                   //다른 사람의 게시글일 경우
                   else{
                       //팝업메뉴 객체 생성
                       PopupMenu popupMenu = new PopupMenu(context, view);
                       //레이아웃xml에 정의한 메뉴 가져오기
                       MenuInflater inflater = popupMenu.getMenuInflater();
                       Menu menu = popupMenu.getMenu();
                       //메뉴item 연결
                       inflater.inflate(R.menu.feedmenuitem_other, menu);
                       popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                           @Override
                           public boolean onMenuItemClick(MenuItem item) {
                               switch (item.getItemId()){
                                   case R.id.feedmenu_report:
                                       //게시글 번호와 로그인유저
                                       Toast.makeText(context, ""+ newFeedMenu, Toast.LENGTH_SHORT).show();
                                       break;
                               }
                               return false;
                           }
                       });

                       popupMenu.show();
                   }

                   break;
                   //추가부분
               case R.id.mainTv_name:
                   TransUser tuser = new TransUser(userinfo);
                   Intent intent = new Intent(context, MainActivity.class);
                   intent.putExtra("frag", 3);
                   intent.putExtra("userinfo", tuser);
                   context.startActivity(intent);
                   ((Activity)context).finish();
                   break;
           }
       }
   };

}
