package com.withpet.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;
import com.withpet.main.*;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;

//종희
public class FindUserActivity extends AppCompatActivity {
    private RecyclerView finduserlistRecyclerView;
    private RecyclerView.Adapter finduserlistwAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText et_finduserinput;
    private ArrayList<User> userlist;
    private ArrayList<User> finduserlist;
    private String loginuserid;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);
        et_finduserinput = findViewById(R.id.findUserEt_input);                 // 유저 닉네임 검색 칸
        finduserlistRecyclerView = findViewById(R.id.findUserRv_List);          // 찾은 유저의 리스트를 보여주는 리스트 뷰
        loginuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();     // 로그인 유저 정보 가져오기

        userlist = new ArrayList<User>();
        finduserlist = new ArrayList<User>();

        //리사이클러 뷰, 어댑터 설정정
       layoutManager = new LinearLayoutManager(this);
        finduserlistRecyclerView.setHasFixedSize(true); //리사이클러뷰 기존 성능 강화
        finduserlistRecyclerView.setLayoutManager(layoutManager);
        finduserlistwAdapter = new FindUserAdapter(this, finduserlist);
        finduserlistRecyclerView.setAdapter(finduserlistwAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbreference = db.getReference("User");

        dbreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userlist.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    userlist.add(ds.getValue(User.class));  //유저 정보
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        et_finduserinput.addTextChangedListener(new TextWatcher() {
            @Override   // 입력 전 호출
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override   //입력할 때 호출
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String findid = et_finduserinput.getText().toString();
                finduserlist.clear();
                if(!findid.equals("")){
                    for(User user : userlist){
                        if(user.getNickname().contains(findid)){
                            if(!user.getUid().equals(loginuserid))  // 검색한 닉네임이 로그인한 유저의 닉네임인지 판단
                                finduserlist.add(user);
                        }

                    }
                }
                finduserlistwAdapter.notifyDataSetChanged();
            }

            @Override   // 입력 끝났을 때 호출
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}