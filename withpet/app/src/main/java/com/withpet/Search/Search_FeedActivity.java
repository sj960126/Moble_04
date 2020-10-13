package com.withpet.Search;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

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
import java.util.ArrayList;

public class Search_FeedActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView find_recyclerview;
    private RecyclerView.Adapter finduserlistAdapter;
    private ArrayList<User> userlist;
    private ArrayList<User> finduserlist;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ArrayList<String>> temp;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private String mypetcode;
    private ArrayList<String> feedid;
    private ArrayList<String> arr;
    private SearchView find_userinput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__feed);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); // 유저 아이디 얻기
        database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연동

        //피드 리사이클러 뷰
        recyclerView = findViewById(R.id.search_rc);
        recyclerView.setHasFixedSize(true); //리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        temp = new ArrayList<ArrayList<String>>();
        feedid = new ArrayList<String>();
        adapter = new SearchAdapter(temp,this);
        recyclerView.setAdapter(adapter);

        //유저 찾기 리사이클러 뷰
        find_userinput = findViewById(R.id.find);
        find_recyclerview = findViewById(R.id.find_rc);
        userlist = new ArrayList<User>();
        finduserlist = new ArrayList<User>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        find_recyclerview.setHasFixedSize(true); //리사이클러뷰 기존 성능 강화
        find_recyclerview.setLayoutManager(layoutManager);
        finduserlistAdapter = new Search_findAdapter(this, finduserlist);
        find_recyclerview.setAdapter(finduserlistAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseReference = database.getReference("User"); //db테이블 연결 경로
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userlist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    userlist.add(dataSnapshot.getValue(User.class)); //유저 정보를 담는다
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        find_userinput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false; //검색버튼 눌렀을 경우
            }

            @Override
            public boolean onQueryTextChange(String findid) {
                finduserlist.clear();
                if (!findid.equals("")) {
                    find_recyclerview.setVisibility(View.VISIBLE); // 글 적히면 리사이클 뷰 Visible
                    for (User user : userlist){ //userlist 정보 가져오기
                        if (user.getNickname().contains(findid)){
                            if (!user.getUid().equals(firebaseUser)){
                                finduserlist.add(user);
                            }
                        }
                    }
                }
                if (findid.equals("")){
                    find_recyclerview.setVisibility(View.GONE);
                }
                finduserlistAdapter.notifyDataSetChanged();
                return false; //텍스트가 바뀔떄마다 호출
            }
        });

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
                                arr = new ArrayList<String>();
                            }
                            adapter.notifyDataSetChanged();
                        }

                    }@Override
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
    public void onCancelled(@NonNull DatabaseError error){ }});

    }
}