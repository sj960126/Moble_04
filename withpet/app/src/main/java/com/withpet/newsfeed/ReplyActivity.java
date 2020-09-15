package com.withpet.newsfeed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;
import com.withpet.main.User;

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

        Intent intent = getIntent();
        boardName = intent.getStringExtra("boardName");

        btnBefore = (Button) findViewById(R.id.replyBtn_before);
        civNewsUser = (CircleImageView) findViewById(R.id.replyIv_newimg);
        tvNewsId = (TextView) findViewById(R.id.replyTv_newid);
        tvNewsContext =(TextView) findViewById(R.id.replyTv_newcontext);
        recyclerView = (RecyclerView) findViewById(R.id.replyRv);

        //기본설정
        civNewsUser.setImageResource(R.drawable.userdefault);
        btnBefore.setBackgroundResource(R.drawable.iconbefore);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        //리사이클러뷰 설정
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존 성능 강화
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("wirteUserId",""+boardName);

        FirebaseDatabase NewsFeedDB =  FirebaseDatabase.getInstance();
        DatabaseReference NewsFeedDBR = NewsFeedDB.getReference("Feed");

        //선택한 게시글 찾기
        Query findNewsFeed = NewsFeedDBR.orderByChild("newsName").equalTo(boardName);
        findNewsFeed.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot user : snapshot.getChildren()){
                        News news = user.getValue(News.class);
                        tvNewsId.setText(news.getId());
                        tvNewsContext.setText(news.getContext());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        feedReply = new ArrayList<>();
        DatabaseReference Replydbr = NewsFeedDB.getReference("Reply");
        Query findReply = Replydbr.orderByChild("boardName").equalTo(boardName);
        findReply.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedReply.clear();
                for(DataSnapshot reply: snapshot.getChildren()){
                    feedReply.add(0,reply.getValue(Reply.class));
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