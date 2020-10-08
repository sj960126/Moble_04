package com.withpet.newsfeed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReplyActivity extends AppCompatActivity {

    private String boardName;
    private Button btnBefore;
    private CircleImageView civNewsUser;
    private TextView tvNewsId, tvNewsContext;
    private ArrayList<Reply> feedReply;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        
        //화면전환시 아래에서 위로 올라오는 애니메이션 제거
        overridePendingTransition(0, 0);

        Intent intent = getIntent();
        boardName = intent.getStringExtra("boardName");

        btnBefore = (Button) findViewById(R.id.replyBtn_before);
        civNewsUser = (CircleImageView) findViewById(R.id.replyIv_newimg);
        tvNewsId = (TextView) findViewById(R.id.replyTv_newid);
        tvNewsContext =(TextView) findViewById(R.id.replyTv_newcontext);
        recyclerView = (RecyclerView) findViewById(R.id.replyRv);

        //기본설정
        btnBefore.setBackgroundResource(R.drawable.iconbefore);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        //리사이클러뷰 설정
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존 성능 강화
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseDatabase NewsFeedDB =  FirebaseDatabase.getInstance();
        DatabaseReference NewsFeedDBR = NewsFeedDB.getReference("Feed");

        //선택한 게시글 찾기
        Query findNewsFeed = NewsFeedDBR.orderByChild("newsName").equalTo(boardName);
        findNewsFeed.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot user : snapshot.getChildren()){
                        Feed feed = user.getValue(Feed.class);

                        //자세히 보기 게시글의 닉네임, 프로필이미지
                        SharedPreferences preferences = getSharedPreferences(feed.getUid(), Context.MODE_PRIVATE);
                        String nickName = preferences.getString("nickName", "host");
                        String feedImg = preferences.getString("img","");

                        //댓글 게시글 자세히 보기 설정
                        tvNewsId.setText(nickName);
                        tvNewsContext.setText(feed.getContext());
                        Glide.with(recyclerView).load(feedImg).circleCrop().into(civNewsUser);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        feedReply = new ArrayList<>();
        DatabaseReference Replydbr = NewsFeedDB.getReference("Reply");
        Query latelyReply = Replydbr.child(boardName).orderByChild("date");

        latelyReply.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedReply.clear();
                for(DataSnapshot reply: snapshot.getChildren()){
                    feedReply.add(reply.getValue(Reply.class));
                }
                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        adapter = new ReplyAdapter(feedReply, this);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결

        //이전 페이지 이동
        btnBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}