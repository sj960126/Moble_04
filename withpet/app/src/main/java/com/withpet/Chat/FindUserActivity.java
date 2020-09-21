package com.withpet.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);
        et_finduserinput = findViewById(R.id.findUserEt_input);
        finduserlistRecyclerView = findViewById(R.id.findUserRv_List);
        userlist = new ArrayList<User>();
        finduserlist = new ArrayList<User>();
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
                    userlist.add(ds.getValue(User.class));
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