package com.withpet.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.withpet.mypage.*;
import com.withpet.newsfeed.*;
import com.withpet.Chat.*;
import com.withpet.health.*;
import com.withpet.walk.*;
import com.withpet.*;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

//Mypage page 종희
public class MypageFrag extends Fragment {

    private View rootview;
    private RecyclerView list;
    private CircleImageView iv_profilephoto;
    private TextView tv_nickname;
    private Button btn_profliemodify;
    private Button btn_setting;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Feed> myfeed;
    private FirebaseDatabase db;
    private DatabaseReference dbreference;
    private FirebaseUser firebaseUser;
    private TransUser loginuser;        // 로그인한 유저의 정보(자기 자신)
    private TransUser choiceuser;       // 메인피드의 프로필을 눌러서 들어온 유저의 정보
    private TransUser nowuserinfo;  // 현재 마이페이지의 유저 정보(메인피드의 프로필을 눌러서 들어왔으면 해당 게시글의 유저정보, 마이페이지 메뉴로 들어오면 자기자신)
    private String requestfrom;
    final  int requestcode = 1001;

    // 로그인한 사람의 게시글만 보이게 변경(로그인 정보 가져와야함)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_mypage,container,false);

        // 메인피드에서 프로필을 클릭해 마이페이지로 온경우
        Bundle bundle = this.getArguments();
        requestfrom = bundle.getString("from");

        if(requestfrom.equals("proflie"))  {
            choiceuser = (TransUser) bundle.getSerializable("userinfo");
            nowuserinfo = choiceuser;
        }

        db = FirebaseDatabase.getInstance(); //파이어베스 데이터베이스 연동
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        iv_profilephoto = rootview.findViewById(R.id.myPageIv_profile);
        tv_nickname = rootview.findViewById(R.id.myPageTv_username);

        btn_setting = (Button) rootview.findViewById(R.id.mypageBtn_setting);
        btn_setting.setOnClickListener(onClickListener);
        btn_profliemodify = rootview.findViewById(R.id.myPageBtn_modify);
        btn_profliemodify.setOnClickListener(onClickListener);

        // 메뉴에서 마이페이지를 눌렀을 때때
       if(requestfrom.equals("menu") || nowuserinfo.getUid().equals(firebaseUser.getUid())) {
            btn_setting.setBackgroundResource(R.drawable.iconsetting);
            btn_setting.setTag(R.integer.btnResource, R.drawable.iconsetting);
            btn_profliemodify.setText("프로필 수정");
        }
       // 메인에서 프로필을 눌러 마이페이지에 왔을 때
        else{
            btn_setting.setBackgroundResource(R.drawable.iconchatt);
            btn_setting.setTag(R.integer.btnResource, R.drawable.iconchatt);
            btn_profliemodify.setText("관심 추가");
            tv_nickname.setText(nowuserinfo.getNickname());
            Glide.with(rootview).load(nowuserinfo.getImgUrl()).override(800).into(iv_profilephoto);
        }

        list = rootview.findViewById(R.id.myPageListview_mynotice);
        list.setHasFixedSize(true); //리사이클러뷰 기존 성능 강화

        final int col = 3;  // 그리드 뷰 컬럼 수
        layoutManager = new GridLayoutManager(rootview.getContext(), col);  // 그리드 뷰 레이아웃으로 설정
        list.setLayoutManager(layoutManager);
        myfeed = new ArrayList<>(); //유저 객체를 담을 (어댑터쪽으로)

        adapter = new MyPageNoticeAdapter(myfeed, getContext(), nowuserinfo);
        list.setAdapter(adapter); //리사이클러뷰에 어댑터 연결



        return rootview;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

    //이미지 새로고침 안됨 프로필 수정 후 다시 마이페이지 돌아왔을때 프사 안바뀜
    @Override
    public void onResume() {
        super.onResume();
        Log.i("resume start", "resume start");

        //파이어베이스에서 로그인유저 nickname 정보 가져오기
        if(requestfrom.equals("menu") || nowuserinfo.getUid().equals(firebaseUser.getUid())){
            final DatabaseReference userdbreference = db.getReference("User");
            userdbreference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Log.i("childstart", "start!!!!");
                    if(snapshot.getKey().equals(firebaseUser.getUid())){
                        loginuser = new TransUser(snapshot.getValue(User.class));
                        nowuserinfo = loginuser;
                        tv_nickname.setText(nowuserinfo.getNickname());
                        Glide.with(rootview).load(nowuserinfo.getImgUrl()).override(800).into(iv_profilephoto);
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
        dbreference = db.getReference("Feed");//연동한 DB의 테이블 연결
        dbreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                myfeed.clear(); //기존 배열가 존재하지 않게 초기화 방지차원
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    myfeed.add(0, snapshot.getValue(Feed.class));
                }
                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //데베 데이터를 가져오던 중 에러 발생 시
                Toast.makeText(getActivity(), "에러라라고오오옹", Toast.LENGTH_SHORT).show();
            }
        });
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.mypageBtn_setting:
                    if((int)v.getTag(R.integer.btnResource) == R.drawable.iconchatt){
                        intent = new Intent(v.getContext(),ChattingActivity.class);
                        intent.putExtra("Opponent", nowuserinfo);
                        startActivity(intent);
                    }
                    else if((int)v.getTag(R.integer.btnResource) == R.drawable.iconsetting){

                    }
                    break;
                case R.id.myPageBtn_modify:
                    // 파이어베이스에서 가져온 닉네임, 사진정보 프로필 수정 액태비티로 전달(프로필 수정 버튼 이벤트)
                    if(((Button)v).getText().toString().equals("프로필 수정")) {
                        intent = new Intent(v.getContext(), ProfileModifyActivity.class);
                        intent.putExtra("loginuser", nowuserinfo);
                        startActivityForResult(intent, requestcode);
                    }
                    else if(((Button)v).getText().toString().equals("관심 추가")){
                        Log.i("add 관심", " 관심추가");
                    }
                    break;
            }
        }
    };

}
