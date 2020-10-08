package com.withpet.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import com.withpet.*;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

//Mypage page 종희
public class MypageFrag extends Fragment {
    private View rootview;              // 프래그먼트가 실행시킬 레이아웃
    private RecyclerView list;
    private CircleImageView iv_profilephoto;
    private TextView tv_nickname;
    private TextView tv_noticenum;
    private TextView tv_interestnum;
    private Button btn_profliemodify, btn_setting;
    private RecyclerView.Adapter adapter;
    private ArrayList<Feed> myfeed;
    private ArrayList<String> myfollowlist;
    private FirebaseDatabase db;
    private DatabaseReference dbreference;
    private FirebaseUser firebaseUser;  // 로그인 유저의 uid정보를 가지고 있는 변수
    private TransUser loginuser;        // 로그인한 유저의 정보(자기 자신) : 프로필 사진정보, 닉네임, uid, 이름 등
    private TransUser choiceuser;       // 메인피드의 프로필을 눌러서 들어온 유저의 정보
    private TransUser nowuserinfo;  // 현재 마이페이지의 유저 정보(메인피드의 프로필을 눌러서 들어왔으면 해당 게시글의 유저정보, 마이페이지 메뉴로 들어오면 자기자신)
    private String requestfrom;
    final  int requestcode = 1001;

    // 로그인한 사람의 게시글만 보이게 변경(로그인 정보 가져와야함)
    @SuppressLint("ResourceAsColor")
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

        iv_profilephoto = rootview.findViewById(R.id.walkIv_user);
        tv_nickname = rootview.findViewById(R.id.myPageTv_username);
        tv_noticenum = rootview.findViewById(R.id.myPageTv_noticenum);
        tv_interestnum = rootview.findViewById(R.id.myPageTv_interestnum);
        tv_interestnum.setOnClickListener(onClickListener);

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
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(rootview.getContext(), col);  // 그리드 뷰 레이아웃으로 설정
        list.setLayoutManager(layoutManager);
        myfeed = new ArrayList<>(); //유저 객체를 담을 (어댑터쪽으로)
        myfollowlist = new ArrayList<String>();
        adapter = new MyPageNoticeAdapter(myfeed, getContext(), nowuserinfo);
        list.setAdapter(adapter); //리사이클러뷰에 어댑터 연결



        return rootview;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("resume start", "resume start");

        // 메뉴를 통해 들어오거나, 메인피드 게시글 프로필을 눌러서 들어오지 않은 경우

        if(requestfrom.equals("menu") || nowuserinfo == null ){
            DatabaseReference databaseReference = db.getReference("User").child(firebaseUser.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    nowuserinfo = new TransUser(snapshot.getValue(User.class));
                    tv_nickname.setText(nowuserinfo.getNickname());                 // 마이페이지에 유저 닉네임 출력
                    Glide.with(rootview).load(nowuserinfo.getImgUrl()).override(800).into(iv_profilephoto);     // 유저 프로필 사진 출력
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            //nowuserinfo = new TransUser(((NotifyApplication)getActivity().getApplication()).getUser(firebaseUser.getUid()));
        }
        else{
            tv_nickname.setText(nowuserinfo.getNickname());                 // 마이페이지에 유저 닉네임 출력
            Glide.with(rootview).load(nowuserinfo.getImgUrl()).override(800).into(iv_profilephoto);     // 유저 프로필 사진 출력

        }
/*
        tv_nickname.setText(nowuserinfo.getNickname());                 // 마이페이지에 유저 닉네임 출력
        Glide.with(rootview).load(nowuserinfo.getImgUrl()).override(800).into(iv_profilephoto);     // 유저 프로필 사진 출력
*/

        //
        dbreference = db.getReference("Feed");
        dbreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myfeed.clear();
                for(DataSnapshot feeddata: snapshot.getChildren()){
                    Feed feed = feeddata.getValue(Feed.class);
                    if(feed.getUid().equals(nowuserinfo.getUid())){
                        myfeed.add(0, feed);
                    }
                }
                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
                tv_noticenum.setText(""+myfeed.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //파이어베이스에서 로그인유저 nickname 정보 가져오기
        // 메뉴를 눌러 마이페이지로 오거나, 피드에서 내가 올린 게시글에서 프로필을 눌러서 왔는지 확인하는 코드

        dbreference = db.getReference("Follow");
        dbreference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // 팔로우 정보에서 로그인한 유저의 정보 찾기
                if(snapshot.getKey().equals(firebaseUser.getUid())){
                    // 로그인 한 유저의 팔로우 목록 전체 실행
                    for(DataSnapshot followuserdata :snapshot.getChildren()){
                        // 로그인 한 유저의 팔로우 목록 중 마이페이지에 출력할 유저가 있는지 찾기
                        if(followuserdata.getKey().equals(nowuserinfo.getUid())){
                            btn_profliemodify.setText("관심 삭제");    // 있다면 프로필 수정 버튼을 문구를 관심 삭제로 변경
                        }
                    }
                }
                if(snapshot.getKey().equals(nowuserinfo.getUid())){
                    myfollowlist.clear();
                    for(DataSnapshot followuserdata :snapshot.getChildren()){
                        myfollowlist.add(followuserdata.getKey());
                    }
                    tv_interestnum.setText(""+myfollowlist.size());
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {   }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {    }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @SuppressLint("ResourceAsColor")
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
                        //로그아웃, 회원탈퇴
                        intent = new Intent(v.getContext(), SettingActivity.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.myPageBtn_modify:
                    // 파이어베이스에서 가져온 닉네임, 사진정보 프로필 수정 액태비티로 전달(프로필 수정 버튼 이벤트)
                    DatabaseReference followreference = db.getReference("Follow");
                    if(((Button)v).getText().toString().equals("프로필 수정")) {
                        intent = new Intent(v.getContext(), ProfileModifyActivity.class);
                        intent.putExtra("loginuser", nowuserinfo);
                        startActivityForResult(intent, requestcode);
                    }
                    else if(((Button)v).getText().toString().equals("관심 추가")){
                        followreference.child(firebaseUser.getUid()).child(nowuserinfo.getUid()).child("followid").setValue(nowuserinfo.getUid());      // 해당 유저의 uid를 데이터베이스에 추가
                        ((Button)v).setText("관심 삭제");
                        //btn_profliemodify.setBackgroundTintList(ColorStateList.valueOf(R.color.diary_bg));
                    }
                    else if(((Button)v).getText().toString().equals("관심 삭제")){
                        followreference.child(firebaseUser.getUid()).child(nowuserinfo.getUid()).removeValue(); // 해당 유저를 관심 삭제 하기위해 데이터 베이스에서 값 삭제
                        ((Button)v).setText("관심 추가");
                        //btn_profliemodify.setB(ColorStateList.valueOf(R.color.c1));
                    }
                    break;
                case R.id.myPageTv_interestnum:
                    intent = new Intent(v.getContext(), FollowListActivity.class);
                    intent.putExtra("userid", nowuserinfo.getUid());
                    startActivity(intent);
                    break;
            }
        }
    };

}
