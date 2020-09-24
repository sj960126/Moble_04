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
import android.widget.Toast;

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

    // 개선사항 :  채팅 ui 수정
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
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
        //사용시 차일드 알아서 하나 넘어감, chat의 차일드가 들어가짐
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
                dbreference = db.getReference("Chat/"+chatroomname);    // 왜 해당 메시지가 있는 경로를 설정해줘야만 알아서 갱신이 되는가..?
                dbreference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        // onChildAdded의 Datasnapshot은 DatabaseReference로 설정한 경로의 child로 시작함
                        // 즉, DatabaseReference로 chat을 설정했으면 Datasnapshot은 chat의 child(대화방정보 : uid_uid)가 됨
                        // 따라서 Datasnapshot의 .getChildren을 사용하면 push로 넣은 대화의 정보가 나오게 된다.
                        // 다른 리스너의 경우 chat으로 DatabaseReference로 Chat을 설정한 경우 .getChildren을 해야 대화방 정보가 나옴
                        // previousChildName : 현재 child 이전의 child이름
                        Chat chat = snapshot.getValue(Chat.class);
                        ((ChattingAdapter)chattingAdapter).addChat(chat);   // 어댑터에 파이어베이스에서 가져온 내용 추가
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

}