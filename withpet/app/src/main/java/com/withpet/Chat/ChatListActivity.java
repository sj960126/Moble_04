package com.withpet.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;
import com.withpet.main.*;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

//종희
public class ChatListActivity extends AppCompatActivity {
    private FirebaseDatabase db;
    private FirebaseUser loginUser;
    private ArrayList<ChattingRoom> chatroomlist;
    private ArrayList<User> userlist;
    private RecyclerView chatListRecyclerView;
    private TextView tv_newchatnum;
    public  RecyclerView.Adapter chatListAdapter;
    final int requestcode = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        chatroomlist = new ArrayList<ChattingRoom>();
        userlist = new ArrayList<User>();
        db = FirebaseDatabase.getInstance(); //파이어베스 데이터베이스 연동

        tv_newchatnum = findViewById(R.id.chatListTv_NewChatNum);
        loginUser = FirebaseAuth.getInstance().getCurrentUser();    //로그인한 유저의 정보 가져오기
        findViewById(R.id.chatListBtn_Find).setOnClickListener(onClickListener);
        findViewById(R.id.chatListBtn_Back).setOnClickListener(onClickListener);

        // 리사이클러 뷰, 어탭터 설정
        chatListRecyclerView = findViewById(R.id.chatListRv_List);
        chatListRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        chatListRecyclerView.setLayoutManager(mLayoutManager);
        chatListAdapter = new ChatListAdapter(this, chatroomlist, userlist, loginUser.getUid());
        chatListRecyclerView.setAdapter(chatListAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseReference userreference = db.getReference("User");
        userreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userlist.clear();       // 다시 창 켜졌을 때 기존에 있는 값 제거
                for(DataSnapshot userdata : snapshot.getChildren()){
                    User user = userdata.getValue(User.class);
                    ((ChatListAdapter)chatListAdapter).addUserinfo(user);   // user정보를 어댑터에 추가
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference chatreference = db.getReference("Chat");
        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatroomlist.clear();   // 다시 창 켜졌을 때 기존에 있는 값 제거
                chatListAdapter.notifyDataSetChanged();
                for(DataSnapshot chatroomdata : snapshot.getChildren()){
                    if(chatroomdata.getKey().contains(loginUser.getUid())){
                        ChattingRoom ctr = new ChattingRoom();                  // 채팅방 정보를 담을 객체 생성( 채팅내역, 채팅방 이름 )
                        ctr.setChatroomname(chatroomdata.getKey());
                        ctr.setChildcount(chatroomdata.getChildrenCount());
                        for(DataSnapshot chatdata : chatroomdata.getChildren()){
                            ctr.addChattingList(chatdata.getValue(Chat.class));
                        }
                        ((ChatListAdapter)chatListAdapter).addChatRoom(ctr);    //어댑터에 채팅방 정보 추가
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.chatListBtn_Back:
                    finish();
                    break;
                case R.id.chatListBtn_Find:
                    Intent intent = new Intent(view.getContext(), FindUserActivity.class);
                    startActivityForResult(intent, requestcode);
                    break;
            }
        }
    };

}