package com.withpet.Search;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;
import com.withpet.main.User;
import com.withpet.newsfeed.Feed;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Search_FeedActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ArrayList<String>> temp;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private int mypetcode;
    private ArrayList<String> feedid;
    private ArrayList<String> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__feed);
        recyclerView = findViewById(R.id.search_rc);
        recyclerView.setHasFixedSize(true); //리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        temp = new ArrayList<ArrayList<String>>();
        feedid = new ArrayList<String>();
        database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연동

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseReference = database.getReference("User"); //db테이블 연결 경로
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> userlist = new ArrayList<User>(); //유저 객체를 담는 리스트를 만든다
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    User u1 = snapshot1.getValue(User.class); // 데이터베이스 스냅샷을 유저class 형태로  u1객체를 생성한다
                    userlist.add(u1);  //유저객체를 담는 리스트에 유저 아이디를 담는다
                    if (firebaseUser.getUid().equals(u1.getUid())){ //나의 로그인 아이디를 찾는다
                       mypetcode=u1.getPetcode(); // 나의 로그인 아이디의  petcode를 저장한다
                    }
                }
                for (DataSnapshot snapshot2 : snapshot.getChildren()){
                    if (mypetcode == snapshot2.getValue(User.class).getPetcode()){ //내 팻 코드와 같은 사람들의 정보를
                        feedid.add(snapshot2.getValue(User.class).getUid()); //feedid에 저장한다
                    }
                    Log.i("1","ㅋ");
                }
                arr = new ArrayList<String>();

                databaseReference = database.getReference("Feed"); //db테이블 연결 경로
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Feed feed = snapshot.getValue(Feed.class);
                        for (String f : feedid) {
                            if (f.equals(feed.getUid())) {
                                arr.add(feed.getImgUrl());
                            }
                            if (arr.size()==7){
                                temp.add(arr);
                                adapter.notifyDataSetChanged();
                            }
                        }

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
        Log.i("시발"," ㄴ");
        adapter = new SearchAdapter(temp,this);
        recyclerView.setAdapter(adapter);
    }
}