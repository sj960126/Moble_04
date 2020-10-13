package com.withpet.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    private Button btn_send;
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
        et_sendmeaasge = findViewById(R.id.chattingEt_input);                      // 대화 내용을 출력할 textview
        opponent = (TransUser)intent.getSerializableExtra("Opponent");      // 대화 상대의 정보 가져오기
        meid = FirebaseAuth.getInstance().getCurrentUser().getUid();               // 로그인 한 유저의 uid
        chattingList = new ArrayList<Chat>();
        btn_send = findViewById(R.id.chattingBtn_send);
        btn_send.setVisibility(View.INVISIBLE);
        btn_send.setOnClickListener(onClickListener);    // 보내기 버튼

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
        chattingList.clear();                               // 채팅내역 리스트 초기화
        chattingAdapter.notifyDataSetChanged();             // 어댑터에 리스트 초기화 됐음을 인지시킴
        dbreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 전체 채팅방 리스트 가져오기
                for(DataSnapshot chatroomdata : snapshot.getChildren()){
                    // 전체 채팅방 정보에서 로그인 유저와 채팅 상대 이름이 들어간 채팅방을 찾기
                    if(chatroomdata.getKey().contains(meid) && chatroomdata.getKey().contains(opponent.getUid())){
                        // 로그인 유저와 채팅상대가 들어간 채팅방 이름 가져오기
                        chatroomname = chatroomdata.getKey();
                        break;
                    }
                }
                // 실행된 채팅방이 파이어베이스에 존재하지 않으면 새로운 채팅방을 만들어 파이어베이스에 저장하기 위해 채팅방 이름 생성
                chatroomname = (chatroomname != null)? chatroomname : meid + "_" + opponent.getUid();
                // 들어간 채팅방 알림을 받지 않기 위해서 들어간 채팅방이 어떤 것인지 확인
                applicationinfo.setEnterChattingRoom(chatroomname);
                dbreference = db.getReference("Chat/"+chatroomname);    // 왜 해당 메시지가 있는 경로를 설정해줘야만 알아서 갱신이 되는가..?
                dbreference.addValueEventListener(valueEventListener);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // edit textview의 내용 변경 이벤트
        et_sendmeaasge.addTextChangedListener(new TextWatcher() {
            @Override   // 입력 전 호출
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override   //입력할 때 호출
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String message = et_sendmeaasge.getText().toString();
                // 입력 창이 공백이면 보내기 버튼 비활성화
                if(message.equals("")){
                    btn_send.setVisibility(View.INVISIBLE);
                }
                else{
                    btn_send.setVisibility(View.VISIBLE);
                }

            }

            @Override   // 입력 끝났을 때 호출
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        applicationinfo.setEnterChattingRoom(null);
        dbreference.removeEventListener(valueEventListener);                // 백그라운드에 들어가면 리스너가 실행되지 않게 리스너 삭제
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
                // 어댑터에 채팅방 내용 추가
                ((ChattingAdapter)chattingAdapter).addChat(chat);
                // 리사이클러 뷰 포커스 마지막에 맞추기
                chattingRecyclerView.smoothScrollToPosition(chattingAdapter.getItemCount()-1);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

}