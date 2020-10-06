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
import com.withpet.Chat.ChatListAdapter;
import com.withpet.Chat.ChattingRoom;
import com.withpet.Chat.NotifyApplication;
import com.withpet.main.User;

import android.app.PendingIntent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
        followListAdapter = new FollowListAdapter(this, followuserlist);
        chatListRecyclerView.setAdapter(followListAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Follow").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followuserlist.clear();
                followListAdapter.notifyDataSetChanged();
                for(DataSnapshot followuserdata : snapshot.getChildren()){
                    for(User user: appinfo.getUserlist()){
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
            switch (v.getId()) {
                case R.id.followListBtn_Back:
                    finish();
                    break;
            }
        }
    };
}