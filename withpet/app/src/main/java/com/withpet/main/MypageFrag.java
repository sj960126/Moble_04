package com.withpet.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.withpet.iot.*;
import com.withpet.mypage.*;
import com.withpet.newsfeed.*;
import com.withpet.health.*;
import com.withpet.walk.*;
import com.withpet.*;

import java.util.ArrayList;

//Mypage page 종희
public class MypageFrag extends Fragment {

    private View rootview;
    private RecyclerView list;
    private ImageView iv_profilephoto;
    private TextView tv_nickname;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<News> myfeed;
    private FirebaseDatabase db;
    private DatabaseReference dbreference;
    private FirebaseUser firebaseUser;
    private TransUser loginuser;
    final  int requestcode = 1001;
    // 로그인한 사람의 게시글만 보이게 변경
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_mypage,container,false);
        iv_profilephoto = rootview.findViewById(R.id.myPageIv_profile);
        tv_nickname = rootview.findViewById(R.id.myPageTv_username);

        Button btn_setting = (Button) rootview.findViewById(R.id.mypageBtn_setting);
        btn_setting.setBackgroundResource(R.drawable.iconsetting);

        list = rootview.findViewById(R.id.myPageListview_mynotice);
        list.setHasFixedSize(true); //리사이클러뷰 기존 성능 강화
        layoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(layoutManager);
        myfeed = new ArrayList<>(); //유저 객체를 담을 (어댑터쪽으로)

        db = FirebaseDatabase.getInstance(); //파이어베스 데이터베이스 연동
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        adapter = new MyPageNoticeAdapter(myfeed, getContext());
        list.setAdapter(adapter); //리사이클러뷰에 어댑터 연결
        // 파이어베이스에서 가져온 닉네임, 사진정보 프로필 수정 액태비티로 전달
        rootview.findViewById(R.id.myPageBtn_modify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent modify = new Intent(getActivity(), ProfileModifyActivity.class);
                modify.putExtra("loginuser", loginuser);
                startActivityForResult(modify, requestcode);
            }
        });
        return rootview;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

    //이미지 새로고침 안됨 프로필 수정 후 다시 마이페이지 돌아왔을때 프사 안바뀜
    @Override
    public void onStart() {
        super.onStart();
        Log.i("resume start", "resume start");
        dbreference = db.getReference("Feed");//연동한 DB의 테이블 연결
        dbreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                myfeed.clear(); //기존 배열가 존재하지 않게 초기화 방지차원
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    //반복문으로 데이터 리스트를 추출
                    News news = snapshot.getValue(News.class); //만들어뒀던 news 객체에 데이터를 담음
                    myfeed.add(news); //담은 데이터들을 배열리스터에 넣고 리사이클뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //데베 데이터를 가져오던 중 에러 발생 시
                Toast.makeText(getActivity(), "에러라라고오오옹", Toast.LENGTH_SHORT).show();
            }
        });

        //파이어베이스에서 로그인유저 nickname 정보 가져오기
        DatabaseReference userdbreference = db.getReference("User");
        userdbreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    if(ds.getKey().equals(firebaseUser.getUid())){
                        loginuser = new TransUser(ds.getValue(User.class));
                        tv_nickname.setText(loginuser.getNickname());
                        Glide.with(rootview).load(loginuser.getImgUrl()).override(800).into(iv_profilephoto);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
