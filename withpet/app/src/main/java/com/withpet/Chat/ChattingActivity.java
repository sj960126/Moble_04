package com.withpet.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;
import com.withpet.main.*;

import java.util.ArrayList;

//종희
public class ChattingActivity extends AppCompatActivity {
    private FirebaseDatabase db;
    private DatabaseReference dbreference;
    private TransUser opponent;
    private String meid;
    private ArrayList<Chat> chattingList;
    private EditText et_sendmeaasge;
    private String chatroomname;
    private RecyclerView chattingRecyclerView;
    public  RecyclerView.Adapter chattingAdapter;
    private NotifyApplication applicationinfo;

    // 개선사항 :  채팅 ui 수정
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        applicationinfo = (NotifyApplication)getApplication();

        Intent intent = getIntent();
        et_sendmeaasge = findViewById(R.id.chattingEt_input);
        opponent = (TransUser)intent.getSerializableExtra("Opponent");      // 대화 상대의 정보 가져오기
        meid = FirebaseAuth.getInstance().getCurrentUser().getUid();               // 로그인 한 유저의 uid
        chattingList = new ArrayList<Chat>();
        findViewById(R.id.chattingBtn_send).setOnClickListener(onClickListener);

        // 리사이클러 뷰, 어댑터 설정
        chattingRecyclerView = findViewById(R.id.chattingRv_chat);
        chattingRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        chattingRecyclerView.setLayoutManager(mLayoutManager);
        chattingAdapter = new ChattingAdapter(this, chattingList, meid, opponent.TransformUser());
        chattingRecyclerView.setAdapter(chattingAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = FirebaseDatabase.getInstance();
        dbreference = db.getReference("Chat");
        chattingList.clear();
        chattingAdapter.notifyDataSetChanged();
        dbreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot chatroomdata : snapshot.getChildren()){
                    if(chatroomdata.getKey().contains(meid) && chatroomdata.getKey().contains(opponent.getUid())){
                        chatroomname = chatroomdata.getKey();
                        break;
                    }
                }
                chatroomname = (chatroomname != null)? chatroomname : meid + "_" + opponent.getUid();
                applicationinfo.setEnterChattingRoom(chatroomname);
                dbreference = db.getReference("Chat/"+chatroomname);    // 왜 해당 메시지가 있는 경로를 설정해줘야만 알아서 갱신이 되는가..?
                dbreference.addValueEventListener(valueEventListener);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        applicationinfo.setEnterChattingRoom(null);
        dbreference.removeEventListener(valueEventListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference dbreference = db.getReference("Chat");
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.chattingBtn_send:
                    Chat chat = new Chat(meid, et_sendmeaasge.getText().toString());
                    dbreference.child(chatroomname).push().setValue(chat);              //db에 채팅내용 작성
                    et_sendmeaasge.setText("");
                    break;
            }
        }
    };
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            ((NotifyApplication)getApplication()).refreshChattingChildCount(snapshot.getKey(), snapshot.getChildrenCount());
            Log.i("차일드 수 확인", ""+ snapshot.getChildrenCount());
            chattingList.clear();
            chattingAdapter.notifyDataSetChanged();
            for(DataSnapshot chatdata : snapshot.getChildren()) {
                Chat chat = chatdata.getValue(Chat.class);
                ((ChattingAdapter)chattingAdapter).addChat(chat);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

}