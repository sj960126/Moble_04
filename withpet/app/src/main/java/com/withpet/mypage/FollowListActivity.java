package com.withpet.mypage;

import androidx.annotation.NonNull;
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
import com.withpet.Chat.NotifyApplication;
import com.withpet.main.User;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class FollowListActivity extends AppCompatActivity {
    private FirebaseUser loginUser;
    private ArrayList<User> followuserlist;
    private RecyclerView chatListRecyclerView;
    private NotifyApplication appinfo;
    public  RecyclerView.Adapter followListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);
        followuserlist = new ArrayList<User>();

        appinfo = (NotifyApplication)getApplication();

        loginUser = FirebaseAuth.getInstance().getCurrentUser();    //로그인한 유저의 정보 가져오기
        findViewById(R.id.followListBtn_Back).setOnClickListener(onClickListener);

        // 리사이클러 뷰, 어탭터 설정
        chatListRecyclerView = findViewById(R.id.followListRv_FollowUserList);
        chatListRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        chatListRecyclerView.setLayoutManager(mLayoutManager);
        followListAdapter = new FollowListAdapter(this, followuserlist, getIntent().getStringExtra("userid"));
        chatListRecyclerView.setAdapter(followListAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Follow").child(getIntent().getStringExtra("userid"));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followuserlist.clear();     // 기존 데이터 삭제
                followListAdapter.notifyDataSetChanged();       // 어댑터 갱신
                // reference를 마이페이지에 드러온 유저의 uid로 걸었기 때문에 snapshot은 그 유저의 팔로우 리스트
                // followuserdata : 현재 들어온 마이페이지 유저의 팔로우 리스트
                for(DataSnapshot followuserdata : snapshot.getChildren()){
                    // appinfo.getUserlist() : 모든 유저의 정보
                    // user : 모든 유저 정보 중 1명의 유저 정보를 저장하는 객체
                    for(User user: appinfo.getUserlist()){
                        // 팔로우한 유저의 id와 user객체에 저장된 id가 같은지 확인
                        if(user.getUid().equals(followuserdata.getKey())){
                            ((FollowListAdapter)followListAdapter).addfollowUser(user);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            switch (v.getId()) {
                case R.id.followListBtn_Back:
                    finish();
                    break;
            }
        }
    };
}